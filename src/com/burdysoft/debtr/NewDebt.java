package com.burdysoft.debtr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.burdysoft.debtr.resources.AllDebtr;
import com.burdysoft.debtr.resources.FileSaving;
import com.burdysoft.debtr.resources.People;
import com.burdysoft.debtr.resources.PeopleListAdapter;
import com.burdysoft.debtr.resources.Debtr;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class NewDebt extends ActionBarActivity {

	
	ListView list;
    PeopleListAdapter adapter;
    static ArrayList<People> peopleList = new ArrayList<People>();
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
			peopleList.add(new People("Me"));
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
        		peopleList.add(new People(newname));
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
		
		EditText description = (EditText) findViewById(R.id.account_description);
		EditText name = (EditText) findViewById(R.id.account_name);
		
		//ensure newaccount is empty.
		Debtr newaccount = new Debtr();
		
		newaccount.setDebtors(peopleList);
		newaccount.setDescription(description.getText().toString());
		newaccount.setName(name.getText().toString());
		
		//create todays date
		Calendar c = new GregorianCalendar();
	    c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    Date d1 = c.getTime(); 
		newaccount.setDate(d1);
		
		//create a variable we need for later.
		int debtrref;
		boolean exists = false;
		AllDebtr alldebtr = new AllDebtr();
		
		//Try and load existing AllDebtr object from file
		try  {

			alldebtr = FileSaving.readFile(content);
			exists = true;

			System.out.println("Opened");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if (exists == true) {
			
			//if it does exist, then just add the current Debt obj to the AllDebtr obr
			alldebtr.getDebtrList().add(newaccount);
			debtrref = alldebtr.getDebtrList().size() - 1;
		
		} else {
			
			//if it doesn't exist create one
			
			ArrayList<Debtr> debtrarraylist = new ArrayList<Debtr>();
			debtrarraylist.add(newaccount);
			alldebtr.setDebtrList(debtrarraylist);
			debtrref = debtrarraylist.size() - 1;
			
		}

		
			
		
		//save the file again
		try {
			FileSaving.saveFile(alldebtr, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
			
		Intent intent = new Intent(content, MainActivity.class);
		intent.putExtra("DebtrRef",debtrref);
		intent.putExtra("Exists", true);
		
		
		startActivity(intent);
		
		
	}
	
}
