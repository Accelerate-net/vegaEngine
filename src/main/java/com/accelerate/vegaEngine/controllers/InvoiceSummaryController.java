package com.accelerate.vegaEngine.controllers;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accelerate.vegaEngine.models.*;


import com.accelerate.vegaEngine.CouchDBRestClient;
import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.JSONArray;


@RestController
public class InvoiceSummaryController {

    @RequestMapping("/sumbybillingmode")
    public String summary(@RequestParam("mode_name") String mode_name, @RequestParam("start_date") String start_date, @RequestParam("end_date") String end_date) {

    	//Validate parameters
    	if(mode_name == ""){
    		return "Mode Name not mentioned";
    	}
    	
    	String DatabaseName = "";
    	
    	CouchDBRestClient ServerRequest = new CouchDBRestClient();
    	String req_url = "/zaitoon_invoices/_design/invoice-summary/_view/sumbybillingmode?startkey=["+mode_name+","+start_date+"]&endkey=["+mode_name+","+end_date+"]";
    	System.out.println(req_url);
    	String ServerResponse = ServerRequest.get(req_url);
    	//Sample Response: {"rows":[ {"key":null,"value":{"sum":22873,"count":136,"min":0,"max":4043,"sumsqr":24057163}} ]}
    	
    	JSONObject ServerJSONResponse = new JSONObject(new JSONTokener(ServerResponse));
    	JSONArray rowsArray = ServerJSONResponse.getJSONArray("rows");
    	JSONObject resultObject = ((JSONObject) rowsArray.get(0)).getJSONObject("value");
    	
    	int summary_sum = resultObject.getInt("sum");
    	
    	String result = "The total sales is Rs. "+summary_sum;
    	
    	System.out.println(summary_sum);
    	
        return result;//ServerJSONResponse.getString("rows");
        
      //  return new InvoiceSummary(mode_name, start_date, end_date);
    }
}

/* Summary by Billing Modes 

[{
"mode": "Dine",
"amount": 24000,
"splits": [{
	"name": "Cash",
	"amount": 10000
}, {
	"name": "Card",
	"amount": 14000
}]
},
{
"mode": "Delivery",
"amount": 8000,
"splits": [{
	"name": "Prepaid",
	"amount": 8000
}]
}
]

*/