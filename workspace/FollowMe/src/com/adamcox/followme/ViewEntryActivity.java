package com.adamcox.followme;

import com.adamcox.followme.database.DatabaseAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;


/**
 * This will view a single entry based on the id passed in through the extras.
 * 
 * It should handle all the types of entries such as text, picture, audio, video.
 * 
 * @author Adam
 *
 */

public class ViewEntryActivity extends Activity {
    /** Called when the activity is first created. */
	
	private DatabaseAdapter databaseAdapter;
	private Cursor mCursor;

	private int id; //id of localhost
	//private int userid;
	private String username;
	//private int groupid;
	private int type;
	private String title;
	private String content;
	private String timestamp;
	
	//int id, int userid, String username, int groupid, int type, String title, String content, String timestamp
	
	private TextView tvTitle;
	private TextView tvContent;
	private TextView tvUsername;
	private TextView tvTimestamp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	databaseAdapter = new DatabaseAdapter(this);
    	databaseAdapter.open();
        
        //Get id from extras

        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
        	id = extras.getInt("id");
        }

        //Select entry from database
	    mCursor = databaseAdapter.fetchEntryByLocalId(id);

	    //Toast.makeText(ViewEntryActivity.this, "view entry got id: " + id, Toast.LENGTH_SHORT).show();
	    
	    if( mCursor.moveToFirst() ) {
			//userid = mCursor.getInt( mCursor.getColumnIndex("userid") );
			username = mCursor.getString( mCursor.getColumnIndex("username") );
			//groupid = mCursor.getInt( mCursor.getColumnIndex("groupid") );
			type = mCursor.getInt( mCursor.getColumnIndex("type") );
			title = mCursor.getString( mCursor.getColumnIndex("title") );
			content = mCursor.getString( mCursor.getColumnIndex("content") );
			timestamp = mCursor.getString( mCursor.getColumnIndex("timestamp") );
	    }
        //Switch on type of entry

	    mCursor.close();
	    
	    //Toast.makeText(ViewEntryActivity.this, "got type " + type + " and title " + title, Toast.LENGTH_SHORT).show();
	    
	    switch( type ) {
	    case 1:
	    	//View Text Entry:
	    	viewTextEntry();
	    	break;
	    case 2:
	    	//View Picture Entry:
	    	viewPictureEntry();
	    	break;
	    case 3:
	    	//View Audio Entry:
	    	viewAudioEntry();
	    	break;
	    case 4:
	    	//View Video Entry:
	    	viewVideoEntry();
	    	break;
	    }
	    
	    databaseAdapter.close();
	    
    }
    
	private void viewTextEntry() {
        setContentView(R.layout.view_text_entry);

		tvTitle = (TextView) findViewById(R.id.title);
		tvContent = (TextView) findViewById(R.id.content);
		tvUsername = (TextView) findViewById(R.id.username);
		tvTimestamp = (TextView) findViewById(R.id.timestamp);

		tvTitle.setText( "Title: " + title );
		tvContent.setText( "Content: " + content );
		tvUsername.setText( "Username: " + username );
		tvTimestamp.setText( "Timestamp: " + timestamp );
		
	}
    
	private void viewPictureEntry() {
        //setContentView(R.layout.view_picture_entry);
		
	}
    
	private void viewAudioEntry() {
		
	}
    
	private void viewVideoEntry() {
		
	}
	
}