package com.burdysoft.debtr.resources;

import java.util.ArrayList;
import java.util.HashMap;

public class Stats {

	public ArrayList<HashMap<String, String>> getSummary(Debtr debtr) {
		//TODO returns a Hashmap of <Name, Ammount>
		
				ArrayList<HashMap<String,String>> peopledebtarray = new ArrayList<HashMap<String,String>>();
				
				//create an ArrayList of the values
				ArrayList<String> values = new ArrayList<String>(); 
				for (int m=0; m<debtr.getDebtors().size(); m++) {
					values.add("0.00");
				}
				
				
				try {
					ArrayList<Event> eventarray = debtr.getDebtevent();
					System.out.println(eventarray.size());
					

								
					for (int j=0; j<eventarray.size(); j++) {
						
						//get the event
						Event ev = eventarray.get(j);
						
						//get the payee
						String payee = ev.getPayee();
						
						//get the amount
						float amount = ev.getCost();
						
						//loop though each of the payees, and mkae some totals
						ArrayList<HashMap<String,String>> split = ev.getSplit();
						
						System.out.println("Key set " + split.get(0).keySet());
						System.out.println("Who paid " + payee);
						System.out.println("What was the amount " + amount);
						
						for (int k=0; k<split.size(); k++) {
							
							
							System.out.println("Split number " + k + " " +  split.get(k));
							
							
							if (debtr.getDebtors().get(k).getName() == payee) {
								float splitvalue = amount - Float.parseFloat(split.get(k).get("line2"));
								values.set(k, Float.toString(splitvalue + Float.parseFloat(values.get(k))));
							} else {
								float splitvalue = -Float.parseFloat(split.get(k).get("line2"));
								values.set(k, Float.toString(splitvalue + Float.parseFloat(values.get(k))));
							}
							
						}
						
						
					}
					
			//		peopledebtarray.ensureCapacity(debtr.getDebtors().size());
					
					for (int m = 0; m< debtr.getDebtors().size(); m ++ ) {
						HashMap<String,String> peopledebtmapping = new HashMap<String,String>();
						
					//	peopledebtarray.get(m).put("line1",debtr.getDebtors().get(m).getName());
						
				//		peopledebtarray.get(m).put("line2",values.get(m));
						
						
						peopledebtmapping.put("line1",debtr.getDebtors().get(m).getName());
						peopledebtmapping.put("line2",values.get(m));
						peopledebtarray.add(peopledebtmapping);
						
						System.out.println(peopledebtmapping);
						System.out.println("m"  + m);
						System.out.println("Array of hashamp size " + peopledebtarray.size());
						
						for (int l =0; l <=m; l++) {
							System.out.println("Debtarray item " + l + ": " + peopledebtarray.get(l));
						}
					}
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					
					for (int i=0; i<debtr.getDebtors().size(); i++) {
						HashMap<String,String> peopledebtmapping = new HashMap<String,String>();
						peopledebtmapping.put("line1",debtr.getDebtors().get(i).getName());
						peopledebtmapping.put("line2","0.00");
						peopledebtarray.add(peopledebtmapping);
						System.out.println(peopledebtmapping);
					}
					
				}
				
				//peopledebtarray.add(peopledebtmapping);
				System.out.println("At the end...");
				System.out.println(peopledebtarray);
				return peopledebtarray;

		
		
	}
	
	public double getTotalSpend(Debtr debtr) {
		//TODO returns the total spend of the events 
		
		return 0;
	}
	
	public int getTotalEvents(Debtr debtr) {
		//TODO returns the number of Events we have

		return 0;
	}
	
	
	public int getTotalPeople(Debtr debtr) {
		//TODO returns the number of people associated to the debtr
		
		return 0;
		
	}
	
	public ArrayList<HashMap<String, String>> getSummaryWithSign(Debtr debtr) {
	
		ArrayList<HashMap<String,String>> summarywithsign = new ArrayList<HashMap<String,String>>();
		summarywithsign = getSummary(debtr);
		
		for (int i=0; i<summarywithsign.size(); i++) {
			if (Float.parseFloat(summarywithsign.get(i).get("line2")) < 0) {
				summarywithsign.get(i).put("line2", "(£" +  String.format("%.2f",Math.abs(Float.parseFloat(summarywithsign.get(i).get("line2")))) + ")");
			} else {
				summarywithsign.get(i).put("line2", "£" +  String.format("%.2f",Float.parseFloat(summarywithsign.get(i).get("line2"))));
			}
			
		}
		
		return summarywithsign;
		
	}
}
