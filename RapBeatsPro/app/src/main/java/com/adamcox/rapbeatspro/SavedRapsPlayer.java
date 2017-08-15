package com.adamcox.rapbeatspro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * This will be loaded each time a song is clicked.
 * It will check the FileSystem for file and either download it if it don't exist or play it if it does exist...
 * 
 * 
 */

public class SavedRapsPlayer extends Activity {
	private MediaPlayer mPlayer = null;
	private MediaPlayer rPlayer = null;

    private TextView tv;

    private String Title;

    private String Instrumentals;
    private String Lyrics;

    ImageView playButton;
    ImageView pauseButton;
    ImageView stopButton;

    AudioManager audioManager;
    int maxVolume;
    int mvOffset = 2; //Max Volume Offset for the beat (not the recording)
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        
       Bundle extras = getIntent().getExtras();

        if(extras !=null) {
        	Title = extras.getString("Title");
        	Instrumentals = extras.getString("Instrumentals");
        	Lyrics = extras.getString("Lyrics");
        	
            Toast.makeText(SavedRapsPlayer.this, Title, Toast.LENGTH_SHORT).show();

            setContentView(R.layout.saved_raps_player);
            setUpTitle();
            setUpButtons();

        } else {
        	Log.d( "MP3Downloader", "Could not get Extras" );
        }

    }


    @Override
    protected void onResume() {
    	super.onResume();

    	if (mPlayer != null) {
            mPlayer.start();
        }
    	if (rPlayer != null) {
            rPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
    }
    
    protected void setUpTitle()
    {
        tv = (TextView) findViewById(R.id.song_title);
        tv.setText(Title);
    }

    /**
     * 
     * Set-up the listeners for the 3 buttons...
     * 
     */
    
    protected void setUpButtons()
    {
        playButton = (ImageView) findViewById(R.id.play);
        pauseButton = (ImageView) findViewById(R.id.pause);
        stopButton = (ImageView) findViewById(R.id.stop);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(SavedRapsPlayer.this, "Play", Toast.LENGTH_SHORT).show();
            	startPlaying();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
                Toast.makeText(SavedRapsPlayer.this, "Pause", Toast.LENGTH_SHORT).show();
            	pausePlayer();
            }
        });
        
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(SavedRapsPlayer.this, "Stop", Toast.LENGTH_SHORT).show();
            	stopPlaying();
            }
        });
        
    }

    /**
     * 
     * This will play the beats file...
     * 
     */
    
    private void PlayMP3() {
    	setVolumeControlStream(AudioManager.STREAM_MUSIC);

    	//Get rid of path from file name to see if it is the 3 beats stored as a resource
    	String[] insts = Instrumentals.split("/");
    	String instsStr = insts[insts.length-1];

        Toast.makeText(SavedRapsPlayer.this, instsStr, Toast.LENGTH_SHORT).show();
    
        if( instsStr.equals( "battle" ) || instsStr.equals( "irish_beat" ) || instsStr.equals( "pass_the_dutch" ) ) {
			try {
		    	if( instsStr.equals( "battle" ) ) mPlayer = MediaPlayer.create(this, R.raw.battle);
		    	if( instsStr.equals( "irish_beat" ) ) mPlayer = MediaPlayer.create(this, R.raw.irish_beat);
		    	if( instsStr.equals( "pass_the_dutch" ) ) mPlayer = MediaPlayer.create(this, R.raw.pass_the_dutch);

		        mPlayer.setLooping(false);
		        mPlayer.setVolume(maxVolume-mvOffset, maxVolume-mvOffset);
		        mPlayer.start();
	    	} catch ( Exception e ) {
	    			Log.d("SavedRapsPlayer", e.getMessage());
	   		}
	
	    } else {

	        mPlayer = new MediaPlayer();
	
	        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        	public void onCompletion(MediaPlayer mp) {
	        		//finish();
	        	}
	        });
	        
	        File file = new File( Instrumentals );
	        
	        FileInputStream fis;
			try {
				fis = new FileInputStream(file);
		        try {
		            mPlayer.setDataSource(fis.getFD());
		            mPlayer.prepare();
		            mPlayer.setLooping(false);
		            mPlayer.setVolume(maxVolume-mvOffset, maxVolume-mvOffset);
		            mPlayer.start();
		        } catch (IOException e) {
		            //mView.setText("Couldn't load music file, " + e.getMessage());
		            mPlayer = null;
		        }
			} catch (FileNotFoundException e1) {
				Log.d("MP3Downloader", "Error: " + e1 );
			}
    	}
    }

    /**
     * 
     * This will play the lyrics file...
     * 
     */
    
    private void playRecording() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        rPlayer = new MediaPlayer();

        rPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        	public void onCompletion(MediaPlayer mp) {
        		//stopPlaying();
        	}
        });
        
        File file = new File( Lyrics );
        
        FileInputStream fis;
		try {
			fis = new FileInputStream(file);
	        try {
	            rPlayer.setDataSource(fis.getFD());
	            rPlayer.prepare();
	            rPlayer.setLooping(false);
	            rPlayer.setVolume(maxVolume, maxVolume);
	            rPlayer.start();
	        } catch (IOException e) {
	            rPlayer = null;
	        }
		} catch (FileNotFoundException e1) {
			Log.d("MP3Downloader", "Error: " + e1 );
		}
    }

    /**
     * 
     * Stop playing both the beat & lyric media player
     * 
     */
    
    private void stopPlaying() {
    	if( mPlayer != null ) {
    		mPlayer.release();
    		mPlayer = null;
    	}
    	if( rPlayer != null ) {
    		rPlayer.release();
    		rPlayer = null;
    	}
    }

    /**
     * 
     * Start playing both the beat & lyric media player
     * 
     */
    
    private void startPlaying() {
    	
    	//Play the lyrics:
    	if( rPlayer == null ) {
    		playRecording();
    	} else {
            rPlayer.setVolume(maxVolume, maxVolume);
    		rPlayer.start();
    	}
    	
    	//Let's delay to start the beat because of delay while recording it...
		try {
			Thread.sleep(550);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Play the beat:
		if( mPlayer == null ) {
    		PlayMP3();
    	} else {
            mPlayer.setVolume(maxVolume-mvOffset, maxVolume-mvOffset);
    		mPlayer.start();
    	}
    }

    /**
     * 
     * Start playing both the beat & lyric media player
     * 
     */
    

    private void pausePlayer() {
    	//Play Beat:
        if (mPlayer != null) {
            mPlayer.pause();
            if (isFinishing()) {
                mPlayer.stop();
                mPlayer.release();
            }
        }
        //Play Lyrics:
        if (rPlayer != null) {
            rPlayer.pause();
            if (isFinishing()) {
                rPlayer.stop();
                rPlayer.release();
            }
        }
    }

}
