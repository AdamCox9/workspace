package com.adamcox.followme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class RegisterActivity extends Activity {
    /** Called when the activity is first created. */

	//Either Java is or I am Stupid: can't access 'this' from inside textButton.setOnClickListener...
	RegisterActivity me = this;

	/*
	 * 
	 * Need to attempt to read a file that has the SignedUp flag set.
	 * If it is already set, the user has already signed up.
	 * If it is not, attempt to sign up the user and then write that flag file.
	 * 
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_layout);
        
        Button registerButton = (Button) findViewById(R.id.register_button);

        /*
         *
         * Handle the user clicking the "add text entry button"
         * 
         */
        registerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	Intent intent = new Intent(me, TextEntryActivity.class);
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