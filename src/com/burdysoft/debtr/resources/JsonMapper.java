package com.burdysoft.debtr.resources;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

	private static AllDebtr alldebtr;
	
	public static String writeJSON(AllDebtr alldebtr) {
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonstring = "";

		try {
			jsonstring = mapper.writeValueAsString(alldebtr);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
		return jsonstring;
		
	}
	
	
	public static AllDebtr readJSON(String jsonstring) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			alldebtr = mapper.readValue(jsonstring, AllDebtr.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alldebtr;

	}
	
	
	
}
