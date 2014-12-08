package com.adamcox.followme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {
    /** Called when the activity is first created. */

	//Either Java is or I am Stupid: can't access 'this' from inside textButton.setOnClickListener...
	LoginActivity me = this;

	private EditText usernameEditor;
	private EditText passwordEditor;
	
	private String username;
	private String password;

	private String path; //Path to where files are saved...
    private String fileName;

	HttpService httpService;

	private String dialogText = null;
	private int dialogNum = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpService = new HttpService();
        
        setContentView(R.layout.login_layout);

        File externalDir = Environment.getExternalStorageDirectory();
        path = externalDir.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + "com.adamcox.followme" +  File.separator + "login" + File.separator;
        fileName = "loggedIn.txt";

        usernameEditor = (EditText) findViewById(R.id.username);
        passwordEditor = (EditText) findViewById(R.id.password);

        Button loginButton = (Button) findViewById(R.id.login_button);

        /*
         *
         * Handle the user clicking the "Create Account!" button
         * 
         */
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            	username = usernameEditor.getText().toString();
            	password = passwordEditor.getText().toString();

            	if( username.equals(null) || username.equals("") ) {
            		dialogText = "Username can't be null.";
            		showDialog(dialogNum++);
            		return;
            	}
            	
            	if( password.equals(null) || password.equals("") ) {
            		dialogText = "Password can't be null.";
            		showDialog(dialogNum++);
            		return;
            	}
            	
            	
            	//Send data to server...
            	try {

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("Login","1"));
                    nameValuePairs.add(new BasicNameValuePair("username",username));
                    nameValuePairs.add(new BasicNameValuePair("password",password));
            		
					String response = httpService.signUpUser(nameValuePairs).replaceAll("[^A-Z_]", "");
					
					//response_text.setText( response );
					if( response.equals( "USER_LOGGEDIN" ) ) {
						//log user in
		        	    FileOutputStream f = new FileOutputStream( new File( path, fileName ) );
		        	    
		        	    //Should make some global login functionality instead of setting/unsetting this all over the place...
		        	    //Maybe the file should have a hash of username/password and this is used over the net...
		        	    f.write( username.getBytes() );
		        	    f.close();
						//end this activity
	                	me.setResult( RESULT_OK );
	                	Log.d("LoginActivity", "finishing");
						me.finish();
					} else {
						//make a dialog telling user of the problem...
						dialogText = response;
						showDialog(dialogNum++);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }

	@Override
    protected Dialog onCreateDialog(int id) {
        return new AlertDialog.Builder(LoginActivity.this)
            .setTitle("FollowMe")
            .setMessage(dialogText)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so do some stuff */
                }
            })
            .create();
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