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

public class MathActivity extends Activity {
    /** Called when the activity is first created. */

	private int op1 = 1;
	private int op2 = 1;
	private int op = 1; //1+ 2- 3x 4/
	String answer = "2";

    SoundPool soundPool = null;
	private Services services = null;
	private SpeakOutMath speakOutMath = null;
	AssetFileDescriptor[] afd;
	int[] afdId;
	AssetManager assetManager;

	TextView Op1Field;
	TextView OperatorField;
	TextView Op2Field;
	TextView EqualField;
	TextView AnswerField;
	boolean alive = true;
	
    @Override
    public void onDestroy() {
    	alive = false;
    	if( speakOutMath != null ) {
    		speakOutMath.cancel(true);
    	}
    	if( services != null ) {
    		services.cancel(true);
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
        afd = new AssetFileDescriptor[106];
        afdId = new int[106];

        setContentView(R.layout.download);

        services.execute();
    }
    
    public void onAfterLoad() {
        
        setContentView(R.layout.domath);
    	Op1Field = (TextView) findViewById(R.id.mathOp1);
    	OperatorField = (TextView) findViewById(R.id.operator);
    	Op2Field = (TextView) findViewById(R.id.mathOp2);
    	EqualField = (TextView) findViewById(R.id.equals);
    	AnswerField = (TextView) findViewById(R.id.answer);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/dsdigit.ttf");

        Op1Field.setTypeface(myTypeface);
        OperatorField.setTypeface(myTypeface);
        Op2Field.setTypeface(myTypeface);
        EqualField.setTypeface(myTypeface);
        AnswerField.setTypeface(myTypeface);

    	ImageView upOp1Image = (ImageView) findViewById(R.id.upOp1Image);
    	upOp1Image.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				op1++;
				SetAnswer();
				FSpeakOutMath();
				return false;
			}
		});
        
    	ImageView downOp1Image = (ImageView) findViewById(R.id.downOp1Image);
    	downOp1Image.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if( op1 > 0 )
					op1--;
				SetAnswer();
				FSpeakOutMath();
				return false;
			}
		});
        
    	ImageView upOp2Image = (ImageView) findViewById(R.id.upOp2Image);
    	upOp2Image.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				op2++;
				SetAnswer();
				FSpeakOutMath();
				return false;
			}
		});
        
    	ImageView downOp2Image = (ImageView) findViewById(R.id.downOp2Image);
    	downOp2Image.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if( op2 > 0 )
					op2--;
				SetAnswer();
				FSpeakOutMath();
				return false;
			}
		});

    	OperatorField.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if( op == 1 ) {
					op = 2;
					OperatorField.setText("-");
				} else if( op == 2 ) {
					op = 3;
					OperatorField.setText("x");
				} else if( op == 3 ) {
					op = 4;
					OperatorField.setText("/");
				} else {
					op = 1;
					OperatorField.setText("+");
				}
				
				SetAnswer();
				FSpeakOutMath();
				
				return false;
			}
		});

		FSpeakOutMath();

    }

    public void SetAnswer() {
		Op1Field.setText( String.valueOf( op1 ) );
		Op2Field.setText( String.valueOf( op2 ) );

		if( op == 1 ) {
			answer = String.valueOf(op1+op2);
		} else if( op == 2 ) {
			answer = String.valueOf(op1-op2);
		} else if( op == 3 ) {
			answer = String.valueOf(op1*op2);
		} else {
			answer = String.valueOf((double)Math.round(Float.valueOf(op1)/Float.valueOf(op2) * 100) / 100);
		}
		
		AnswerField.setText(String.valueOf(answer));
    }
    
    public void FSpeakOutMath() {
    	if( speakOutMath != null ) {
	    	speakOutMath.cancel(true);
	    	speakOutMath = null;
    	}
        speakOutMath = new SpeakOutMath();
    	speakOutMath.execute();
    }
    
    private class SpeakOutMath extends AsyncTask<String, Integer, Bitmap> {

    	@Override
    	protected Bitmap doInBackground(String... _fileName) {

    		//We only have voice up to 99
    		if( op1 > 99 || op2 > 99 || (int) Double.parseDouble(answer) > 99 )
    			return null;
    		
    		//Field 1
    		if( alive )
    			soundPool.play(afdId[op1], 1, 1, 0, 0, 1);
    		
    		//Sleep
    		TheSleeper();
    		
    		//Operation
    		if( op == 1 && alive )
	    		soundPool.play(afdId[100], 1, 1, 0, 0, 1);
    		if( op == 2 && alive )
	    		soundPool.play(afdId[101], 1, 1, 0, 0, 1);
    		if( op == 3 && alive )
	    		soundPool.play(afdId[102], 1, 1, 0, 0, 1);
    		if( op == 4 && alive )
	    		soundPool.play(afdId[103], 1, 1, 0, 0, 1);
    		TheSleeper();

    		//We only have voice up to 99
    		if( op1 > 99 || op2 > 99 || (int) Double.parseDouble(answer) > 99 )
    			return null;

    		//Field 2
    		if( alive ) {
    			soundPool.play(afdId[op2], 1, 1, 0, 0, 1);
    			TheSleeper();
    		}
    		
    		//Equals
    		if( alive ) {
    			soundPool.play(afdId[104], 1, 1, 0, 0, 1);
    			TheSleeper();
    		}

    		//We only have voice up to 99
    		if( op1 > 99 || op2 > 99 || (int) Double.parseDouble(answer) > 99 )
    			return null;

    		//Answer
    		if( answer.indexOf(".") == -1 && alive )
    			soundPool.play(afdId[Integer.parseInt(answer)], 1, 1, 0, 0, 1);
    		else if( alive ) {
            	for(char c : answer.toCharArray() ) {
            		if( c == '.' ) {
            			soundPool.play(afdId[105], 1, 1, 0, 0, 1);
            		} else {
            			soundPool.play(afdId[Integer.parseInt(String.valueOf(c))], 1, 1, 0, 0, 1);
            		}
            		TheSleeper();
            	}
    		}
    		
    		return null;
    	}
    	
    	private void TheSleeper() {
    		try {
    			if( alive )
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
    		Log.d("math","onPostExecute");
            onAfterLoad();
    	}

    	@Override
        protected void onProgressUpdate(Integer... progress) {
    		//Log.d("Download Progress", progress[0] + " KB downloaded" );
        }
    	
    	@Override
    	protected Bitmap doInBackground(String... _fileName) {
    		Log.d("math","doInBackground");

            try {
            	
    	        for( int x = 0; x <= 99; x++ ) {
    	        	if( ! alive )
    	        		return null;
    		    	afd[x] = assetManager.openFd(String.valueOf(x)+".mp3");
    		        afdId[x] = soundPool.load(afd[x], 1);
    	        }
    	        
	        	if( ! alive )
	        		return null;
		    	afd[100] = assetManager.openFd("nn_plus.mp3");
		        afdId[100] = soundPool.load(afd[100], 1);
	        	if( ! alive )
	        		return null;
		    	afd[101] = assetManager.openFd("nn_minus.mp3");
		        afdId[101] = soundPool.load(afd[101], 1);
	        	if( ! alive )
	        		return null;
		    	afd[102] = assetManager.openFd("nn_times.mp3");
		        afdId[102] = soundPool.load(afd[102], 1);
	        	if( ! alive )
	        		return null;
		    	afd[103] = assetManager.openFd("nn_dividedby.mp3");
		        afdId[103] = soundPool.load(afd[103], 1);
	        	if( ! alive )
	        		return null;
		    	afd[104] = assetManager.openFd("nn_equals.mp3");
		        afdId[104] = soundPool.load(afd[104], 1);
	        	if( ! alive )
	        		return null;
		    	afd[105] = assetManager.openFd("nn_point.mp3");
		        afdId[105] = soundPool.load(afd[105], 1);

            } catch (IOException e) {
                //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
            }

    		return null;
    	}
    }

}