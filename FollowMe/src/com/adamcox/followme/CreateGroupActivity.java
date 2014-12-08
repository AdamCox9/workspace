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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateGroupActivity extends Activity {
    /** Called when the activity is first created. */

	CreateGroupActivity me = this;

	private String username;

	private EditText groupnameEditor;
	private EditText passwordEditor;
	private EditText password_confirmEditor;

	HttpService httpService;

	private String dialogText = null;
	private int dialogNum = 1;

	private String groupname;
	private String password;
	private String password_confirm;
	private String response;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        httpService = new HttpService();

        setContentView(R.layout.create_group_layout);
        
        groupnameEditor = (EditText) findViewById(R.id.groupname);
        passwordEditor = (EditText) findViewById(R.id.password);
        password_confirmEditor = (EditText) findViewById(R.id.password_confirm);

        Button create_groupButton = (Button) findViewById(R.id.create_group_button);

        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
        	username = extras.getString("username");
        }
        
        /*
         *
         * Handle the user clicking the "add text entry button"
         * 
         */
        create_groupButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	//Need to send this group to the server...

               	groupname = groupnameEditor.getText().toString();
            	password = passwordEditor.getText().toString();
            	password_confirm = password_confirmEditor.getText().toString();

            	if( groupname.equals(null) || groupname.equals("") ) {
            		dialogText = "Username can't be null.";
            		showDialog(dialogNum++);
            		return;
            	}
            	
            	if( password.equals(null) || password.equals("") ) {
            		dialogText = "Password can't be null.";
            		showDialog(dialogNum++);
            		return;
            	}
            	
            	if( ! password.equals(password_confirm) ) {
            		dialogText = "Password & Confirm Password must be the same.";
            		showDialog(dialogNum++);
            		return;
            	}
            	
            	//Send data to server...
            	try {

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("AddGroup","1"));
                    nameValuePairs.add(new BasicNameValuePair("username",username));
                    nameValuePairs.add(new BasicNameValuePair("groupname",groupname));
                    nameValuePairs.add(new BasicNameValuePair("password",password));
            		
					response = httpService.createGroup(nameValuePairs).replaceAll("[^A-Z_]", "");
					
					//response_text.setText( response );
					if( response.equals( "REQUEST_ACCEPTED" ) ) { //hack because user is auto accepted into group...
						//Maybe we should save this locally...it'll sync up later anyways
						
						me.setResult( RESULT_OK );
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
        return new AlertDialog.Builder(CreateGroupActivity.this)
            .setTitle("FollowMe")
            .setMessage(dialogText)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so don't do some stuff */
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