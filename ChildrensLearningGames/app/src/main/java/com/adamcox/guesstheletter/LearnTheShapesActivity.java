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


public class LearnTheShapesActivity extends Activity {
    /** Called when the activity is first created. */
	
	private LearnTheShapesActivity me = this;
	int next = 0;

    SoundPool soundPool = null;

    int circleId = -1;
    int heartId = -1;
    int ovalId = -1;
    int rectangleId = -1;
    int squareId = -1;
    int starId = -1;
    int triangleId = -1;

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
        setContentView(R.layout.learntheshapes);

        Button nextShapeButton = (Button) findViewById(R.id.next_shape_button);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            
            AssetFileDescriptor square = assetManager.openFd("square.mp3");
            squareId = soundPool.load(square, 1);

            AssetFileDescriptor circle = assetManager.openFd("circle.mp3");
            circleId = soundPool.load(circle, 1);

            AssetFileDescriptor heart = assetManager.openFd("heart.mp3");
            heartId = soundPool.load(heart, 1);

            AssetFileDescriptor oval = assetManager.openFd("oval.mp3");
            ovalId = soundPool.load(oval, 1);

            AssetFileDescriptor rectangle = assetManager.openFd("rectangle.mp3");
            rectangleId = soundPool.load(rectangle, 1);

            AssetFileDescriptor star = assetManager.openFd("star.mp3");
            starId = soundPool.load(star, 1);

            AssetFileDescriptor triangle = assetManager.openFd("triangle.mp3");
            triangleId = soundPool.load(triangle, 1);

        } catch (IOException e) {
            //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
        }

    	ImageView firstImage = (ImageView) findViewById(R.id.shape_image);
    	firstImage.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				soundPool.play(squareId, 1, 1, 0, 0, 1);
				return false;
			}
		});
		soundPool.play(squareId, 1, 1, 0, 0, 1);
        
        /*
         * Handle the user clicking the "next picture button"
         */
        nextShapeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            	ImageView image = (ImageView) findViewById(R.id.shape_image);

            	int shape = me.getNextShape();
            	
            	Resources res = getResources();
            	Drawable shapeImage = res.getDrawable( shape );
            	
            	image.setImageDrawable(shapeImage);

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
			soundPool.play(squareId, 1, 1, 0, 0, 1);
		break;
		case 2:
			soundPool.play(circleId, 1, 1, 0, 0, 1);
		break;
		case 3:
			soundPool.play(heartId, 1, 1, 0, 0, 1);
		break;
		case 4:
			soundPool.play(ovalId, 1, 1, 0, 0, 1);
		break;
		case 5:
			soundPool.play(rectangleId, 1, 1, 0, 0, 1);
		break;
		case 6:
			soundPool.play(starId, 1, 1, 0, 0, 1);
		break;
		case 7:
			soundPool.play(triangleId, 1, 1, 0, 0, 1);
		break;
		default:
			soundPool.play(squareId, 1, 1, 0, 0, 1);
		break;
		}
    }
    
    public int getNextShape() {
    	switch( next++ ) {
    	case 0:
    		return getResources().getIdentifier( "shape_circle" , "drawable", "com.adamcox.guesstheletter");
    	case 1:
    		return getResources().getIdentifier( "shape_heart" , "drawable", "com.adamcox.guesstheletter");
    	case 2:
    		return getResources().getIdentifier( "shape_oval" , "drawable", "com.adamcox.guesstheletter");
    	case 3:
    		return getResources().getIdentifier( "shape_rectangle" , "drawable", "com.adamcox.guesstheletter");
    	case 4:
    		return getResources().getIdentifier( "shape_star" , "drawable", "com.adamcox.guesstheletter");
    	case 5:
    		return getResources().getIdentifier( "shape_triangle" , "drawable", "com.adamcox.guesstheletter");
    	default:
    		next = 0;
    		return getResources().getIdentifier( "shape_square" , "drawable", "com.adamcox.guesstheletter");
    	}
    	
    }
    
}