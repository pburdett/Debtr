package com.burdysoft.debtr.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {
	
	int debtid;
	int eventid;
	Date date;
	String name;
	String payee;
	float cost;
	ArrayList<HashMap<String,String>> split;
	boolean phototaken;
	String photofile;
	
	public Event() {
		//JSON deserilizer
	}
	
	public Event(int debtid, int eventid, Date date, String name, String payee, float cost, ArrayList<HashMap<String,String>> split, boolean phototaken, String photofile) {
		this.debtid = debtid;
		this.eventid = eventid;
		this.date = date;
		this.name = name;
		this.payee = payee;
		this.cost = cost;
		this.split = split;
		this.phototaken = phototaken;
		this.photofile = photofile;
	}
	
	public void setDebtid(int debtid) {
		this.debtid = debtid;
	}
	
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	public void setCost(float cost) {
		this.cost = cost;
	}
	
	public void setSplit(ArrayList<HashMap<String,String>> split) {
		this.split = split;
	}
	
	public void setPhototaken(boolean phototaken) {
		this.phototaken = phototaken;
	}
	
	public void setPhotofile(String photofile) {
		this.photofile = photofile;
	}  
	
	@JsonProperty
	public int getDebtid() {
		return debtid;
	}
	
	@JsonProperty
	public int getEventid() {
		return eventid;
	}
	
	@JsonProperty
	public Date getDate() {
		return date;
	}
	
	@JsonProperty
	public String getName() {
		return name;
	}
	
	@JsonProperty
	public String getPayee() {
		return payee;
	}
	
	@JsonProperty
	public float getCost() {
		return cost;
	}
	
	@JsonProperty
	public ArrayList<HashMap<String,String>> getSplit() {
		return split;
	}	
	 
	@JsonProperty
	public boolean getPhototaken() {
		return phototaken;
	}
	
	@JsonProperty
	public String getPhotofile() {
		return photofile;
	} 

}
