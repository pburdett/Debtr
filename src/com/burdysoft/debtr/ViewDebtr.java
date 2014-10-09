package com.burdysoft.debtr;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CreationHelper;

import com.burdysoft.debtr.resources.AllDebtr;
import com.burdysoft.debtr.resources.Debtr;
import com.burdysoft.debtr.resources.FileSaving;
import com.burdysoft.debtr.resources.Stats;
import com.burdysoft.debtr.resources.ViewBillAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
	
	int position;
	Activity content = this;
	
    ListView peoplelistdebt;
    SimpleAdapter adapter;
    Debtr debtr;
    
    ArrayList<HashMap<String,String>> summary;
    

	static String xlfileName = "/sdcard/Debtr/Worksheet.xls";
    
    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_debtr);
		
		String fileName = "TestFile.txt";
		AllDebtr alldebtr = new AllDebtr();
		
		//get the position from the extras
		position = Integer.parseInt(getIntent().getExtras().getString("DebtrRef"));
		
		
		
		//READ THE FILE
		
		try {
			alldebtr = FileSaving.readFile(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		//get the Debtr item from the file
		debtr = alldebtr.getDebtrList().get(position);
		
		System.out.println(debtr.getName());
		
		
		
		//Set the basic variables...
		TextView debtrname_tv = (TextView) findViewById(R.id.debtrname);
		TextView debtrdescription_tv = (TextView) findViewById(R.id.description);
		TextView debtrdate_tv = (TextView) findViewById(R.id.date);
		
		debtrname_tv.setText(debtr.getName());
		debtrdescription_tv.setText(debtr.getDescription());
		
		//format date for the view
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy");
  	  	String datetime = "";
  	  
		try {	
			datetime = formatter.format(debtr.getDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		debtrdate_tv.setText(datetime);
		
		
		//get the financial summary of what is going on
		Stats stat = new Stats();
		summary = stat.getSummaryWithSign(debtr);

		adapter = new SimpleAdapter(this, summary,
        		 	R.layout.people_list_debt_summary,
        	        new String[] { "line1","line2" },
        	        new int[] {R.id.name, R.id.debt});
         
		peoplelistdebt = (ListView)findViewById(R.id.peoplelistdebt);
		peoplelistdebt.setAdapter(adapter);
		
		
		//populate the list view to have a link to the view Bills page
		ArrayList<String> viewbillarray = new ArrayList<String>();
		
		try {
			int numBills = debtr.getDebtevent().size();
			
			Float totalspend = (float) 0;
			
			for (int i=0; i<numBills; i++) {
				totalspend = totalspend + debtr.getDebtevent().get(i).getCost();
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
						try {
							int x = debtr.getDebtevent().size();	

							System.out.println("View Bill Clicked");
							
							Intent intent = new Intent(content, ViewBills.class);
							intent.putExtra("DebtrRef",Integer.toString(position));
							startActivity(intent);
					
							
						} catch (Exception e) {
							e.printStackTrace();
							
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
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewdebtrmenu, menu);
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
			intent.putExtra("DebtrRef",Integer.toString(position));
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

		
		DataFormat format = wb.createDataFormat();
		cellStyleccy.setDataFormat(format.getFormat("£#,##0.00"));
		
		
		//Add a row for the title
		Row row = sheet1.createRow((short)0);
	    // Create a cell and put a value in it.
	    Cell cell = row.createCell(0);
	    cell.setCellValue(debtr.getName());
		
	    // add another row
	    Row row2 = sheet1.createRow(1);
	    row2.createCell(0).setCellValue(debtr.getDescription());
		
	    // add the date
	    Row row3 = sheet1.createRow(2);
	    
	    CreationHelper createHelper = wb.getCreationHelper();
	    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
	    cell = row3.createCell(0);
	    cell.setCellValue(new Date());
	    cell.setCellStyle(cellStyle);
	    cell.setCellValue(debtr.getDate());
	    
	    // add the 5th row
	    row = sheet1.createRow(4);
	    row.createCell(0).setCellValue("Summary");
	    
	    

	    // add the 6th row
	    row = sheet1.createRow(5);
	    row.createCell(0).setCellValue("Debtor");
	    row.createCell(1).setCellValue("Amount");
	    
	    int i =0; 
	    for (i=0; i<debtr.getDebtors().size(); i++) {
	    	
	    	row = sheet1.createRow(5+i);
	    	row.createCell(0).setCellValue(summary.get(i).get("line1"));
	    	row.createCell(1).setCellValue(summary.get(i).get("line2"));
	    	
	    }
	    
	    //add some labels
	    row = sheet1.createRow(6+i);
	    row.createCell(0).setCellValue("Name");
		row.createCell(1).setCellValue("Date");
		row.createCell(2).setCellValue("Payee");
		row.createCell(3).setCellValue("Cost");
	    
		for (int m = 0; m<debtr.getDebtors().size(); m++) {
			row.createCell(5+m).setCellValue(debtr.getDebtors().get(m).getName());
		}
		
	    
	    for (int k = 0; k<debtr.getDebtevent().size(); k++) {
	    	
	    	row = sheet1.createRow(7+i+k);
	    	
	    	row.createCell(0).setCellValue(debtr.getDebtevent().get(k).getName());
	    	
	   // 	cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
		    cell = row.createCell(1);
		//    cell.setCellValue(new Date());
		    cell.setCellStyle(cellStyle);
    		cell.setCellValue(debtr.getDebtevent().get(k).getDate());
  
    		row.createCell(2).setCellValue(debtr.getDebtevent().get(k).getPayee());
    		
    		//set cost cell and formatting
    		Cell costcell = row.createCell(3);
    		costcell.setCellValue(debtr.getDebtevent().get(k).getCost());
    		costcell.setCellStyle(cellStyleccy);
    		
	    	
	    	for (int j = 0; j<debtr.getDebtors().size(); j++) {
	    		
			    Cell cell2 = row.createCell(5+j);
	    		cell2.setCellValue(Float.parseFloat(debtr.getDebtevent().get(k).getSplit().get(j).get("line2")));
			    cell2.setCellStyle(cellStyleccy);
	    		
	    	}
	    	
	    }
	    
	    
	    
	    
	    FileSaving.saveExcelFile(wb,content);
	    
	    
	    
	    
	   emailDebtr();
	    
	    
	}
	
	
	
}
