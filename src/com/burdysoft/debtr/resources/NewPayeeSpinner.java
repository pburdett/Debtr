package com.burdysoft.debtr.resources;


import java.util.ArrayList;

import com.burdysoft.debtr.NewBill;
import com.burdysoft.debtr.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewPayeeSpinner extends ArrayAdapter<String>  {

        private Activity activity;
        private ArrayList data;
        public Resources res;
        People tempValues=null;        
        private static LayoutInflater inflater=null;
        int check;
        
        

        public NewPayeeSpinner(NewBill activitySpinner, 
                int textViewResourceId,   
                ArrayList objects,
                Resources resLocal) {
        		
        	super(activitySpinner, textViewResourceId, objects);
        
            
            /********** Take passed values **********/
             activity = activitySpinner;
             data = objects;
             res = resLocal;
          
             //start my counter
             check = 0;
             
             /***********  Layout inflator to call external xml layout () ***********/
              inflater = (LayoutInflater)activity.
                                          getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          
     }
        
        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
     
        	
             	return getCustomView(position, convertView, parent);

        }
     
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
  
            	return getCustomView(position, convertView, parent);
        }
        
        
        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {
     
            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.event_payee_spinner, parent, false);
             
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
      
            tempValues = (People) data.get(position);
             
            TextView label = (TextView) row.findViewById(R.id.textView1);
             
           
                // Set values for spinner each row 
                label.setText(tempValues.getName());
            
     
            return row;
          }
        
        
        
        
        
   
        
        
        
     }
