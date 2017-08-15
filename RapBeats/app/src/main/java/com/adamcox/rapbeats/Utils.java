package com.adamcox.rapbeats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

public class Utils {

    /*
     * 
     *  Read settings from disk
     *  
     */
    
    public static String ReadSettings(Context context, String file){
        FileInputStream fIn = null;
        InputStreamReader isr = null;

        char[] inputBuffer = new char[13];

        String data = null;

        try{
	         fIn = context.openFileInput(file);
	         isr = new InputStreamReader(fIn);
	         isr.read(inputBuffer);
	         data = new String(inputBuffer);
        } catch (Exception e) {      
	         e.printStackTrace();
        } finally {
            try {
               isr.close();
               fIn.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
       }
       return data;

    }    
    
    /*
     * 
     *	Write settings to disk
     *  
     */
    
    public static void WriteSettings(Context context, String data, String file){
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;

        try{
	         fOut = context.openFileOutput( file, android.content.Context.MODE_PRIVATE );      
	         osw = new OutputStreamWriter( fOut );
	         osw.write(data);
	         osw.flush();
        } catch (Exception e) {      
			 e.printStackTrace();
        } finally {
            try {
                   osw.close();
                   fOut.close();
            } catch (IOException e) {
                   e.printStackTrace();
            }
         }
    }    

    
    /**
     * Helper function to ensure the file exists...
     * 
     * @return
     */
    public static boolean testRecordingFileExists(String file) {
        File r_file = new File( file );
    	
    	if ( ! r_file.exists() ) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    
    /**
     * Helper function to ensure that file is large enough...
     * 
     * @return
     */
    public static boolean testFileSize(String file) {
        File r_file = new File( file );

        if ( r_file.length() < 30000 ) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    
    //Used for saving
    public static boolean CopyFile(String src, String dest){
    	try{
    		File f1 = new File( src );
    		File f2 = new File( dest );
    		InputStream in = new FileInputStream(f1);

    		//For Overwrite the file.
    		OutputStream out = new FileOutputStream(f2);

    		byte[] buf = new byte[1024];
    		int len;
    		while ((len = in.read(buf)) > 0) {
    			out.write(buf, 0, len);
    		}
    		in.close();
    		out.close();
    		return true;
    	} catch(Exception e){
			Log.d("Utils", "Error 3: " + e );
    	}
    	return false;
    }
    
    
	
}
