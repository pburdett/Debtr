package com.burdysoft.debtr.resources;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.burdysoft.debtr.NewBill;
import com.burdysoft.debtr.R;

public class PeopleSplitAdapter extends BaseAdapter implements OnClickListener {
 
	private Context context;
    private Activity activity;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;

	TextView value_et;
	AlertDialog alertDialog;
	View promptsView;

	
	
 
    public PeopleSplitAdapter(Activity a, ArrayList<HashMap<String,String>> d, Context context) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position; 
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.people_list_value, null);
        }
        
       TextView name_tv = (TextView) vi.findViewById(R.id.name);
       value_et = (TextView) vi.findViewById(R.id.value);
       
       name_tv.setText(data.get(position).get("line1"));
       value_et.setText(data.get(position).get("line2")); 
        
        
        
       
       //set dialog pop up for clicking on the edittext
       value_et.setOnClickListener(new OnClickListener() 
       { 
           @Override
           public void onClick(View v) 
           {
           	System.out.println("Hello");
           	
           	// get view
			LayoutInflater li = LayoutInflater.from((NewBill)context);
			promptsView = li.inflate(R.layout.people_value_popup, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((NewBill)context);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView.findViewById(R.id.value);
			
			userInput.setText(data.get(position).get("line2"));
			userInput.setFilters(new InputFilter[] {new DecimalInputFilter(5,2)});
			
			TextView userInputLabel = (TextView) promptsView.findViewById(R.id.name);
			userInputLabel.setText(data.get(position).get("line1") + ":");
			
			
			//make the "done button" do same as "Ok"
			userInput.setOnEditorActionListener(new OnEditorActionListener() {
		        @Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) ) {
		               // update the data
		            	data.get(position).put("line2", String.format("%.2f",Float.parseFloat(userInput.getText().toString())));
		                notifyDataSetChanged();
		            
		                //close the dialog
		                alertDialog.cancel();
		            
		            }    
		            return false;
		        }
		    });
			

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setTitle("Edit Amount")
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
						
				    	data.get(position).put("line2", String.format("%.2f",Float.parseFloat(userInput.getText().toString())));
		                notifyDataSetChanged();
		                
		                alertDialog.cancel();
		                
		                //update the radiogroup select
		                RadioButton rb1 = (RadioButton)  parent.getRootView().findViewById(R.id.split3);
		                rb1.setChecked(true);
		                
		            	
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				    }
				  });

			// create alert dialog
			alertDialog = alertDialogBuilder.create();
			alertDialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);

			// show it
			alertDialog.show();
           	
           	
           }

       });  
       
        
        //return the view
        return vi;
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		System.out.println(arg0.toString());
		
		
		
	}
	

}
