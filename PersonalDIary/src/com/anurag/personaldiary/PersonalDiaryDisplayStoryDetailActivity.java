package com.anurag.personaldiary;

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

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class PersonalDiaryDisplayStoryDetailActivity extends Activity {
	
	ContentResolver cResolver;
	String[] projection={"story_video_path","story_image_path"};
	String selection="_id=? and story_tile=?";
	String videoPath;
	String imagePath;
	MediaController mController;
	VideoView vView;
	ImageView iView;
	HandlerThread httpHandlerThread;
	PersonalDiaryHTTPRunnable httpRunnable;
	Handler httpHandlerThreadHandler;
	Handler thisThreadHandler;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: Activity to display the story details");
		this.setContentView(R.layout.story_details_layout);
	    vView=(VideoView)this.findViewById(R.id.storyVideoView);
	    iView=(ImageView)this.findViewById(R.id.storyImageView);
	    mController=new MediaController(this);
		vView.setMediaController(mController);
		mController.setEnabled(false);
		mController.setAnchorView(this.findViewById(R.layout.story_details_layout));
		Intent storyDetailsIntent=this.getIntent();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The id of the story is "+storyDetailsIntent.getStringExtra("StoryID"));
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The title of the story is "+storyDetailsIntent.getStringExtra("StoryTitle"));
		String[] selectionArgs={storyDetailsIntent.getStringExtra("StoryID"),storyDetailsIntent.getStringExtra("StoryTitle")};
		cResolver=this.getContentResolver();
		Cursor data=cResolver.query(Uri.parse(PersonalDiaryConstants.PROVIDER_URL), projection, selection, selectionArgs, null);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The number of rows returned are: "+data.getCount());
		data.moveToFirst();
			videoPath=data.getString(0);
			imagePath=data.getString(1);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The video path is: "+videoPath);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The image path is: "+imagePath);
		//vView.setVideoURI(Uri.parse(videoPath));
		URI svcUri=URI.create("http://videosvc.elasticbeanstalk.com/anurag/videoService");
		HTTPDownloadTask asycTask=new HTTPDownloadTask();
		//asycTask.execute(svcUri);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: Init runnable");
		httpRunnable=new PersonalDiaryHTTPRunnable(svcUri,new Handler(this.getMainLooper(),new Handler.Callback() {
			public boolean handleMessage(Message msg) {
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: Looks like there was a message");
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The message is **** "+msg.getData().get("HTTPRESPONSE"));
				return true;
			}
		}));
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: Init handler thread");
		httpHandlerThread=new HandlerThread("downloadHTTP");
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: Starting the thread handler thread");
		httpHandlerThread.start();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: Getting the handler associated with the handler thread");
		httpHandlerThreadHandler=new Handler(httpHandlerThread.getLooper());
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: Posting runnable to the handler thread");
		httpHandlerThreadHandler.post(httpRunnable);
		
	}
	
	/*public void onResume(){
		super.onResume();
		iView.setImageURI(Uri.parse(imagePath));
		iView.setVisibility(ImageView.VISIBLE);
			vView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The video is ready to be played ");
				mController.setEnabled(true);
				mp.start();
				
			}
		});
	}*/
	
	private class HTTPDownloadTask extends AsyncTask<URI, Void, String>{
		

		protected String doInBackground(URI... params) {
			
			Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity:Inside AsyncTask, the URI is: "+params[0]);
			String response=new String();
			HttpGet getReq= new HttpGet();
			AndroidHttpClient httpClient=AndroidHttpClient.newInstance("anurag");
			getReq.setURI(params[0]);
			try {
				 response=httpClient.execute(getReq, new ResponseHandler<String>(){
						String responseJSON;
					@Override
					public String handleResponse(HttpResponse arg0)
							throws ClientProtocolException, IOException {
						Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity:Inside response handler");
						BufferedReader reader = new BufferedReader(new InputStreamReader(arg0.getEntity().getContent(), "UTF-8"));
						JSONTokener tokener= new JSONTokener(reader.readLine());
						 try {
							JSONObject object = (JSONObject) tokener.nextValue();
							responseJSON=object.getString("description");
							Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity:Inside response handler.The response is: "+responseJSON);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return responseJSON;
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

		 protected void onPostExecute (String response) {
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity:Inside onPostExecute().The response is: "+response);
		 }

		
	}
}
