package com.adamcox.guesstheletter;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WhatsTheTimeActivity extends Activity {
    /** Called when the activity is first created. */

	private int hour = 12;
	private String minute = "00";

    SoundPool soundPool = null;
	private Services services = null;
	private SpeakOutTime speakOutTime = null;
	AssetFileDescriptor[] afd;
	int[] afdId;
	AssetManager assetManager;
	
	boolean alive = true;

    @Override
    public void onDestroy() {
    	alive = false;
    	if( services != null ) {
    		services.cancel(true);
    		services = null;
    	}
    	if( speakOutTime != null ) {
    		speakOutTime.cancel(true);
    		speakOutTime = null;
    	}
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
        afd = new AssetFileDescriptor[61];
        afdId = new int[101];

        setContentView(R.layout.download);

        services.execute();
    }
    
    public void onAfterLoad() {
        
        setContentView(R.layout.whatsthetime);
    	final TextView HourField = (TextView) findViewById(R.id.hours);
    	final TextView SemicolonField = (TextView) findViewById(R.id.semicolon);
    	final TextView MinuteField = (TextView) findViewById(R.id.minutes);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/dsdigit.ttf");

        HourField.setTypeface(myTypeface);
        SemicolonField.setTypeface(myTypeface);
        MinuteField.setTypeface(myTypeface);

    	ImageView upHourImage = (ImageView) findViewById(R.id.upHourImage);
    	upHourImage.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				incrementHour();
				HourField.setText(String.valueOf(hour));
				FSpeakOutTime();
				return false;
			}
		});
        
    	ImageView downHourImage = (ImageView) findViewById(R.id.downHourImage);
    	downHourImage.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				decrementHour();
				HourField.setText(String.valueOf(hour));
				FSpeakOutTime();
				return false;
			}
		});
        
    	ImageView upMinuteImage = (ImageView) findViewById(R.id.upMinuteImage);
    	upMinuteImage.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				incrementMinute();
				MinuteField.setText(minute);
				HourField.setText(String.valueOf(hour));
				FSpeakOutTime();
				return false;
			}
		});
        
    	ImageView downMinuteImage = (ImageView) findViewById(R.id.downMinuteImage);
    	downMinuteImage.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				decrementMinute();
				MinuteField.setText(minute);
				HourField.setText(String.valueOf(hour));
				FSpeakOutTime();
				return false;
			}
		});

		FSpeakOutTime();

    }

    private void incrementHour() {
    	Log.d("whatsthetime","incrementHour");
    	if( hour == 12 )
    		hour = 1;
    	else
    		hour++;
    }
    private void decrementHour() {
    	Log.d("whatsthetime","decrementHour");
    	if( hour == 1 )
    		hour = 12;
    	else
    		hour--;
    	
    }
    private void incrementMinute() {
    	Log.d("whatsthetime","incrementMinute");
    	int temp;
    	if( minute.equals("59") ) {
    		minute = "00";
    		incrementHour();
    	} else {
    		temp = Integer.parseInt(minute);
    		temp++;
    		if( temp >= 0 && temp <= 9 ) {
    			minute = "0" + String.valueOf(temp);
    		} else {
    			minute = String.valueOf(temp);
    		}
    	}
    }
    private void decrementMinute() {
    	Log.d("whatsthetime","decrementMinute");
    	int temp;
    	if( minute.equals("00") ) {
    		minute = "59";
    		decrementHour();
    	} else {
    		temp = Integer.parseInt(minute);
    		temp--;
    		if( temp >= 0 && temp <= 9 ) {
    			minute = "0" + String.valueOf(temp);
    		} else {
    			minute = String.valueOf(temp);
    		}
    	}
    }

    public void FSpeakOutTime() {
    	if( speakOutTime != null ) {
    		speakOutTime.cancel(true);
    		speakOutTime = null;
    	}
        speakOutTime = new SpeakOutTime();
    	speakOutTime.execute();
    }

    
    private class SpeakOutTime extends AsyncTask<String, Integer, Bitmap> {

    	@Override
    	protected Bitmap doInBackground(String... _fileName) {
    		int temp = Integer.parseInt(minute);
    		if( alive )
    			soundPool.play(afdId[hour], 1, 1, 0, 0, 1);
    		TheSleeper();
    		if( temp < 10 ) {
    			if( alive )
        			soundPool.play(afdId[0], 1, 1, 0, 0, 1);//O
        		TheSleeper();
        		if( temp == 0 )
        			if( alive )
            			soundPool.play(afdId[60], 1, 1, 0, 0, 1);//clock
        		else
        			if( alive )
            			soundPool.play(afdId[temp], 1, 1, 0, 0, 1);//number
    		} else {
    			if( alive )
        			soundPool.play(afdId[temp], 1, 1, 0, 0, 1);//number
    		}

    		return null;
    	}
    	
    	private void TheSleeper() {
    		try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
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
    		Log.d("Whats The Time","doInBackground");

            try {
		    	afd[0] = assetManager.openFd("timeo.mp3");
		        afdId[0] = soundPool.load(afd[0], 1);
    	        for( int x = 1; x <= 59; x++ ) {
    	        	if( ! alive )
    	        		return null;
    		    	afd[x] = assetManager.openFd(String.valueOf(x)+".mp3");
    		        afdId[x] = soundPool.load(afd[x], 1);
    	        }
		    	afd[60] = assetManager.openFd("clock.mp3");
		        afdId[60] = soundPool.load(afd[60], 1);
            } catch (IOException e) {
                //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
            }

    		return null;
    	}
    }

}