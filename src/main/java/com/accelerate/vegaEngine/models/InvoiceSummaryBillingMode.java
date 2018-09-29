package com.accelerate.vegaEngine.models;

import java.util.List;

public class InvoiceSummaryBillingMode {

    private String mode;
    private Integer amount;
    private List<SumByPaymentMode> splits; //List of sum-by-payment-modes: Eg: { "name": "Cash", "amount": 10000 }
    

    public InvoiceSummaryBillingMode(String mode, Integer amount, List<SumByPaymentMode> splits) {
        this.mode = mode;
        this.amount = amount;
        this.splits = splits;
    }


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public Integer getAmount() {
		return amount;
	}


	public void setAmount(Integer amount) {
		this.amount = amount;
	}


	public List<SumByPaymentMode> getSplits() {
		return splits;
	}


	public void setSplits(List<SumByPaymentMode> splits) {
		this.splits = splits;
	}
}

/* Summary by Billing Modes 

[
{
"mode": "Dine",
"amount": 24000,
"splits": [{           		<-- SumByPaymentMode (Controller)
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