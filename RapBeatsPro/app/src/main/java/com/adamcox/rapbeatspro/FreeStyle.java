package com.adamcox.rapbeatspro;

//My libraries:
import com.adamcox.rapbeats.database.DatabaseAdapter;

//Java libraries:
import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.Date;

//Android libraries:
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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

public class FreeStyle extends Activity {
	private FreeStyle me = this;
	
	private MediaPlayer rPlayer = null;
    private MediaRecorder mRecorder = null;
	private DatabaseAdapter databaseAdapter;

    private TextView tv;

    private String Title;

	private String path; //Path to where files are saved...
    private String FileName;

    ImageView recordButton;
    ImageView play_recordingButton;
    ImageView stop_recordingButton;

	private Date date;
	private Timestamp Ts;
	private long timestamp;

	private int lyric_offset = 550;
	
    AudioManager audioManager;
    int maxVolume;
    int mvOffset = 2; //Max Volume Offset for the beat (not the recording)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        //Let's see what type of storage the user has:
        if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
            File externalDir = Environment.getExternalStorageDirectory();
	        path = externalDir.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + me.getResources().getString(R.string.app_version) +  File.separator;
            File file = new File( path );
            file.mkdirs();
        } else {
    		//We still need path set-up: (this should change based on if there is SD card or not...
            File internalDir = Environment.getDataDirectory();
	        path = internalDir.getAbsolutePath() + File.separator + "data" + File.separator + me.getResources().getString(R.string.app_version) +  File.separator + "files" + File.separator;
            File file = new File( path );
            file.mkdirs();
        }

   		setUpUI();

    }

    @Override
    protected void onResume() {
    	super.onResume();
    	
    	databaseAdapter = new DatabaseAdapter(this);
    	databaseAdapter.open();

    	//Lyrics Player:
    	if (rPlayer != null) {
            rPlayer.setVolume(maxVolume-mvOffset, maxVolume-mvOffset);
            rPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    	stopPlaying();
    	stopRecording();
        databaseAdapter.close();
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	stopPlaying();
    	stopRecording();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	stopPlaying();
    	stopRecording();
    }

    /**************
     * 
     * 
     * 
     * 
     * This section contains some basic functions to set-up the UI:
     * 
     * 
     * 
     * 
     **************/

    //Set content view and call the other set-up methods below:
    private void setUpUI()
    {
		setContentView(R.layout.freestyle);
        setUpTitle();
        setUpButtons();
    }

    //Set up the title:
    protected void setUpTitle()
    {
        tv = (TextView) findViewById(R.id.song_title);
        tv.setText(Title);
    }

    //Set up the buttons:
    protected void setUpButtons()
    {
        recordButton = (ImageView) findViewById(R.id.record);
        play_recordingButton = (ImageView) findViewById(R.id.play_recording);
        stop_recordingButton = (ImageView) findViewById(R.id.stop_recording);

        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(FreeStyle.this, "Recording", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record_active);
            	stopRecording(); 
            	stopPlaying();
            	startRecording();
                startPlaying();
            }
        });

        play_recordingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(FreeStyle.this, "Play Recording", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	stopPlaying();
            	playLyricsAndBeats();
            }
        });

        stop_recordingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(FreeStyle.this, "Stop Recording", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	stopPlaying();
            }
        });

    }

    /**
     * Insert rap into the database for later:
     * 
     */

    @SuppressWarnings("unused")
	private boolean insertRap() {
    	date= new Date();
    	Ts = new Timestamp(date.getTime());
    	timestamp = Ts.getTime();
    	if( databaseAdapter.createEntry(Title, path+FileName, path + "r_" + timestamp + FileName) == -1 ) {
            //Toast.makeText(MP3Downloader.this, "Database Error", Toast.LENGTH_SHORT).show();
	    	return false;
	    } else {
	    	return true;
	    }
    }
    
    /**
     * Release both the media players:
     * 
     */
    
    private void stopPlaying() {
    	if( rPlayer != null ) {
    		rPlayer.release();
    		rPlayer = null;
    	}
    }

    /**
     * Start playing just the beats
     */
    
    private void startPlaying() {
    	if( rPlayer == null ) {
    		playLyrics( FileName, maxVolume );
    	} else {
            rPlayer.setVolume(maxVolume-mvOffset, maxVolume-mvOffset);
    		rPlayer.start();
    	}
    }

    /**
     * Pause both the beats & lyrics player:
     */
    
    @SuppressWarnings("unused")
	private void pausePlayer() {
        if (rPlayer != null) {
            rPlayer.pause();
            if (isFinishing()) {
                rPlayer.stop();
                rPlayer.release();
            }
        }
    }

    /**
     * Release the recorder...
     */
    
    private void stopRecording() {
    	if( mRecorder != null ) {
    		mRecorder.release();
    		mRecorder = null;
    	}
    }

    /**
     * Make a new MediaRecorder object and start recording...
     */
    
    private void startRecording() {

	    try {
	        int version = android.os.Build.VERSION.SDK_INT;

	    	mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

	        /*
	         * Todo upgrade to API 16 and make some more virtual devices for testing...
	         */
	        if( version >= 16 ) {
	        	//use AAC_ADTS at 96 kHz
	        } else if( version >=10 ) {
	        	//use AMR_NB at 16 kHz
	        } else if( version >= 8 ){
	        	//use RAW_AMR at 8 kHz
	        } else { //version == 7 (lowest supported version)
	        	
	        }
	        
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

	        MediaRecorder.getAudioSourceMax();
	        
	        File m_file = new File( path + "r_" + FileName );
	        if( m_file.exists() ) {
	        	m_file.delete();
	        }
	        mRecorder.setOutputFile( path + "r_" + FileName );
	
	        
	        if( version >= 8 ) {
		        //mRecorder.setAudioEncodingBitRate(16);
		        //mRecorder.setAudioSamplingRate(44100);
		        //mRecorder.setAudioChannels(1);
	        }
	        
	        mRecorder.prepare();
	        mRecorder.start();
	    } catch (Exception e) {
	        Log.e("MP3Downloader", "MediaRecorder Failed: " + e.getMessage() );
			e.printStackTrace();
	    }
    }


    /**
     * 
     * Play the lyrics on the rPlayer: 
     * 
     */
    
    
    private void playLyrics( String FileName, int maxVolume ) {
    	try {
	        rPlayer = new MediaPlayer();
			
	        rPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        	public void onCompletion(MediaPlayer mp) {
	            	recordButton.setImageResource(R.drawable.record);
	            	stopRecording();
	        	}
	        });
	        
	        File file = new File( path + FileName );
	        FileInputStream fis;
	        fis = new FileInputStream(file);
	        rPlayer.setDataSource(fis.getFD());
	        rPlayer.prepare();
	        rPlayer.setLooping(false);
	        rPlayer.setVolume(maxVolume-mvOffset, maxVolume-mvOffset);
	        rPlayer.start();
		} catch (Exception e) {
		    Log.e("MP3Downloader", "Play MP3 Failed" + e.getMessage() );
			e.printStackTrace();
		}
    }
    
    private void playLyricsAndBeats() {
		playLyrics( "r_" + FileName, maxVolume );
    }
    


    /**
     * 
     * 
     * Handle clicks to the pop up dialogs...
     * 
     * The way I'm doing it is deprecated, so it needs to be updated...
     * 
     * 
     */
    
    
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
    	switch( id ) {
    	default:
            return new AlertDialog.Builder(FreeStyle.this)
	        	.setCancelable(false)
	            .setTitle("Publish Rap?")
	            .setMessage("This will generate an MP3 and publish it on the web. You will be able to save the MP3 to your phone or computer or share it on Facebook.")
	            .setPositiveButton("Publish Rap", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	recordButton.setImageResource(R.drawable.record);
	                	stopRecording();
	                	stopPlaying();

	        	    	Intent intent = new Intent(me, WebPublisher.class);

	        	    	Log.d("MP3Downloader", "Sending " + Title + " and path " + path + "r_" + FileName + " to generate wav.");
	        	    	
	        	    	intent.putExtra( "Title", Title );
	        			intent.putExtra( "FileName", path + "r_" + FileName );
	        			intent.putExtra( "lyric_offset", lyric_offset );

	        	    	startActivity(intent);
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked Cancel so do nothing... */
	                }
	            })
	            .create();
    	}
    }

}
