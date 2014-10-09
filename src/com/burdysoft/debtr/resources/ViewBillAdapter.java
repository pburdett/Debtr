package com.burdysoft.debtr.resources;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.burdysoft.debtr.R;

public class ViewBillAdapter extends BaseAdapter implements OnClickListener {
 
	private Context context;
    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater=null;

	
	
 
    public ViewBillAdapter(Activity a, ArrayList<String> d) {
        activity = a;
        data=d;
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
            vi = inflater.inflate(R.layout.view_bill_list, null);
        }
        
        final TextView viewbilltext = (TextView) vi.findViewById(R.id.billsummary); // name

 
        // Setting all values in listview
        viewbilltext.setText(data.get(position));
        
        System.out.println(data.get(position));
        
        //return the view
        return vi;
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		System.out.println(arg0.toString());
		
		
		
	}
	

}
