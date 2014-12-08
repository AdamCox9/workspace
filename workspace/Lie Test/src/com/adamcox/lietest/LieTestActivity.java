package com.adamcox.lietest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;


//Need to implement SensorListener
public class LieTestActivity extends Activity implements SensorEventListener {
	// For shake motion detection.
	private SensorManager sensorMgr;
	private Sensor mAccelerometer;
	
	Bitmap bitmap_green;
	Bitmap bitmap_yellow;
	Bitmap bitmap_orange;
	Bitmap bitmap_red;
	
	int bitmap_flag = 0;
 
	private long lastUpdate = -1;
	private float x, y, z;
	private float last_x, last_y, last_z;
	
	private float offset = -20;

    Display display; 
    int width;
    int height;
    
	private static final int SHAKE_THRESHOLD_G = 5; // < In the green...
	private static final int SHAKE_THRESHOLD_Y = 7; // < In the yellow...
	private static final int SHAKE_THRESHOLD_O = 9; // < In the orange... > In the red...
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bitmap_green = BitmapFactory.decodeResource(getResources(), R.drawable.wave_green);
		bitmap_yellow = BitmapFactory.decodeResource(getResources(), R.drawable.wave_yellow);
		bitmap_orange = BitmapFactory.decodeResource(getResources(), R.drawable.wave_orange);
		bitmap_red = BitmapFactory.decodeResource(getResources(), R.drawable.wave_red);

        setContentView(R.layout.main);

        display = getWindowManager().getDefaultDisplay(); 
        width = display.getWidth();
        height = display.getHeight();
        
        FrameLayout main = (FrameLayout) findViewById(R.id.main_views);
        main.addView(new LieWave(this));
        
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	
		boolean accelSupported = sensorMgr.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

		if (!accelSupported) {
			// on accelerometer on this device
			sensorMgr.unregisterListener(this);
			// tell user that their phone isn't supported...
		}
	}

	protected void onPause() {
		if (sensorMgr != null) {
			sensorMgr.unregisterListener(this);
			sensorMgr = null;
		}
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
		if( sensorMgr == null )
			sensorMgr.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void onAccuracyChanged(Sensor s, int arg1) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(SensorEvent se) {

		long curTime = System.currentTimeMillis();
	    // only allow one update every 100ms.
	    if ((curTime - lastUpdate) > 200) {
			long diffTime = (curTime - lastUpdate);
			lastUpdate = curTime;
	
			x = se.values[0];
			y = se.values[1];
			z = se.values[2];
	
			float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;
			
			if (speed < SHAKE_THRESHOLD_G) { //Green
        		bitmap_flag = 0;
			} else if (speed > SHAKE_THRESHOLD_G && speed <= SHAKE_THRESHOLD_Y) { //Yellow
        		bitmap_flag = 1;
			} else if (speed > SHAKE_THRESHOLD_Y && speed <= SHAKE_THRESHOLD_O) { //Orange
        		bitmap_flag = 2;
			} else { //Red
        		bitmap_flag = 3;
			} 

			last_x = x;
			last_y = y;
			last_z = z;
			
	        FrameLayout main = (FrameLayout) findViewById(R.id.main_views);
	        main.addView(new LieWave(this));

	    }
	}


    public class LieWave extends View {
	    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	    public LieWave(Context context) {
	        super(context);
	        mPaint.setColor(0xFFFF0000);
	    }

		@Override
	    protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			//Reset Canvas:
			canvas.drawColor(Color.BLACK);

			Paint lPaint = new Paint();
			lPaint.setColor(Color.GREEN);

			if( bitmap_flag == 0 ) {
				canvas.drawBitmap(bitmap_green, offset++, height/3, null);
			} else if( bitmap_flag == 1 ) {
				canvas.drawBitmap(bitmap_yellow, offset++, height/3, null);
			} else if( bitmap_flag == 2 ) {
				canvas.drawBitmap(bitmap_orange, offset++, height/3, null);
			} else if( bitmap_flag == 3 ) {
				canvas.drawBitmap(bitmap_red, offset++, height/3, null);
			}
				
			//Text for reporting:
			lPaint.setTextSize(50);
			canvas.drawText("Lie Test", 25, 75, lPaint );

				
			//Reset bitmap x axis offset to zero to mimic the wave moving left to right...
			if( offset == 0 ) {
				offset = -20;
			}
	    }
	}

}