package com.adamcox.rapbeatspro;

/**
 * This class is for "Make a Rap" UI window...
 */

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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adamcox.rapbeats.database.DatabaseAdapter;

@SuppressWarnings("deprecation")
public class MySavedRaps extends ListActivity {
	private DatabaseAdapter databaseAdapter;

	private MySavedRaps me = this;
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;
	
	private int dialog_num = 1;
	
	private int s_position;
	
	private Cursor mCursor;
	
	private ArrayList<Integer> l_id;
	private ArrayList<String> l_title;
	private ArrayList<String> l_instrumentals;
	private ArrayList<String> l_lyrics;
	
	private Integer[] imgid = {
			R.drawable.music48px
	};

	ListView lv;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	databaseAdapter = new DatabaseAdapter(this);
    	databaseAdapter.open();

        //Load saved raps...
        loadSavedRaps();
        
        //display the users entries...
        displayEntries();
        
        databaseAdapter.close();
    }

	//Now that we know they are all there, make a list with each entry as an item...
	//Clicking the list item will start an activity displaying that entry...
    private void displayEntries()
    {
		setContentView(R.layout.my_saved_raps);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		
		//Need to get all user entriess from database...
		
		for( int i=0; i < l_title.size(); i++){
		    Log.d("ViewGroupsActivity", "display title " + l_title.get(i) );
			try {
				rd = new RowData(i,l_title.get(i),"Click to Listen");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);

		}
		
		CustomAdapter adapter = new CustomAdapter(this, R.layout.list, R.id.title, data);
		
		setListAdapter(adapter);
		
		lv = getListView();
		
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
				onLongListItemClick(v,pos,id);
				return true;
			}
		}); 
		
		lv.setLongClickable(true);
		
		registerForContextMenu( getListView() );
    }

	//Get the groups from the local database and load them into memory...
	private void loadSavedRaps()
	{
	    mCursor = databaseAdapter.fetchAllEntries();

	    l_id = new ArrayList<Integer>();
	    l_title = new ArrayList<String>();
	    l_instrumentals = new ArrayList<String>();
	    l_lyrics = new ArrayList<String>();

	    int count = mCursor.getCount();

	    if( mCursor.moveToFirst() ) {
		    for(int x = 0; x < count; x++ ) {
		    	//The title on the menu:
		    	l_id.add(x, mCursor.getInt( mCursor.getColumnIndex("id") ) );
		    	l_title.add(x, mCursor.getString( mCursor.getColumnIndex("title") ) );
		    	l_instrumentals.add(x, mCursor.getString( mCursor.getColumnIndex("instrumentals") ) );
		    	l_lyrics.add(x, mCursor.getString( mCursor.getColumnIndex("lyrics") ) );

				mCursor.moveToNext();
		    }
	    }
	    
	    mCursor.close();

	}

    //this will probably only be used for error reporting to user...
	@Override
    protected Dialog onCreateDialog(int id) {
		if( id == 0 ) {
	        return new AlertDialog.Builder(MySavedRaps.this)
            .setTitle("Rap Beats Pro")
            .setMessage( me.getResources().getString(R.string.upgrade) )
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so do some stuff */
                	Intent intent = new Intent(Intent.ACTION_VIEW);
                	intent.setData(Uri.parse("market://details?id=com.adamcox.rapbeatspro"));
                	startActivity(intent);
                	me.finish();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked Cancel so do some stuff */
                }
            })
            .create();
		} else {
			return new AlertDialog.Builder(MySavedRaps.this)
	            .setTitle("Deleting...")
	            .setMessage("Are you sure you want to delete this rap?")
	            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int whichButton) {
	            		//Do nothing...
	            	}
	            })
	            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	databaseAdapter = new DatabaseAdapter(me);
	                	databaseAdapter.open();
	
	                	databaseAdapter.deleteEntry( l_id.get(s_position));
	
	                	l_id = null;
	                	l_title = null;
	                	l_instrumentals = null;
	                	l_lyrics = null;
	                	
	                	lv.invalidate();
	
	                    //Load saved raps...
	                    loadSavedRaps();
	                    
	                    //display the users entries...
	                    displayEntries();
	                	
	                	databaseAdapter.close();
	                }
	            })
	            .create();
		}
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


	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Log.d("MySavedRaps", "You have selected rap id: " + l_id.get(position) );
		
    	Intent intent = new Intent(me, SavedRapsPlayer.class);
		intent.putExtra("Title",l_title.get(position));
		intent.putExtra("Instrumentals",l_instrumentals.get(position));
		intent.putExtra("Lyrics",l_lyrics.get(position));
    	startActivity(intent);
   		if( ! me.getResources().getBoolean(R.bool.app_is_pro)) {
   			showDialog(0);
   		}
	}

    protected void onLongListItemClick(View v, int position, long id) {
    	//prompt user to delete list item
    	s_position = position;
		showDialog(dialog_num++);

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
					i11 = (ImageView) mRow.findViewById(R.id.itemlogo);
				}
                return i11;
			}
		}
	}
}