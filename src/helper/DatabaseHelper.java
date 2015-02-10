package helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "debtrapp";
 
    // Table Names
    private static final String TABLE_DEBTR = "debtr";
    private static final String TABLE_EVENT = "event";
    private static final String TABLE_PEOPLE = "people";
    private static final String TABLE_SPLIT = "split";
    
    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DEBTR_ID = "debtr_id";
    
    //DEBTR table - columns names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_SETTLED = "settled";
    
    //EVENT table - column names
    private static final String KEY_PAYEE_ID = "payee_id";
    private static final String KEY_PHOTOTAKEN = "photo_taken";
    private static final String KEY_PHOTOFILE = "photofile";
    
    //PEOPLE table - column names
    //none required that aren't common
    
    //SPLIT tbale - column names
    private static final String KEY_PEOPLE_ID = "people_id";
    private static final String KEY_EVENT_ID = "event_id";
    
    
    //TABLE CREATE STATEMENTS
    
    //Create DEBTR table
    private static final String CREATE_DEBTR_TABLE = "CREATE TABLE " + TABLE_DEBTR + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
    		+ KEY_DESCRIPTION + " TEXT," + KEY_DATE + " DATETIME, " + KEY_ACTIVE + " BOOLEAN, " + KEY_SETTLED + " BOOLEAN)";

    //Create EVENT table
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
    		+ KEY_DATE + " DATETIME," + KEY_AMOUNT + " REAL, " + KEY_PAYEE_ID + " INTEGER," + KEY_DEBTR_ID + " INTEGER," + KEY_PHOTOTAKEN + " INTEGER,"
    		+ KEY_PHOTOFILE + " TEXT)";
    
    //Create PEOPLE table
    private static final String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_PEOPLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
    		+ KEY_DEBTR_ID + " INTEGER)";

    //Create SPLIT table
    private static final String CREATE_SPLIT_TABLE = "CREATE TABLE " + TABLE_SPLIT + "(" +  KEY_ID + " INTEGER PRIMARY KEY," + KEY_PEOPLE_ID + " INTEGER,"
    		+ KEY_EVENT_ID + " INTEGER," + KEY_AMOUNT + " REAL)";
    
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_DEBTR_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_PEOPLE_TABLE);
        db.execSQL(CREATE_SPLIT_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBTR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPLIT);
        
        // create new tables
        onCreate(db);
    }
    
    
    /*
     * Creating a debtr
     */
    public long createDebtr(Debtr debtr) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, debtr.getName());
        values.put(KEY_DESCRIPTION, debtr.getDescription());
        values.put(KEY_DATE, debtr.getDate());
        values.put(KEY_ACTIVE, true);
        values.put(KEY_SETTLED, false);
     
        // insert row
        long debtr_id = db.insert(TABLE_DEBTR, null, values);
       
        return debtr_id;
    }
    
    
    /*
     * get single Debtr
     */
    public Debtr getDebtr(long debtr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_DEBTR + " WHERE "
                + KEY_ID + " = " + debtr_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     
        Debtr debtr = new Debtr();
        debtr.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        debtr.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        debtr.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
        debtr.setDate(c.getString(c.getColumnIndex(KEY_DATE)));

        if (c.getInt(c.getColumnIndex(KEY_ACTIVE)) == 1) {
        	debtr.setActive(true);
        } else {
        	debtr.setActive(false);
        }
        
        if (c.getInt(c.getColumnIndex(KEY_SETTLED)) == 1) {
        	debtr.setSettled(true);
        } else {
        	debtr.setSettled(false);
        }
        
        return debtr;
    }
    
    
    /*
     * getting all DEBTR
     * */
    public List<Debtr> getAllDebtr() {
        List<Debtr> debtrs = new ArrayList<Debtr>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEBTR;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Debtr debtr = new Debtr();
                debtr.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                debtr.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                debtr.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
                debtr.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
               
                if (c.getInt(c.getColumnIndex(KEY_ACTIVE)) == 1) {
                	debtr.setActive(true);
                } else {
                	debtr.setActive(false);
                }
                
                if (c.getInt(c.getColumnIndex(KEY_SETTLED)) == 1) {
                	debtr.setSettled(true);
                } else {
                	debtr.setSettled(false);
                } 
                
                // adding to todo list
                debtrs.add(debtr);
            } while (c.moveToNext());
        }
     
        return debtrs;
    }
    
    
    /*
     * getting all Active DEBTR
     * */
    public List<Debtr> getAllActiveDebtr() {
        List<Debtr> debtrs = new ArrayList<Debtr>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEBTR + " WHERE " + KEY_ACTIVE + "=1 AND " + KEY_SETTLED + "=0";
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Debtr debtr = new Debtr();
                debtr.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                debtr.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                debtr.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
                debtr.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                
                if (c.getInt(c.getColumnIndex(KEY_ACTIVE)) == 1) {
                	debtr.setActive(true);
                } else {
                	debtr.setActive(false);
                }
                
                if (c.getInt(c.getColumnIndex(KEY_SETTLED)) == 1) {
                	debtr.setSettled(true);
                } else {
                	debtr.setSettled(false);
                }

                // adding to todo list
                debtrs.add(debtr);
            } while (c.moveToNext());
        }
     
        return debtrs;
    }
    
    
    
    /*
     * getting all Deleted DEBTR
     * */
    public List<Debtr> getAllDeletedDebtr() {
        List<Debtr> debtrs = new ArrayList<Debtr>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEBTR + " WHERE " + KEY_ACTIVE + "=0";
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Debtr debtr = new Debtr();
                debtr.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                debtr.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                debtr.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
                debtr.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                
                if (c.getInt(c.getColumnIndex(KEY_ACTIVE)) == 1) {
                	debtr.setActive(true);
                } else {
                	debtr.setActive(false);
                }
                
                if (c.getInt(c.getColumnIndex(KEY_SETTLED)) == 1) {
                	debtr.setSettled(true);
                } else {
                	debtr.setSettled(false);
                }

                // adding to todo list
                debtrs.add(debtr);
            } while (c.moveToNext());
        }
     
        return debtrs;
    }
    
    
    /*
     * getting all Settled
     * */
    public List<Debtr> getAllSettledDebtr() {
        List<Debtr> debtrs = new ArrayList<Debtr>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEBTR + " WHERE " + KEY_SETTLED + "=1";
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Debtr debtr = new Debtr();
                debtr.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                debtr.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                debtr.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
                debtr.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                
                if (c.getInt(c.getColumnIndex(KEY_ACTIVE)) == 1) {
                	debtr.setActive(true);
                } else {
                	debtr.setActive(false);
                }
                
                if (c.getInt(c.getColumnIndex(KEY_SETTLED)) == 1) {
                	debtr.setSettled(true);
                } else {
                	debtr.setSettled(false);
                }

                // adding to todo list
                debtrs.add(debtr);
            } while (c.moveToNext());
        }
     
        return debtrs;
    }
    
    
    /*
     * Updating a DEBTR
     */
    public int updateDebtr(Debtr debtr) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, debtr.getName());
        values.put(KEY_DESCRIPTION, debtr.getDescription());
        values.put(KEY_DATE, debtr.getDate());
     
        // updating row
        return db.update(TABLE_DEBTR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(debtr.getId()) });
    }
    
    /*
     * Set DEBTR as inactive 
     */
    public int inactiveDebtr(int debtr_id) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, false);
        
        // updating row
        return db.update(TABLE_DEBTR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(debtr_id)});
    }
    
    
    /*
     * Set DEBTR as settled
     */
    public int settledDebtr(int debtr_id) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_SETTLED, false);
        
        // updating row
        return db.update(TABLE_DEBTR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(debtr_id)});
    }
    
    
    
    
    /*
     * Deleting a DEBTR
     */
    public void deleteDebtr(long debtr_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEBTR, KEY_ID + " = ?",
                new String[] { String.valueOf(debtr_id) });
    }
    
    
    /*
     * Creating an EVENT
     */
    public long createEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_AMOUNT, event.getCost());
        values.put(KEY_PAYEE_ID, event.getPayee_id());
        values.put(KEY_DEBTR_ID, event.getDebtr_id());
        values.put(KEY_PHOTOTAKEN, event.getPhototaken());
        values.put(KEY_PHOTOFILE, event.getPhotofile());
        
     
        // insert row
        long event_id = db.insert(TABLE_EVENT, null, values);
     
        return event_id;
    }
    
    
    /*
     * get single Event
     */
    public Event getEvent(int event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT + " WHERE "
                + KEY_ID + " = " + event_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     

        Event event = new Event();
        event.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        event.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        event.setCost(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
        event.setPayee_id(c.getInt(c.getColumnIndex(KEY_PAYEE_ID)));
        event.setDebtr_id(c.getInt(c.getColumnIndex(KEY_DEBTR_ID)));
        event.setPhototaken(c.getInt(c.getColumnIndex(KEY_PHOTOTAKEN)));
        event.setPhotofile(c.getString(c.getColumnIndex(KEY_PHOTOFILE)));
        
        return event;
    }
    
    
    
    public List<Event> getAllEventsByDebtrId(int debtr_id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	List<Event> events = new ArrayList<Event>();
    	
    	String selectQuery = "SELECT * FROM " + TABLE_EVENT + " WHERE "
    			+ KEY_DEBTR_ID + " = " + debtr_id;
    	
    	Log.e(LOG, selectQuery);
    	
    	Cursor c = db.rawQuery(selectQuery, null);
    	
    	// looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
            	Event event = new Event();
                event.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                event.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                event.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                event.setCost(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
                event.setPayee_id(c.getInt(c.getColumnIndex(KEY_PAYEE_ID)));
                event.setDebtr_id(c.getInt(c.getColumnIndex(KEY_DEBTR_ID)));
                event.setPhototaken(c.getInt(c.getColumnIndex(KEY_PHOTOTAKEN)));
                event.setPhotofile(c.getString(c.getColumnIndex(KEY_PHOTOFILE)));
                
                events.add(event);
            }
            while (c.moveToNext());
        }
     
        return events;
    	
    }
    
    
    
    
    
    /*
     * Deleting an EVENT
     */
    public void deleteEvent(int event_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT, KEY_ID + " = ?",
                new String[] { String.valueOf(event_id) });
    }
    
    
    
    
    
    
    
    
    
    
    /*
     * Creating a PEOPLE
     */
    public long createPeople(People people) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, people.getName());
        values.put(KEY_DEBTR_ID, people.getDebtr_id());
        
        // insert row
        long people_id = db.insert(TABLE_PEOPLE, null, values);
        
        return people_id;
    }
    
    
    /*
     * get single People
     */
    public People getPeople(int people_id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE+ " WHERE "
                + KEY_ID + " = " + people_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     

        People people = new People();
        people.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        people.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        people.setDebtr_id(c.getInt(c.getColumnIndex(KEY_DEBTR_ID)));
        
        return people;
    }
    
    
    /*
     * getting all People for a given Debtr
     * */
    public List<People> getAllPeopleDebtrID(int debtr_id) {
        List<People> peoples = new ArrayList<People>();
        String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE + " WHERE "
                + KEY_DEBTR_ID + " = " + debtr_id;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
            	People people = new People();
                people.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                people.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                people.setDebtr_id(c.getInt(c.getColumnIndex(KEY_DEBTR_ID)));
             
                // adding to todo list
                peoples.add(people);
            } while (c.moveToNext());
        }
     
        return peoples;
    }
    
    
    /*
     * Get the People name for the event id (ie the payee)
     */
    
    public People getPayeeName(int payee_id) {
    	
    	 String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE + " WHERE "
                 + KEY_ID + " = " + payee_id;
      
         Log.e(LOG, selectQuery);
      
         SQLiteDatabase db = this.getReadableDatabase();
   
         Cursor c = db.rawQuery(selectQuery, null);
         
         if (c != null)
             c.moveToFirst();
      

         People people = new People();
         people.setId(c.getInt(c.getColumnIndex(KEY_ID)));
         people.setName(c.getString(c.getColumnIndex(KEY_NAME)));
         people.setDebtr_id(c.getInt(c.getColumnIndex(KEY_DEBTR_ID)));
         
         return people;
         
         
         
    }
    
   
    /*
     * Deleting a PEOPLE
     */
    public void deletePeople(int people_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEOPLE, KEY_ID + " = ?",
                new String[] { String.valueOf(people_id) });
    }
    
    
    
    
    
    
    
    
    
    /*
     * Creating a SPLIT
     */
    public long createSplit(Split splits) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_PEOPLE_ID, splits.getPeople_id());
        values.put(KEY_EVENT_ID, splits.getEvent_id());
        values.put(KEY_AMOUNT, splits.getAmount());
        
        // insert row
        long split_id = db.insert(TABLE_SPLIT, null, values);
        
        return split_id;
    }
    
    
    
    /*
     * get single Split
     */
    public Split getSplit(int split_id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_SPLIT + " WHERE "
                + KEY_ID + " = " + split_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     

        Split split = new Split();
        split.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        split.setPeople_id(c.getInt(c.getColumnIndex(KEY_PEOPLE_ID)));
        split.setEvent_id(c.getInt(c.getColumnIndex(KEY_EVENT_ID)));
        split.setAmount(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
        
        return split;
    }
    
    
    
    /*
     * get Splits for a given EventId
     */
    public List<Split> getSplitsByEventId(int event_id) {
    	 List<Split> splits = new ArrayList<Split>();
    	SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_SPLIT + " WHERE "
                + KEY_EVENT_ID + " = " + event_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
     // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
	
		        Split split = new Split();
		        split.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		        split.setPeople_id(c.getInt(c.getColumnIndex(KEY_PEOPLE_ID)));
		        split.setEvent_id(c.getInt(c.getColumnIndex(KEY_EVENT_ID)));
		        split.setAmount(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
		        
		        //add to the splits list
		        splits.add(split);
            } while (c.moveToNext());
        }
        
        return splits;
    }
    
    
    /*
     * Deleting a SPLIT
     */
    public void deleteSplit(int split_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SPLIT, KEY_ID + " = ?",
                new String[] { String.valueOf(split_id) });
    }
    
    
    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
      


    // get stats by person id
    public List<PeopleStats> getStatsByDebtrId(int debtr_id) {
    	
    	 List<PeopleStats> peoplestats = new ArrayList<PeopleStats>();
     	SQLiteDatabase db = this.getReadableDatabase();
      
         String selectQuery = "SELECT " + KEY_ID + "," + KEY_NAME + ", sum(paidamount - " + KEY_AMOUNT + ") as " + KEY_AMOUNT + " from ( " +
        		 	"Select p." + KEY_ID + " as " + KEY_ID +", p." + KEY_NAME + " as " + KEY_NAME + ", case when p." + KEY_ID + " = ev." + KEY_PAYEE_ID + " then ev." + KEY_AMOUNT + " else 0 end as paidamount, s." + KEY_AMOUNT + "  from " + TABLE_EVENT + " ev " +
        		 	"Join " + TABLE_SPLIT + " s on s." + KEY_EVENT_ID + " = ev." + KEY_ID + " " +
        		 	"Join " + TABLE_PEOPLE + " p on p." + KEY_ID + " = s." + KEY_PEOPLE_ID + " " +
        		 	"where ev." + KEY_DEBTR_ID + " = " + debtr_id + ") as foo " +
        		 	"Group by " + KEY_ID + ", " + KEY_NAME;
      
         Log.e(LOG, selectQuery);
      
         Cursor c = db.rawQuery(selectQuery, null);
      
      // looping through all rows and adding to list
         if (c.moveToFirst()) {
             do {
 	
 		        PeopleStats peoplestat = new PeopleStats();
 		        peoplestat.setPeople_id(c.getInt(c.getColumnIndex(KEY_ID)));
 		        peoplestat.setName(c.getString(c.getColumnIndex(KEY_NAME)));
 		        peoplestat.setAmount(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
 		        
 		        //add to the splits list
 		       peoplestats.add(peoplestat);
             } while (c.moveToNext());
         }
         
         return peoplestats;
    }
} 
