package com.burdysoft.debtr;

import helper.DatabaseHelper;
import helper.Debtr;
import helper.Event;
import java.util.ArrayList;
import java.util.List;

import com.burdysoft.debtr.resources.ViewAllBillDetailsAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class ViewBills  extends Activity implements OnItemClickListener {
	
	ViewBills content = this;
    SimpleAdapter adapter;
    ListView billdetaillist;
    int debtr_id;
    Debtr  debtr;
    
 
    ListView list;
    Dialog listDialog;
	
    DatabaseHelper db;
    
    List<Event> events;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_bill);
		
		db = new DatabaseHelper(getApplicationContext());
		
		//get the position from the extras
		debtr_id = Integer.parseInt(getIntent().getExtras().getString("DebtrRef"));
		
		//get all the Events for the debtr_id
		events = new ArrayList<Event>();
		events = db.getAllEventsByDebtrId(debtr_id);
	
		debtr = db.getDebtr(debtr_id);
		
		ViewAllBillDetailsAdapter adapter2 = new  ViewAllBillDetailsAdapter(content, this, events);
		billdetaillist = (ListView)findViewById(R.id.billlist);
		billdetaillist.setAdapter(adapter2);
				
		//close database
		db.close();
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	

	

	
	
}
