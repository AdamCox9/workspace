package com.adamcox.followme;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.lang.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.IBinder;
import android.app.Service;
import android.content.Intent;

public class HttpService extends Service {

	public String postText(String _data) throws IOException {
		byte [] b = new byte[1024];
		URL url;
		url = new URL("http://www.bindingdesigns.com/androidapps/Script.php");
		URLConnection con = url.openConnection();
		

		con.setDoOutput(true);
	    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
	    wr.write("data=" + _data);
	    wr.flush();		
		
		
		InputStream in = con.getInputStream();
		in.read(b);
		
		return new String(b);
	}
	
	public String postPicture(ArrayList<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException 
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new
		HttpPost("http://www.bindingdesigns.com/androidapps/Script.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		InputStream is = entity.getContent();
		//@Todo take action if photo was not successfully uploaded...
		return null;
	}

	/*
	 * Post the audio data stored in string to the server.
	 * This is a large amount of data, so it is done in a new thread.
	 */
	public String postAudio(String audio) throws ClientProtocolException, IOException {
		final String fAudio = audio;
		new Thread() {
			public void run() {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("audio",fAudio));
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new
				HttpPost("http://www.bindingdesigns.com/androidapps/Script.php");
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpResponse response;
				try {
					response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		//@Todo take action if photo was not successfully uploaded...
		return null;
	}

	public String signUpUser(String user, String pass) throws IOException {
		byte [] b = new byte[1024];
		URL url;
		url = new URL("http://www.bindingdesigns.com/androidapps/Script.php");
		URLConnection con = url.openConnection();
		

		con.setDoOutput(true);
	    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
	    wr.write( "user=" + user + "&pass=" + pass );
	    wr.flush();		
		
		
		InputStream in = con.getInputStream();
		in.read(b);
		
		return new String(b);
	}
	

	public String postVideo()
	{
		return null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}