package com.adamcox.rapbeats;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

/*
 * 
 * This file should download the XML list of songs, the credits the user has earned, make analytics, etc...
 * 
 * It will be launched when application is started, do the downloading, open up the LandingPage and then finish...
 * 
 * This should handle no internet connection gracefully...have a default song list, etc...
 * 
 * This PreLandingPage should be able to be launched anytime from within the app to refresh earned credits, etc...
 * 
 */

public class PreLandingPage extends Activity {
	private PreLandingPage me = this;
	private Services services = new Services();
	private TextView mText;
	private String m_host;
	private String uniqueId;
	private int numCredits = 1;
	private int m_id = 0; //id of device from web server database for referrers...

	//_____If we can't get a list from the server, let's use these:
	String[] title = new String[] { "AC", "Battle Club", "Beantown", "Beat Down", "Beat Town", "Big Thingz", "Black", "Blow Up the Club", "Blue", "Boston", "Box Cutter", "Bullet Thrower", "Cali", "Chicago", "Club Banga", "Club Banga II", "Cmon", "Commrottery", "Comp", "Creepuponya", "Dance Hall", "Dark Ages", "Darkness", "Dead Army", "Defcon 3", "Deisel", "Deliverance", "Dieselist", "Dont FK Wit Tis", "Dont Start", "Eastcoast", "Eastcoast Banga", "Eastcoast II", "Eastcoast III", "Eclipse", "Fat Lip", "Fight", "Find Yourself Dead", "Fire Hotness", "Force of Chopped", "Free", "Funk", "Funk II", "Future Leads", "Gangsta", "Gangsta II", "Gangsta III", "Gangsta IV", "Get Dough", "Get Off", "Get the Armor", "Goodness", "Grab the Gat", "Gun Style", "Hang Tight", "Heavy Load", "Hella Good", "Helltown", "Here We Go", "Hip Hop", "Honest", "Honor Mega", "Hybrid", "Hydra", "Innovator", "Jemima", "Joint Venture", "Kick It", "Killa", "Lawtown", "Lovin California", "Mercury", "Miami", "Mouth", "My Money", "Nolia Nightz", "Nuts", "Old School", "Old School Gangsta", "One Power", "Pack Full Level", "Pankakes", "Pride", "Profit", "Quit While U Ahead", "R and B", "Red Eye", "Reggaeton", "Rockin", "Rollcall", "Royalty", "Sand Man", "Shotgun", "Slow n Heavy", "Spit Fire", "Spit Start", "State College", "Step to My Cypher", "Step Up", "Sure Enough", "The Anthem", "The Beginning", "The Dead Zone", "Three Two Fo", "Ticonderoga", "True Child", "True to the Heart", "Try to Get the Money", "Twin Engines", "Violent Stomp", "Want It", "We Get This", "We Hot", "Westcoast", "Westcoast II", "Westcoast III", "Westcoast Old School", "Winningz", "Wont Do You No Good", "Yeah What" };
	String[] detail = new String[] { "ac", "battle_club", "beantown", "beat_down", "beat_town", "big_thingz", "black", "blow_up_the_club", "blue", "boston", "box_cutter", "bullet_thrower", "cali", "chicago", "club_banga", "club_banga_ii", "cmon", "commrottery", "comp", "creepuponya", "dance_hall", "dark_ages", "darkness", "dead_army", "defcon3", "deisel", "deliverance", "dieselist", "dont_fk_wit_tis", "dont_start", "eastcoast", "eastcoast_banga", "eastcoast_ii", "eastcoast_iii", "eclipse", "fat_lip", "fight", "find_yourself_dead", "fire_hotness", "force_of_chopped", "free", "funk", "funk_ii", "future_leads", "gangsta", "gangsta_ii", "gangsta_iii", "gangsta_iv", "get_dough", "get_off", "get_the_armor", "goodness", "grab_the_gat", "gun_style", "hang_tight", "heavy_load", "hella_good", "helltown", "here_we_go", "hip_hop", "honest", "honor_mega", "hybrid", "hydra", "innovator", "jemima", "joint_venture", "kick_it", "killa", "lawtown", "lovin_california", "mercury", "miami", "mouth", "my_money", "nolia_nightz", "nuts", "old_school", "old_school_gangsta", "one_power", "pack_full_level", "pankakes", "pride", "profit", "quit_while_u_ahead", "r_and_b", "red_eye", "reggaeton", "rockin", "rollcall", "royalty", "sand_man", "shotgun", "slow_n_heavy", "spit_fire", "spit_start", "state_college", "step_to_my_cypher", "step_up", "sure_enough", "the_anthem", "the_beginning", "the_dead_zone", "three_two_fo", "ticonderoga", "true_child", "true_to_the_heart", "try_to_get_the_money", "twin_engines", "violent_stomp", "want_it", "we_get_this", "we_hot", "westcoast", "westcoast_ii", "westcoast_iii", "westcoast_old_school", "winningz", "wont_do_you_no_good", "yeah_what" };	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
    	//_____Pro version don't need this...
        if( me.getResources().getBoolean(R.bool.app_is_pro) ) {
    		//Launch the LandingPage and Finish this activity...
	    	Intent intent = new Intent(me, LandingPage.class);
	    	startActivity(intent);
	    	me.finish();
        }
        */
        
