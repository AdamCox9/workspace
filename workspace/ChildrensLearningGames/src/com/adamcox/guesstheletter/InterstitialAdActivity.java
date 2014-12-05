/**
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at http://aws.amazon.com/apache2.0/
 * or in the "license" file accompanying this file.
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adamcox.guesstheletter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class InterstitialAdActivity extends Activity {
    private static final String APP_KEY = "fd2a4c53017145a48515463513767352"; // Sample Application Key. Replace this value with your Application Key.
    private static final String LOG_TAG = "InterstitialAd"; // Tag used to prefix all log messages.

    private InterstitialAdActivity me = this;
	private Services services;
	
	private boolean launched = false;
    
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        
        //AdRegistration.enableLogging(true);
        //AdRegistration.enableTesting(true);
        
                
        services = new Services();
        
        services.execute();
    }
    
    @Override
    public void onDestroy() {
    	services.cancel(true);
    	super.onDestroy();
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
            //onAfterLoad();
    	}

    	@Override
        protected void onProgressUpdate(Integer... progress) {
    		//Log.d("Download Progress", progress[0] + " KB downloaded" );
        }
    	
    	@Override
    	protected Bitmap doInBackground(String... _fileName) {
    		Log.d("LearnTheNumbers","doInBackground");

    		
    		return null;
    	}
    }
    
}