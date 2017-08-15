package com.adamcox.rapbeats;

//Java libraries:
import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.Date;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.adamcox.rapbeats.database.DatabaseAdapter;

public class MediaCenter extends Activity {

	//This is me...
	private MediaCenter me = this;

	//Some variables to point to objects...
	private MediaPlayer mPlayer = null;
	private MediaPlayer rPlayer = null;
	private MediaRecorder mRecorder = null;
	private DatabaseAdapter databaseAdapter;

	//Some strings...
    private String Title;
	private String path; //Path to where files are saved...
    private String FileName;

    //A text view...
	private TextView tv;

	//Some image views...
	ImageView playButton;
	ImageView pauseButton;
	ImageView stopButton;
	ImageView recordButton;
	ImageView play_recordingButton;
	ImageView play_lyrics_recordingButton;
	ImageView stop_recordingButton;
	ImageView saveButton;
	ImageView wwwButton;
	ImageView redownloadButton;

	//Some stuff to hold date stuff...
	private Date date;
	private Timestamp Ts;
	private long timestamp;

	//The seek bar stuff...
	private SeekBar offset_bar;
	private SeekBar beatvol_bar;
	private SeekBar lyricvol_bar;
	
	
	private int lyric_offset = 550;

	//Some volume stuff...
    AudioManager audioManager;
    int maxVolume;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        
        Log.d("MediaCenter", "Got Max Volume: " + maxVolume );
        
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	Title = extras.getString("Title");
        	path = extras.getString("path");
        	FileName = extras.getString("FileName");

