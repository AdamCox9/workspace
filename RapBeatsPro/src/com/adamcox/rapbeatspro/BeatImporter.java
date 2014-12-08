package com.adamcox.rapbeatspro;

import java.io.File;
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
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * This class needs to search the hard drive for MP3s, WAVs, and other common music formats and play them when they are clicked...
 * Or, if there is a way to get the items from the media folder, that would be ideal...
 * 
 */

public class BeatImporter extends ListActivity {
	private BeatImporter me = this;
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;
	
	/*
	 * Import Beats Variables
	 */
	private List<String> item = null;
	private List<String> path = null;


	private Integer[] imgid = {
			R.drawable.music48px,
			R.drawable.folder48px
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_beats);
		
		item = new ArrayList<String>();
		path = new ArrayList<String>();
	
    	//_____Import Beats Stuff:

        Bundle extras = getIntent().getExtras();

        if( extras != null ) {
        	String m_path = extras.getString("path");

    		//Make sure users can go up a directory when they need to...
        	//The "return" button on the phone should be overwritten to mimick this behavior...
        	if( m_path.compareTo( Environment.getExternalStorageDirectory().getAbsolutePath() ) != 0 && m_path.compareTo( Environment.getRootDirectory().getAbsolutePath() ) != 0 ) {
	        	File f2 = new File( m_path );
	        	
	        	path.add( f2.getParent()  );
	        	item.add( "*** Parent Directory ***" );
        	}

    		Toast.makeText(getApplicationContext(), m_path,  Toast.LENGTH_SHORT).show();

        	getDir( m_path );

        } else {
        	if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
        		getDir( Environment.getExternalStorageDirectory().getAbsolutePath() );
        	} else {
        		
        		Toast.makeText(getApplicationContext(), Environment.getDataDirectory().toString(),  Toast.LENGTH_SHORT).show();
        		
        		//File internalDir = Environment.getDataDirectory();
        		getDir( Environment.getRootDirectory().getAbsolutePath() );
        	}
        }


        
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		
		for( int i=0; i < item.size(); i++){
			try {
				rd = new RowData(i,item.get(i),path.get(i));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);
		}
		
		CustomAdapter adapter = new CustomAdapter(this, R.layout.list, R.id.title, data);
		
		setListAdapter(adapter);
		
		getListView().setTextFilterEnabled(true);

	}

	/*
	 * For Import Beats stuff:
	 */
	
	private void getDir(String dirPath)
	{
		File f;
		File[] files;
		try {
			f = new File(dirPath);
			files = f.listFiles();
	
			if( files.length > 0 ) {
				for(int i=0; i < files.length; i++)
				{
					File file = files[i];
		
					//We want to list directories: need to change Icon of Directory to picture of a Folder and leave music files as Music symbol...
					if( file.canRead() ) {
						if( file.isDirectory()) {
							path.add(file.getPath());
							item.add(file.getName());
						} else {//Only list MP3s
							if( file.getName().endsWith(".mp3") || file.getName().endsWith(".wav") ) {
								path.add(file.getPath());
								item.add(file.getName());
							}
						}
					}
				}
			}
		} catch( Exception e ) {
			Log.d("ImportBeatsActivity", "Error/EXC 23: " + e.getMessage());
		}
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id) {
		
        return new AlertDialog.Builder(BeatImporter.this)
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
    }
    
	
	public void onListItemClick(ListView parent, View v, int position, long id) {        	
		//Toast.makeText(getApplicationContext(), "You have selected " + (position+1) + "th item",  Toast.LENGTH_SHORT).show();

		File file = new File( path.get(position) );

		//If file then launch a new instance of this class and finish the currently running instance of this class
		if( file.isDirectory()) {
			//Pass in the directory:
			Intent intent = new Intent(me, BeatImporter.class);
			intent.putExtra("path",path.get(position));
	    	startActivity(intent);
	    	me.finish();
		} else { //Otherwise, just run the MP3 like normal...
			Intent intent = new Intent(me, Downloader.class);
			intent.putExtra("Title",item.get(position));
			intent.putExtra("FileName",path.get(position));
	    	startActivity(intent);
		}
	}

	private class RowData {
		protected int mId;
		protected String mTitle;
		protected String mDetail;
		
		RowData(int id,String title,String detail) {
			mId=id;
			mTitle = title;
			mDetail = detail;
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

			//Display folder when it should be displayed:
			File i_file = new File( rowData.mDetail );
			if( i_file.isDirectory() ) { //it's a directory:
				i11.setImageResource(imgid[1]);
			} else { //it's a file:
				i11.setImageResource(imgid[0]);
			}
			

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