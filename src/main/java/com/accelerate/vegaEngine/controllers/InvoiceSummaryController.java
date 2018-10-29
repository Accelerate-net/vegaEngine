package com.accelerate.vegaEngine.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.accelerate.vegaEngine.models.*;
import com.accelerate.vegaEngine.CouchDBRestClient;
import org.json.JSONTokener;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.accelerate.vegaEngine.exceptionHandling.InvalidFormatException;
import com.accelerate.vegaEngine.exceptionHandling.NoResultsFoundException;
import com.accelerate.vegaEngine.exceptionHandling.ResourceNotFoundException;
import com.accelerate.vegaEngine.exceptionHandling.ServerConnectionErrorException;


@RestController
public class InvoiceSummaryController {

    @RequestMapping("/sumbybillingmode")
    public InvoiceSummaryBillingMode summary(@RequestParam("start_date") String start_date, @RequestParam("end_date") String end_date) throws ResourceNotFoundException, InvalidFormatException, ServerConnectionErrorException, NoResultsFoundException{

    	//Psuedo initialization of dates
    	if(start_date == ""){
    		start_date = "20180101"; //When the software was first released
    	}
    	
    	if(end_date == ""){
    		Date today = new Date();
    		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    		
    		end_date = dateFormat.format(today); //Today
    	};
    	
    	
    	
    	//Parameter validation
    	SummaryDate reportStartDate = new SummaryDate(start_date);
    	SummaryDate reportEndDate = new SummaryDate(end_date);
        	    	
    	if(!reportStartDate.isValidDate()){
    		throw new InvalidFormatException(reportStartDate.validationMessage());
    	}
    	
    	if(!reportEndDate.isValidDate()){
    		throw new InvalidFormatException(reportEndDate.validationMessage());
    	}
    	
    	//Connection to billing server
    	CouchDBRestClient ServerRequest = new CouchDBRestClient();
    	
    	
    	//STEP 1: Fetch all the billing modes
    	String req_url_billing_modes = "/zaitoon_settings/_find";
    	String response_billing_modes;
    	
    	try {
    		response_billing_modes = ServerRequest.post(req_url_billing_modes, "{ \"selector\" :{ \"identifierTag\": \"ZAITOON_BILLING_MODES\" }, \"fields\" : [\"identifierTag\", \"value\"] }");  
    	} catch (ResourceAccessException e) {
			throw new ServerConnectionErrorException("Failed to communicate with the cloud billing server.");
		} catch (HttpClientErrorException e){
			throw new ServerConnectionErrorException("Bad Request. Check the parameters are in the required format.");
		}
    	
    	JSONObject responseJSON = new JSONObject(new JSONTokener(response_billing_modes));
    	JSONArray allResultsArray = responseJSON.getJSONArray("docs");
    	
    	JSONArray list_billing_modes = null;
    	
    	try{
    		list_billing_modes = ((JSONObject) allResultsArray.get(0)).getJSONArray("value");
    	} catch(JSONException e){
    		throw new NoResultsFoundException("No results found.");
    	}
    	
    	
    	int billingModesCount = list_billing_modes.length();
    	if(billingModesCount == 0){
    		throw new NoResultsFoundException("No billing modes found.");
    	}
    	
    	
    	//STEP 2: Fetch details of each billing mode
    	String billing_mode = "";
    	int billing_mode_sum = 0;
    	int billing_mode_count = 0;
    	JSONObject resultObject = null;
    	List<SumByBillingMode> result_list_billing_modes = new ArrayList<SumByBillingMode> ();
    	
    	boolean isAtleastOneBillingMode = false;
    	
    	int n = 0;
    	while(n < billingModesCount){
    		billing_mode = ((JSONObject) list_billing_modes.get(n)).getString("name");
    		
        	if(billing_mode == "" || billing_mode == null){
        		//Skip
        	}
        	else{
        		
	    		String req_url = "/zaitoon_invoices/_design/invoice-summary/_view/sumbybillingmode?startkey=[\""+billing_mode+"\",\""+reportStartDate.getDate()+"\"]&endkey=[\""+billing_mode+"\",\""+reportEndDate.getDate()+"\"]";
	        	String ServerResponse;
	        	//Sample Response: {"rows":[ {"key":null,"value":{"sum":22873,"count":136,"min":0,"max":4043,"sumsqr":24057163}} ]}
	    		
	        	try {
	    			ServerResponse = ServerRequest.get(req_url);
	    		} catch (ResourceAccessException e) {
	    			throw new ServerConnectionErrorException("Failed to communicate with the cloud billing server.");
	    		} catch (HttpClientErrorException e){
	    			throw new ServerConnectionErrorException("Bad Request. Check the parameters are in the required format.");
	    		}  
	        	
	        	responseJSON = new JSONObject(new JSONTokener(ServerResponse));
	        	allResultsArray = responseJSON.getJSONArray("rows");
	        	resultObject = null;
	        	
	        	try{
	        		resultObject = ((JSONObject) allResultsArray.get(0)).getJSONObject("value");
	        		billing_mode_sum = resultObject.getInt("sum");
	        		billing_mode_count = resultObject.getInt("count");
	        		
	        		isAtleastOneBillingMode = true;
	        		
	        	} catch(JSONException e){
	        		//No results found for this particular billing mode.
	        		billing_mode_sum = 0;
	        		billing_mode_count = 0;
	        	}
	        	
	        	//add mode to list
	        	SumByBillingMode selected_mode = new SumByBillingMode(billing_mode, billing_mode_sum, billing_mode_count);
	        	result_list_billing_modes.add(n, selected_mode);
	        	
        	}
        	
    		n++;
    	}
    	
    	if(isAtleastOneBillingMode){
    		return new InvoiceSummaryBillingMode(true, 200, "", result_list_billing_modes);
    	}
    	else{
    		return new InvoiceSummaryBillingMode(false, 200, "There are no recorded transactions.", null);
    	}
    }
}
