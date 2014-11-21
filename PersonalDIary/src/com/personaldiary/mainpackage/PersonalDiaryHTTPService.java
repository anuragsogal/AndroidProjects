package com.personaldiary.mainpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Service;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class PersonalDiaryHTTPService extends Service {

	HandlerThread thread;
	Handler thisHandler;
	
	public void onCreate(){
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPService::onCreate(). The service has just got created");
		thisHandler=new Handler(this.getMainLooper());
		thread=new HandlerThread("DownloadHTTPThread");
	}
	
	public int onStartCommand(Intent intent,int flags,int startId){
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPService::onStartCommand(). The service has started");
		String uri=intent.getStringExtra("URI");
		thread.start();
		Handler handler=new Handler(thread.getLooper(),new Handler.Callback() {
			public boolean handleMessage(Message msg) {
				//PersonalDiaryHelper diaryHelper=new PersonalDiaryHelper();
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPService: Looks like there was a message");
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPService: The message is **** "+msg.getData().get("URI"));
				String URIString=(String)msg.getData().get("URI");
				String response=downLoadDataFromCloud(URI.create(URIString));
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPService: Inside handleMessage. The response is: "+response);
				stopSelf();
				return true;
			}
		});
		//handler.post(new PersonalDiaryHTTPRunnable(URI.create(uri),thisHandler));
		/*String response=downLoadDataFromCloud(URI.create(uri));*/
		Message messageToSend=Message.obtain();
		Bundle bundleData=messageToSend.getData();
		bundleData.putString("URI", uri);
		messageToSend.setData(bundleData);
		handler.sendMessage(messageToSend);
		
		return Service.START_NOT_STICKY;
	}
	
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void onDestroy(){
		super.onDestroy();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPService::onDestory(). Destroyed the service");
	}
	
	public String downLoadDataFromCloud(URI uri){
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryHTTPService::downLoadDataFromCloud(). Downloading data from the cloud");
		String response=null;
		AndroidHttpClient httpClient= AndroidHttpClient.newInstance("downloadHttpService");
		HttpGet getReq=new HttpGet();
		getReq.setURI(uri);
		try {
			response=httpClient.execute(getReq, new ResponseHandler<String>(){
				String responseString;
				JSONTokener jsonTokener;
				public String handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					
					BufferedReader bufReader=new BufferedReader(new InputStreamReader(arg0.getEntity().getContent()));
					jsonTokener=new JSONTokener(bufReader.readLine());
					try {
						JSONObject json=(JSONObject)jsonTokener.nextValue();
						responseString=json.getString("description");
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
		return response;
	}


	

}