        /*
         * Let's delete the files when testing...
         * This should be commented out when live!!!
         * 
        String path;
        File file;

        path = "/data/data/" + me.getResources().getString(R.string.app_version) + "/files/m_uniqueid.dat";
        file = new File( path );
        file.delete();
        
        path = "/data/data/" + me.getResources().getString(R.string.app_version) + "/files/m_credits.dat";
        file = new File( path );
        file.delete();
        
        path = "/data/data/" + me.getResources().getString(R.string.app_version) + "/files/m_list.dat";
        file = new File( path );
        file.delete();
         */
        

        //_____Let's set the unique device ID so we can register it below...
        uniqueId = GetUniqueDeviceId();

		//_____Let's set the host to send the signals to...
		m_host = me.getResources().getString(R.string.m_host);

		//_____The user can look at a spinner while we are doing this...
		setContentView( R.layout.download );
        mText = (TextView) findViewById(R.id.textBar1);
		mText.setText("Setting Up Application");

		//_____Let's start the internet connections...
		services.execute();

    }

    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /*
     * Another duplicate of this function that is all up in this app...
     */
    
	private String GetUniqueDeviceId()
	{
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    return deviceUuid.toString();
	}
	
    /**
     * This class handles the downloading of the song and display's the progress bar with KB downloaded so far...
     * @author Adam
     *
     */
    
    public class Services extends AsyncTask<String, Integer, Bitmap> {
    	String fileName;
    	
    	@Override
        protected void onPostExecute(Bitmap bm) {
    		//Launch the LandingPage and Finish this activity...
	    	Intent intent = new Intent(me, LandingPage.class);
	    	startActivity(intent);
	    	me.finish();
        }

    	@Override
        protected void onProgressUpdate(Integer... progress) {
    		Log.d("App Setup", "Setting up app" );
        }
    	
    	private void RegisterUniqueId()
    	{
		    //_____Write the Unique ID to server and get the Server ID to store to file...
            Log.d("PreLandingPage", "Attempting to register the device..." );
			try {
			    URL url = new URL( m_host + "credits.php?uniqueId=" + uniqueId );
	
			    //_____Read all the text returned by the server
			    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			    String str;
			    if ((str = in.readLine()) != null) {
		    		 //_____This is user's referrer id:
		    		m_id = Integer.valueOf( str );
		    		
	            	//_____Write the Unique ID to file...
	            	Utils.WriteSettings(me, str, "m_uniqueid.dat" );

		            Log.d("PreLandingPage", "Registered Server ID: " + m_id );
			    }
			    in.close();
			} catch (Exception e) {
	    		//_____I guess they can't collect ref credits...
	            Log.d("PreLandingPage", "Error 2: " + e);
			}
    	}

    	private void RegisterDeviceOnServer()
    	{
    		String r_path = "/data/data/" + me.getResources().getString(R.string.app_version) + "/files/m_uniqueid.dat";
            File file = new File( r_path );

            //_____If file don't exists let's create it and send the id over the server...
            if ( file.exists() ) { //should have ! but no ! when testing so this happens every install!!!

            	try {
            		
	            	//_____read the Server ID from file so we don't need to ping the server:
            		m_id = Integer.valueOf( Utils.ReadSettings(me, "m_uniqueid.dat" ).trim() );
		            Log.d("PreLandingPage", "Read Server ID from File: " + m_id );
		            
		            //_____Try to get it from the server again if m_id is 0 or less...not valid if so...
		            if( m_id <= 0 ) {
			            Log.d("PreLandingPage", "Problem: Id in file is 0 or less...re-registering" );
		            	RegisterUniqueId();
		            }
				} catch (Exception e) {

					//_____We can't read Server ID from file...so, maybe we can try to register the id again or something...
            		RegisterUniqueId();
		            Log.d("PreLandingPage", "Error 7: " + e);
					//Toast.makeText(getApplicationContext(), "Connectivity Issues: 1",  Toast.LENGTH_SHORT).show();
				}
            	
            } else {
	            Log.d("PreLandingPage", "File don't exist...registering" );
            	RegisterUniqueId();
            }
            Log.d("RapBeatsActivity", m_id + " is id..." );
    	}
    	
    	private void DownloadFileListFromServer()
    	{
    		try {
    		    //_____Create a URL for the desired page
    		    URL url = new URL( m_host + "xml.php" );

    		    //_____Read all the text returned by the server
    		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    		    String str;
    		    String strFile = "";

    			Log.d("PreLandingPage", "Reading song list from server" );
        		while ((str = in.readLine()) != null) {
       				strFile += str+"\n";
        		}
    		    in.close();

    			Log.d("PreLandingPage", "Writing song list to file" );
    		    Utils.WriteSettings(me, strFile, "m_list.dat");
    		} catch (Exception e) {
    			/*
    			 * Saves a default list if one can't be retrieved through download...
    			 */
    			
    			String excStr = "";
    			
    			for( int x = 0; x < title.length; x++ ) {
    				excStr = excStr + detail[x] + "ADAMCOXROCKS" + title[x] + "ADAMCOXROCKS" + "ADAMCOXROCKS" + "\n";
    			}

    		    Utils.WriteSettings(me, excStr, "m_list.dat");

                Log.d("PreLandingPage", "Error 3: " + e);

				//Toast.makeText(getApplicationContext(), "Connectivity Issues: 2",  Toast.LENGTH_SHORT).show();
    		}
            Log.d("RapBeatsActivity", "Finished downloading file list..." );
    	}
    	
    	private void DownloadCreditCountFromServer()
    	{
			try {
			    // Create a URL for the desired page
			    URL url = new URL( m_host + "credits.php?getref=" + m_id );

	            Log.d("PreLandingPage", "Reading credit count from server for: " + m_id );

			    // Read all the text returned by the server
			    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			    String str;
			    if ((str = in.readLine()) != null) {
			    	numCredits = Integer.valueOf( str ) + numCredits;
		            Log.d("PreLandingPage", "Got Number of Credits for " + m_id + " is " + numCredits );
			    }
			    in.close();
    			Log.d("PreLandingPage", "Writing number of credits to file" );
    		    Utils.WriteSettings(me, String.valueOf(numCredits)+"\n", "m_credits.dat");
			} catch (Exception e) {
				//If can't get results, they will have default number of credits...
    		    Utils.WriteSettings(me, String.valueOf(numCredits)+"\n", "m_credits.dat");
	            Log.d("PreLandingPage", "Error 5: " + e);
				//Toast.makeText(getApplicationContext(), "Connectivity Issues",  Toast.LENGTH_SHORT).show();
			}
            Log.d("RapBeatsActivity", "Finished downloading credits..." );
    	}
    	
    	@Override
    	protected Bitmap doInBackground(String... nothing_for_now) {

    		//_____Register device on server if it has not been already:
    		RegisterDeviceOnServer();
    		
    		 //_____Download the file list and save it to disk:
            DownloadFileListFromServer();
    		
            //_____Get the number of credits this user has earned:
            DownloadCreditCountFromServer();
            
            //_____Return Null:
			return null;

    	}

    }

}