       		setUpUI();
        	
        } else {
        	Log.d( "MediaCenter", "Could not get Extras" );
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
           //return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
           //return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	
    	databaseAdapter = new DatabaseAdapter(this);
    	databaseAdapter.open();

    	//Beat Player:
    	if (mPlayer != null) {
            mPlayer.start();
        }
    	
    	//Lyrics Player:
    	if (rPlayer != null) {
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
     * This section contains some basic functions to set-up the UI:
     * 
     **************/

    //Set content view and call the other set-up methods below:
    private void setUpUI()
    {
		setContentView(R.layout.media);
		setUpSeekBars();
        setUpTitle();
        setUpButtons();
    }

    //Set up the title:
    protected void setUpTitle()
    {
        tv = (TextView) findViewById(R.id.song_title);
        tv.setText(Title);
    }

    //Set up the seek bars:
    protected void setUpSeekBars()
    {
        //Offset seek bar stuff:
        offset_bar = (SeekBar)findViewById(R.id.offset_bar); // make seek bar object
        offset_bar.setOnSeekBarChangeListener( new offsetBarListener() );

        //Lyric seek bar stuff:
        lyricvol_bar = (SeekBar)findViewById(R.id.lyricvol_bar); // make seek bar object
        lyricvol_bar.setOnSeekBarChangeListener( new lyricvolBarListener() );
        lyricvol_bar.setMax(maxVolume*10);
        lyricvol_bar.setProgress(maxVolume*5); //volume at 1/2

        //Beat seek bar stuff:
        beatvol_bar = (SeekBar)findViewById(R.id.beatvol_bar); // make seek bar object
        beatvol_bar.setOnSeekBarChangeListener( new beatvolBarListener() );
        beatvol_bar.setMax(maxVolume*10);
        beatvol_bar.setProgress(maxVolume*5); //volume at 1/2       

    }

    //Set up the buttons:
    protected void setUpButtons()
    {
        playButton = (ImageView) findViewById(R.id.play);
        pauseButton = (ImageView) findViewById(R.id.pause);
        stopButton = (ImageView) findViewById(R.id.stop);
        recordButton = (ImageView) findViewById(R.id.record);
        play_recordingButton = (ImageView) findViewById(R.id.play_recording);
        play_lyrics_recordingButton = (ImageView) findViewById(R.id.play_lyrics_recording);
        stop_recordingButton = (ImageView) findViewById(R.id.stop_recording);
        saveButton = (ImageView) findViewById(R.id.save);
        wwwButton = (ImageView) findViewById(R.id.share);
        redownloadButton = (ImageView) findViewById(R.id.redownload);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MediaCenter.this, "Play", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	startPlaying();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
                Toast.makeText(MediaCenter.this, "Pause", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	pausePlayer();
            }
        });
        
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MediaCenter.this, "Stop", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	stopPlaying();
            }
        });
        
        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MediaCenter.this, "Recording", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record_active);
            	stopRecording(); 
            	stopPlaying();
            	startRecording();
                startPlaying();
            }
        });

        play_recordingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MediaCenter.this, "Play Recording", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	stopPlaying();
            	playLyricsAndBeats();
            }
        });

        play_lyrics_recordingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MediaCenter.this, "Play Lyrics", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	stopPlaying();
        		playLyrics( "r_" + FileName, maxVolume );
            }
        });

        stop_recordingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MediaCenter.this, "Stop Recording", Toast.LENGTH_SHORT).show();
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	stopPlaying();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	recordButton.setImageResource(R.drawable.record);
            	stopRecording();
            	stopPlaying();
                //Make sure that there is a lyrics file for the instrumentals file...
            	if( Utils.testRecordingFileExists( path + "r_" + FileName ) ) {
            		if( insertRap() && Utils.CopyFile(path + "r_" + FileName, path + "r_" + timestamp + FileName) ) {
                        Toast.makeText(MediaCenter.this, "Rap Saved", Toast.LENGTH_SHORT).show();
            		}
            	} else {
                    Toast.makeText(MediaCenter.this, "Please Create Lyrics", Toast.LENGTH_SHORT).show();
            	}
                //@Todo Prompt user for title
                //Insert entry into database...
            }
        });

        wwwButton.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("deprecation")
				public void onClick(View view) {
		        	if( Utils.testRecordingFileExists( path + "r_" + FileName ) ) {
		        		if( Utils.testFileSize( path + "r_" + FileName ) ) {
		        			showDialog(0);
		        		} else {
			                Toast.makeText(MediaCenter.this, "Lyrics Too Short", Toast.LENGTH_SHORT).show();
		        		}
		        	} else {
		                Toast.makeText(MediaCenter.this, "Please Create Lyrics", Toast.LENGTH_SHORT).show();
		        	}
	            }
        });

        redownloadButton.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
            public void onClick(View view) {
        		showDialog(1);
            }
        });
    }

    /**
     * Insert rap into the database for later:
     * 
     */
    
    private boolean insertRap() {
    	date= new Date();
    	Ts = new Timestamp(date.getTime());
    	timestamp = Ts.getTime();
    	if( databaseAdapter.createEntry(Title, path+FileName, path + "r_" + timestamp + FileName) == -1 ) {
            //Toast.makeText(MediaCenter.this, "Database Error", Toast.LENGTH_SHORT).show();
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
     * Start playing just the beats
     * 
     */
    
    private void startPlaying() {
    	if( mPlayer == null ) {
    		playBeats( FileName, maxVolume );
    	} else {
    		mPlayer.start();
    	}
    }

    /**
     * Pause both the beats & lyrics player:
     * 
     */
    
    private void pausePlayer() {
        if (mPlayer != null) {
            mPlayer.pause();
            if (isFinishing()) {
                mPlayer.stop();
                mPlayer.release();
            }
        }
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
     * 
     */
    
    private void stopRecording() {
    	if( mRecorder != null ) {
    		mRecorder.release();
    		mRecorder = null;
    	}
    }

    /**
     * Make a new MediaRecorder object and start recording...
     * 
     */
    
    private void startRecording() {

	    try {
	        int version = android.os.Build.VERSION.SDK_INT;

	    	mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

	        /*
	         * TODO upgrade to API 16 and make some more virtual devices for testing...
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
	        Log.e("MediaCenter", "MediaRecorder Failed: " + e.getMessage() );
			e.printStackTrace();
	    }
    }

    /**
     * Play the beats on the mPlayer: 
     * 
     */
    
    private void playBeats( String FileName, int maxVolume ) {
    	Log.d("MediaCenter", "playing: " + FileName );

		float vol = getScaledVolume( beatvol_bar.getProgress() );

		try {
    		//Handle the files in the resources folder:
	    	if( FileName.equals( "battle" ) || FileName.equals( "irish_beat" ) || FileName.equals( "pass_the_dutch" ) ) {
	    		if( FileName.equals( "battle" ) ) mPlayer = MediaPlayer.create(this, R.raw.battle);
	    		if( FileName.equals( "irish_beat" ) ) mPlayer = MediaPlayer.create(this, R.raw.irish_beat);
	    		if( FileName.equals( "pass_the_dutch" ) )mPlayer = MediaPlayer.create(this, R.raw.pass_the_dutch);
	            
	    		mPlayer.setLooping(false);
	    		mPlayer.setVolume( vol, vol );
	    		mPlayer.start();
		        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		        	public void onCompletion(MediaPlayer mp) {
		            	recordButton.setImageResource(R.drawable.record);
		            	stopRecording();
		        	}
		        });
	    	} else {
	    		//Handle a file that is in the file system:
		        mPlayer = new MediaPlayer();
		
		        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		        	public void onCompletion(MediaPlayer mp) {
		            	recordButton.setImageResource(R.drawable.record);
		            	stopRecording();
		        	}
		        });
		        
		        File file = new File( path + FileName );
		        FileInputStream fis;
		        fis = new FileInputStream(file);
	            mPlayer.setDataSource(fis.getFD());
	            mPlayer.prepare();
	            mPlayer.setLooping(false);
	            mPlayer.setVolume( vol, vol );
	            mPlayer.start();
			}
	    } catch (Exception e) {
	        Log.e("MediaCenter", "Play MP3 Failed" + e.getMessage() );
			e.printStackTrace();
	    }
    }
    
    /**
     * Play the lyrics on the rPlayer: 
     * 
     */
    
    private void playLyrics( String FileName, int maxVolume ) {
		float vol = getScaledVolume( lyricvol_bar.getProgress() );

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
	        rPlayer.setVolume( vol, vol );
	        rPlayer.start();
		} catch (Exception e) {
		    Log.e("MediaCenter", "Play MP3 Failed" + e.getMessage() );
			e.printStackTrace();
		}
    }
    
    /**
     * Play both the lyrics and beats by calling their invoking methods...
     * 
     */
    
    private void playLyricsAndBeats() {
		playLyrics( "r_" + FileName, maxVolume );
		try {
			Thread.sleep(lyric_offset);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playBeats( FileName, maxVolume );
    }
    
    /**
     * 
     * Handle clicks to the pop up dialogs...
     * 
     * The way I'm doing it is deprecated, so it needs to be updated...
     * 
     */
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
    	switch( id ) {
    	case 0:
            return new AlertDialog.Builder(MediaCenter.this)
	        	.setCancelable(false)
	            .setTitle("Publish Rap?")
	            .setMessage("This will generate an MP3 and publish it on the web. You will be able to save the MP3 to your phone or computer or share it on Facebook.")
	            .setPositiveButton("Publish Rap", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	recordButton.setImageResource(R.drawable.record);
	                	stopRecording();
	                	stopPlaying();

	        	    	Intent intent = new Intent(me, WebPublisher.class);

	        	    	Log.d("MediaCenter", "Sending " + Title + " and path " + path + "r_" + FileName + " to generate wav.");
	        	    	
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
    	default: /* case 1 */
            return new AlertDialog.Builder(MediaCenter.this)
	        	.setCancelable(false)
	            .setTitle("Redownload?")
	            .setMessage("If the song did not completely download, this will attempt to download the song again.")
	            .setPositiveButton("Redownload", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	stopRecording();
	                	stopPlaying();
	                	
	                	//Delete current file...
	                    File file = new File( path + FileName );
	                	
	                	if ( file.exists() ) {
	                		file.delete();
	                	}

	                	//Start new activity just like this one...
	                	Intent intent = new Intent(me, MediaCenter.class);
	            		intent.putExtra("Title",Title);
	            		intent.putExtra("FileName",FileName);
	                	startActivity(intent);

	                	//Finish this current activity...
	                	me.finish();

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
    
    private float getScaledVolume(int arg1)
    {
    	double vol = arg1 / 10;
    	
    	Log.d("MediaCenter", "vol in getScaledVolume is " + vol );
    	
    	if( vol <= maxVolume / 20 ) {
        	vol = vol / 23;
        } else if( vol <= maxVolume / 19 ) {
        	vol = vol / 22;
        } else if( vol <= maxVolume / 18 ) {
        	vol = vol / 21;
        } else if( vol <= maxVolume / 17 ) {
        	vol = vol / 20;
        } else if( vol <= maxVolume / 16 ) {
        	vol = vol / 19;
        } else if( vol <= maxVolume / 15 ) {
        	vol = vol / 18;
        } else if( vol <= maxVolume / 14 ) {
        	vol = vol / 17;
        } else if( vol <= maxVolume / 13 ) {
        	vol = vol / 16;
        } else if( vol <= maxVolume / 12 ) {
        	vol = vol / 15;
        } else if( vol <= maxVolume / 11 ) {
        	vol = vol / 14;
        } else if( vol <= maxVolume / 10 ) {
        	vol = vol / 13;
        } else if( vol <= maxVolume / 9 ) {
        	vol = vol / 12;
        } else if( vol <= maxVolume / 8 ) {
        	vol = vol / 11;
        } else if( vol <= maxVolume / 7 ) {
        	vol = vol / 10;
        } else if( vol <= maxVolume / 6 ) {
        	vol = vol / 9;
        } else if ( vol <= maxVolume / 5 ) {
        	vol = vol / 8;
        } else if ( vol <= maxVolume / 4 ) {
        	vol = vol / 7;
        } else if ( vol <= maxVolume / 3 ) {
        	vol = vol / 6;
        } else { //Let it be:
        	vol = arg1 / 10;
        }
        return (float)vol;
    }
    
    /**
     * 
     * Let's do some seek bar stuff like make some seek bar classes and other seek bar stuff....
     * 
     */

    private class offsetBarListener implements OnSeekBarChangeListener {
    	
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			//Toast.makeText(MP3Downloader.this, arg1, Toast.LENGTH_SHORT).show();
			lyric_offset = arg1;
		}
	
	
		//Need to add a second media player for recording...

		public void onStopTrackingTouch(SeekBar seekBar) {
			//startPlaying();
		}
	
	
		public void onStartTrackingTouch(SeekBar seekBar) {
			stopPlaying();
			stopRecording();
		}
    }

    /**
     * 
     * Let's do some seek bar stuff like make some seek bar classes and other seek bar stuff....
     * 
     */

    private class lyricvolBarListener implements OnSeekBarChangeListener {
    	
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			
			float vol = getScaledVolume(arg1);

	        Log.d("MediaCenter", "Set Volume from: " + arg1/10 + " and scale it to: " + vol );
	        
			if( rPlayer != null ) {
				rPlayer.setVolume(vol, vol);
			}
		}

		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	
	
		public void onStartTrackingTouch(SeekBar seekBar) {

		}
    }

    /**
     * 
     * Let's do some seek bar stuff like make some seek bar classes and other seek bar stuff....
     * 
     */

    private class beatvolBarListener implements OnSeekBarChangeListener {
    	
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			
			float vol = getScaledVolume(arg1);

	        Log.d("MediaCenter", "Set Volume from: " + arg1/10 + " and scale it to: " + vol );
	        
			if( mPlayer != null ) {
				mPlayer.setVolume(vol, vol);
			}
		}
	
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	
	
		public void onStartTrackingTouch(SeekBar seekBar) {

		}
    }
    	
}

