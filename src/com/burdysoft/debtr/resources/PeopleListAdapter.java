package com.burdysoft.debtr.resources;

import helper.People;

import java.util.ArrayList;

import com.burdysoft.debtr.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

 
public class PeopleListAdapter extends BaseAdapter implements OnClickListener {
 
    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater=null;
 
    public PeopleListAdapter(Activity a, ArrayList<String> peopleList) {
        activity = a;
        data=peopleList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.people_list, null);
        }
        
        final TextView name = (TextView)vi.findViewById(R.id.name); // name
 
        String person = "";
        person = data.get(position);
 
        // Setting all values in listview
        name.setText(person);
        
        //set the controls of the edit button
        ImageButton editbutton= (ImageButton)  vi.findViewById(R.id.editbutton);
        editbutton.setOnClickListener(new OnClickListener() 
        { 
            @Override
            public void onClick(View v) 
            {
            	TextView newtv = (TextView) parent.getRootView().findViewById(R.id.newperson);
            	newtv.setText(name.getText());
            	data.remove(position);
                notifyDataSetChanged();
                

            }

        });   
        
        //set the controls of the delete button
        ImageButton deletebutton= (ImageButton)  vi.findViewById(R.id.deletebutton);
        deletebutton.setOnClickListener(new OnClickListener() 
        { 
            @Override
            public void onClick(View v) 
            {	
            	//TODO add a question of whether you really want to delete the name;
            	data.remove(position);
                notifyDataSetChanged();
            }
        });

        
        //return the view
        return vi;
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}
    
    
}
