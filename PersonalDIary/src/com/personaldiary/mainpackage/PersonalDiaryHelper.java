package com.personaldiary.mainpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.util.Log;

public class PersonalDiaryHelper {
	
	public static boolean isMediaMounted(){
		String externStorageState=Environment.getExternalStorageState();
		if(externStorageState.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			return true;
		else
		return false;
	}
	
	public static Uri getFilePath(String typeOfFile){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss",Locale.US);
		String dateOfCreation=dateFormat.format(new Date());
		if(typeOfFile.equalsIgnoreCase(PersonalDiaryConstants.IMAGE_FILE)){
			File picDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File imageFileDirectory= new File(picDirectory,"personalDiaryPics");
			String imageFileText = imageFileDirectory.toString()+"/"+"PersonalDiaryIMG_"+dateOfCreation+".png";
			File imageFile=new File(imageFileText);
			return Uri.fromFile(imageFile);
		}
		else if(typeOfFile.equalsIgnoreCase(PersonalDiaryConstants.VIDEO_FILE)){
			File picDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
			File videoFileDirectory= new File(picDirectory,"personalDiaryVideos");
			String videoFileText = videoFileDirectory.toString()+"/"+"PersonalDiaryVID_"+dateOfCreation+".mp4";
			File videoFile=new File(videoFileText);
			return (Uri.fromFile(videoFile));
		}
		return null;
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
