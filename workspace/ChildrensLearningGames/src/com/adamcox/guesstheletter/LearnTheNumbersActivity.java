package com.adamcox.guesstheletter;


import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LearnTheNumbersActivity extends Activity {
    /** Called when the activity is first created. */
	
	int current = 0;
    SoundPool soundPool = null;
	private Services services = null;
	AssetFileDescriptor[] afd;
	AssetManager assetManager;
	int[] afdId;
	boolean alive = true;
	
    @Override
    public void onDestroy() {
    	alive = false;
    	if( services != null )
    		services.cancel(true);
    	if( soundPool != null ) {
    		soundPool.release();
    		soundPool = null;
    	}
    	super.onDestroy();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        assetManager = getAssets();
        services = new Services();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        afd = new AssetFileDescriptor[101];
        afdId = new int[101];

        setContentView(R.layout.download);

        services.execute();
    }

    public void onAfterLoad() {
        setContentView(R.layout.learnthenumbers);
    	
        Button nextPictureButton = (Button) findViewById(R.id.next_number_button);
        Button randomPictureButton = (Button) findViewById(R.id.random_number_button);
        Button previousPictureButton = (Button) findViewById(R.id.previous_number_button);
       
    	final TextView NumberField = (TextView) findViewById(R.id.number_field);
    	NumberField.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
				return false;
			}
		});
        soundPool.play(afdId[current], 1, 1, 0, 0, 1);

        /*
         * Handle the user clicking the "next picture button"
         */
        nextPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if( current == 100 )
            		current = 0;
            	else
            		current++;
 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
            	NumberField.setText(String.valueOf(current));
            	NumberField.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
		 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
						return false;
					}
            	});
            }
        });
        randomPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	current = (int)(Math.random()*100);
 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
            	NumberField.setText(String.valueOf(current));
            	NumberField.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
		 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
						return false;
					}
            	});
            }
        });
        previousPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if( current == 0 )
            		current = 100;
            	else
            		current--;
 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
            	NumberField.setText(String.valueOf(current));
            	NumberField.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
		 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
						return false;
					}
            	});
            }
        });
    }

    /**
     * This class handles the downloading of the song and display's the progress bar with KB downloaded so far...
     * @author Adam
     *
     */
    private class Services extends AsyncTask<String, Integer, Bitmap> {

    	@Override
        protected void onPostExecute(Bitmap bm) {
    		Log.d("LearnTheNumbers","onPostExecute");
            onAfterLoad();
    	}

    	@Override
        protected void onProgressUpdate(Integer... progress) {
    		//Log.d("Download Progress", progress[0] + " KB downloaded" );
        }
    	
    	@Override
    	protected Bitmap doInBackground(String... _fileName) {
    		Log.d("LearnTheNumbers","doInBackground");

            try {
    	        for( int x = 0; x <= 100; x++ ) {
    	        	if( !alive )
    	        		return null;
    		    	afd[x] = assetManager.openFd(String.valueOf(x)+".mp3");
    		        afdId[x] = soundPool.load(afd[x], 1);
    	        }
            } catch (IOException e) {
                //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
            }

    		return null;
    	}
    }
}