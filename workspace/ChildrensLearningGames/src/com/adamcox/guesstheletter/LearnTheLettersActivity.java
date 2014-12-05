package com.adamcox.guesstheletter;


import java.io.IOException;
import java.util.Hashtable;

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


public class LearnTheLettersActivity extends Activity {
    /** Called when the activity is first created. */
	
	int current = 1;
    SoundPool soundPool = null;
	private Services services = null;
    Hashtable<String, Integer> afd;
    AssetManager assetManager;
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

        afd = new Hashtable<String, Integer>();
        assetManager = getAssets();
        services = new Services();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        setContentView(R.layout.download);

        services.execute();
        
    }

    public void onAfterLoad() {
        setContentView(R.layout.learntheletters);

    	Button nextPictureButton = (Button) findViewById(R.id.next_letter_button);
        Button randomPictureButton = (Button) findViewById(R.id.random_letter_button);
        Button previousPictureButton = (Button) findViewById(R.id.previous_letter_button);

        final char[] letters = new char[26];
        letters[0] = 'A';
        letters[1] = 'B';
        letters[2] = 'C';
        letters[3] = 'D';
        letters[4] = 'E';
        letters[5] = 'F';
        letters[6] = 'G';
        letters[7] = 'H';
        letters[8] = 'I';
        letters[9] = 'J';
        letters[10] = 'K';
        letters[11] = 'L';
        letters[12] = 'M';
        letters[13] = 'N';
        letters[14] = 'O';
        letters[15] = 'P';
        letters[16] = 'Q';
        letters[17] = 'R';
        letters[18] = 'S';
        letters[19] = 'T';
        letters[20] = 'U';
        letters[21] = 'V';
        letters[22] = 'W';
        letters[23] = 'X';
        letters[24] = 'Y';
        letters[25] = 'Z';

    	final TextView LetterField = (TextView) findViewById(R.id.letter_field);
    	LetterField.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
 	            soundPool.play(afd.get("1"), 1, 1, 0, 0, 1);
				return false;
			}
		});

        /*
         * Handle the user clicking the "next picture button"
         */
        nextPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if( current == 26 )
            		current = 1;
            	else
            		current++;
 	            soundPool.play(afd.get(String.valueOf(current)), 1, 1, 0, 0, 1);
            	LetterField.setText(String.valueOf(letters[current-1]));
            	LetterField.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
		 	            soundPool.play(afd.get(String.valueOf(current)), 1, 1, 0, 0, 1);
						return false;
					}
            	});
            }
        });
        randomPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	current = 1+(int)(Math.random()*26);
 	            soundPool.play(afd.get(String.valueOf(current)), 1, 1, 0, 0, 1);
            	LetterField.setText(String.valueOf(letters[current-1]));
            	LetterField.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
		 	            soundPool.play(afd.get(String.valueOf(current)), 1, 1, 0, 0, 1);
						return false;
					}
            	});
            }
        });
        previousPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if( current == 1 )
            		current = 26;
            	else
            		current--;
 	            soundPool.play(afd.get(String.valueOf(current)), 1, 1, 0, 0, 1);
            	LetterField.setText(String.valueOf(letters[current-1]));
            	LetterField.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
		 	            soundPool.play(afd.get(String.valueOf(current)), 1, 1, 0, 0, 1);
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
    		Log.d("LearnTheLetters","onPostExecute");
            soundPool.play(afd.get("1"), 1, 1, 0, 0, 1);
            onAfterLoad();
    	}

    	@Override
        protected void onProgressUpdate(Integer... progress) {
    		//Log.d("Download Progress", progress[0] + " KB downloaded" );
        }
    	
    	@Override
    	protected Bitmap doInBackground(String... _fileName) {
    		Log.d("LearnTheLetters","onPostExecute");
            try {
            	AssetFileDescriptor afd_a = assetManager.openFd("a.mp3");
    	        afd.put("1",soundPool.load(afd_a, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_b = assetManager.openFd("b.mp3");
    	        afd.put("2",soundPool.load(afd_b, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_c = assetManager.openFd("c.mp3");
    	        afd.put("3",soundPool.load(afd_c, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_d = assetManager.openFd("d.mp3");
    	        afd.put("4",soundPool.load(afd_d, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_e = assetManager.openFd("e.mp3");
    	        afd.put("5",soundPool.load(afd_e, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_f = assetManager.openFd("f.mp3");
    	        afd.put("6",soundPool.load(afd_f, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_g = assetManager.openFd("g.mp3");
    	        afd.put("7",soundPool.load(afd_g, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_h = assetManager.openFd("h.mp3");
    	        afd.put("8",soundPool.load(afd_h, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_i = assetManager.openFd("i.mp3");
    	        afd.put("9",soundPool.load(afd_i, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_j = assetManager.openFd("j.mp3");
    	        afd.put("10",soundPool.load(afd_j, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_k = assetManager.openFd("k.mp3");
    	        afd.put("11",soundPool.load(afd_k, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_l = assetManager.openFd("l.mp3");
    	        afd.put("12",soundPool.load(afd_l, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_m = assetManager.openFd("m.mp3");
    	        afd.put("13",soundPool.load(afd_m, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_n = assetManager.openFd("n.mp3");
    	        afd.put("14",soundPool.load(afd_n, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_o = assetManager.openFd("o.mp3");
    	        afd.put("15",soundPool.load(afd_o, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_p = assetManager.openFd("p.mp3");
    	        afd.put("16",soundPool.load(afd_p, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_q = assetManager.openFd("q.mp3");
    	        afd.put("17",soundPool.load(afd_q, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_r = assetManager.openFd("r.mp3");
    	        afd.put("18",soundPool.load(afd_r, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_s = assetManager.openFd("s.mp3");
    	        afd.put("19",soundPool.load(afd_s, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_t = assetManager.openFd("t.mp3");
    	        afd.put("20",soundPool.load(afd_t, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_u = assetManager.openFd("u.mp3");
    	        afd.put("21",soundPool.load(afd_u, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_v = assetManager.openFd("v.mp3");
    	        afd.put("22",soundPool.load(afd_v, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_w = assetManager.openFd("w.mp3");
    	        afd.put("23",soundPool.load(afd_w, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_x = assetManager.openFd("x.mp3");
    	        afd.put("24",soundPool.load(afd_x, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_y = assetManager.openFd("y.mp3");
    	        afd.put("25",soundPool.load(afd_y, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_z = assetManager.openFd("z.mp3");
    	        afd.put("26",soundPool.load(afd_z, 1));

            } catch (IOException e) {
                //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
            }
    		return null;
    	}	

    }
    
}
