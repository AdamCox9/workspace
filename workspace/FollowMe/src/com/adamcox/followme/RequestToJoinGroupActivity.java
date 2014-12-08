package com.adamcox.followme;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RequestToJoinGroupActivity extends Activity {
    /** Called when the activity is first created. */

	//Either Java is or I am Stupid: can't access 'this' from inside textButton.setOnClickListener...
	RequestToJoinGroupActivity me = this;

	private EditText passwordEditor;
	
	private String username;
	private String password;
	private int groupid;

	HttpService httpService;

	private String dialogText = null;
	private int dialogNum = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpService = new HttpService();
        
        setContentView(R.layout.request_to_join_group_layout);
        
        //@Todo Should ask user if they have a password...
        	//They will then automatically be allowed to join group...

        
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
        	username = extras.getString("username");
        	groupid = extras.getInt("groupid");
        }
        

        passwordEditor = (EditText) findViewById(R.id.edit_request_password);

        Button request_to_joinButton = (Button) findViewById(R.id.request_to_join_button);

        /*
         *
         * Handle the user clicking the "Create Account!" button
         * 
         */
        request_to_joinButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            	password = passwordEditor.getText().toString();

            	//Send data to server...
            	try {

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("AddUserToGroup","1"));
                    nameValuePairs.add(new BasicNameValuePair("username",username));
                    nameValuePairs.add(new BasicNameValuePair("password",password));
                    nameValuePairs.add(new BasicNameValuePair("groupid",String.valueOf(groupid)));
                    nameValuePairs.add(new BasicNameValuePair("confirmed","0"));

                    //100% sure there is a better way...
					String response = httpService.requestToJoinGroup(nameValuePairs).replaceAll("[^A-Z_]", "");

					//Send joing group request to the server...
					
					//Need to handle this response in the calling activity...
					if( response.equals( "REQUEST_ACCEPTED" ) ) {
	                	me.setResult( RESULT_OK );
	                	Log.d("LoginActivity", "finishing");
						me.finish();
						
					//if they sent password, they would be automatically accepted...
						
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
        return new AlertDialog.Builder(RequestToJoinGroupActivity.this)
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