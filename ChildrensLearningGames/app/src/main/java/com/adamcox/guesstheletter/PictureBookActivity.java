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
import android.widget.TextView;

public class PictureBookActivity extends Activity {
    /** Called when the activity is first created. */
	
	private PictureBookActivity me = this;
	private int current = 0;

    SoundPool soundPool = null;

    int carId = -1;
    int eggId = -1;
    int frogId = -1;
    int houseId = -1;
    int laptopcomputerId = -1;
    int moonId = -1;
    int mouseId = -1;

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
        setContentView(R.layout.picturebook);

        Button nextPictureButton = (Button) findViewById(R.id.next_picture_button);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            
            AssetFileDescriptor moon = assetManager.openFd("moon.mp3");
            moonId = soundPool.load(moon, 1);

            AssetFileDescriptor car = assetManager.openFd("car.mp3");
            carId = soundPool.load(car, 1);

            AssetFileDescriptor egg = assetManager.openFd("egg.mp3");
            eggId = soundPool.load(egg, 1);

            AssetFileDescriptor frog = assetManager.openFd("frog.mp3");
            frogId = soundPool.load(frog, 1);

            AssetFileDescriptor house = assetManager.openFd("house.mp3");
            houseId = soundPool.load(house, 1);

            AssetFileDescriptor laptopcomputer = assetManager.openFd("laptopcomputer.mp3");
            laptopcomputerId = soundPool.load(laptopcomputer, 1);

            AssetFileDescriptor mouse = assetManager.openFd("mouse.mp3");
            mouseId = soundPool.load(mouse, 1);

        } catch (IOException e) {
            //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
        }

    	ImageView firstImage = (ImageView) findViewById(R.id.picture_image);
    	firstImage.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				playSound();
				return false;
			}
		});
		playSound();
        

        /*
         * Handle the user clicking the "next picture button"
         */
        nextPictureButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            	ImageView image = (ImageView) findViewById(R.id.picture_image);
            	TextView text = (TextView) findViewById(R.id.pictureText);
            	
            	int resid = me.getNextPicture();
            	text.setText( getTextById(current) );
            	
            	Resources res = getResources();
            	Drawable pictureImage = res.getDrawable( resid );

            	image.setImageDrawable(pictureImage);

				playSound();

            	image.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						playSound();
						return false;
					}
				});
            }
        });
    }
    
    public void playSound() {
		switch( current ) {
		case 1:
			soundPool.play(frogId, 1, 1, 0, 0, 1);
		break;
		case 2:
			soundPool.play(laptopcomputerId, 1, 1, 0, 0, 1);
		break;
		case 3:
			soundPool.play(mouseId, 1, 1, 0, 0, 1);
		break;
		case 4:
			soundPool.play(eggId, 1, 1, 0, 0, 1);
		break;
		case 5:
			soundPool.play(houseId, 1, 1, 0, 0, 1);
		break;
		case 6:
			soundPool.play(carId, 1, 1, 0, 0, 1);
		break;
		default:
			soundPool.play(moonId, 1, 1, 0, 0, 1);
		break;
		}
    }
    
    public String getTextById(int id) {
    	switch(id) {
    	case 1:
    		return "Frog";
    	case 2:
    		return "Laptop Computer";
    	case 3:
    		return "Mouse";
    	case 4:
    		return "Egg";
    	case 5:
    		return "House";
    	case 6:
    		return "Car";
    	default:
    		return "Moon";
    	}
    }
    
    public int getNextPicture() {
    	switch ( current++ ) {
    	case 0:
    		return getResources().getIdentifier( "picture_frog" , "drawable", "com.adamcox.guesstheletter");
    	case 1:
    		return getResources().getIdentifier( "picture_laptop_computer" , "drawable", "com.adamcox.guesstheletter");
    	case 2:
    		return getResources().getIdentifier( "picture_mouse" , "drawable", "com.adamcox.guesstheletter");
    	case 3:
    		return getResources().getIdentifier( "picture_egg" , "drawable", "com.adamcox.guesstheletter");
    	case 4:
    		return getResources().getIdentifier( "picture_house" , "drawable", "com.adamcox.guesstheletter");
    	case 5:
    		return getResources().getIdentifier( "picture_car" , "drawable", "com.adamcox.guesstheletter");
    	default:
    		current = 0;
    		return getResources().getIdentifier( "picture_moon" , "drawable", "com.adamcox.guesstheletter");
    	}
    }
    
    
}