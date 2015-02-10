package com.burdysoft.debtr;

import helper.DatabaseHelper;
import helper.Debtr;
import helper.People;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.burdysoft.debtr.resources.PeopleListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class NewDebt extends ActionBarActivity {

	
	ListView list;
    PeopleListAdapter adapter;
    static ArrayList<String> peopleList = new ArrayList<String>();
    Activity content = this;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		System.out.println("NewDebt: onCreate");
		
		//onCreate + SetContentView
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_debt);
		
		
		
		if(savedInstanceState ==null ) {
			//it means we havent't rotated the screen, so add a default person to the list
			peopleList.clear(); //clear incase we have some poeple in the list already
			peopleList.add("Me");
		} else {
			//we have come from a rotated screen
			
			//nothing to do here
			
		}
		
		list=(ListView)findViewById(R.id.userlist);
        adapter=new PeopleListAdapter(this, peopleList);
        list.setAdapter(adapter);
        
        
        //Add button having changed the name
        ImageButton add_button = (ImageButton) findViewById(R.id.addbutton);
        final EditText new_person = (EditText) findViewById(R.id.newperson);
        add_button.setOnClickListener(new View.OnClickListener() {
            
        	@SuppressLint("ResourceAsColor")
			@Override
            public void onClick(View v) {
            	
            	//hide the keyboard
            	InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
            	inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            	
            	
        		String newname = new_person.getText().toString();
        		peopleList.add(newname);
        		System.out.println("We are here " + newname);
        		list.setAdapter(adapter);
        		new_person.setText("Add new debt'r");
        		new_person.setTextColor(R.color.black);
            }
        });
        
        //On CLick on the newname text box behavuour
        new_person.setOnClickListener(new View.OnClickListener() {
            
        	@SuppressLint("ResourceAsColor")
			@Override
            public void onClick(View v) {
        		new_person.setText("");
        		new_person.setTextColor(R.color.black);
        	
        	}
            	
        });
        
     
        
        
	}

	@Override
	public void onResume() {
		System.out.println("NewDebt: Resume");
		super.onResume();
	}
	
	@Override
	public void onStop() {
		
		System.out.println("NewDebt: Stopped");
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		
		System.out.println("NewDebt: Destroyed");
		super.onDestroy();
	}
	
	@Override
	public void onPause() {
		
	/*	//empty the default arraylist of people when we closes the page
		peopleList.clear();   */
		
		System.out.println("NewDebt: Paused");
		super.onPause();
	}
	
	@Override
	public void onRestart() {
		
		System.out.println("NewDebt: Restart");
		super.onRestart();
	}
	
	@Override
	public void onStart() {
		
		System.out.println("NewDebt: Start");
		super.onStart();
	}
	
	 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newaccountmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
   
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.saveevent) {
			createAccount();
			return true;
		}
		return true;
    }

	public void createAccount() {
		
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		
		EditText description = (EditText) findViewById(R.id.account_description);
		EditText name = (EditText) findViewById(R.id.account_name);
		
		//ensure newaccount is empty.
		Debtr newaccount = new Debtr();
		
		newaccount.setName(name.getText().toString());
		newaccount.setDescription(description.getText().toString());
		
		//create todays date
		Calendar c = new GregorianCalendar();
	    c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    Date d1 = c.getTime(); 
		newaccount.setDate(d1.toString());
		System.out.println(d1.toString());
		
		
		//insert the Debtr into the table
		
		long debtr_id;
		debtr_id = db.createDebtr(newaccount);
		
		
		
		
		for (int i = 0 ; i < peopleList.size(); i++) {
			
			People people = new People();
			people.setName(peopleList.get(i));
			people.setDebtr_id((int)debtr_id);
			
			//long people_id = db.createPeople(people);
			db.createPeople(people);
		}
		
		
		db.close();
		
			
		Intent intent = new Intent(content, MainActivity.class);
		intent.putExtra("DebtrRef",debtr_id);
		
		
		startActivity(intent);
		
		
	}
	
}
