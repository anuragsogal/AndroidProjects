package com.anurag.personaldiary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
		vView.setVideoURI(Uri.parse(videoPath));
		
	}
	
	public void onResume(){
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
	}

}
