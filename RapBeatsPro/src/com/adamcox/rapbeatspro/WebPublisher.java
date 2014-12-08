package com.adamcox.rapbeatspro;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

/*
 *
 * Make URL post the actual file to the server...
 * Then, redirect the user to a webpage where they can access that file...and share it...
 * Server only has to output an id for the location of the file on the web...
 * 
 */

public class WebPublisher extends Activity {
	private Services services = new Services();

    private TextView tv;

    protected WebPublisher me = this;
    
    String rapUrl = "";
    
	private TextView mText;
    private String Title;

    private String FileName;

    ImageView playButton;

    //New Post Stuff:
    HttpURLConnection conn;

	private int m_id = 0;		//Unique id of this phone on the server database...

	private int lyric_offset;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		//Unique ID for this device to see how many credits this user earned:
		try {
			m_id = GetMyId();
		} catch( Exception e ) {
			Log.d("RapBeatsActivity", "Problem reading ID from file...");
		}

        Bundle extras = getIntent().getExtras();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        if(extras !=null) {
        	Title = extras.getString("Title");
        	FileName = extras.getString("FileName");
        	lyric_offset = extras.getInt("lyric_offset");

        	Log.d("GenerateWAV", "Got " + Title + " and " + FileName );

    		//_____The user can look at a spinner while we are doing this...
    		setContentView( R.layout.download );
            mText = (TextView) findViewById(R.id.textBar1);
    		mText.setText("Processing Lyrics...");

        	
            File t_file = new File( FileName );
            
        	if( t_file.exists() ) {
        		services.execute(FileName);
        	} else {
            	Log.d( "GenerateWav", "File appears not to exist..." );
	        }
        	
        } else {
        	Log.d( "GenerateWav", "Could not get Extras" );
        }

    }

	/*
	 * Read server ID from file...
	 */
	public int GetMyId()
	{
		return Integer.valueOf( Utils.ReadSettings(me, "m_uniqueid.dat" ).trim() );
	}
	
	
	
    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }
    
    protected void setUpTitle()
    {
        tv = (TextView) findViewById(R.id.song_title);
        tv.setText(Title);
    }

    protected void setUpButtons()
    {
        playButton = (ImageView) findViewById(R.id.play);
        
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(WebPublisher.this, "Play", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * This class handles the downloading of the song and display's the progress bar with KB downloaded so far...
     * @author Adam
     *
     */
    public class Services extends AsyncTask<String, Integer, Bitmap> {
    	String fileName;
    	private String id;

    	@Override
        protected void onPostExecute(Bitmap bm) {
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setData(Uri.parse("http://www.8d8apps.com/rapbeatsmp3.php?id="+id));
        	startActivity(intent);
        	me.finish();
        }

    	@Override
        protected void onProgressUpdate(Integer... progress) {
    		//Log.d("Download Progress", progress[0] + " KB downloaded" );
    		mText.setText(progress[0] + "KB of " + progress[1] + "KB downloaded");
        }
    	
    	@Override
    	protected Bitmap doInBackground(String... _fileName) {
        
    		fileName = _fileName[0];
    		
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            try {
                // ------------------ CLIENT REQUEST ------------------- //

                Log.d("GenerateWAV", "Inside second Method with " + fileName);

                FileInputStream fileInputStream = new FileInputStream( new File(fileName) );

                //_____Open a URL connection to a web page:
                Log.d("GenerateWAV", "Opening conn to: http://www.8d8apps.com/generate.php?DeviceId="+m_id+"&offset="+lyric_offset);
                URL url = new URL("http://www.8d8apps.com/generate.php?DeviceId="+m_id+"&offset="+lyric_offset);

                //_____Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();

                //_____Allow Inputs
                conn.setDoInput(true);

                //_____Allow Outputs
                conn.setDoOutput(true);

                //_____Don't use a cached copy.
                conn.setUseCaches(false);

                //_____Use a post method.
                conn.setRequestMethod("POST");

                //_____Keep connection alive while opening file, etc...
                conn.setRequestProperty("Connection", "Keep-Alive");

                //_____Let's add some headers:
                
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: post-data; name=uploadedfile;filename=" + FileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                Log.d("GenerateWAV", "Headers are written");

                //_____Create a buffer of maximum size

                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1000;
                // int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bytesAvailable];

                //_____Read file and write it into form...

                int bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bytesAvailable);
                    bytesAvailable = fileInputStream.available();
                    bytesAvailable = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
                }

                //_____Send multi-part form data necessary after file data...

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                //_____Close streams:
                Log.d("GenerateWAV", "File is written");
                fileInputStream.close();
                dos.flush();
                dos.close();

                //_____Let's read the output from the server now:

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    Log.d("GenerateWAV", "Message: " + line);
                    id = line.toString();
                }
                rd.close();
            } catch (Exception ex) {
                Log.e("GenerateWAV", "error: " + ex.getMessage(), ex);
            }




        	/*
        	 * 
        	 * Old Stuff - Not Used But Here For Reference...
        	 * 
        	 * try {

        		URL u = new URL( "http://www.8d8apps.com/test.php");
        		HttpURLConnection c = (HttpURLConnection) u.openConnection();
        		c.setRequestMethod("POST");
        		c.addRequestProperty("testName", "testValue");
        	    c.setDoOutput(false);
        	    c.connect();
        	    
        		//File f1 = new File(path + "r_" + FileName);
        		//InputStream fin = new FileInputStream(f1);
        	    
                
                //Define InputStreams to read from the URLConnection.
                InputStream in = c.getInputStream();

                //Let's make a LogCat entry:
                Log.d("MP3Downloader", "downloading: " + path + fileName);

                //Read bytes to the Buffer until there is nothing more to read(-1).
                byte[] buffer = new byte[1024];
                

                int len1 = 0;

            	while ( ( len1 = in.read(buffer) ) > 0) {

                    Log.d("MP3Downloader", "downloading: " + len1 + " " + String.valueOf(buffer) );

                }

        	} catch (IOException e) {
                    Log.d("MP3Downloader", "Error 1: " + e);
            }*/

    		// TODO Auto-generated method stub
    		return null;
    	}	

    }

}
