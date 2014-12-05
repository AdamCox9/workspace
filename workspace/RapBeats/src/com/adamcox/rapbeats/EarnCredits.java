package com.adamcox.rapbeats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 
 * This will be loaded each time a song is clicked.
 * It will check the FileSystem for file and either download it if it don't exist or play it if it does exist...
 * 
 * 
 */

public class EarnCredits extends Activity {
    private TextView tv;

    private String refUrl;

    EarnCredits me = this;
    
    private int m_id = 0;
    
    ImageView myButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_id = Integer.valueOf( Utils.ReadSettings(me, "m_uniqueid.dat" ).trim() );

       	refUrl = "http://www.8d8apps.com/credits.php?ref=" + m_id;
       	
        setContentView(R.layout.earn_credits);
        setUpText();
        setUpButtons();


    }


    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    protected void setUpText()
    {
        tv = (TextView) findViewById(R.id.earn_credits_text);
        tv.setText("Earn credits by referring other users to the app. " +
        		"You will earn 1 download credit for each of your friends that clicks on your personal link. " + 
        		"Post your link on Facebook, Twitter, forums, or other websites so people can click on it. " +
        		"You can Text Message or Email your link directly to your friends. \n\n" +
        			refUrl);
    }

    protected void setUpButtons()
    {
        myButton = (ImageView) findViewById(R.id.share);

        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(Intent.ACTION_SEND);
            	intent.setType("text/plain");
            	intent.putExtra(Intent.EXTRA_TEXT, "http://www.8d8apps.com/credits.php?ref=" + m_id );
            	startActivity(Intent.createChooser(intent, "Share with"));
            }
        });
    }
	    
}
