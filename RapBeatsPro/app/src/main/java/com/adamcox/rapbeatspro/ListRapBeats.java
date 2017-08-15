package com.adamcox.rapbeatspro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * For each MP3, need: Title, FilePath, URL, Available
 * This will have to be stored in DB
 * First time user clicks an item, it will download & update DB
 * Then, if user clicks item, it will play.
 * 
 */

@SuppressWarnings("deprecation")
public class ListRapBeats extends ListActivity {
	private ListRapBeats me = this;
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;

	CustomAdapter adapter;
	
	private int numCredits = 0;		//Number of credits user has earned...

	private int m_id = 0;		//Unique id of this phone on the server database...
	
	ArrayList<String> downloadedSongs = new ArrayList<String>();
	
	//Image ID array:
	private Integer[] imgid = {
			R.drawable.music48px,
			R.drawable.lockmusic48px,
			R.drawable.xmusic48px
	};

	//A dirty way to pass a parameter...
	int lastItemClicked;
	
	/*
	 * This will load the appropriate songs based on if it is trial version versus paid version...
	 */

	ArrayList<String> filename = new ArrayList<String>();
	ArrayList<String> title = new ArrayList<String>(); //will replace title (string array) above...
	ArrayList<String> author = new ArrayList<String>();
	ArrayList<String> website = new ArrayList<String>();
	
	private void loadSongsFromResFolder()
	{
		filename.add( "battle" );
		title.add( "Battle" );
		author.add( "" );
		website.add( "" );
		
		filename.add( "irish_beat" );
		title.add( "Irish Beat" );
		author.add( "" );
		website.add( "" );
		
		filename.add( "pass_the_dutch" );
		title.add( "Pass the Dutch" );
		author.add( "" );
		website.add( "" );
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Unique ID for this device to see how many credits this user earned:
		try {
			m_id = Integer.valueOf( Utils.ReadSettings(me, "m_uniqueid.dat" ).trim() );
		} catch( Exception e ) {
			Log.d("RapBeatsActivity", "Problem reading ID from file...");
		}

		//_____Load the default strings:
		loadSongsFromResFolder();
		
		//_____Need to get the XML list of songs from file:
		LoadSongFile();
		
		//_____Let's build the list UI:
		setContentView(R.layout.main);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();

 		for( int i=0; i < title.size(); i++){
			try {
				rd = new RowData( i, title.get(i), filename.get(i) );
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);
		}
		
		adapter = new CustomAdapter(this, R.layout.list, R.id.title, data);
		
		setListAdapter(adapter);
		
		getListView().setTextFilterEnabled(true);

		if( ! me.getResources().getBoolean(R.bool.app_is_pro) ) {
				
			//_____This will calculate how many credits a user has remaining:
			TestCredits();
	
			//_____Tell the user how many credits they have if they have at least 0...
			if( numCredits >= 0 ) {
				Toast.makeText(getApplicationContext(), numCredits + " credits remaining.",  Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "0 credits remaining.",  Toast.LENGTH_SHORT).show();
			}
		
		}
	}

	/*
	 *
	 * Let's see if user has any credits
	 * 
	 */
	private void TestCredits()
	{
		try {
			numCredits = Integer.valueOf( Utils.ReadSettings(me, "m_credits.dat" ).trim() );
			Log.d("RapBeatsActivity", "This device earned " + numCredits + " credits." );
			for( int x = 0; x < filename.size(); x++ ) {
				if( TestSongExists( filename.get(x)) ) {
					numCredits--;
				}
			}
		} catch( Exception e ) {
			Log.d("RapBeatsActivity", e.getMessage() );
		}
	}

	/*
	 * 
	 *	We will need to read the file that was written to the filesystem on system launch...
	 * 
	 */
	
