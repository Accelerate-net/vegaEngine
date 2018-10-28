package com.accelerate.vegaEngine.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryDate {
	
	private String date;
	private String errorMessage;

    public SummaryDate(String input_date) {
        this.date = input_date;
        this.errorMessage = "";
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public boolean isValidDate() {
        try {
        	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        	format.setLenient(false);
        	Date parsed_date = format.parse (date); 

            return true;
        } catch (ParseException e) {
        	this.errorMessage = "Invalid Date Format. Date has to be in YYYYMMDD format.";
            return false;
        }	
	}
	
	public String validationMessage(){
		
		return errorMessage;
	}
}
