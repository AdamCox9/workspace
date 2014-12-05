package com.adamcox.rapbeats.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
  
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rapbeats";
    private static final int DATABASE_VERSION = 5;
  
    // Entry table creation SQL statement  
    private static final String ENTRY_TABLE_CREATE = "create table entries ("
    		+ "id integer primary key autoincrement, "
    		+ "title text not null, " //this will be title of rap...
    		+ "instrumentals text not null, " //this will be path to instrumentals...
            + "lyrics text not null );";  //this will be path to lyrics...
  
    /*
     * To Do:
     * 
     * Need to create a "beats" table that will list all beats.
     * 
     * This will allow imported beats.
     * 
     * Title, Filename, Downloaded, Type
     * 
     * Type will be either:		0 - "downloaded over the web" either default beats or user added beats
     * 							1 - from Media folder, so we won't have to copy them over...we will be able to play them from Media folder
     * 
     */

    public DatabaseHelper(Context context) {  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }  
  
    // Method is called during creation of the database  
    @Override  
    public void onCreate(SQLiteDatabase database) {  
        database.execSQL(ENTRY_TABLE_CREATE);
    }  
  
    // Method is called during an upgrade of the database, e.g. if you increase  
    // the database version  
    @Override  
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {  
        Log.d(DatabaseHelper.class.getName(),  
        	"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");  
        //database.execSQL("DROP TABLE IF EXISTS entries");
        //onCreate(database);  
    }  
}  