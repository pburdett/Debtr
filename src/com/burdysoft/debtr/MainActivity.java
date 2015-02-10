package com.burdysoft.debtr;

import helper.DatabaseHelper;
import helper.Debtr;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.burdysoft.debtr.resources.DebtrListAdapter;
import com.burdysoft.debtr.resources.DebtrDeletedListAdapter;

public class MainActivity extends ActionBarActivity  {

	Activity content = this;
	Context context = this;
	
	DatabaseHelper db;
	List<Debtr> debtrs;

    String status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		db = new DatabaseHelper(getApplicationContext());
		
		
		//try and get all the Debtr objects from the database
		
		debtrs = new ArrayList<Debtr>();

		try {
			status = getIntent().getExtras().getString("status");
			
			System.out.println("My Status " + status);
			
			if (status.matches("Active")) {
				debtrs = db.getAllActiveDebtr();
				System.out.println("My Status " + status);
				
			} else if (status.matches("Deleted")) {
				debtrs = db.getAllDeletedDebtr();
				System.out.println("My Status " + status);
				
			} else if (status.matches("Settled")) {
				System.out.println("My Status " + status);
				debtrs = db.getAllSettledDebtr();
				
			} 
			
		} catch (Exception e) { 
			e.printStackTrace();
			
			//if we can't get anywhere then we show all active.
			debtrs = db.getAllActiveDebtr();
			status = "Active";
		}
		
		
		if (debtrs.size() == 0) {
            //then we don't have anything in the db, so lets make a view saying add!
            setContentView(R.layout.nodebtr);
            return;

    /*    } else if (status.matches("Deleted")) {
            setContentView(R.layout.main_deleted);
            TextView statustv = (TextView) findViewById(R.id.status);
            statustv.setText(status);
      */
		} else {
			setContentView(R.layout.activity_main);
			TextView statustv = (TextView) findViewById(R.id.status);
			statustv.setText(status);
			
		}
		
		
		//Now do whatever we need to do...
			
		//fill in the listview depending on what we are looking at...
		ListView list=(ListView)findViewById(R.id.debtrlist);

        if (status.matches("Deleted")) {
            DebtrDeletedListAdapter adapter=new DebtrDeletedListAdapter(this, debtrs, content);
            list.setAdapter(adapter);
        } else {
            DebtrListAdapter adapter=new DebtrListAdapter(this, debtrs, content);
            list.setAdapter(adapter);
        }
        
        //close the database connection
        db.close();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        //select the menu dependent on whether we are active, deleted, settled etc.
        if (status.matches("Active")) {
            getMenuInflater().inflate(R.menu.main, menu);

        } else if (status.matches("Deleted")) {
            getMenuInflater().inflate(R.menu.maindeleted, menu);

        } else if (status.matches("Settled")) {
            //Note this needs to be changed
            getMenuInflater().inflate(R.menu.maindeleted, menu);
        }

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
			startActivity(intent);
			return true;
		/*} else if (id == R.id.settled) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("Activity", this.getLocalClassName());
			intent.putExtra("status", "Settled");
			startActivity(intent);
			return true; */
		} else if (id == R.id.deleted) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("Activity", this.getLocalClassName());
			intent.putExtra("status", "Deleted");
			startActivity(intent);
			return true;
		} else if (id == R.id.active) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("Activity", this.getLocalClassName());
			intent.putExtra("status", "Active");
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
	

}
