package com.burdysoft.debtr.resources;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AllDebtr {

	ArrayList<Debtr> debtrList = new ArrayList<Debtr>();
	
	public AllDebtr() {
		//empty constructor
	}
	
	public AllDebtr(ArrayList<Debtr> debtrList) {
		this.debtrList = debtrList;
	}
	
	public void setDebtrList(ArrayList<Debtr> debtrList) {
		this.debtrList = debtrList;
	}
	
	@JsonProperty
	public ArrayList<Debtr> getDebtrList() {
		return debtrList;
	}
	
}
