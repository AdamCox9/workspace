package com.adamcox.guesstheletter;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;


public class PushSoundsActivity extends Activity {
    /** Called when the activity is first created. */
    SoundPool soundPool = null;
    PushSoundsActivity me = this;
	
    int birdsId = -1;
    int dogsId = -1;
    int duckId = -1;
    int frogId = -1;
    int hornId = -1;
    int horseId = -1;
    int meowId = -1;
    int monkeyId = -1;
    int whistleId = -1;
    
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
        setContentView(R.layout.main);

        super.onCreate(savedInstanceState);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            
            AssetFileDescriptor birds = assetManager.openFd("birds.wav");
            birdsId = soundPool.load(birds, 1);

            AssetFileDescriptor dogs = assetManager.openFd("dogs.mp3");
            dogsId = soundPool.load(dogs, 1);

            AssetFileDescriptor duck = assetManager.openFd("duck.wav");
            duckId = soundPool.load(duck, 1);

            AssetFileDescriptor frog = assetManager.openFd("frog.wav");
            frogId = soundPool.load(frog, 1);

            AssetFileDescriptor horn = assetManager.openFd("horn.wav");
            hornId = soundPool.load(horn, 1);

            AssetFileDescriptor horse = assetManager.openFd("horse.wav");
            horseId = soundPool.load(horse, 1);

            AssetFileDescriptor meow = assetManager.openFd("meow.wav");
            meowId = soundPool.load(meow, 1);

            AssetFileDescriptor monkey = assetManager.openFd("monkey.wav");
            monkeyId = soundPool.load(monkey, 1);

            AssetFileDescriptor whistle = assetManager.openFd("whistle.wav");
            whistleId = soundPool.load(whistle, 1);

        } catch (IOException e) {
            //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
        }

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                switch( position ) {
                	case 0:
                        soundPool.play(birdsId, 1, 1, 0, 0, 1);
                    	break;
                	case 1:
                        soundPool.play(dogsId, 1, 1, 0, 0, 1);
                    	break;
                	case 2:
                        soundPool.play(duckId, 1, 1, 0, 0, 1);
                    	break;
                	case 3:
                        soundPool.play(frogId, 1, 1, 0, 0, 1);
                    	break;
                	case 4:
                        soundPool.play(hornId, 1, 1, 0, 0, 1);
                    	break;
                	case 5:
                        soundPool.play(horseId, 1, 1, 0, 0, 1);
                    	break;
                	case 6:
                        soundPool.play(meowId, 1, 1, 0, 0, 1);
                    	break;
                	case 7:
                        soundPool.play(monkeyId, 1, 1, 0, 0, 1);
                    	break;
                	case 8:
                        soundPool.play(whistleId, 1, 1, 0, 0, 1);
                    	break;
                }

                
            }

        });        
    }

    

    
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(125, 125));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // birds, dogs, duck, frog, car, horse, meow, monkey, train
        private Integer[] mThumbIds = {
                R.drawable.sounds_bird, R.drawable.sounds_dog, R.drawable.sounds_duck,
                R.drawable.sounds_frog, R.drawable.sounds_car, R.drawable.sounds_horse,
                R.drawable.sounds_cat, R.drawable.sounds_monkey, R.drawable.sounds_train
        };
    }
    
}