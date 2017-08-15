package com.adamcox.followme.database;

/*
 * Thanks to http://www.vogella.de/code/de.vogella.android.todos/src/de/vogella/android/todos/database/codestartpage.html
 */

import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.SQLException;  
import android.database.sqlite.SQLiteDatabase;  

public class DatabaseAdapter {

    private static final String DATABASE_ENTRIES_TABLE = "entries";
    private static final String DATABASE_GROUPS_TABLE = "groups";
    
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
     * Create a new entry.
     * If the entry is successfully created return the new 
     * rowId for that entry, otherwise return a -1 to indicate failure. 
     */  
    public long createEntry(int id, int userid, String username, int groupid, int type, String title, String content, String timestamp) {
        ContentValues initialValues = createContentValues(id, userid, username, groupid, type, title, content, timestamp);
        return database.insert(DATABASE_ENTRIES_TABLE, null, initialValues);
    }  

    /** 
     * Create a new group.
     * If the entry is successfully created return the new 
     * rowId for that entry, otherwise return a -1 to indicate failure. 
     */  
    public long createGroup(int id, int adminid, String groupname, String password, String time) {//id is that of row in server database...
        ContentValues initialValues = createGroupContentValues(id, adminid, groupname, password, time);
        return database.insert(DATABASE_GROUPS_TABLE, null, initialValues);  
    }

    /** 
     * Update the entry
     */  
    public boolean updateEntry(long rowId, int id, int userid, String username, int groupid, int type, String title, String content, String timestamp) {
        ContentValues updateValues = createContentValues(id, userid, username, groupid, type, title, content, timestamp);
        return database.update(DATABASE_ENTRIES_TABLE, updateValues, "_id=" + rowId, null) > 0;
    }  
  
    /** 
     * Deletes an entry
     */  
    public boolean deleteEntry(long rowId) {  
        return database.delete(DATABASE_ENTRIES_TABLE, "_id" + "=" + rowId, null) > 0;  
    }  
  
    /** 
     * Return a Cursor over the list of all entries in the database 
     *  
     * @return Cursor over all notes 
     */  
    public Cursor fetchAllEntries() {  
        return database.query(DATABASE_ENTRIES_TABLE, new String[] { 
        		"_id", "id", "userid", "username", "groupid", "type", "title", "content", "timestamp" }, null, null, null,  
                null, null);  
    }
  
    /** 
     * Return a Cursor over the list of all entries by username in the database 
     *  
     * @return Cursor over all notes 
     */  
    public Cursor fetchUserEntries(String username) {
        return database.query(DATABASE_ENTRIES_TABLE, new String[] { 
        		"_id", "id", "userid", "username", "groupid", "type", "title", "content", "timestamp" },
        		"username='" + username + "'", null, null, null, null, null);  
    }  

    /** 
     * Return a Cursor over the list of all entries by group id in the database 
     *  
     * @return Cursor over all notes 
     */  
    public Cursor fetchGroupEntries(int groupid) {
        return database.query(DATABASE_ENTRIES_TABLE, new String[] { 
        		"_id", "id", "userid", "username", "groupid", "type", "title", "content", "timestamp" },
        		"groupid=" + groupid, null, null, null, null, null);
    }  

    /** 
     * Return a Cursor over the list of all groups in the database 
     *  
     * @return Cursor over all groups
     */  
    public Cursor fetchAllGroups() {
        return database.query(DATABASE_GROUPS_TABLE, new String[] { 
        		"_id", "id", "adminid", "groupname", "password", "time" }, null, null, null, null, null);  
    }  
  
    /** 
     * Return a Cursor positioned at the defined entry
     * This searches by local primary key.
     */  
    public Cursor fetchEntryByLocalId(long rowId) throws SQLException {  
        return database.query(true, DATABASE_ENTRIES_TABLE, new String[] {
        		"_id", "id", "userid", "username", "groupid", "type", "title", "content", "timestamp" },
                "_id=" + rowId, null, null, null, null, null);
    }

    /** 
     * Return a Cursor positioned at the defined entry
     * This searches by local primary key.
     */  
    public Cursor fetchEntryByServerId(long rowId) throws SQLException {  
        return database.query(true, DATABASE_ENTRIES_TABLE, new String[] {
        		"_id", "id", "userid", "username", "groupid", "type", "title", "content", "timestamp" },
                "id=" + rowId, null, null, null, null, null);
    }

    /** 
     * Return a Cursor positioned at the defined entry
     * This searches by local primary key.
     */  
    public Cursor fetchGroup(long rowId) throws SQLException {  
        return database.query(true, DATABASE_GROUPS_TABLE, new String[] {
        		"_id", "id", "adminid", "groupname", "password", "time" },
        		"_id=" + rowId, null, null, null, null, null);
    }

    /** 
     * Return a Cursor positioned at the defined entry
     * This searches by local primary key.
     */  
    public Cursor fetchGroupByServerId(long rowId) throws SQLException {  
        return database.query(true, DATABASE_GROUPS_TABLE, new String[] {
        		"_id", "id", "adminid", "groupname", "password", "time" },
        		"id=" + rowId, null, null, null, null, null);
    }

    /**
     * Helper method to create the content values for an entry as it is in the local database.
     * The only difference between this and the server database is that this has id of row on server database.
     * 
     * KEY_ROWID is auto increment key for row in local database.
     * 
     * @param id
     * @param userid
     * @param username
     * @param groupid
     * @param type
     * @param title
     * @param content
     * @param timestamp
     * @return
     */
    private ContentValues createContentValues(int id, int userid, String username, int groupid, int type, String title, String content, String timestamp) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("userid", userid);
        values.put("username", username);
        values.put("groupid", groupid);
        values.put("type", type);
        values.put("title", title);
        values.put("content", content);
        values.put("timestamp", timestamp);
        return values;
    }

    private ContentValues createGroupContentValues(int id, int adminid, String groupname, String password, String time) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("adminid", adminid);
        values.put("groupname", groupname);
        values.put("password", password);
        values.put("time", time);
        return values;
    }

}