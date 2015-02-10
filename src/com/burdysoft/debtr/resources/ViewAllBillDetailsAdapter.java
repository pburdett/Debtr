package com.burdysoft.debtr.resources;

import helper.DatabaseHelper;
import helper.Event;
import helper.People;
import helper.Split;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.burdysoft.debtr.R;
import com.burdysoft.debtr.ViewBills;

public class ViewAllBillDetailsAdapter extends BaseAdapter implements OnClickListener {
 
	private Context context;
    private Activity activity;
    private List<Event> data;
    private static LayoutInflater inflater=null;
    DatabaseHelper db;
	
    
    ListView list;
    Dialog listDialog;
	
 
    public ViewAllBillDetailsAdapter(Context context, Activity a, List<Event> d) {
        this.context  = context;
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
            vi = inflater.inflate(R.layout.view_bill_detail_list, null);
        }
        
       
        TextView date = (TextView) vi.findViewById(R.id.date);
        TextView billname = (TextView) vi.findViewById(R.id.billname);
        TextView payee = (TextView) vi.findViewById(R.id.payee);
        TextView amount = (TextView) vi.findViewById(R.id.amount);
        ImageView billpic = (ImageView) vi.findViewById(R.id.billpic);
        
        
        billname.setText(data.get(position).getName());
    
        db = new DatabaseHelper(context);
        System.out.println("this is the payee id: " + data.get(position).getPayee_id());
        People payeename = db.getPayeeName(data.get(position).getPayee_id());
        db.close();
        
        payee.setText(payeename.getName());
        amount.setText("£" + String.format("%.2f", data.get(position).getCost()));
        
        
        
        //format the date
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy");
  	  	String datetime = "";
  	  	
  	  
		try {
			datetime = formatter.format(Date.parse(data.get(position).getDate()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		date.setText(datetime);


        //Run the Image Async loading program
        billpic.setTag(data.get(position).getPhotofile());
        new LoadImageLV(billpic,data.get(position)).execute();


		//Create a listener for clicking on the bill picture....
		billpic.setOnClickListener(new OnClickListener() 
        { 
            @Override
            public void onClick(View v) 
            {
            	
            	File imgFile = new  File(data.get(position).getPhotofile());
        		if(imgFile.exists()){
            	
        			//only display the dialog if we have a picture
	            	final Dialog nagDialog = new Dialog((ViewBills)context,android.R.style.Theme_Holo_DialogWhenLarge);
	                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	                nagDialog.setCancelable(false);
	                nagDialog.setContentView(R.layout.preview_bill);
	                Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
	                ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
	
	                Bitmap bigpic = decodeSampledBitmapFromFile(data.get(position).getPhotofile(),800,800);
	                ivPreview.setImageBitmap(bigpic);
	          
	                
	                
	                
	                btnClose.setOnClickListener(new OnClickListener() {
	                    @Override
	                    public void onClick(View arg0) {
	
	                        nagDialog.dismiss();
	                    }
	                });
	                nagDialog.show(); 
        		
        		} else {
        			//do nothing

        		}
            }
            
        });
		
		
		
		RelativeLayout bodytext = (RelativeLayout) vi.findViewById(R.id.clickable);
		
        bodytext.setOnClickListener(new OnClickListener() 
        { 
            @Override
            public void onClick(View v) 
            {
            	System.out.println("Rel Layout Clicked");
            	
            	List<Split> splits = db.getSplitsByEventId(data.get(position).getId());
				List<People> peoples = db.getAllPeopleDebtrID(data.get(position).getDebtr_id());
				
				System.out.println("Size of splits is " + splits.size());
				
				//create a list of all the people and splits.
				
				String[] values = new String[splits.size()];
				
						for (int i=0; i<splits.size(); i++) {
							values[i] = peoples.get(i).getName() + ": £" + String.format("%.2f", splits.get(i).getAmount());
						}
						
				showdialog(values);

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
	
	private void showdialog(String[] val)
    {
        listDialog = new Dialog(context);
        listDialog.setTitle("Select Item");
         LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View v = li.inflate(R.layout.split_list_parent, null, false);
         listDialog.setContentView(v);
         listDialog.setCancelable(true);
         
         listDialog.setTitle("Bill Split:");

 
         ListView list1 = (ListView) listDialog.findViewById(R.id.splitlistparent);
         list1.setOnItemClickListener((ViewBills)context);
         list1.setAdapter(new ArrayAdapter<String>((ViewBills) context,android.R.layout.simple_list_item_1, val));
         //now that the dialog is set up, it's time to show it
         listDialog.show();
    }
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    { }

	
	public static Bitmap decodeSampledBitmapFromFile(String imgFile, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	   // BitmapFactory.decodeResource(res, resId, options);
	    BitmapFactory.decodeFile(imgFile, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(imgFile, options);
	}

	
	
	
	public static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	
	
	}
	

}
