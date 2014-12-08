package com.adamcox.rapbeats;

//Java libraries:
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

/*
 * 
 * This will be loaded each time a song is clicked.
 * It will check the FileSystem for file and either download it if it don't exist or play it if it does exist...
 * 
 * 
 */

public class Downloader extends Activity {
	private Downloader me = this;
	
	private Services services = new Services();

    private String Title;
	private String path; //Path to where files are saved...
    private String FileName;

	private TextView mText;

    private String rapUrl = "http://c15007487.r87.cf2.rackcdn.com/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	Title = extras.getString("Title");
        	FileName = extras.getString("FileName");

        	//Let's see what type of storage the user has:
            if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
	            File externalDir = Environment.getExternalStorageDirectory();
		        path = externalDir.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + me.getResources().getString(R.string.app_version) +  File.separator;
	            File file = new File( path );
	            file.mkdirs();
            } else {
        		//We still need path set-up: (this should change based on if there is SD card or not...
	            File internalDir = Environment.getDataDirectory();
		        path = internalDir.getAbsolutePath() + File.separator + "data" + File.separator + me.getResources().getString(R.string.app_version) +  File.separator + "files" + File.separator;
	            File file = new File( path );
	            file.mkdirs();
            }

            //Access sample files from resources:
        	if( FileName.equals( "battle" ) || FileName.equals( "irish_beat" ) || FileName.equals( "pass_the_dutch" ) ) {
        		forwardToMediaCenter();
        	} else {
        		//This is somewhere else on the device:
	            File t_file = new File( FileName );
	        	if( t_file.exists() ) {
	        		path = t_file.getParent() +  File.separator;
	        		FileName = t_file.getName();
	
	        		forwardToMediaCenter();
	        	} else {
	        		//The file needs to be downloaded from the server:
		            File d_file = new File( path );
		            d_file.mkdirs();
		            d_file = new File( path + FileName );
	
		        	if ( ! d_file.exists() ) {
		        		setContentView( R.layout.download );
		                mText = (TextView) findViewById(R.id.textBar1);
		        		services.execute(FileName);
		        	} else {
		        		forwardToMediaCenter();
		        	}
	        	}
        	}
        	
        } else {
        	Log.d( "MP3Downloader", "Could not get Extras" );
        }

    }

    private void forwardToMediaCenter() {
    	
    	Intent intent = new Intent(me, MediaCenter.class);

    	Log.d("MediaCenter", "Sending " + Title + " and path " + path + "r_" + FileName + " to generate wav.");
    	
    	intent.putExtra( "Title", Title );
		intent.putExtra( "path", path );
		intent.putExtra( "FileName", FileName );

    	startActivity(intent);

    	this.finish();
    }
  
    /**
     * This class handles the downloading of the song and display's the progress bar with KB downloaded so far...
     * @author Adam
     *
     */
    private class Services extends AsyncTask<String, Integer, Bitmap> {
    	String fileName;

    	@Override
        protected void onPostExecute(Bitmap bm) {
    		forwardToMediaCenter();
        }

    	@Override
        protected void onProgressUpdate(Integer... progress) {
    		//Log.d("Download Progress", progress[0] + " KB downloaded" );
    		mText.setText(progress[0] + "KB of " + progress[1] + "KB downloaded");
        }
    	
    	@Override
    	protected Bitmap doInBackground(String... _fileName) {
        
    		fileName = _fileName[0];

        	try {

        		URL u = new URL( rapUrl + fileName); //you can write here any link
        		HttpURLConnection c = (HttpURLConnection) u.openConnection();
        		c.setRequestMethod("GET");
        	    c.setDoOutput(false);
        	    c.connect();
        	    
        	    int ContentLength = c.getContentLength();

        	    FileOutputStream f = new FileOutputStream(new File( path, fileName ) );

                /*
                 * Define InputStreams to read from the URLConnection.
                 */
                InputStream in = c.getInputStream();

                /*
                 * Let's make a LogCat entry:
                 */
                Log.d("MP3Downloader", "downloading: " + path + fileName);

                /*
                 * Read bytes to the Buffer until there is nothing more to read(-1).
                 */

                byte[] buffer = new byte[1024];
                
                Integer [] est = new Integer[2];
            	est[1] = ContentLength/900; //No Fucking Clue!!!
                int x = 0;
                int len1 = 0;

            	while ( ( len1 = in.read(buffer) ) > 0) {
                	f.write(buffer,0,len1);
                	est[0] = (int) x++;
                    //Log.d("MP3Downloader", "downloading: " + x);
                    publishProgress(est);
                }

                f.close();

        	} catch (IOException e) {
                    Log.d("MP3Downloader", "Error 1: " + e);
            }

    		// TODO Auto-generated method stub
    		return null;
    	}	

    }

}
