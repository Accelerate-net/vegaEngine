package com.accelerate.vegaEngine.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accelerate.vegaEngine.models.*;
import com.accelerate.vegaEngine.models.SumByPaymentMode;

import com.accelerate.vegaEngine.CouchDBRestClient;
import org.json.JSONTokener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;


@RestController
public class InvoiceSummaryController {

    @RequestMapping("/sumbybillingmode")
    public InvoiceSummaryBillingMode summary(@RequestParam("mode_name") String mode_name, @RequestParam("start_date") String start_date, @RequestParam("end_date") String end_date) {

    	//Validate parameters
    	if(mode_name == ""){
    		//return "Mode Name not mentioned";
    	}
    	
    	CouchDBRestClient ServerRequest = new CouchDBRestClient();
    	String req_url = "/zaitoon_invoices/_design/invoice-summary/_view/sumbybillingmode?startkey=["+mode_name+","+start_date+"]&endkey=["+mode_name+","+end_date+"]";
    	System.out.println(req_url);
    	String ServerResponse = ServerRequest.get(req_url);
    	//Sample Response: {"rows":[ {"key":null,"value":{"sum":22873,"count":136,"min":0,"max":4043,"sumsqr":24057163}} ]}
    	
    	JSONObject ServerJSONResponse = new JSONObject(new JSONTokener(ServerResponse));
    	JSONArray rowsArray = ServerJSONResponse.getJSONArray("rows");
    	JSONObject resultObject = ((JSONObject) rowsArray.get(0)).getJSONObject("value");
    	
    	int summary_sum = resultObject.getInt("sum");
    	
    	SumByPaymentMode test_mode_1 = new SumByPaymentMode("PayTM", 100);
    	SumByPaymentMode test_mode_2 = new SumByPaymentMode("Cash", 240);
    	
    	List<SumByPaymentMode> payment_splits = new ArrayList<SumByPaymentMode> ();
    	payment_splits.add(0, test_mode_1);
    	payment_splits.add(1, test_mode_2);
    	
        return new InvoiceSummaryBillingMode(mode_name, summary_sum, payment_splits);
    }
}
