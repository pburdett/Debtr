package com.burdysoft.debtr;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.burdysoft.debtr.resources.AllDebtr;
import com.burdysoft.debtr.resources.Debtr;
import com.burdysoft.debtr.resources.FileSaving;

public class ViewBills  extends Activity implements OnItemClickListener {
	
	ViewBills content = this;
    SimpleAdapter adapter;
    ListView billdetaillist;
    int position;
    Debtr  debtr;
    
 
    ListView list;
    Dialog listDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_bill);
		
		String fileName = "TestFile.txt";
		AllDebtr alldebtr = new AllDebtr();
		
		//get the position from the extras
		position = Integer.parseInt(getIntent().getExtras().getString("DebtrRef"));
		
		
		
		//read the file
		alldebtr = FileSaving.readFile(content);
	
		
		//get the Debtr item from the file
		debtr = alldebtr.getDebtrList().get(position);
		
		
		ArrayList<HashMap<String, String>> summary = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i< debtr.getDebtevent().size(); i++) {
			HashMap<String, String> eventsummary = new HashMap<String,String>();
			
			//format the date
			SimpleDateFormat formatter = new SimpleDateFormat("dMMMyy");
	  	  	TextView dateView = (TextView) findViewById(R.id.eventdate);
	  	  	String datetime = "";
	  	  
			try {
				datetime = formatter.format(debtr.getDebtevent().get(i).getDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			eventsummary.put("date", datetime);
			eventsummary.put("name", debtr.getDebtevent().get(i).getName());
			eventsummary.put("payee", debtr.getDebtevent().get(i).getPayee());
			eventsummary.put("amount","£" + String.format("%.2f", debtr.getDebtevent().get(i).getCost()));
			
			summary.add(eventsummary);
			
		}
		
		
		
		
		
		
		adapter = new SimpleAdapter(this, summary,
    		 	R.layout.view_bill_detail_list,
    	        new String[] { "date","name","payee","amount" },
    	        new int[] {R.id.date, R.id.billname, R.id.payee, R.id.amount});
     
		billdetaillist = (ListView)findViewById(R.id.billlist);
		billdetaillist.setAdapter(adapter);
		
		
		
		billdetaillist.setOnItemClickListener(new OnItemClickListener() {
	         

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
		
				
				System.out.println("Arg2 " + arg2);
				
				
				//create a list of all the people and splits.
				
				String[] values = new String[debtr.getDebtevent().get(arg2).getSplit().size()];
				
						for (int i=0; i<debtr.getDebtevent().get(arg2).getSplit().size(); i++) {
						//	System.out.println(debtr.getDebtevent().get(arg2).getSplit().get(i));
							values[i] = debtr.getDebtevent().get(arg2).getSplit().get(i).get("line1") + ": £" + String.format("%.2f", Float.parseFloat(debtr.getDebtevent().get(arg2).getSplit().get(i).get("line2")));
						//	System.out.println(values[i]);
						}
						
					
						
				showdialog(values);
				
				
				
			}
		});
		
		
		
	
		
		
	}
	
	private void showdialog(String[] val)
    {
        listDialog = new Dialog(this);
        listDialog.setTitle("Select Item");
         LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View v = li.inflate(R.layout.split_list_parent, null, false);
         listDialog.setContentView(v);
         listDialog.setCancelable(true);
         
         listDialog.setTitle("Bill Split:");

 
         ListView list1 = (ListView) listDialog.findViewById(R.id.splitlistparent);
         list1.setOnItemClickListener(this);
         list1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, val));
         //now that the dialog is set up, it's time to show it
         listDialog.show();
    }
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    { }

	

	
	
}
