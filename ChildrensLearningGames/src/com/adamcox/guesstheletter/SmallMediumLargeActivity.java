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


public class SmallMediumLargeActivity extends Activity {
    /** Called when the activity is first created. */

    SoundPool soundPool = null;

    int smallId = -1;
    int mediumId = -1;
    int largeId = -1;

	private SmallMediumLargeActivity me = this;
	int next = 0;
	int[] intArr = new int[3];
	
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
        setContentView(R.layout.smallmediumlarge);

        Button nextSmallMediumLargeButton = (Button) findViewById(R.id.next_smallmediumlarge_button);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            
            AssetFileDescriptor moon = assetManager.openFd("small.mp3");
            smallId = soundPool.load(moon, 1);

            AssetFileDescriptor car = assetManager.openFd("medium.mp3");
            mediumId = soundPool.load(car, 1);

            AssetFileDescriptor egg = assetManager.openFd("large.mp3");
            largeId = soundPool.load(egg, 1);
        } catch (IOException e) {
            //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
        }
        
    	final ImageView left_image = (ImageView) findViewById(R.id.left_image);
    	final ImageView center_image = (ImageView) findViewById(R.id.center_image);
    	final ImageView right_image = (ImageView) findViewById(R.id.right_image);
    	final Resources res = getResources();

    	left_image.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				soundPool.play(smallId, 1, 1, 0, 0, 1);
				v.performClick();
				return false;
			}
		});
    	center_image.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				soundPool.play(mediumId, 1, 1, 0, 0, 1);
				v.performClick();
				return false;
			}
		});
    	right_image.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				soundPool.play(largeId, 1, 1, 0, 0, 1);
				v.performClick();
				return false;
			}
		});

        /*
         * Handle the user clicking the "next picture button"
         */
        nextSmallMediumLargeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	
            	me.getNextSML();
            	
            	Drawable sImage = res.getDrawable( intArr[0] );
            	Drawable mImage = res.getDrawable( intArr[1] );
            	Drawable lImage = res.getDrawable( intArr[2] );
            	
            	left_image.setImageDrawable(sImage);
            	center_image.setImageDrawable(mImage);
            	right_image.setImageDrawable(lImage);

            }

        });
        
    }
    
    public void getNextSML() {
    	switch( next ) {
    	case 0:
    		next++;
    		intArr[0] = getResources().getIdentifier( "size_balloon_small" , "drawable", "com.adamcox.guesstheletter");
    		intArr[1] = getResources().getIdentifier( "size_balloon_medium" , "drawable", "com.adamcox.guesstheletter");
    		intArr[2] = getResources().getIdentifier( "size_balloon_large" , "drawable", "com.adamcox.guesstheletter");
    		break;
    	case 1:
    		next++;
    		intArr[0] = getResources().getIdentifier( "size_sun_small" , "drawable", "com.adamcox.guesstheletter");
    		intArr[1] = getResources().getIdentifier( "size_sun_medium" , "drawable", "com.adamcox.guesstheletter");
    		intArr[2] = getResources().getIdentifier( "size_sun_large" , "drawable", "com.adamcox.guesstheletter");
    		break;
    	case 2:
    		next++;
    		intArr[0] = getResources().getIdentifier( "size_cube_small" , "drawable", "com.adamcox.guesstheletter");
    		intArr[1] = getResources().getIdentifier( "size_cube_medium" , "drawable", "com.adamcox.guesstheletter");
    		intArr[2] = getResources().getIdentifier( "size_cube_large" , "drawable", "com.adamcox.guesstheletter");
    		break;
    	case 3:
    		next++;
    		intArr[0] = getResources().getIdentifier( "size_dice_small" , "drawable", "com.adamcox.guesstheletter");
    		intArr[1] = getResources().getIdentifier( "size_dice_medium" , "drawable", "com.adamcox.guesstheletter");
    		intArr[2] = getResources().getIdentifier( "size_dice_large" , "drawable", "com.adamcox.guesstheletter");
    		break;
    	case 4:
    		next++;
    		intArr[0] = getResources().getIdentifier( "size_ribbon_small" , "drawable", "com.adamcox.guesstheletter");
    		intArr[1] = getResources().getIdentifier( "size_ribbon_medium" , "drawable", "com.adamcox.guesstheletter");
    		intArr[2] = getResources().getIdentifier( "size_ribbon_large" , "drawable", "com.adamcox.guesstheletter");
    		break;
    	default:
    		next = 0;
    		intArr[0] = getResources().getIdentifier( "size_circle_small" , "drawable", "com.adamcox.guesstheletter");
    		intArr[1] = getResources().getIdentifier( "size_circle_medium" , "drawable", "com.adamcox.guesstheletter");
    		intArr[2] = getResources().getIdentifier( "size_circle_large" , "drawable", "com.adamcox.guesstheletter");
    	}
    }
    
}