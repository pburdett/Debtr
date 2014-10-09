package com.burdysoft.debtr.resources;

import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Debtr {

	String name; //name of the Debt'r Account
	String description; //optional description
	ArrayList<People> debtors; //ArrayList of People 
	ArrayList<Event> debtevent; //ArrayList of "Events", ie the events that resulted in a debt.
	Date date; //date the debtr was created
	
	public Debtr() {
		//JSON deserializer
	}
	
	public Debtr(String name, String description, ArrayList<People> debtors, ArrayList<Event> debtevent, Date date) {
		this.name = name;
		this.description = description;
		this.debtors = debtors;
		this.debtevent = debtevent;
		this.date = date;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDebtors(ArrayList<People> debtors) {
		this.debtors = debtors;
	}
	
	public void setDebtevent(ArrayList<Event> debtevent) {
		this.debtevent = debtevent;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	@JsonProperty
	public String getName() {
		return name;
	}
	
	@JsonProperty
	public String getDescription() {
		return description;
	}
	
	@JsonProperty
	public ArrayList<People> getDebtors() {
		return debtors;
	}
	
	@JsonProperty
	public ArrayList<Event> getDebtevent() {
		return debtevent;
	}
	  
	@JsonProperty
	public Date getDate() {
		return date;
	}
	
	
	
}
