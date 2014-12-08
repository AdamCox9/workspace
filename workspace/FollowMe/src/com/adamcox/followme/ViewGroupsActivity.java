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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adamcox.followme.database.DatabaseAdapter;

public class ViewGroupsActivity extends ListActivity {
	private DatabaseAdapter databaseAdapter;
	private HttpService httpService;
	private ViewGroupsActivity me = this;

	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;
	
	private String dialogText = null;
	
//	private int dialogNum = 0;

	private String username = null;
	
	private Cursor mCursor;
	
	private final int REQUESTTOJOINGROUP = 1234321;
	
	private ArrayList<Integer> l_id;
	private ArrayList<String> l_title;
	private ArrayList<String> l_detail;
	
	private Integer[] imgid = {
			R.drawable.group
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

        //Eventually, we won't want to get all the groups, but for now it is OK...
        //Could select a random few and then allow users to search for groups...
        
        //First the groups need to be synchronized with the server...
        syncEntries();
        
        //Load the groups from database to arrays, so UI don't get hung
        loadGroups();
        
        //display the groups...
        displayEntries();
        
        databaseAdapter.close();
    }

	//Now that we know they are all there, make a list with each entry as an item...
	//Clicking the list item will start an activity displaying that entry...
    private void displayEntries()
    {
		setContentView(R.layout.view_groups);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		
		//Need to get all user items from database...
		
		for( int i=0; i < l_title.size(); i++){
		    Log.d("ViewGroupsActivity", "display title " + l_title.get(i) );
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
    
	//Get the groups from the local database and load them into memory...
	private void loadGroups()
	{
	    mCursor = databaseAdapter.fetchAllGroups();

	    l_id = new ArrayList<Integer>();
	    l_title = new ArrayList<String>();
	    l_detail = new ArrayList<String>();

	    int count = mCursor.getCount();

	    if( mCursor.moveToFirst() ) {
		    for(int x = 0; x < count; x++ ) {
		    	//The title on the menu:
		    	l_id.add(x, mCursor.getInt( mCursor.getColumnIndex("id") ) );
		    	l_title.add(x, mCursor.getString( mCursor.getColumnIndex("groupname") ) );
		    	l_detail.add(x, "open group" );

				mCursor.moveToNext();
		    }
	    }
	    
	    mCursor.close();

	}

    /**
     * This will synchronize the groups on the server database with the local database...
     * This should be done as an Async task so it don't hang the UI...
     * There should be a progress bar, also...
     *
     */
    private void syncEntries()
    {
    	//Server should output values in JSON...
    	try {
			JSONArray jSonArray = new JSONArray( httpService.getGroups() );
			JSONArray jSonGroup;
			JSONObject row;
			JSONObject group;

			int id; //server id of entry row in database...
			int adminid;
			String groupname;
			String password;
			String time;
			
	    	//Loop through ids...
			for (int i = 0; i < jSonArray.length(); i++) {
			    row = jSonArray.getJSONObject(i);
			    id = Integer.valueOf( row.getString("id") );

			    //Toast.makeText(ViewGroupsActivity.this, "got id " + id, Toast.LENGTH_SHORT).show();
			    
                //Query database for this entry...
			    mCursor = databaseAdapter.fetchGroupByServerId( (long)id );

			    
            	//If don't have local copy, get it from the server and insert it into database...
			    if( mCursor.getCount() == 0 ) {
			    	//Get it from the server:
			    	jSonGroup = new JSONArray( httpService.getGroup(id) );
				    group = jSonGroup.getJSONObject(0);
				    id = group.getInt("id");
				    adminid = group.getInt("adminid"); //Admin Name could be useful...
				    groupname = group.getString("groupname");
				    password = group.getString("password");
				    time = group.getString("time");

				    if( databaseAdapter.createGroup(id, adminid, groupname, password, time) == -1 ) {
				    	//make a dialog telling user there was an error with the database...
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
        return new AlertDialog.Builder(ViewGroupsActivity.this)
            .setTitle("FollowMe")
            .setMessage(dialogText)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so do some stuff */
                }
            })
            .create();
    }

	/**
	 * Should probably do something here...
	 */
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

	/**
	 * This handles user permissions to join/view this group
	 */
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Log.d("ViewGroupsActivity", "You have selected groupid: " + l_id.get(position) );
		
		//Check server if user is joined to this group...
		
		String response;
		try {
			response = httpService.checkUserConfirmedToGroup(username, l_id.get(position)).replaceAll("[^A-Z_]", "");//sloppy
			
            Toast.makeText(ViewGroupsActivity.this, "Response: " + response, Toast.LENGTH_SHORT).show();
			
			//If joined to group...
			if( response.equals( "GROUP_MEMBER" ) ) {
				//View Entries...
		    	Intent intent = new Intent(me, ViewGroupEntriesActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("groupid", l_id.get(position));
		    	startActivity(intent);
			} else {
			//If not joined to group...
				//Send them to RequestToJoinGroupActivity
		    	Intent intent = new Intent(me, RequestToJoinGroupActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("groupid", l_id.get(position));
	        	startActivityForResult(intent, REQUESTTOJOINGROUP);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  

        Log.d("onActivityResult", "ViewGroupsActivity: " + requestCode + " " + resultCode + ":" + RESULT_OK );
    	
    	//This can be returned by any Activity:
    	switch ( requestCode ) {
    	case REQUESTTOJOINGROUP:
    		if( resultCode == RESULT_OK ) {
                Toast.makeText(ViewGroupsActivity.this, "Join Request Sent", Toast.LENGTH_SHORT).show();
    		}
    		break;
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
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