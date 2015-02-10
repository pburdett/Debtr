package com.burdysoft.debtr;

import helper.DatabaseHelper;
import helper.Debtr;
import helper.Event;
import helper.People;
import helper.Split;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.burdysoft.debtr.resources.DecimalInputFilter;
import com.burdysoft.debtr.resources.NewPayeeSpinner;
import com.burdysoft.debtr.resources.PeopleSplitAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class NewBill extends ActionBarActivity {
	
	int debtr_id;
	Debtr debtr; 
	Activity content = this;
	
	NewBill activity = null;
	NewPayeeSpinner nps;
	
	//photorequirements
	String photofilename;
	boolean taken;
	protected static final String PHOTO_TAKEN = "photo_taken";
	protected static final String PHOTO_FILE = "photo_file";
	
	
	//for the datepicker
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111; 
    
    ArrayList<String> payeeList = null;
    ListView peoplelist;
    RadioGroup radioGroup;
    EditText amount_et;
    Spinner  SpinnerPayee;
    
    List<People> peoples;
    ArrayList<HashMap<String,String>> peoplevaluelist;
    SimpleAdapter adapter;
    PeopleSplitAdapter adapter2;
    
    AlertDialog alertDialog;
    
    DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_event);
		
		db = new DatabaseHelper(getApplicationContext());
		
					
		//get the details of what I am playing with
		debtr_id = Integer.parseInt(getIntent().getExtras().getString("DebtrRef"));	
		
		//get the people associated with the debtr
		peoples = db.getAllPeopleDebtrID(debtr_id);
		
		System.out.println("Peoples return " + peoples.size() + " rows");
		
		 peoplevaluelist = new ArrayList<HashMap<String,String>>();
         HashMap<String,String> peoplevalueitem;
         
         
         for (int j=0; j< peoples.size();j++) {
        	 peoplevalueitem = new HashMap<String,String>();
        	 peoplevalueitem.put("line1",peoples.get(j).getName());
        	 peoplevalueitem.put("line2","0.00");
        	 peoplevalueitem.put("id", Integer.toString(peoples.get(j).getId()));
        	 peoplevaluelist.add(peoplevalueitem);
         }
         
         
        adapter2 = new PeopleSplitAdapter(this,peoplevaluelist,content);
		peoplelist = (ListView)findViewById(R.id.peoplelist);
		peoplelist.setAdapter(adapter2);
		
		
		//upload a picture only if not taken!
		ImageView photoimage = (ImageView) findViewById(R.id.photo);
		
		if (taken != true) {
		//	 ImageView photoimage = (ImageView) findViewById(R.id.photo);
			 photoimage.setImageResource(R.drawable.upload);
		}
		
		photoimage.setOnClickListener(new OnClickListener() {
       	 	@Override
       	 	public void onClick(View v) {
       	 		startCameraActivity();
       	 	}
		});
		
		
		
		//Set Current Date
        //Set Date things
        
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        TextView dateText = (TextView) findViewById(R.id.eventdate);
        dateText.setText(new StringBuilder()
        // Month is 0 based, just add 1
        .append(day).append("-").append(month+1).append("-")
        .append(year));
        
   
         
         //OnClickLsitener for the Date Text to edit it...
         
         dateText.setOnClickListener(new OnClickListener() {
        	 @Override
             public void onClick(View v) {
                  
                 // On button click show datepicker dialog 
                 showDialog(DATE_PICKER_ID);
  
             }
        	 
         });
		

         
         
		 //et up variables for the payee list spinner
		 activity = this;
		 SpinnerPayee = (Spinner)findViewById(R.id.payee_spinner);
         Resources res =getResources();
         
         // Create custom adapter objec
         nps = new NewPayeeSpinner(activity, R.layout.event_payee_spinner, peoples,res);
         
         // Set adapter to spinner
         SpinnerPayee.setAdapter(nps);
         
         
         // Listener called when spinner item selected         
         SpinnerPayee.setOnItemSelectedListener(new OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                updateAmounts();
             }
  
             @Override
             public void onNothingSelected(AdapterView<?> parentView) {
                 // your code here
             }
  
         });
         
         // Set the amount figure to 0
         amount_et = (EditText) findViewById(R.id.amount);
         amount_et.setText("0.00");
         amount_et.setFilters(new InputFilter[] {new DecimalInputFilter(5,2)});
         
         //add a listener for the amount_Et
         amount_et.addTextChangedListener(new TextWatcher(){
        	 @Override
        	 public void afterTextChanged(Editable s) {
               updateAmounts();
             }

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
         });
         
         
         
         //behaviour of the Radio buttons
         radioGroup = (RadioGroup) findViewById(R.id.radio_split);        
         radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
         {
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 // update the amounts
            	 updateAmounts();
             }
         });

         //on create we also want to update the amounts
         updateAmounts();
  
        
	}

	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_PICKER_ID:
             
            // open datepicker dialog. 
            // set date picker for current date 
            // add pickerListener listner to date picker
            DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month,day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }
        return null;
    }
 
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
 
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                int selectedMonth, int selectedDay) {
             
            year  = selectedYear;
            month = selectedMonth; 	
            day   = selectedDay;
 
            // Show selected date 
            TextView dateText = (TextView) findViewById(R.id.eventdate);
            dateText.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year).append(" "));
     
           }
        };
	
	
          
        
      public void updateAmounts() {
    	  
    	  
    	  //get the id of the radio button selected
    	  // radioGroup = (RadioGroup) findViewById(R.id.radio_split);
    	  int checkedId = radioGroup.getCheckedRadioButtonId();
    	  
    	  //get the id of the user that is the payee
    	  int payeeId = SpinnerPayee.getSelectedItemPosition();
    	  
    	  float amount = 0;
    	  //get the amount
    	  if ("".equals(amount_et.getText().toString())) {
    		  //avoid unwanted Cannot parse "" as float error that crashes app
    		  System.out.println("we are here");
    		  return;
    	  } else {
    		  amount = Float.parseFloat(amount_et.getText().toString());
    	  }
   		 
    	  
    	  System.out.println(checkedId);
    	  
    	  //check we have a selected button and good amount
    	  if (amount == 0) {
    		  return;
    	  } else if (checkedId == -1) {
    		  return;
    	  }
   		 

   		 //how many People do we have
   		 int numPeople = peoples.size();
   		 
   		 //find which radio button we are talking baout...
   		 RadioButton whichbutton = (RadioButton) findViewById(checkedId);
   		 
	   	 //behaviour under different cases
	   	    switch (whichbutton.getText().toString()) {
	            case "Equal Split":
	                     //we have Equal Split
	            		
	            		for (int i=0; i<numPeople; i++) {   			
	            			peoplevaluelist.get(i).put("line2", String.format("%.2f",(amount/numPeople)));
	            			
	            		}          
	            		break;
	            
	            case "All Other":
	            		//we have All other split
	            		
	            	for (int i=0; i<numPeople; i++) {
	            		if(i != payeeId) {
	            			peoplevaluelist.get(i).put("line2", String.format("%.2f",(amount/(numPeople-1))));
		            	} else {
		            		peoplevaluelist.get(i).put("line2", "0.00");
		            	}
	            	}
	            	break;
	            	
	            case "Other":
	            	//do nothing	
      			}
	            
	   	    peoplelist.setAdapter(adapter2);

      }
	
      
      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          // Inflate the menu items for use in the action bar
          MenuInflater inflater = getMenuInflater();
          inflater.inflate(R.menu.neweventmenu, menu);
          return super.onCreateOptionsMenu(menu);
      }
     
      @Override
  	public boolean onOptionsItemSelected(MenuItem item) {
  		// Handle action bar item clicks here. The action bar will
  		// automatically handle clicks on the Home/Up button, so long
  		// as you specify a parent activity in AndroidManifest.xml.
  		int id = item.getItemId();
  		if (id == R.id.saveevent) {
  			saveEvent();
  			return true;
  		} else if (id == R.id.add_photo) {
  			startCameraActivity();
  			return true;
  		}
  		return true;
      }
  		
      
      
	public void saveEvent() {
		
		//check if the numbers all add up
		if(billCheck()==true) {
		
			
			//get all the data we need
			EditText eventname_et = (EditText) findViewById(R.id.name);
			String eventname = eventname_et.getText().toString(); 
			
//		
			int payee_id = (int) ((People) SpinnerPayee.getSelectedItem()).getId();
			
			
			float amount = Float.parseFloat(amount_et.getText().toString());
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	  	  	TextView dateView = (TextView) findViewById(R.id.eventdate);
	  	  	Date datetime = null;
	  	  
			try {
				datetime = formatter.parse(dateView.getText().toString());
				System.out.println("datetime: " + datetime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Event ev = new Event();
			ev.setName(eventname);
			ev.setDate(datetime.toString());
		System.out.println(datetime.toString());
			//	ev.setDate(datetime);
			ev.setCost(amount);
			ev.setPayee_id(payee_id);
			ev.setDebtr_id(debtr_id);
			
			if (taken == true) {
				ev.setPhototaken(1);
				ev.setPhotofile(photofilename);
			} else {
				ev.setPhototaken(0);
				ev.setPhotofile("");
			}
			
			
			
			
			
			int event_id = (int) db.createEvent(ev);
			
			
			
			for (int i = 0; i<peoples.size(); i++) {
				
				Split split = new Split();
				split.setEvent_id(event_id);
				split.setAmount(Float.parseFloat(peoplevaluelist.get(i).get("line2")));
				split.setPeople_id(Integer.parseInt(peoplevaluelist.get(i).get("id")));
						
				db.createSplit(split);
			}
			
			
			db.close();
			
				
				//launch my new intent
				Intent intent = new Intent(content, ViewDebtr.class);
				intent.putExtra("DebtrRef",Integer.toString(debtr_id));
				
				startActivity(intent);
					
		} 			
		
	}
	
	
	
	public boolean billCheck() {
		
		//get the amount we actually have
		float amount = Float.parseFloat(amount_et.getText().toString());
		float total = 0;
		
		//get the sum of the values in the ListView
		for (int i=0; i<peoplevaluelist.size(); i++) {
			total = total + Float.parseFloat(peoplevaluelist.get(i).get("line2"));
			System.out.println(peoplevaluelist.get(i).get("line2"));
		}
		
		//compare these values
		if (total == amount) {
			//we are good
			return true;
		} else {
			//we need to throw a dialog error
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			
			alertDialogBuilder
			.setCancelable(false)
			.setTitle("Error:")
			.setMessage("Splits do not total correctly.")
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
	
	                alertDialog.cancel();
	               		    	
			    }
			  });
			
			  alertDialog = alertDialogBuilder.create();
			  alertDialog.show();
			  
			//return false
			return false;
		}
	}
	
	//activity to start the camera
	protected void startCameraActivity()
	{
		
		photofilename = "/sdcard/Debtr/images/";
		
		//make a directory for the pics
		File directory = new File(photofilename);
		directory.mkdirs();
		
		//count how many files in photos directory
		File dir = new File(photofilename);
		File childfile[] = dir.listFiles();
		int counter = 1;
		for (File file2 : childfile) {
			counter++;
		}
		
		System.out.println("how many files?" + counter);
		
		
		photofilename = photofilename + counter + ".jpg";
	    File file = new File( photofilename );
	    Uri outputFileUri = Uri.fromFile( file );
	    	
	    Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
	    cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri );
	    	
	    startActivityForResult( cameraintent, 0);
	}
      
	
	
	//called when the camera activity is over!
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{	

		System.out.println("Result code?" + resultCode);
	    switch( resultCode )
	    {
	    	case 0:
	    		System.out.println("Photo not taken");
	    		break;
	    			
	    	case -1:
	    		System.out.println("Here we are... hoto done?");
	    		onPhotoTaken(photofilename);
	    		break;
	    }
	}

	
	
	//called after the photo was taken.
	protected void onPhotoTaken(String filename)
	{
	
		System.out.println("Here!!!! Photo taken");
		
		taken = true;
	    	
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 2;
	    	
	    Bitmap bitmap = BitmapFactory.decodeFile( filename, options );
	//    System.out.println(filename);
	    ImageView photoimage = (ImageView) findViewById(R.id.photo);
	    photoimage.setImageBitmap(bitmap);
	    	
	}
	
	
	//deals with the phone going to landscape and not having to save a new picture
	
	@Override
	protected void onSaveInstanceState( Bundle outState ) {
	    outState.putBoolean(NewBill.PHOTO_TAKEN, taken );
	    outState.putString(NewBill.PHOTO_FILE, photofilename);
	}
	
	@Override 
	protected void onRestoreInstanceState( Bundle savedInstanceState)
	{
	    if( savedInstanceState.getBoolean(NewBill.PHOTO_TAKEN) ) {
	    	onPhotoTaken(savedInstanceState.getString(NewBill.PHOTO_FILE));
	    	taken = true;
	    	photofilename = savedInstanceState.getString(NewBill.PHOTO_FILE);
	    }
	}
	
	
	public void onBackPressed(){
		
		//we need to delete the picture if we go back!
		if (taken == true) {
			File file = new File(photofilename);
			file.delete();
			taken=false;
		}
		
		super.onBackPressed();
		}
}


	
