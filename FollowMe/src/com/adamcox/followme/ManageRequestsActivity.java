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

public class ManageRequestsActivity extends ListActivity {
	private HttpService httpService;

	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;
	
	private String dialogText = null;
	private int dialogNum = 0;

	//Will be set to logged in user:
	private String username = null;
	
	//Values to be sent over the network:
	private int position_clicked;
	private String username_requested;

	//Store values got from server:
	private ArrayList<Integer> l_id;
	private ArrayList<String> l_username;
	private ArrayList<String> l_title;
	private ArrayList<String> l_detail;
	
	private Integer[] imgid = {
			R.drawable.group
	};

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	httpService = new HttpService();
    	
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
        	username = extras.getString("username");
        }

        //First the group join requests need to be synchronized with the server...just to local memory...not storage
        syncRequestsToJoinGroups();

		//display the groups...
        displayRequestsToJoinGroups();
        
    }


	//Now that we know they are all there, make a list with each entry as an item...
	//Clicking the list item will start an activity displaying that entry...
    private void displayRequestsToJoinGroups()
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
    
    /**
     * This will synchronize the groups on the server database with the local database...
     * This should be done as an Async task so it don't hang the UI...
     * There should be a progress bar, also...
     *
     */
    private void syncRequestsToJoinGroups()
    {
    	//Server should output values in JSON...

	    l_id = new ArrayList<Integer>();
	    l_username = new ArrayList<String>();
	    l_title = new ArrayList<String>();
	    l_detail = new ArrayList<String>();

	    try {
			JSONArray jSonArray = new JSONArray( httpService.getRequestsToJoinGroups( username ) );
			JSONObject row;

			int groupid;
			String username; //Don't overwrite parent version...
			String groupname;
			
	    	//Loop through ids...
			for (int i = 0; i < jSonArray.length(); i++) {
			    row = jSonArray.getJSONObject(i);
			    
			    groupid = row.getInt("groupid"); //Admin Name could be useful...
			    username = row.getString("username");
			    groupname = row.getString("groupname");

		    	l_id.add(i, groupid ); //Group ID on the server?
		    	l_username.add(i, username ); //Group ID on the server?
		    	l_title.add(i, groupname );
		    	l_detail.add(i, "Allow/Deny? " + username );

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
        return new AlertDialog.Builder(ManageRequestsActivity.this)
            .setTitle("Manage Requests")
            .setMessage(dialogText)
            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked Allow so do some stuff */
            	    //Make a request to the server to update with positive confirm status
                	try {
						String response = httpService.confirmUserToGroup(username_requested, position_clicked).replaceAll("[^A-Z_]", "");//sloppy
						if( response.equals( "CONFIRM_STATUS_UPDATED" ) ) {
		            	    Toast.makeText(ManageRequestsActivity.this, "User Confirmed to Group", Toast.LENGTH_SHORT).show();
						} else {
		            	    Toast.makeText(ManageRequestsActivity.this, response, Toast.LENGTH_SHORT).show();
						}
                	} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            })
            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked Deny so do some stuff */
            	    Toast.makeText(ManageRequestsActivity.this, "You Denied Them", Toast.LENGTH_SHORT).show();
            	    //Already denied to group by default...
            	    //@Todo allow user to unjoin someone to a group...
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
		Log.d("Manage Requests Activity", "You have selected group join request id: " + l_id.get(position) );
		
		//Show dialog asking to accept/deny
		position_clicked = l_id.get(position);
		username_requested = l_username.get(position);
   		dialogText = "Allow them to join? groupid: " + position_clicked;
   		showDialog(dialogNum++);
		
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

	//Since this don't change, it should be put into an external class and reused...
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