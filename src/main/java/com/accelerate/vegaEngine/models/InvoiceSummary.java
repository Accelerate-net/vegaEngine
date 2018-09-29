package com.accelerate.vegaEngine.models;

public class InvoiceSummary {

    private long id;
    private String content;

    public InvoiceSummary(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent(){
    	return content;
    }
}

