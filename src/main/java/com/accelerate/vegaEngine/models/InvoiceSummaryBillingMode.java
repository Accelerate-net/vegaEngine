package com.accelerate.vegaEngine.models;

import java.util.List;

public class InvoiceSummaryBillingMode {

	private boolean status;
	private int statusCode;
	private String error;
	
    private List<SumByBillingMode> response; //List of sum-by-billing-modes: Eg: { "name": "Dine In", "amount": 10000, "count": 45 }
    

    public InvoiceSummaryBillingMode(boolean requestStatus, Integer requestStatusCode, String errorMessage, List<SumByBillingMode> modes_list) {
        this.status = requestStatus;
        this.statusCode = requestStatusCode;
        this.error = errorMessage;
        this.response = modes_list;
    }

	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}


	public List<SumByBillingMode> getResponse() {
		return response;
	}


	public void setResponse(List<SumByBillingMode> response) {
		this.response = response;
	}

	
}

/* Summary by Billing Modes 

{
	"status": true,
	"statusCode": 200,
	"error": "",
	"response": [{
			"mode": "Dine",
			"amount": 24000,
			"count": 82
		},
		{
			"mode": "Delivery",
			"amount": 8000,
			"count": 37
		}
	]
}

*/