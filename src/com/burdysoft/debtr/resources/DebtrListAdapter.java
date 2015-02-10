package com.burdysoft.debtr.resources;

import helper.DatabaseHelper;
import helper.Debtr;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.burdysoft.debtr.R;
import com.burdysoft.debtr.ViewDebtr;
import com.burdysoft.debtr.MainActivity;

public class DebtrListAdapter extends BaseAdapter implements OnClickListener {
 
	private Context context;
    private Activity activity;
    private List<Debtr> data;
    private static LayoutInflater inflater=null;
	
	DatabaseHelper db;
	
 
    public DebtrListAdapter(Activity a, List<Debtr> d, Context context) {
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
            vi = inflater.inflate(R.layout.debtr_list, null);
        }
        
        final TextView debtrname = (TextView) vi.findViewById(R.id.debtrname); // name
 
        Debtr debtr = new Debtr();
        debtr = data.get(position);
 
        // Setting all values in listview
        debtrname.setText(debtr.getName());
        debtrname.setTag(debtr.getId());
        
        //set the controls of the delete button
        ImageButton deletebutton= (ImageButton)  vi.findViewById(R.id.deletebutton);
        deletebutton.setOnClickListener(new OnClickListener() 
        { 
            @Override
            public void onClick(View v) 
            {	
            	
            	new AlertDialog.Builder( ((MainActivity)context))
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // continue with delete
                    	
                    	int debtr_id = data.get(position).getId();
                    	
                    	data.remove(position);
                        notifyDataSetChanged();
                      
                        db = new DatabaseHelper((MainActivity)context);
                        db.inactiveDebtr(debtr_id);
                 //       ((MainActivity)context).saveData(data);
                    	
                    }
                 })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // do nothing
                    }
                 })
                .setIcon(android.R.drawable.ic_dialog_alert)
                 .show();
            	
            	
                
                
            }
        });
        
        //set the controls of text
        debtrname.setOnClickListener(new OnClickListener() 
        { 
            @Override
            public void onClick(View v) 
            {
            	System.out.println("You have clicked on position " + debtrname.getTag().toString());
            	System.out.println("You have click on list position " + parent);
            	
            	Intent intent = new Intent(context,ViewDebtr.class);
            	intent.putExtra("Activity", this.getClass().getName());
            	intent.putExtra("Exists", true);
            	intent.putExtra("DebtrRef", debtrname.getTag().toString());
    			context.startActivity(intent);
            	
            	
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
