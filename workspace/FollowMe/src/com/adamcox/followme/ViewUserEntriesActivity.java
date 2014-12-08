package com.adamcox.followme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adamcox.followme.database.DatabaseAdapter;

/*
 * This class will view all the entries created by the currently logged in user.
 */

public class ViewUserEntriesActivity extends ListActivity {
	private DatabaseAdapter databaseAdapter;
	private HttpService httpService;
	private ViewUserEntriesActivity me = this;

	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;
	
	private String dialogText = null;
	private int dialogNum = 0;

	private String username = null;
	
	private Cursor mCursor;
	
	private ArrayList<Integer> l_id;
	private ArrayList<String> l_title;
	private ArrayList<String> l_detail;
	
	private Integer[] imgid = {
			R.drawable.text_entry
	};

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	httpService = new HttpService();
    	databaseAdapter = new DatabaseAdapter(this);
    	databaseAdapter.open();
    	
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
        	username = extras.getString("username");
        }

	    Log.d("ViewUserEntriesActivity", "Got username: " + username );
        
        //First the items need to be synched with the server...
        
        syncEntries();
        
        //Load the entries from database to arrays, so UI don't get hung
        
        loadEntries();
        
        //display the entries...
        
        displayEntries();
        
        databaseAdapter.close();
    }

	private void loadEntries()
	{
	    mCursor = databaseAdapter.fetchUserEntries(username);
	    
	    l_id = new ArrayList<Integer>();
	    l_title = new ArrayList<String>();
	    l_detail = new ArrayList<String>();

	    int count = mCursor.getCount();

	    if( mCursor.moveToFirst() ) {
		    for(int x = 0; x < count; x++ ) {
		    	l_id.add(x, mCursor.getInt( mCursor.getColumnIndex("_id") ) );
		    	l_title.add(x, mCursor.getString( mCursor.getColumnIndex("title") ) );
		    	l_detail.add(x, String.valueOf( mCursor.getInt( mCursor.getColumnIndex("type") ) ) );

		    	mCursor.moveToNext();
		    }
	    }

	    mCursor.close();
	}

	//Now that we know they are all there, make a list with each entry as an item...
	//Clicking the list item will start an activity displaying that entry...
    private void displayEntries()
    {
		setContentView(R.layout.view_entries);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		
		//Need to get all user items from local memory...
		
		for( int i=0; i < l_title.size(); i++){
			try {
				rd = new RowData(i,l_title.get(i),l_detail.get(i));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);
		}
		
		CustomAdapter adapter = new CustomAdapter(this, R.layout.list, R.id.title, data);
		
		setListAdapter(adapter);
		
		getListView().setTextFilterEnabled(true);
    }
    
    //This will synchronize the entries on the server database with the local database...
    //This should be done as an Async task so it don't hang the UI...
    //There should be a progress bar, also...
    private void syncEntries()
    {
    	//Server should output values in JSON...
    	try {
			JSONArray jSonArray = new JSONArray( httpService.getUserEntries(username) );
			JSONArray jSonEntry;
			JSONObject row;
			JSONObject entry;

			int id; //server id of entry row in database...
			int userid;
			int groupid;
			int type;
			String title;
			String content;
			String timestamp;
			
	    	//Loop through ids...
			for (int i = 0; i < jSonArray.length(); i++) {
			    row = jSonArray.getJSONObject(i);
			    id = row.getInt("id");

                //Query database for this entry...
			    mCursor = databaseAdapter.fetchEntryByServerId( (long)id );
			    
            	//If don't have local copy, get it from the server and insert it into database...
			    if( mCursor.getCount() == 0 ) {
			    	//Get it from the server:
			    	jSonEntry = new JSONArray( httpService.getEntry(id) );
				    entry = jSonEntry.getJSONObject(0);
				    id = entry.getInt("id");
				    userid = entry.getInt("userid");
				    //we already know the username..
				    groupid = entry.getInt("groupid");
				    type = entry.getInt("type");
				    title = entry.getString("title");
				    content = entry.getString("content");
				    timestamp = entry.getString("timestamp");

				    //Toast.makeText(ViewUserEntriesActivity.this, "got id " + id + " and title " + title, Toast.LENGTH_SHORT).show();

				    //Insert it into database:

				    //To do: if this is anything other than text, we should save file to SD card and put filename as content...
				    
				    if( databaseAdapter.createEntry(id, userid, username, groupid, type, title, content, timestamp) == -1 ) {
						dialogText = "LOCAL_DATABASE_ERROR";
						showDialog(dialogNum++);
				    }
				    
			    } //else it already exists in local database...
			    
			    mCursor.close();

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//entries on phone & server are now synchronized...
    }

    //this will probably only be used for error reporting to user...
	@Override
    protected Dialog onCreateDialog(int id) {
        return new AlertDialog.Builder(ViewUserEntriesActivity.this)
            .setTitle("FollowMe")
            .setMessage(dialogText)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so do some stuff */
                }
            })
            .create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	return true;
    }
    
	OnClickListener mBackListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	public void onListItemClick(ListView parent, View v, int position, long id) {        	
		//Toast.makeText(getApplicationContext(), "You have selected " + (position+1) + "th item",  Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(me, ViewEntryActivity.class);
		intent.putExtra("id", l_id.get(position));
    	startActivity(intent);
	}

	private class RowData {
		protected int mId;
		protected String mTitle;
		protected String mDetail;
		
		RowData(int id,String title,String detail) {
			mId=id;
			mTitle = title;
			mDetail=detail;
		}

		@Override
       public String toString() {
               return mId+" "+mTitle+" "+mDetail;
       }
		
	}
	
	private class CustomAdapter extends ArrayAdapter<RowData> {

		public CustomAdapter(Context context, int resource, int textViewResourceId, List<RowData> objects) {               
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {   
			ViewHolder holder = null;
			TextView title = null;
			TextView detail = null;
			ImageView i11=null;
			RowData rowData= getItem(position);

			if(null == convertView){
				convertView = mInflater.inflate(R.layout.list, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			title = holder.gettitle();
			title.setText(rowData.mTitle);
			detail = holder.getdetail();
			detail.setText(rowData.mDetail);                                                     
			i11=holder.getImage();
			i11.setImageResource(imgid[0]);

			return convertView;
		}

		private class ViewHolder {

			private View mRow;
			private TextView title = null;
			private TextView detail = null;
			private ImageView i11=null; 
			
			public ViewHolder(View row) {
				mRow = row;
            }
			
			public TextView gettitle() {
				if(null == title){
					title = (TextView) mRow.findViewById(R.id.title);
                }
				return title;
			}     
			
			public TextView getdetail() {
				if(null == detail){
					detail = (TextView) mRow.findViewById(R.id.detail);
				}
				return detail;
			}
			
			public ImageView getImage() {
				
				if(null == i11){
					i11 = (ImageView) mRow.findViewById(R.id.text_entry);
				}
                return i11;
			}
		}
	}
}