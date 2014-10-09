package com.burdysoft.debtr.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;

public  class FileSaving {


	static String fileName = "/sdcard/Debtr/mysdfile.txt";
	static String directoryName = "/sdcard/Debtr/";
	
	static String xlfileName = "/sdcard/Debtr/Worksheet.xls";
	

	static String jsonfileName = "/sdcard/Debtr/mysdfile.json";
	
	
	public static void saveFile(AllDebtr alldebtr, Activity content){
		
//going to continue to save it both ways, just in case!!!
		try {
			File directory = new File(directoryName);
			directory.mkdirs();
			
			File myFile = new File(fileName);
	        myFile.createNewFile();
	        FileOutputStream fOut = new FileOutputStream(myFile);
	        ObjectOutputStream os = new ObjectOutputStream(fOut);
			os.writeObject(alldebtr);
			os.close();
			fOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/* The JSON way to save a file!!!
		 * 
		 */
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonstring = "";
		BufferedWriter writer = null;
		try {
			jsonstring = mapper.writeValueAsString(alldebtr);
			System.out.println("write this to a file: " + jsonstring);
			File directory = new File(directoryName);
			directory.mkdirs();
			
			
			
			File myFile = new File(jsonfileName);
			writer = new BufferedWriter ( new FileWriter(myFile));
			writer.write(jsonstring);
			
			/*
	        myFile.createNewFile();
	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStream os = new OutputStream(fOut);
	        os.write(buffer);
//	        ObjectOutputStream os = new ObjectOutputStream(fOut);
			os.writeObject(jsonstring);
			os.close();
			fOut.close();
		*/	
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	
	
	public static AllDebtr readFile(Activity content){
		
		AllDebtr alldebtr = new AllDebtr();
		
		//Load the file
		
		/* THIS IS THE OLD WAY, keep for the time being, just in case I need it!
		 * 
		 */
		
	/*	try {
			File myFile = new File(fileName);
            FileInputStream fIn = new FileInputStream(myFile);
            ObjectInputStream is = new ObjectInputStream(fIn);
            alldebtr = (AllDebtr) is.readObject();
            fIn.close();
            is.close();
			
			return alldebtr;
            
        } catch (Exception e) {
           e.printStackTrace();
        }
		return alldebtr;
		*/
		
		//The JSON way
		ObjectMapper mapper = new ObjectMapper();
		try {
			BufferedReader reader = new BufferedReader( new FileReader(jsonfileName));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.seperator");
			
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			//	stringBuilder.append(ls);
			}
			
			System.out.println(stringBuilder.toString());
			alldebtr = mapper.readValue(stringBuilder.toString(), AllDebtr.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alldebtr;
		
		
	}  
	
	
	public static void saveExcelFile(Workbook wb, Activity content){
		

		try {
		
			File xlfile = new File(xlfileName);
			xlfile.createNewFile();
			 FileOutputStream fileOut = new FileOutputStream(xlfile);
			 wb.write(fileOut);
			 fileOut.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