	private void LoadSongFile()
	{
        FileInputStream fIn = null;
        InputStreamReader isr = null;

        char[] inputBuffer = new char[13];

        String data = "";

        try{
	         fIn = openFileInput("m_list.dat");
	         isr = new InputStreamReader(fIn);
	         while( isr.read(inputBuffer) != -1) {
	        	 data += new String(inputBuffer);
	         }
        } catch (Exception e) {      
	         e.printStackTrace();
        } finally {
            try {
               isr.close();
               fIn.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
       }
		
		String[] lines = data.trim().split("\n");
		String[] line;

		Log.d("RapBeatsActivity", "Loading Song File");
		Log.d("RapBeatsActivity", "lines[0]: " + lines[0]);
		
		for ( int x = 0; x < lines.length; x++ ) {
			//Parse the data as it is being downloaded...
			line = lines[x].split("ADAMCOXROCKS");

		    //Fill up the ArrayLists with the parsed data from the server:
	    	try {
    			//Log.d("RapBeatsActivity", "temp size: " + line.length );
    			//Log.d("RapBeatsActivity", "line[0]: " + line[0] );

    			if( line.length > 0 ) 
    				filename.add( line[0] );
    			
    			if( line.length > 1 )
    				title.add( line[1] );
    			
    			if( line.length > 2 )
    				author.add( line[2] );
    			
    			if( line.length > 3 )
    				website.add( line[3] );

	    	} catch( Exception e) {
	            Log.d("RapBeatsActivity", "Load Song File Exception: " + e );
	    	}
    			
		}
		
	}

	/*
	 * 
	 * candidate for shared static class...
	 * 
	 */
	private boolean TestSongExists( String fileName ) {

    	//Need to check if this file is here or not...
		File externalDir, internalDir, file;
		String path;
        if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
            externalDir = Environment.getExternalStorageDirectory();
	        path = externalDir.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + me.getResources().getString(R.string.app_version) +  File.separator;
            file = new File( path + fileName );
        } else {
    		//We still need path set-up: (this should change based on if there is SD card or not...
            internalDir = Environment.getDataDirectory();
	        path = internalDir.getAbsolutePath() + File.separator + "data" + File.separator + me.getResources().getString(R.string.app_version) +  File.separator + "files" + File.separator;
            file = new File( path + fileName );
        }

        if( file.exists() ) {
			return true;
        } else {
        	return false;
        }
	}
	
	
	/*
	 * 
	 * Tell user to upgrade!
	 * This needs to be one function for all files...not one function for each file!
	 * 
	 */
	
