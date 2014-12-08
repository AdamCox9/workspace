package com.adamcox.followme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;


public class PictureEntryActivity extends Activity {
    /** Called when the activity is first created. */

	static final private int CAMERA_PIC_REQUEST = 1337;
	private HttpService httpService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.picture_entry);
        
        Button takePictureButton = (Button) findViewById(R.id.take_picture_button);
        
        /*
         * Handle the user clicking the "take picture button"
         */
        takePictureButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
        		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }

        });
        
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView) findViewById(R.id.take_picture_image);  
            image.setImageBitmap(thumbnail);
            
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte [] ba = bao.toByteArray();
            String ba1=Base64.encodeToString(ba, 0);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image",ba1));
            
    		try{
				String text = httpService.postPicture( nameValuePairs );
    		}catch(Exception e){
    			//do nothing...
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