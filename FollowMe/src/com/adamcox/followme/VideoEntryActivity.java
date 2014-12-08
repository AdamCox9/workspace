package com.adamcox.followme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;


public class VideoEntryActivity extends Activity {
    /** Called when the activity is first created. */

	static final private int CAMCORDER_VIDEO_REQUEST = 1338;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.video_entry);
        
        Button takeVideoButton = (Button) findViewById(R.id.take_video_button);
        
        /*
         * Handle the user clicking the "take picture button"
         */
        takeVideoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
        		Intent camcorderIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        		startActivityForResult(camcorderIntent, CAMCORDER_VIDEO_REQUEST);
            }

        });
        
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMCORDER_VIDEO_REQUEST) {

			try {
	            byte[] ba = IOUtil.readFile( data.getDataString() );

	            String ba1=Base64.encodeToString(ba, 0);

	            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	            nameValuePairs.add(new BasicNameValuePair("video",ba1));
	    		try{
		    		HttpClient httpclient = new DefaultHttpClient();
		    		HttpPost httppost = new
		    		HttpPost("http://www.bindingdesigns.com/androidapps/Script.php");
		    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    		HttpResponse response = httpclient.execute(httppost);
		    		HttpEntity entity = response.getEntity();
		    		@SuppressWarnings("unused")
					InputStream is = entity.getContent();
		    		//@Todo take action if photo was not successfully uploaded...
	    		}catch(Exception e){
	    			//do nothing...
	    		}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }  
        	
    }  

    public static class IOUtil
    {   
        public static byte[] readFile (String file) throws IOException {
            return readFile(new File(file));
        }

        public static byte[] readFile (File file) throws IOException {
            // Open file
            RandomAccessFile f = new RandomAccessFile(file, "r");

            try {
                // Get and check length
                long longlength = f.length();
                int length = (int) longlength;
                if (length != longlength) throw new IOException("File size >= 2 GB");

                // Read file and return data
                byte[] data = new byte[length];
                f.readFully(data);
                return data;
            }
            finally {
                f.close();
            }
        }
    }

    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);

    	return true;
    }
    
	
}