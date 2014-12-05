package com.adamcox.rapbeats;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class LandingPage extends ListActivity {
    /** Called when the activity is first created. */

	private LandingPage me = this;
	
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;

	private Integer[] imgid = {
			R.drawable.make_a_rap,
			R.drawable.freestyle48px,
			R.drawable.my_saved_raps,
			R.drawable.leave_review,
			R.drawable.publishbeats48px,
			R.drawable.import_beats,
			R.drawable.www48px,
			R.drawable.earncredits48px,
			R.drawable.upgrade48px
	};
	
	/*
	 * Import Beats Variables
	 */
	private List<String> item = null;
	private List<String> m_path = null;

	
    String path;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.landing_page);

		item = new ArrayList<String>();
		m_path = new ArrayList<String>();
	
    	
    	//____finish free trial stuff...

        //id=0
    	m_path.add( "" );
    	item.add( "Make a Rap" );

        //id=1
    	m_path.add( "" );
    	item.add( "Freestyle" );

        //id=2
    	m_path.add( "" );
    	item.add( "My Saved Raps" );

        //id=3
    	m_path.add( "" );
    	item.add( "Leave Review" );

        //id=4
    	m_path.add( "" );
    	item.add( "Publish Beats" );

        //id=5
    	m_path.add( "" );
    	item.add( "Import Beats" );

        //id=6
    	m_path.add( "" );
    	item.add( "www.8d8Apps.com" );

    	//_____Pro version don't need earn credits and upgrade buttons...
        if( ! me.getResources().getBoolean(R.bool.app_is_pro) ) {

	        //id=7
	    	m_path.add( ""  );
	    	item.add( "Earn Credits" );
	
	        //id=8
	    	m_path.add( ""  );
	    	item.add( "Upgrade" );

        }
	    	
    	mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();

		for( int i=0; i < item.size(); i++){
			try {
				rd = new RowData(i,item.get(i),m_path.get(i));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);
		}
		
		CustomAdapter adapter = new CustomAdapter(this, R.layout.list, R.id.title, data);
		
		setListAdapter(adapter);
		
		getListView().setTextFilterEnabled(true);

    }
    
	public String GetUniqueDeviceId()
	{
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    return deviceUuid.toString();
	}

    /*
     * Lets do some 10 day free trial stuff for the non-pro version only...
     * 
     * This is not being used now, but is being saved for potential use in the future...
     * 
     */

    public void FreeTrial()
    {

        if( ! me.getResources().getBoolean(R.bool.app_is_pro) ) {
	        path = "/data/data/com.adamcox.rapbeats/files/m_settings.dat"; //path is OK since this will only execute for free version...
	        File file = new File( path );
	
	        if ( file.exists() ) {
	
	           String installdate = Utils.ReadSettings(this, "m_settings.dat");
	
	           Date now = new Date( System.currentTimeMillis() );
	           Date expires = new Date( Long.valueOf( installdate ) + 10*24*60*60*1000 ); //Can't make a number bigger than 15 days or so...wraps around...I think...
	
	           SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
	           
	           Log.d( "onActivityResult", "1)Now:          " + sdf.format( now ) );
	           Log.d( "onActivityResult", "1)Expires:    " + sdf.format( expires ) );
	           
	           //Let's not kick users out of the app...
	           //We will slowly make features fade away and replace them with an upgrade message...
	           if( now.after( expires ) ) {
	           		//showDialog(0);
	           }
	
	    	} else {
	      		Utils.WriteSettings(this, String.valueOf(System.currentTimeMillis()), "m_settings.dat" );
	    	}
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) 
    {
    	switch( id ) {
    	case 1:
            return new AlertDialog.Builder(LandingPage.this)
	        	.setCancelable(false)
	            .setTitle("Leave Review?")
	            .setMessage("This will open the Google Play app where you can rate and review this app. Feedback is appreciated!")
	            .setPositiveButton("Leave Review", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	Intent intent = new Intent(Intent.ACTION_VIEW);
	                	//Make sure they go to correct review page based on version:
	                    if( me.getResources().getBoolean(R.bool.app_is_pro) ) {
	                    	intent.setData(Uri.parse("market://details?id=com.adamcox.rapbeatspro"));
	                    } else {
	                    	intent.setData(Uri.parse("market://details?id=com.adamcox.rapbeats"));
	                    }
	                	startActivity(intent);
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked Cancel so do nothing... */
	                }
	            })
	            .create();
    	case 2:
            return new AlertDialog.Builder(LandingPage.this)
	        	.setCancelable(false)
	            .setTitle("Publish Beats?")
	            .setMessage("This will open a web browser to publish your beats. You can also go directly to \nwww.8d8apps.com/\nfrom your desktop computer to publish your beats.")
	            .setPositiveButton("Publish Beats", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	Intent intent = new Intent(Intent.ACTION_VIEW);
                    	intent.setData(Uri.parse("http://www.8d8apps.com/upload.php"));
	                	startActivity(intent);
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked Cancel so do nothing... */
	                }
	            })
	            .create();
    	default:
	            return new AlertDialog.Builder(LandingPage.this)
	        	.setCancelable(false)
	            .setTitle("Upgrade")
	            .setMessage( me.getResources().getString(R.string.upgrade) )
	            .setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked OK so do some stuff */
	                	Intent intent = new Intent(Intent.ACTION_VIEW);
	                	intent.setData(Uri.parse("market://details?id=com.adamcox.rapbeatspro"));
	                	startActivity(intent);
	                	me.finish();
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked Cancel so don't do some stuff */
	                }
	            })
	            .create();
    	}
    }


	public void onListItemClick(ListView parent, View v, int position, long id) {
		Intent intent;
		
		switch( position ) {
		case 0:
			//Set the click action for "Make a Rap" list item
	    	intent = new Intent(me, ListRapBeats.class);
	    	startActivity(intent);
			break;
		case 1:
			//Set the click action for "Make a Rap" list item
	    	intent = new Intent(me, FreeStyle.class);
	    	startActivity(intent);
			break;
		case 2:
			//Set the click action for "My Saved Raps" list item
	    	intent = new Intent(me, MySavedRaps.class);
	    	startActivity(intent);
			break;
		case 3:
			//Set the click action for "Leave Review" list item
			showDialog(1);
			break;
		case 4:
			//Set the click action for "Publish Beats" list item
			showDialog(2);
			break;
		case 5:
			//Set the click action for "Import Beats" list item
	    	intent = new Intent(me, BeatImporter.class);
	    	startActivity(intent);
			break;
		case 6:
			//Set the click action for "www.8d8apps.com" list item
        	intent = new Intent(Intent.ACTION_VIEW);
        	intent.setData(Uri.parse("http://www.8d8apps.com/upload.php"));
        	startActivity(intent);
			break;
		case 7:
			//Set the click action for "Earn Credits" list item
	    	intent = new Intent(me, EarnCredits.class);
	    	startActivity(intent);
			break;
		case 8:
			//Set the click action for "Upgrade" list item
			showDialog(0);
			break;
		}

		//Toast.makeText(getApplicationContext(), "You have selected " + (position+1) + "th item",  Toast.LENGTH_SHORT).show();
	}

	private class RowData {
		protected int mId;
		protected String mTitle;
		protected String mDetail;
		
		RowData(int id,String title,String detail) {
			mId=id;
			mTitle = title;
			mDetail = detail;
		}

		@Override
       public String toString() {
               return mId+" "+mTitle+" "+mDetail;
       }
		
	}
	
	private class CustomAdapter extends ArrayAdapter<RowData> {

		public CustomAdapter(Context context, int resource, int textViewResourceId, List<RowData> objects) {               
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {   
			ViewHolder holder = null;
			TextView title = null;
			TextView detail = null;
			ImageView i11=null;
			RowData rowData= getItem(position);

			if(null == convertView){
				convertView = mInflater.inflate(R.layout.list, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			title = holder.gettitle();
			title.setText(rowData.mTitle);
			detail = holder.getdetail();
			detail.setText(rowData.mDetail);                                                     
			i11=holder.getImage();

			i11.setImageResource(imgid[position]);
			

			return convertView;
		}

		private class ViewHolder {

			private View mRow;
			private TextView title = null;
			private TextView detail = null;
			private ImageView i11=null; 
			
			public ViewHolder(View row) {
				mRow = row;
            }
			
			public TextView gettitle() {
				if(null == title){
					title = (TextView) mRow.findViewById(R.id.title);
                }
				return title;
			}     
			
			public TextView getdetail() {
				if(null == detail){
					detail = (TextView) mRow.findViewById(R.id.detail);
				}
				return detail;
			}
			
			public ImageView getImage() {
				
				if(null == i11){
					i11 = (ImageView) mRow.findViewById(R.id.itemlogo);
				}
                return i11;
			}
		}
	}
}