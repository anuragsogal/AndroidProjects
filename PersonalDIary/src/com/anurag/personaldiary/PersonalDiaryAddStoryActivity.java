package com.anurag.personaldiary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class PersonalDiaryAddStoryActivity extends Activity{
	
	String title,body,imageName,imagePath,videoPath;
	
	ContentResolver cResolver;
	ContentValues cValue;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onCreate()");
		this.setContentView(R.layout.add_story_activity);
		cResolver=this.getContentResolver();
		cValue=new ContentValues();
	}
	
	public void onRestart(){
		super.onRestart();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onRestart()");
	}
	
	public void onStart(){
		super.onStart();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onStart()");
	}
	
	public void onResume(){
		super.onResume();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onResume()");

	}
	
	public void onPause(){
		super.onPause();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onPause()");

	}
	
	public void onStop(){
		super.onStop();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onStop()");

	}
	
	public void onDestroy(){
		super.onDestroy();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onDestroy()");

	}
	
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onSaveInstanceState()");

	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState){
		//super.onRestoreInstanceState(savedInstanceState);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: onRestoreInstanceState()");

	}
	
	public void addImage(View v){
		if(v.getId()==R.id.AddImageButton)
		Log.d(PersonalDiaryConstants.TAG,"Add image button pressed");
		if(PersonalDiaryHelper.isMediaMounted()){
			Uri imageFilePath=PersonalDiaryHelper.getFilePath(PersonalDiaryConstants.IMAGE_FILE);
			imagePath=imageFilePath.toString();
			Log.d(PersonalDiaryConstants.TAG,"Image file is: "+imageFilePath);
			Intent capturePicIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
			this.startActivityForResult(capturePicIntent, PersonalDiaryConstants.CAPTURE_IMAGE);
		}
		else{
			Log.e(PersonalDiaryConstants.TAG,"External media not mounted ");
			this.finish();
		}
		
	}
	
	public void addVideo(View v){
		if(v.getId()==R.id.AddVideoButton)
		Log.d(PersonalDiaryConstants.TAG,"Add video button pressed");
		if(PersonalDiaryHelper.isMediaMounted()){
			Uri videoFilePath=PersonalDiaryHelper.getFilePath(PersonalDiaryConstants.VIDEO_FILE);
			videoPath=videoFilePath.toString();
			Log.d(PersonalDiaryConstants.TAG,"Video file is: "+videoFilePath);
			Intent capturePicIntent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoFilePath);
			this.startActivityForResult(capturePicIntent, PersonalDiaryConstants.CAPTURE_VIDEO);
		}
		else{
			Log.e(PersonalDiaryConstants.TAG,"External media not mounted ");
			this.finish();
		}
		
		
	}
	
	public void createStory(View v){
		Log.d(PersonalDiaryConstants.TAG,"Creating the story");
		EditText editTextTitle=(EditText)this.findViewById(R.id.storyTitleText);
		EditText editTextBody=(EditText)this.findViewById(R.id.storyBodyText);
		EditText editTextImage=(EditText)this.findViewById(R.id.storyImageText);
		title=editTextTitle.getText().toString();
		body=editTextBody.getText().toString();
		imageName=editTextImage.getText().toString();
		cValue.put("story_tile",title);
		cValue.put("story_body",body);
		cValue.put("story_image_name",imageName);
		cValue.put("story_image_path",imagePath);
		cValue.put("story_video_path",videoPath);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryAddStoryActivity: Inserting data into the table");
		cResolver.insert(Uri.parse(PersonalDiaryConstants.PROVIDER_URL), cValue);
		this.finish();
		
	}
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data){
		 if(requestCode==PersonalDiaryConstants.CAPTURE_IMAGE && resultCode==Activity.RESULT_OK)
			 Log.i(PersonalDiaryConstants.TAG,"Image captured successfully");
		 else
			 if(requestCode==PersonalDiaryConstants.CAPTURE_VIDEO && resultCode==Activity.RESULT_OK)
				 Log.i(PersonalDiaryConstants.TAG," Video captured successfully");
			 
	}
	 
	 
}
