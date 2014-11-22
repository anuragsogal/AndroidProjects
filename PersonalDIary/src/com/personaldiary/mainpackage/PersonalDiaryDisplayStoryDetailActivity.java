package com.personaldiary.mainpackage;


import com.anurag.personaldiary.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class PersonalDiaryDisplayStoryDetailActivity extends Activity {
	
	ContentResolver cResolver;
	String[] projection={"story_tile","story_body","story_image_name","story_video_path","story_image_path"};
	String selection="_id=?";
	String videoPath,imagePath,storyTitle,storyBody,storyImageName;
	MediaController mController;
	VideoView vView;
	ImageView iView;
	HandlerThread httpHandlerThread;
	PersonalDiaryHTTPRunnable httpRunnable;
	Handler httpHandlerThreadHandler;
	Handler thisThreadHandler;
	TextView titlePlaceHolderText,bodyPlaceHolderText,imageNamePlaceHolderText;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: onCreate");
		this.setContentView(R.layout.story_details_layout);
	    vView=(VideoView)this.findViewById(R.id.storyVideoView);
	    iView=(ImageView)this.findViewById(R.id.storyImageView);
	    mController=new MediaController(this);
		vView.setMediaController(mController);
		mController.setEnabled(false);
		mController.setAnchorView(this.findViewById(R.layout.story_details_layout));
		titlePlaceHolderText=(TextView)this.findViewById(R.id.titlePlaceHolder);
		bodyPlaceHolderText=(TextView)this.findViewById(R.id.storyPlaceHolder);
		imageNamePlaceHolderText=(TextView)this.findViewById(R.id.imageNamePlaceHolder);
	}
	
	public void onStart(){
		super.onStart();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: onStart");
		Intent storyDetailsIntent=this.getIntent();	
		String[] selectionArgs={storyDetailsIntent.getStringExtra("StoryID")};
		cResolver=this.getContentResolver();
		Cursor data=cResolver.query(Uri.parse(PersonalDiaryConstants.PROVIDER_URL), projection, selection, selectionArgs, null);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The number of rows returned are: "+data.getCount());
		data.moveToFirst();
		storyTitle=data.getString(0);
		storyBody=data.getString(1);
		storyImageName=data.getString(2);
		videoPath=data.getString(3);
		imagePath=data.getString(4);
		titlePlaceHolderText.setText(storyTitle);
		bodyPlaceHolderText.setText(storyBody);
		imageNamePlaceHolderText.setText(storyImageName);
		iView.setImageURI(Uri.parse(imagePath));
		vView.setVideoURI(Uri.parse(videoPath));
		vView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: The video is ready to be played ");
				mController.setEnabled(true);
				mp.start();
			}
		});
	}
	
	public void onResume(){
		super.onResume();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: onResume()");
	}
	
	public void onPause(){
		super.onPause();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: onPause()");
	}
	
	public void onStop(){
		super.onStop();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: onStop()");
	}
	
	public void onDestroy(){
		super.onDestroy();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryDisplayStoryDetailActivity: onDestroy()");
	}
	


}
