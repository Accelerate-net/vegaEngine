package com.accelerate.vegaEngine.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.accelerate.vegaEngine.models.*;
import com.accelerate.vegaEngine.models.SumByPaymentMode;
import com.accelerate.vegaEngine.CouchDBRestClient;
import org.json.JSONTokener;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
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
    public InvoiceSummaryBillingMode summary(@RequestParam("billing_mode") String billing_mode, @RequestParam("start_date") String start_date, @RequestParam("end_date") String end_date) throws ResourceNotFoundException, InvalidFormatException, ServerConnectionErrorException, NoResultsFoundException{

    	SummaryDate reportStartDate = new SummaryDate(start_date);
    	SummaryDate reportEndDate = new SummaryDate(end_date);
    	
    	//Validate parameters
    	if(billing_mode == "" || billing_mode == null){
    		throw new ResourceNotFoundException("Billing Mode can not be empty");
    	}
    	    	
    	if(!reportStartDate.isValidDate()){
    		throw new InvalidFormatException(reportStartDate.validationMessage());
    	}
    	
    	if(!reportEndDate.isValidDate()){
    		throw new InvalidFormatException(reportEndDate.validationMessage());
    	}
    	
    	CouchDBRestClient ServerRequest = new CouchDBRestClient();
    	String req_url = "/zaitoon_invoices/_design/invoice-summary/_view/sumbybillingmode?startkey=[\""+billing_mode+"\",\""+reportStartDate.getDate()+"\"]&endkey=[\""+billing_mode+"\",\""+reportEndDate.getDate()+"\"]";
    	System.out.println(req_url);
    	
    	
    	String ServerResponse;
    	//Sample Response: {"rows":[ {"key":null,"value":{"sum":22873,"count":136,"min":0,"max":4043,"sumsqr":24057163}} ]}
		
    	try {
			ServerResponse = ServerRequest.get(req_url);
		} catch (ResourceAccessException e) {
			throw new ServerConnectionErrorException("Failed to communicate with the cloud billing server.");
		} catch (HttpClientErrorException e){
			throw new ServerConnectionErrorException("Bad Request. Check the parameters are in the required format.");
		}
    	
    	
    	JSONObject ServerJSONResponse = new JSONObject(new JSONTokener(ServerResponse));
    	
    	
    	JSONArray rowsArray = ServerJSONResponse.getJSONArray("rows");
    	JSONObject resultObject = null;
    	
    	try{
    		resultObject = ((JSONObject) rowsArray.get(0)).getJSONObject("value");
    	} catch(JSONException e){
    		throw new NoResultsFoundException("No results found.");
    	}
    	

    	int summary_sum = resultObject.getInt("sum");
    	
    	SumByPaymentMode test_mode_1 = new SumByPaymentMode("PayTM", 100);
    	SumByPaymentMode test_mode_2 = new SumByPaymentMode("Cash", 240);
    	
    	List<SumByPaymentMode> payment_splits = new ArrayList<SumByPaymentMode> ();
    	payment_splits.add(0, test_mode_1);
    	payment_splits.add(1, test_mode_2);
    	
        return new InvoiceSummaryBillingMode(billing_mode, summary_sum, payment_splits);
    }
}
