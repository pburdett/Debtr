package com.burdysoft.debtr;

import helper.DatabaseHelper;
import helper.Debtr;
import helper.Event;
import helper.People;
import helper.PeopleStats;
import helper.Split;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CreationHelper;

import com.burdysoft.debtr.resources.ViewBillAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewDebtr  extends ActionBarActivity {
	
	int debtr_id;
	Activity content = this;
	
    ListView peoplelistdebt;
    SimpleAdapter adapter;
    Debtr debtr;
    Date debtrdate;
    
    ArrayList<HashMap<String,String>> summary;
    

	static String xlfileName = "/sdcard/Debtr/Worksheet.xls";
    
    DatabaseHelper db;
    List<Event> events;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_debtr);
		
		db = new DatabaseHelper(getApplicationContext());
		
		//get the position from the extras
		debtr_id = Integer.parseInt(getIntent().getExtras().getString("DebtrRef"));
		
		debtr = db.getDebtr(debtr_id);
		events = db.getAllEventsByDebtrId(debtr_id);
		
		System.out.println("Debtr Ref: " + debtr_id);
		 
		//Set the basic variables...
		TextView debtrname_tv = (TextView) findViewById(R.id.debtrname);
		TextView debtrdescription_tv = (TextView) findViewById(R.id.description);
		TextView debtrdate_tv = (TextView) findViewById(R.id.date);
		
		debtrname_tv.setText(debtr.getName());
		debtrdescription_tv.setText(debtr.getDescription());
		
		//format date for the view.
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy");
  	  	String datetime = "";
        DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH); //used to import the string as a date, by a given format


		try {
            debtrdate = format.parse(debtr.getDate());
            datetime = formatter.format(debtrdate);

		} catch (Exception e) {
			e.printStackTrace();
		}


        debtrdate_tv.setText(datetime);

		//get the financial summary of what is going on
		List<PeopleStats> stats = new ArrayList<PeopleStats>();
		stats = db.getStatsByDebtrId(debtr_id);
		
		System.out.println("We are on stats size " + stats.size());
		
		//convert to a hashmap
		summary = new ArrayList<HashMap<String,String>>();
		
		if (stats.size() > 0) {
		// this means we have atleast one bill...
			
			for (int i = 0; i< stats.size(); i++) {
				HashMap<String, String> personstat = new HashMap<String, String>();
				personstat.put("name", stats.get(i).getName());
				
				Float amount = stats.get(i).getAmount();
				String amountstring;
				if (amount < 0) {
					amountstring = "(£" + String.format("%.2f", Math.abs(amount)) + ")";
				} else {
					amountstring = "£" + String.format("%.2f", amount);
				}
			
				personstat.put("amount", amountstring);
				System.out.println(personstat);
				summary.add(personstat);
			}
		} else {
			//means we have no bills.
			List<People> people = new ArrayList<People>();
			people = db.getAllPeopleDebtrID(debtr_id);
			
			for (int i = 0; i< people.size(); i++) {
				HashMap<String, String> personstat = new HashMap<String, String>();
				personstat.put("name", people.get(i).getName());
				personstat.put("amount", "£0.00");
				System.out.println(personstat);
				summary.add(personstat);
			}
			
		}
		
		
		adapter = new SimpleAdapter(this, summary,
        		 	R.layout.people_list_debt_summary,
        	        new String[] { "name","amount" },
        	        new int[] {R.id.name, R.id.debt});
         
		peoplelistdebt = (ListView)findViewById(R.id.peoplelistdebt);
		peoplelistdebt.setAdapter(adapter);
		
		
		
		//populate the list view to have a link to the view Bills page
		ArrayList<String> viewbillarray = new ArrayList<String>();
		
		try {
			int numBills = events.size();
			
			Float totalspend = (float) 0;
			
			for (int i=0; i<numBills; i++) {
				totalspend = totalspend + events.get(i).getCost();
			}
			
			if (numBills > 1 ) {
				//we need to pluralise it...
				viewbillarray.add(numBills + " Bills: Totalling £" + String.format("%.2f", totalspend));
			} else {
				viewbillarray.add(numBills + " Bill: Totalling £" + String.format("%.2f", totalspend));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			viewbillarray.add("No Bills: Totalling £0.00");
		}
		
		
		
		
			
		System.out.println(viewbillarray);
		ListView viewbillLV = (ListView) findViewById(R.id.viewbills);
		ViewBillAdapter vbAdapter = new ViewBillAdapter(this, viewbillarray);
		viewbillLV.setAdapter(vbAdapter);	
		
		//Add Event Button behaviour
				
				viewbillLV.setOnItemClickListener(new OnItemClickListener() {
		         

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						
						
						//we want to avoid showing something with no bills
						
						if (events.size()>0) {
							int x = events.size();	

							System.out.println("View Bill Clicked");
							
							Intent intent = new Intent(content, ViewBills.class);
							intent.putExtra("DebtrRef",Integer.toString(debtr_id));
							startActivity(intent);
					
							
						} else {
							
							//Give a dialog box to say no!!!
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(content);
					 
								// set title
								alertDialogBuilder.setTitle("Error:");
					 
								// set dialog message
								alertDialogBuilder
									.setMessage("No bills created for this account")
									.setCancelable(false)
									.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											// if this button is clicked, close
											// current activity
											dialog.cancel();
										}
									  });
					
					 
									// create alert dialog
									AlertDialog alertDialog = alertDialogBuilder.create();
					 
									// show it
									alertDialog.show();
							
						}
						
						
						
					}
				});
		
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        //get the status of out debtr
        System.out.println("what is our status? " + debtr.getActive());

        if (debtr.getActive()==true) {

            // Inflate the menu items for use in the action bar
            inflater.inflate(R.menu.viewdebtrmenu, menu);
        } else {

            // Inflate the menu items for use in the action bar
            inflater.inflate(R.menu.viewdebtrmenudeleted,menu);

        }

        return super.onCreateOptionsMenu(menu);
    }
   
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addbill) {
			Intent intent = new Intent(content, NewBill.class);
			intent.putExtra("DebtrRef",Integer.toString(debtr_id));
			startActivity(intent);
			return true;
		} else if (id==R.id.export) {
			exportDebtrExcel(summary);
			return true;
		}
		return true;
    }


	private void emailDebtr() {
		
		
		String debtrname = debtr.getName();
		String subject =  debtrname + " Debt'r";
		String email_content = "Here is the summary of Debt'r: " + debtrname;
		
		Intent shareintent = new Intent(Intent.ACTION_SEND);
		shareintent.setType("text/plain");
		shareintent.setType("message/rfc822");
		shareintent.putExtra(Intent.EXTRA_SUBJECT, subject);

		
		//attach the file
		File file = new File(xlfileName);
		if (!file.exists() || !file.canRead()) {
		    Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
		    finish();
		    return;
		}
		Uri uri = Uri.parse("file://" + file);
		shareintent.putExtra(Intent.EXTRA_STREAM, uri);
		


		shareintent.putExtra(Intent.EXTRA_TEXT, email_content);
		
		
	    startActivity(Intent.createChooser(shareintent, "Send email..."));
		
	}
	

	
	private void exportDebtrExcel(ArrayList<HashMap<String,String>> summary) {
		
		//create a workbook, and sheet
		Workbook wb = new HSSFWorkbook();
		Sheet sheet1 = wb.createSheet(debtr.getName());
		
		
		//set some formatting
		CellStyle cellStyle = wb.createCellStyle();

	    CellStyle cellStyleccy = wb.createCellStyle();

        CellStyle boldStyle = wb.createCellStyle();//Create style
        Font font = wb.createFont();//Create font
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);//Make font bold
        boldStyle.setFont(font);//set it to bold

		CellStyle italicsStyle = wb.createCellStyle();
        Font font2 = wb.createFont();
        font2.setItalic(true);
        font2.setFontHeightInPoints((short) 10);
        italicsStyle.setFont(font2);


		DataFormat format = wb.createDataFormat();
		cellStyleccy.setDataFormat(format.getFormat("£#,##0.00"));
		
		
		//Add a row for the title
		Row row = sheet1.createRow((short)0);
	    // Create a cell and put a value in it.
	    Cell cell = row.createCell(0);
	    cell.setCellValue(debtr.getName());
        cell.setCellStyle(boldStyle);
		
	    // add another row
	    Row row2 = sheet1.createRow(1);
	    row2.createCell(0).setCellValue(debtr.getDescription());
		
	    // add the date
	    Row row3 = sheet1.createRow(2);
	    
	    CreationHelper createHelper = wb.getCreationHelper();
	    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd mmm yyyy"));
	    cell = row3.createCell(0);
	    cell.setCellValue(new Date());
	    cell.setCellStyle(cellStyle);
        cell.setCellValue(debtrdate);


	    // add the 5th row which heads up the summary
	    row = sheet1.createRow(4);
	    row.createCell(0).setCellValue("Summary*");
        row.getCell(0).setCellStyle(boldStyle);


	    // add the 6th row which is th titles of the summary
	    row = sheet1.createRow(5);
        row.createCell(0);
	    row.createCell(1).setCellValue("Debtor");
	    row.createCell(2).setCellValue("Amount");
        for(int i=0; i<3; i++) {
            row.getCell(i).setCellStyle(boldStyle);
        }

        //Add the summary data
	    int i =0; 
	    for (i=0; i<summary.size(); i++) {
	    	
	    	row = sheet1.createRow(6+i);
	    	row.createCell(1).setCellValue(summary.get(i).get("name"));
	    	row.createCell(2).setCellValue(summary.get(i).get("amount"));
	    	
	    }
	    
	    //add some label headers
	    row = sheet1.createRow(7+i);
	    row.createCell(0).setCellValue("Name");
		row.createCell(1).setCellValue("Date");
		row.createCell(2).setCellValue("Payee");
		row.createCell(3).setCellValue("Cost");

        for(int ii=0; ii<4; ii++) {
            row.getCell(ii).setCellStyle(boldStyle);
        }

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<People> peoples = db.getAllPeopleDebtrID(debtr.getId());
        List<Event> events = db.getAllEventsByDebtrId(debtr.getId());

		for (int m = 0; m<peoples.size(); m++) {
			row.createCell(5+m).setCellValue(peoples.get(m).getName());
            row.getCell(5+m).setCellStyle(boldStyle);
		}

	    for (int k = 0; k<events.size(); k++) {
	    	
	    	row = sheet1.createRow(8+i+k);
	    	
	    	row.createCell(0).setCellValue(events.get(k).getName());
	    	
	    	cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("ddd dd mmm yy"));
		    cell = row.createCell(1);
		    cell.setCellStyle(cellStyle);

            //get the date string and make it realise its a date
            DateFormat format2 = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH); //used to import the string as a date, by a given format

            try {
                cell.setCellValue(format2.parse(events.get(k).getDate()));

            } catch (Exception e) {
                e.printStackTrace();
            }




    		row.createCell(2).setCellValue(db.getPayeeName(events.get(k).getPayee_id()).getName());
    		
    		//set cost cell and formatting
    		Cell costcell = row.createCell(3);
    		costcell.setCellValue(events.get(k).getCost());
    		costcell.setCellStyle(cellStyleccy);

            List<Split> splits = db.getSplitsByEventId(events.get(k).getId());

	    	for (int j = 0; j<peoples.size(); j++) {
	    		
			    Cell cell2 = row.createCell(5+j);
	    		cell2.setCellValue(splits.get(j).getAmount());
			    cell2.setCellStyle(cellStyleccy);
	    		
	    	}
	    	
	    }

        row = sheet1.createRow(11+events.size());
        cell = row.createCell(1);
        cell.setCellValue("* Positive values denote a credit; Negative are debts");
	    cell.setCellStyle(italicsStyle);


        row = sheet1.createRow(13+events.size());
        cell = row.createCell(0);
        cell.setCellValue("Copyright BurdySoft 2014");
        cell.setCellStyle(italicsStyle);


        db.closeDB();





	    
	    boolean success = saveExcelFile(wb,content);

	    
	   if (success == true) {
           emailDebtr();
       } else {
           Toast.makeText(this, "No Excel File saved", Toast.LENGTH_SHORT).show();
       }
	    
	    

	    
	}
	
	
	//ensure we nagivate back to the All debtr page rather than a NewBill input page
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("Activity", this.getLocalClassName());
		intent.putExtra("status", "Active");
		startActivity(intent);
		
	}
	
	public static boolean saveExcelFile(Workbook wb, Context context) {

        boolean success = false;

        File file = new File(xlfileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;


    }
}
