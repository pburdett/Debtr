package com.burdysoft.debtr.resources;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_EVENT = "e";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_AMOUNT = "amount";
  public static final String COLUMN_PAYEEID = "payee_id";
  public static final String COLUMN_SPLITID = "split_id";
  public static final String COLUMN_PHOTO = "photo_boolean";
  public static final String COLUMN_PHOTOLINK = "photo_link";

  private static final String DATABASE_NAME = "debtr.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_EVENT + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_NAME
      + " text not null, " + COLUMN_DATE + " text not null, " + COLUMN_AMOUNT + " real not null, " + COLUMN_PAYEEID
      + " integer not null, " + COLUMN_SPLITID + " integer not null, " + COLUMN_PHOTO + " integer not null, " + COLUMN_PHOTOLINK + " text);";

  
  
  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
    onCreate(db);
  }

} 