package com.adamcox.followme.database;

import android.content.Context;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.util.Log;  
  
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "followme";  
    private static final int DATABASE_VERSION = 21;
  
    // Entry table creation SQL statement  
    private static final String ENTRY_TABLE_CREATE = "create table entries ("
    		+ "_id integer primary key autoincrement, "  /* localhost */
            + "id integer not null, " /* server */
    		+ "userid integer not null, "
    		+ "username text not null, "
            + "groupid integer not null, "
            + "type integer not null, "
    		+ "title text not null, "
            + "content text not null, "
    		+ "timestamp text not null );";  
  
    // Group table creation SQL statement  
    private static final String GROUP_TABLE_CREATE = "create table groups ("
    		+ "_id integer primary key autoincrement, " /* localhost */
            + "id integer not null, " /* server */
    		+ "adminid integer not null, "
    		+ "groupname text not null, "
            + "password integer not null, "
    		+ "time text not null );";

    public DatabaseHelper(Context context) {  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }  
  
    // Method is called during creation of the database  
    @Override  
    public void onCreate(SQLiteDatabase database) {  
        database.execSQL(GROUP_TABLE_CREATE);
        database.execSQL(ENTRY_TABLE_CREATE);
    }  
  
    // Method is called during an upgrade of the database, e.g. if you increase  
    // the database version  
    @Override  
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {  
        Log.d(DatabaseHelper.class.getName(),  
        	"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");  
        database.execSQL("DROP TABLE IF EXISTS entries");
        database.execSQL("DROP TABLE IF EXISTS groups");
        onCreate(database);  
    }  
}  