package com.burdysoft.debtr.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class People {

	String name;
	
	public People() {
		
	}
	
	public People(String name) {
		this.name = name;
	}
	
	@JsonProperty
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
