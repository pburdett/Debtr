package com.burdysoft.debtr;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.burdysoft.debtr.resources.AllDebtr;
import com.burdysoft.debtr.resources.Debtr;
import com.burdysoft.debtr.resources.DebtrListAdapter;
import com.burdysoft.debtr.resources.FileSaving;
import com.burdysoft.debtr.resources.JsonMapper;

public class MainActivity extends ActionBarActivity  {

	AllDebtr alldebtr;
	Activity content = this;
	Context context = this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//try and load up the file...
		boolean exists = false;
		
	
		try {
			alldebtr = FileSaving.readFile(content);
			exists = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		if(savedInstanceState ==null ) {
			System.out.println("SavedInstancState is null");
		} else {
			System.out.println("SavedInstancState is NOT null");
		}
		
		System.out.println("Main: onCreate");
		super.onCreate(savedInstanceState);
		
		if (exists == false) {
			//we have no debtr's so we need a different page
			setContentView(R.layout.nodebtr);
			
		} else {
			
			setContentView(R.layout.activity_main);
			ListView list=(ListView)findViewById(R.id.debtrlist);
			
	
	
			
			
			
			
			
			
			
			if (exists == true) {
				//If we have opened a file succesfully, do something with the data;
				ArrayList<Debtr> debtrlist = alldebtr.getDebtrList();
				
				
				
				//fill in the listview
		        DebtrListAdapter adapter=new DebtrListAdapter(this, debtrlist, content);
		        list.setAdapter(adapter);
				
		        int x = debtrlist.size();
				
				for (int i=0; i<x; i++) {
					System.out.println(debtrlist.get(i).getName());
				}
		        
				
			}
			
			
			
			
			
		}
			
			
			//Does the app already exist?
			try {
				if (getIntent().getExtras().getBoolean("Exists") == true) {
					//This is what we are doing if the app has already been running
					
				}
			} catch (Exception e) {
				// This is what we do if the app didn't exist at start up
			
			}
		
		
			
		

		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.newdebt) {
			Intent intent = new Intent(this, NewDebt.class);
			intent.putExtra("Activity", this.getLocalClassName());
			intent.putExtra("Exists", true);
			intent.putExtra("Debtr", JsonMapper.writeJSON(alldebtr));
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	
	public void onStop() {
		
		System.out.println("Main: Stopped");
		super.onStop();
	}

	@Override
	public void onResume() {
		System.out.println("Main: Resumed");
		super.onResume();
	}
	
	@Override
	public void onPause() {
		System.out.println("Main: Paused");
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		
		System.out.println("Main: Destroyed");
		super.onDestroy();
	}
	
	@Override
	public void onRestart() {
		
		System.out.println("Main: Restart");
		super.onRestart();
	}
	
	@Override
	public void onStart() {
		
		System.out.println("Main: Start");
		super.onStart();
	}
	
	public void saveData(ArrayList<Debtr> d) {
		System.out.println("Called");
		
		AllDebtr ad = new AllDebtr();
		ad.setDebtrList(d);
		
		try {
			FileSaving.saveFile(ad, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
	}

}
