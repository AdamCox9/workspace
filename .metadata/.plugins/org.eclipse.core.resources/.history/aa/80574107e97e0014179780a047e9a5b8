package com.adamcox.drunktest;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.*;
import android.widget.FrameLayout;

public class DrunkTestActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    public DrunkTestActivity me = this;

    Display display; 
    int width;
    int height;
    
    float pitch;
    float roll;
    
    private int x = 0; //Only update screen 1 in X # of Orientation changes

    /** Called when the activity is first created. */

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        display = getWindowManager().getDefaultDisplay(); 
        width = display.getWidth();
        height = display.getHeight();
        
        
        FrameLayout main = (FrameLayout) findViewById(R.id.main_view);
        main.addView(new DrunkCircle(this,50,50,0));
        
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        /*
         * 
         * Need to make a sensor listener that can tell when the screen roll'd or pitch'd
         * 
         */
        
        
        main.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                FrameLayout flView = (FrameLayout) v;
                flView.addView(new DrunkCircle(me, x, y, 25));
                return true;
            }
        });
	}

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        int radius;
        x++;
        if( x == 10 ) {
        	x = 0;

	        pitch = Math.abs( event.values[1] );     // pitch
	        roll = Math.abs( event.values[2] );        // roll

	        float greatest = pitch > roll ? pitch : roll;
	        radius = (int)(greatest * 30);
	
	        FrameLayout flView = (FrameLayout) findViewById(R.id.main_view);
	        flView.addView(new DrunkCircle(me, width/2, height/2, radius));

        }
    }

    public class DrunkCircle extends View {
	    private final float x;
	    private final float y;
	    private final int r;
	    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	    public DrunkCircle(Context context, float x, float y, int r) {
	        super(context);
	        mPaint.setColor(0xFFFF0000);
	        this.x = x;
	        this.y = y;
	        this.r = r;
	    }

		@Override
	    protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			String text = null;

			//Reset Canvas:
			canvas.drawColor(Color.BLACK);

			Paint lPaint = new Paint();
			
			if( r < 30 ) {
				mPaint.setColor(Color.GREEN);
				lPaint.setColor(Color.GREEN);
				text = "Sober!";
			} else if ( r < 60 ) {
				mPaint.setColor(Color.YELLOW);
				lPaint.setColor(Color.YELLOW);
				text = "Buzzed!";
			} else if ( r < 90 ) {
				mPaint.setColor(0xFFFFA500);
				lPaint.setColor(0xFFFFA500);
				text = "Drunk!";
			} else {
				mPaint.setColor(Color.RED);
				lPaint.setColor(Color.RED);
				text = "Wasted!";
			}
			
			//Text for reporting:
			lPaint.setTextSize(50);
			canvas.drawText(text, 55, 55, lPaint );

			//Draw circle based on Orientation:
			canvas.drawCircle(x, y, r, mPaint);
	    }
	}
}