	@Override
    protected Dialog onCreateDialog(int id) {
		
		/*
		 * 
		 * Going to have to make this pop an invite dialog if song is clicked when locked
		 * And, then another one to send an invite
		 * 
		 */

		switch( id ) {
		/*
		 * 
		 * Some comments to say what each case is for would be good...
		 * 
		 */
		case 0:
	        return new AlertDialog.Builder(ListRapBeats.this)
            .setTitle("Rap Beats Pro")
            .setMessage("Would you like to use 1 credit to download this song?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

                	/* 
                	 * User clicked OK so do some stuff
                	 * 
                	 */

        	   		//_____They used a credit for sure:
        			numCredits--;
        			
        			//_____Alert the user:
        			Toast.makeText(getApplicationContext(), numCredits + " credits remaining.",  Toast.LENGTH_SHORT).show();

        			//_____Launch the song in a new activity:
        	    	Intent intent = new Intent(me, Downloader.class);
        			intent.putExtra( "Title", title.get( lastItemClicked ) );
        			intent.putExtra( "FileName", filename.get( lastItemClicked ) );
        	    	startActivity(intent);
        	   		if( ! me.getResources().getBoolean(R.bool.app_is_pro)) {
        	   			showDialog(3);
        	   		}

        	   		//_____Let's make sure this file has the correct icon:
        	   		downloadedSongs.add( filename.get(lastItemClicked) );
        	   		
        			//_____Tell Adapter to refresh the UI:
        	   		adapter.notifyDataSetChanged();

                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked Cancel so do some stuff */
        	   		if( ! me.getResources().getBoolean(R.bool.app_is_pro)) {
        	   			showDialog(3);
        	   		}

                }
            })
            .create();
		case 1:
	        return new AlertDialog.Builder(ListRapBeats.this)
            .setTitle("Rap Beats Pro")
            .setMessage("You have 0 credits remaining. Earn credits by inviting friends. Each time a friend clicks your invite, you earn 1 download credit. Upgrade for $5.99 to unlock all songs.")
            .setPositiveButton("Invite Friends", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                	/* User clicked Invite Friends so do some stuff */
                	
                	Intent intent = new Intent(Intent.ACTION_SEND);
                	intent.setType("text/plain");
                	intent.putExtra(Intent.EXTRA_TEXT, "http://www.8d8apps.com/credits.php?ref=" + m_id );
                	startActivity(Intent.createChooser(intent, "Share with"));

                }
            })
            .setNegativeButton("Upgrade", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                	/* User clicked Upgrade so do some stuff */
                	
                	Intent intent = new Intent(Intent.ACTION_VIEW);
                	intent.setData(Uri.parse("market://details?id=com.adamcox.rapbeatspro"));
                	startActivity(intent);
                	me.finish();
                }
            })
            .create();
		default:
	        return new AlertDialog.Builder(ListRapBeats.this)
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
		
    }
    
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		//Toast.makeText(getApplicationContext(), "You have selected " + (position+1) + "th item",  Toast.LENGTH_SHORT).show();
		
		//Let's make sure user has credits...
		
		if( me.getResources().getBoolean(R.bool.app_is_pro) || TestSongExists( filename.get(position) ) || filename.get(position).equals("battle") || filename.get(position).equals("pass_the_dutch") || filename.get(position).equals("irish_beat") ) {
	    	Intent intent = new Intent(me, Downloader.class);
			intent.putExtra( "Title", title.get(position) );
			intent.putExtra( "FileName", filename.get(position) );
	    	startActivity(intent);
	   		if( ! me.getResources().getBoolean(R.bool.app_is_pro)) {
	   			showDialog(3);
	   		} else {
	   			//Switch Icon:
    	   		//_____Let's make sure this file has the correct icon:
    	   		downloadedSongs.add( filename.get(lastItemClicked) );
    	   		
    			//_____Tell Adapter to refresh the UI:
    	   		adapter.notifyDataSetChanged();
	   		}
		} else if( numCredits > 0 ) {
			lastItemClicked = position;
			showDialog( 0 );
		} else {
			//User has no credits...force invite!
			showDialog( 1 );
		}
    	
	}

	private class RowData {
		protected int mId;
		protected String mTitle;
		protected String mfileName;
		
		RowData(int id,String title,String fileName) {
			mId = id;
			mTitle = title;
			mfileName = fileName;  //filename
		}

		@Override
       public String toString() {
               return mId + " " + mTitle + " " + mfileName;
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
			ImageView i11 = null;
			RowData rowData = getItem(position);

			if(null == convertView){
				convertView = mInflater.inflate(R.layout.list, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();

			title = holder.gettitle();
			title.setText(rowData.mTitle);

			i11 = holder.getImage();
			

			/*
			 * 
			 * If song has been downloaded already, the user must have earned it and it shouldn't be locked...
			 * 
			 * When a user clicks a locked song, it will either:
			 * 		1: download the song if the user has the credit to use it and 
			 * 		2: force the user to earn credits
			 * 
			 */
			//Log.d("RapBeatsActivity", rowData.mfileName + " exists???");
			
			//_____Use the X image for songs that haven't been downloaded yet on the Pro version and lock image on Free version...
			
			if( TestSongExists( rowData.mfileName) || downloadedSongs.contains( rowData.mfileName ) ) { //_____downloadedSong is for a song recently unlocked...
				i11.setImageResource(imgid[0]);
			} else {
				if( rowData.mfileName.equals("battle") || rowData.mfileName.equals("pass_the_dutch") || rowData.mfileName.equals("irish_beat")) {
					i11.setImageResource(imgid[0]);
				} else {
			   		if( me.getResources().getBoolean(R.bool.app_is_pro)) {
			   			i11.setImageResource(imgid[2]);
			   		} else {
			   			i11.setImageResource(imgid[1]);
			   		}
				}
			}

			return convertView;
		}

		private class ViewHolder {

			private View mRow;
			private TextView title = null;
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
			
			public ImageView getImage() {
				if(null == i11){
					i11 = (ImageView) mRow.findViewById(R.id.itemlogo);
				}
                return i11;
			}
			
			
		}
	}
	
}