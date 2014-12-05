package com.adamcox.rapbeats.database;

import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.SQLException;  
import android.database.sqlite.SQLiteDatabase;  

public class DatabaseAdapter {

    private static final String DATABASE_ENTRIES_TABLE = "entries";
    
    private Context context;  
    private SQLiteDatabase database;  
    private DatabaseHelper databaseHelper;  
  
    public DatabaseAdapter(Context context) {  
        this.context = context;  
    }  
  
    public DatabaseAdapter open() throws SQLException {  
        databaseHelper = new DatabaseHelper(context);  
        database = databaseHelper.getWritableDatabase();  
        return this;  
    }  
  
    public void close() {  
        databaseHelper.close();  
    }  

    /** 
     * Creates an entry...
     * If the entry is successfully created return the new...
     * rowId for that entry, otherwise return a -1 to indicate failure...
     */  
    public long createEntry(String title, String instrumentals, String lyrics) {
        ContentValues initialValues = createContentValues(title, instrumentals, lyrics);
        return database.insert(DATABASE_ENTRIES_TABLE, null, initialValues);
    }  

    /** 
     * Deletes an entry...
     */  
    public boolean deleteEntry(long rowId) {  
        return database.delete(DATABASE_ENTRIES_TABLE, "id" + "=" + rowId, null) > 0;  
    }  
  
    /** 
     * Return a Cursor over the list of all entries in the database...
     *  
     * @return Cursor over all notes 
     */  
    public Cursor fetchAllEntries() {  
        return database.query(DATABASE_ENTRIES_TABLE, new String[] { "id", "title", "instrumentals", "lyrics" }, null, null, null, null, null);  
    }

    /** 
     * Return a Cursor over the the entry specified by rowId...
     *  
     * @param id

     * @return Cursor over an entry
     */  
    public Cursor fetchEntryById(long rowId) throws SQLException {  
        return database.query(true, DATABASE_ENTRIES_TABLE, new String[] { "id", "title", "instrumentals", "lyrics" }, "id=" + rowId, null, null, null, null, null);
    }

    /**
     * Stupid function to build an array type structure...
     * 
     * @param title
     * @param instrumentals
     * @param lyrics
     * 
     * @return
     */
    private ContentValues createContentValues(String title, String instrumentals, String lyrics) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("instrumentals", instrumentals);
        values.put("lyrics", lyrics);
        return values;
    }

}