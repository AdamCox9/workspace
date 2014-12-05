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


public class DrumMachineActivity extends Activity {
    /** Called when the activity is first created. */
    SoundPool soundPool = null;
    DrumMachineActivity me = this;
	
    int s0Id = -1;
    int s1Id = -1;
    int s2Id = -1;
    int s3Id = -1;
    int s4Id = -1;
    int s5Id = -1;
    int s6Id = -1;
    int s7Id = -1;
    int s8Id = -1;
    
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
            
            AssetFileDescriptor s0 = assetManager.openFd("s0.mp3");
            s0Id = soundPool.load(s0, 1);

            AssetFileDescriptor s1 = assetManager.openFd("s1.mp3");
            s1Id = soundPool.load(s1, 1);

            AssetFileDescriptor s2 = assetManager.openFd("s2.mp3");
            s2Id = soundPool.load(s2, 1);

            AssetFileDescriptor s3 = assetManager.openFd("s3.mp3");
            s3Id = soundPool.load(s3, 1);

            AssetFileDescriptor s4 = assetManager.openFd("s4.mp3");
            s4Id = soundPool.load(s4, 1);

            AssetFileDescriptor s5 = assetManager.openFd("s5.mp3");
            s5Id = soundPool.load(s5, 1);

            AssetFileDescriptor s6 = assetManager.openFd("s6.mp3");
            s6Id = soundPool.load(s6, 1);

            AssetFileDescriptor s7 = assetManager.openFd("s7.mp3");
            s7Id = soundPool.load(s7, 1);

            AssetFileDescriptor s8 = assetManager.openFd("s8.mp3");
            s8Id = soundPool.load(s8, 1);

        } catch (IOException e) {
            //textView.setText("Couldn't load sound effect from asset, " + e.getMessage());
        }

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                switch( position ) {
	            	case 0:
	                    soundPool.play(s0Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 1:
	                    soundPool.play(s1Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 2:
	                    soundPool.play(s2Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 3:
	                    soundPool.play(s3Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 4:
	                    soundPool.play(s4Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 5:
	                    soundPool.play(s5Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 6:
	                    soundPool.play(s6Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 7:
	                    soundPool.play(s7Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 8:
	                    soundPool.play(s8Id, 1, 1, 0, 0, 1);
	            	case 9:
	                    soundPool.play(s0Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 10:
	                    soundPool.play(s1Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 11:
	                    soundPool.play(s2Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 12:
	                    soundPool.play(s3Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 13:
	                    soundPool.play(s4Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 14:
	                    soundPool.play(s3Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 15:
	                    soundPool.play(s4Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 16:
	                    soundPool.play(s5Id, 1, 1, 0, 0, 1);
	                	break;
	            	case 17:
	                    soundPool.play(s6Id, 1, 1, 0, 0, 1);
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
                imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(4, 4, 4, 4);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // birds, dogs, duck, frog, car, horse, meow, monkey, train
        private Integer[] mThumbIds = {
                R.drawable.drum48_gg,
                R.drawable.drum48_gp,
                R.drawable.drum48_gy,
                R.drawable.drum48_pb,
                R.drawable.drum48_pg,
                R.drawable.drum48_pr,
                R.drawable.drum48_rb,
                R.drawable.drum48_rs,
                R.drawable.drum48_yo,
                R.drawable.drum48_gg,
                R.drawable.drum48_gp,
                R.drawable.drum48_gy,
                R.drawable.drum48_pb,
                R.drawable.drum48_pg,
                R.drawable.drum48_pr,
                R.drawable.drum48_rb,
                R.drawable.drum48_rs,
                R.drawable.drum48_pg
        };
    }
    
}