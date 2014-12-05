package com.adamcox.guesstheletter;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class LearnTheColorsActivity extends Activity {
    /** Called when the activity is first created. */
	
	private LearnTheColorsActivity me = this;
	int next = 0;

    SoundPool soundPool = null;

    int blueId = -1;
    int brownId = -1;
    int grayId = -1;
    int greenId = -1;
    int orangeId = -1;
    int pinkId = -1;
    int purpleId = -1;
    int redId = -1;
    int yellowId = -1;

    @Override
    public void onDestroy() {
    	if( soundPool != null ) {
	    	soundPool.release();
	    	soundPool = null;
    	}
    	super.onDestroy();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learnthecolors);

        Button nextColorButton = (Button) findViewById(R.id.next_color_button);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            
            AssetFileDescriptor blue = assetManager.openFd("blue.mp3");
            blueId = soundPool.load(blue, 1);

            AssetFileDescriptor brown = assetManager.openFd("brown.mp3");
            brownId = soundPool.load(brown, 1);

            AssetFileDescriptor gray = assetManager.openFd("gray.mp3");
            grayId = soundPool.load(gray, 1);

            AssetFileDescriptor green = assetManager.openFd("green.mp3");
            greenId = soundPool.load(green, 1);

            AssetFileDescriptor orange = assetManager.openFd("orange.mp3");
            orangeId = soundPool.load(orange, 1);

            AssetFileDescriptor pink = assetManager.openFd("pink.mp3");
            pinkId = soundPool.load(pink, 1);

            AssetFileDescriptor purple = assetManager.openFd("purple.mp3");
            purpleId = soundPool.load(purple, 1);

            AssetFileDescriptor red = assetManager.openFd("red.mp3");
            redId = soundPool.load(red, 1);

            AssetFileDescriptor yellow = assetManager.openFd("yellow.mp3");
            yellowId = soundPool.load(yellow, 1);

        } catch (IOException e) {
            //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
        }

    	ImageView firstImage = (ImageView) findViewById(R.id.color_image);
    	firstImage.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				soundPool.play(blueId, 1, 1, 0, 0, 1);
				return false;
			}
		});
    	soundPool.play(blueId, 1, 1, 0, 0, 1);

        /*
         * Handle the user clicking the "next picture button"
         */
        nextColorButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            	ImageView image = (ImageView) findViewById(R.id.color_image);

            	int color = me.getNextColor();
            	
            	Resources res = getResources();
            	Drawable colorImage = res.getDrawable( color );
            	
            	image.setImageDrawable(colorImage);

				playSound(next);

            	image.setOnTouchListener(new View.OnTouchListener() {
					
					public boolean onTouch(View v, MotionEvent event) {

						playSound(next);
						
						return false;
					}
				});

            }

        });
        
    }
    
    public void playSound(int next) {
		switch( next+1 ) {
		case 1:
			soundPool.play(blueId, 1, 1, 0, 0, 1);
		break;
		case 2:
			soundPool.play(yellowId, 1, 1, 0, 0, 1);
		break;
		case 3:
			soundPool.play(redId, 1, 1, 0, 0, 1);
		break;
		case 4:
			soundPool.play(purpleId, 1, 1, 0, 0, 1);
		break;
		case 5:
			soundPool.play(pinkId, 1, 1, 0, 0, 1);
		break;
		case 6:
			soundPool.play(orangeId, 1, 1, 0, 0, 1);
		break;
		case 7:
			soundPool.play(greenId, 1, 1, 0, 0, 1);
		break;
		case 8:
			soundPool.play(grayId, 1, 1, 0, 0, 1);
		break;
		case 9:
			soundPool.play(brownId, 1, 1, 0, 0, 1);
		break;
		default:
			soundPool.play(yellowId, 1, 1, 0, 0, 1);
		break;
		}
    }
    
    public int getNextColor() {
    	switch( next++ ) {
    	case 0:
    		return getResources().getIdentifier( "color_yellow" , "drawable", "com.adamcox.guesstheletter");
    	case 1:
    		return getResources().getIdentifier( "color_red" , "drawable", "com.adamcox.guesstheletter");
    	case 2:
    		return getResources().getIdentifier( "color_purple" , "drawable", "com.adamcox.guesstheletter");
    	case 3:
    		return getResources().getIdentifier( "color_pink" , "drawable", "com.adamcox.guesstheletter");
    	case 4:
    		return getResources().getIdentifier( "color_orange" , "drawable", "com.adamcox.guesstheletter");
    	case 5:
    		return getResources().getIdentifier( "color_green" , "drawable", "com.adamcox.guesstheletter");
    	case 6:
    		return getResources().getIdentifier( "color_gray" , "drawable", "com.adamcox.guesstheletter");
    	case 7:
    		return getResources().getIdentifier( "color_brown" , "drawable", "com.adamcox.guesstheletter");
    	default:
    		next = 0;
    		return getResources().getIdentifier( "color_blue" , "drawable", "com.adamcox.guesstheletter");
    	}
    	
    }
    
}