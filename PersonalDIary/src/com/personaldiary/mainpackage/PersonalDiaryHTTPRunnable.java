package com.personaldiary.mainpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class PersonalDiaryHTTPRunnable implements Runnable {
	
	Looper otherThreadLooper;
	URI uri;
	AndroidHttpClient httpClient;
	HttpGet getReq;
	HttpPost postReq;
	JSONTokener tokener;
	String responseString;
	String responseToReturn;
	Handler otherThreadHandler;
	Message messageToSend;
	
	public PersonalDiaryHTTPRunnable(URI uri,Handler mainTHandler){
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPRunnable: Inside the constructor");
		//this.otherThreadLooper=looper;
		this.uri=uri;
		this.otherThreadHandler=mainTHandler;
		this.httpClient=AndroidHttpClient.newInstance("cloudService");
		this.getReq=new HttpGet();
		getReq.setURI(uri);
	}

	public void run() {
		try {
			responseToReturn=httpClient.execute(getReq, new ResponseHandler<String>(){
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					
					BufferedReader reader=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					tokener=new JSONTokener(reader.readLine());
					try {
						JSONObject jsonResponse=(JSONObject) tokener.nextValue();
						responseString=jsonResponse.getString("description");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return responseString;
				}});
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		messageToSend=Message.obtain();
		Bundle bundleData=messageToSend.getData();
		bundleData.putString("HTTPRESPONSE", responseToReturn);
		messageToSend.setData(bundleData);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPRunnable:");
		Log.d(PersonalDiaryConstants.TAG,responseToReturn);
		otherThreadHandler.sendMessage(messageToSend);
	}

}
