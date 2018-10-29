package com.accelerate.vegaEngine.models;

public class SumByBillingMode {
	
	private String name;
    private Integer amount;
    private Integer count;
    
    public SumByBillingMode(String name, Integer amount, Integer count) {
        this.name = name;
        this.amount = amount;
        this.count = count;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
    
}
