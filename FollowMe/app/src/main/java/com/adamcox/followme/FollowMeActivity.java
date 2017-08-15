package com.adamcox.followme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class FollowMeActivity extends Activity {
    /** Called when the activity is first created. */

	//Either Java is or I am Stupid: can't access 'this' from inside textButton.setOnClickListener...
	FollowMeActivity me = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        Button textButton = (Button) findViewById(R.id.text_button);
        Button pictureButton = (Button) findViewById(R.id.picture_button);
        Button audioButton = (Button) findViewById(R.id.audio_button);
        Button videoButton = (Button) findViewById(R.id.video_button);

        /*
         * Handle the user clicking the "add text entry button"
         * 
         */
        textButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	Intent intent = new Intent(me, TextEntryActivity.class);
            	startActivity(intent);
            }

        });
        
        /*
         * Handle the user clicking the "add picture entry button"
         * 
         */
        pictureButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	Intent intent = new Intent(me, PictureEntryActivity.class);
            	startActivity(intent);
            }

        });
        
        /*
         * Handle the user clicking the "add audio entry button"
         * 
         */
        audioButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	Intent intent = new Intent(me, AudioEntryActivity.class);
            	startActivity(intent);
            }

        });
        
        /*
         * Handle the user clicking the "add video entry button"
         * 
         */
        videoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	Intent intent = new Intent(me, VideoEntryActivity.class);
            	startActivity(intent);
            }

        });
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	super.onActivityResult(requestCode, resultCode, data);
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