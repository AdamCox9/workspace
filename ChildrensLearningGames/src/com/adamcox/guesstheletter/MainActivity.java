package com.adamcox.guesstheletter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	private MainActivity me = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent intent;
                switch( position ) {
                	case 0:
                    	intent = new Intent(me, LearnTheLettersActivity.class);
                    	startActivity(intent);
                    	break;
                	case 1:
                    	intent = new Intent(me, LearnTheNumbersActivity.class);
                    	startActivity(intent);
                    	break;
                	case 2:
                    	intent = new Intent(me, MathActivity.class);
                    	startActivity(intent);
                    	break;
                	case 3:            		
                    	intent = new Intent(me, LearnTheShapesActivity.class);
                    	startActivity(intent);
                    	break;
                	case 4:
                    	intent = new Intent(me, LearnTheColorsActivity.class);
                    	startActivity(intent);
                    	break;
                	case 5:
                    	intent = new Intent(me, PictureBookActivity.class);
                    	startActivity(intent);
                    	break;
                	case 6:
                    	intent = new Intent(me, SpellingBeeActivity.class);
                    	startActivity(intent);
                    	break;
                	case 7:
                    	intent = new Intent(me, WhatsTheTimeActivity.class);
                    	startActivity(intent);
                    	break;
                	case 8:
                    	intent = new Intent(me, SmallMediumLargeActivity.class);
                    	startActivity(intent);
                    	break;
                	case 9:
                    	intent = new Intent(me, PushSoundsActivity.class);
                    	startActivity(intent);
                    	break;
                	case 10:
                    	intent = new Intent(me, DrumMachineActivity.class);
                    	startActivity(intent);
                    	break;
                	case 11:
                    	intent = new Intent(me, DragNDropActivity.class);
                    	startActivity(intent);
                    	break;
                	case 12:
                		//Leave Review
            			showDialog(1);
                		break;
                	default:
                		//Share
                		Intent sendIntent = new Intent();
                		sendIntent.setAction(Intent.ACTION_SEND);
                		sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this Children's Learning Games App for Android! https://play.google.com/store/apps/details?id=com.adamcox.guesstheletter");
                		sendIntent.setType("text/plain");
                		startActivity(sendIntent);                		
                		break;
                }

                
            }

        });
    }

    @Override
    protected Dialog onCreateDialog(int id) 
    {
        return new AlertDialog.Builder(MainActivity.this)
        	.setCancelable(false)
            .setTitle("Leave Review?")
            .setMessage("This will open the Google Play app where you can rate and review this app. Feedback is appreciated!")
            .setPositiveButton("Leave Review", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	Intent intent = new Intent(Intent.ACTION_VIEW);
                	//Make sure they go to correct review page based on version:
                   	intent.setData(Uri.parse("market://details?id=com.adamcox.guesstheletter"));
                	startActivity(intent);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked Cancel so do nothing... */
                }
            })
            .create();
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
                imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images: they must be ordered correctly for MainActivity.java switch statement
        private Integer[] mThumbIds = {
                							R.drawable.icon_letters, 
                							R.drawable.icon_numbers, 
                							R.drawable.icon_math, 
                							R.drawable.icon_shapes,
                							R.drawable.icon_colors, 
                							R.drawable.icon, 
                							R.drawable.icon_spelling,
                							R.drawable.icon_clock, 
                							R.drawable.icon_size, 
                							R.drawable.icon_sounds, 
                							R.drawable.icon_drums,
                							R.drawable.icon_puzzle,
                							R.drawable.icon_rate,
                							R.drawable.icon_share
        };
    }

}