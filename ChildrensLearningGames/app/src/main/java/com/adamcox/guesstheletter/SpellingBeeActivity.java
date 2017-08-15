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

public class SpellingBeeActivity extends Activity {
    /** Called when the activity is first created. */ 

	private int current = 0;
	private int closured = 0;
    SoundPool soundPool = null;
	private Services services = null;
	private SpellOutWord spellOutWord = null;
	AssetFileDescriptor[] afd;
    Hashtable<String, Integer> lafd;
    String gWord;
    boolean alive = true;

	AssetManager assetManager;
	int[] afdId;
	final private int NUM_STRINGS = 25;

	private String[] WordArr = new String[NUM_STRINGS];
    final char[] letters = new char[26];

    @Override
    public void onDestroy() {
    	alive = false;
    	if( services != null ) {
    		services.cancel(true);
    		services = null;
    	}
    	if( spellOutWord != null ) {
    		spellOutWord.cancel(true);
    		spellOutWord = null;
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

        lafd = new Hashtable<String, Integer>();

    	WordArr[0] = "dog";
    	WordArr[1] = "cat";
    	WordArr[2] = "pen";
    	WordArr[3] = "fish";
    	WordArr[4] = "book";
    	WordArr[5] = "run";
    	WordArr[6] = "jump";
    	WordArr[7] = "sky";
    	WordArr[8] = "roll";
    	WordArr[9] = "talk";
    	WordArr[10] = "clown";
    	WordArr[11] = "pets";
    	WordArr[12] = "phone";
    	WordArr[13] = "jogging";
    	WordArr[14] = "mom";
    	WordArr[15] = "dad";
    	WordArr[16] = "brother";
    	WordArr[17] = "sister";
    	WordArr[18] = "school";
    	WordArr[19] = "play";
    	WordArr[20] = "swim";
    	WordArr[21] = "plane";
    	WordArr[22] = "moon";
    	WordArr[22] = "house";
    	WordArr[23] = "mouse";
    	WordArr[24] = "fun";

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
        setContentView(R.layout.spellingbee);

        Button nextWordButton = (Button) findViewById(R.id.next_bee_word_button);
        Button spellWordButton = (Button) findViewById(R.id.spell_word_button);

    	final TextView WordField = (TextView) findViewById(R.id.spellingbee_text);
    	WordField.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
				return false;
			}
		});
        soundPool.play(afdId[current], 1, 1, 0, 0, 1);
        current++;
        
        /*
         * Handle the user clicking the "next picture button"
         */
        nextWordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	WordField.setText( getNextWord() );
 	            soundPool.play(afdId[current], 1, 1, 0, 0, 1);
 	            closured = current;
            	WordField.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
		 	            soundPool.play(afdId[closured], 1, 1, 0, 0, 1);
						return false;
					}
            	});
        		if( ++current == NUM_STRINGS ) {
        			current = 0;
        		}
            }
        });

        /*
         * Handle the user clicking the "next picture button"
         */
        spellWordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	FSpellOutWord(WordArr[closured]);
            }
        });
    }
    
    public String getNextWord() {
		if( current == NUM_STRINGS ) {
			return WordArr[NUM_STRINGS-1];
		}
		return WordArr[current];
    }

    public void FSpellOutWord(String word) {
    	if( spellOutWord != null ) {
	    	spellOutWord.cancel(true);
	    	spellOutWord = null;
    	}
        spellOutWord = new SpellOutWord();
    	gWord = word;
    	spellOutWord.execute();
    }

    private class SpellOutWord extends AsyncTask<String, Integer, Bitmap> {

    	@Override
    	protected Bitmap doInBackground(String... _fileName) {

        	for(char c : gWord.toCharArray() ) {
        		if( alive )
        			soundPool.play(lafd.get(String.valueOf(c)), 1, 1, 0, 0, 1);
        		try {
    				Thread.sleep(750);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
    		
    		return null;
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
    		Log.d("LearnTheNumbers","doInBackground");

            try {
    	        for( int x = 0; x < NUM_STRINGS; x++ ) {
    		    	afd[x] = assetManager.openFd(WordArr[x]+".mp3");
    		        afdId[x] = soundPool.load(afd[x], 1);
    	        }
            } catch (IOException e) {
                //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
            }

            try {
            	AssetFileDescriptor afd_a = assetManager.openFd("a.mp3");
    	        lafd.put("a",soundPool.load(afd_a, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_b = assetManager.openFd("b.mp3");
    	        lafd.put("b",soundPool.load(afd_b, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_c = assetManager.openFd("c.mp3");
    	        lafd.put("c",soundPool.load(afd_c, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_d = assetManager.openFd("d.mp3");
    	        lafd.put("d",soundPool.load(afd_d, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_e = assetManager.openFd("e.mp3");
    	        lafd.put("e",soundPool.load(afd_e, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_f = assetManager.openFd("f.mp3");
    	        lafd.put("f",soundPool.load(afd_f, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_g = assetManager.openFd("g.mp3");
    	        lafd.put("g",soundPool.load(afd_g, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_h = assetManager.openFd("h.mp3");
    	        lafd.put("h",soundPool.load(afd_h, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_i = assetManager.openFd("i.mp3");
    	        lafd.put("i",soundPool.load(afd_i, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_j = assetManager.openFd("j.mp3");
    	        lafd.put("j",soundPool.load(afd_j, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_k = assetManager.openFd("k.mp3");
    	        lafd.put("k",soundPool.load(afd_k, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_l = assetManager.openFd("l.mp3");
    	        lafd.put("l",soundPool.load(afd_l, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_m = assetManager.openFd("m.mp3");
    	        lafd.put("m",soundPool.load(afd_m, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_n = assetManager.openFd("n.mp3");
    	        lafd.put("n",soundPool.load(afd_n, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_o = assetManager.openFd("o.mp3");
    	        lafd.put("o",soundPool.load(afd_o, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_p = assetManager.openFd("p.mp3");
    	        lafd.put("p",soundPool.load(afd_p, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_q = assetManager.openFd("q.mp3");
    	        lafd.put("q",soundPool.load(afd_q, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_r = assetManager.openFd("r.mp3");
    	        lafd.put("r",soundPool.load(afd_r, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_s = assetManager.openFd("s.mp3");
    	        lafd.put("s",soundPool.load(afd_s, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_t = assetManager.openFd("t.mp3");
    	        lafd.put("t",soundPool.load(afd_t, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_u = assetManager.openFd("u.mp3");
    	        lafd.put("u",soundPool.load(afd_u, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_v = assetManager.openFd("v.mp3");
    	        lafd.put("v",soundPool.load(afd_v, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_w = assetManager.openFd("w.mp3");
    	        lafd.put("w",soundPool.load(afd_w, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_x = assetManager.openFd("x.mp3");
    	        lafd.put("x",soundPool.load(afd_x, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_y = assetManager.openFd("y.mp3");
    	        lafd.put("y",soundPool.load(afd_y, 1));

	        	if( ! alive )
	        		return null;
            	AssetFileDescriptor afd_z = assetManager.openFd("z.mp3");
    	        lafd.put("z",soundPool.load(afd_z, 1));

            } catch (IOException e) {
                //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
            }

    		return null;
    	}
    }

    
}