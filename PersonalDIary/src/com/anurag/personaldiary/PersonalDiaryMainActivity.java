package com.anurag.personaldiary;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PersonalDiaryMainActivity extends Activity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
	
	String[] from= {"_id","story_tile"};
	int[] to={R.id.storyIDTextView,R.id.storyTitleTextView};
	SimpleCursorAdapter cAdapter;
	ListView listView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity: onCreate()");
		this.setContentView(R.layout.main_activity);
		listView=(ListView)this.findViewById(R.id.storyListView);
		cAdapter=new SimpleCursorAdapter(this.getApplicationContext(),R.layout.list_view_layout,null,from,to,0);
		this.getLoaderManager().initLoader(1, null, this);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				TextView storyIDTxt=(TextView)view.findViewById(R.id.storyIDTextView);
				TextView storyTitleTxt=(TextView)view.findViewById(R.id.storyTitleTextView);
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity:ID of item is: "+storyIDTxt.getText().toString());
				Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity:Title of item is: "+storyTitleTxt.getText().toString());
				displayStoryDetails(storyIDTxt.getText().toString(),storyTitleTxt.getText().toString());
				
			}});
		
		listView.setAdapter(cAdapter);
		
	}
	
	public void onStart(){
		super.onStart();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity: onStart()");

	}
	
	public void onResume(){
		super.onResume();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity: onResume()");

	}
	
	public void onPause(){
		super.onPause();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity: onPause()");

	}
	
	public void onStop(){
		super.onStop();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity: onStop()");

	}
	
	public void onDestroy(){
		super.onDestroy();
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity: onDestory()");

	}
	
	public void addStory(View v){
		if(v.getId()==R.id.AddStoryButton){
			Intent addNewStoryIntent=new Intent(this,PersonalDiaryAddStoryActivity.class);
			this.startActivity(addNewStoryIntent);
		}
	}

	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity: Initializing loader with the id "+id+" whoose data is a cursor");
		CursorLoader cursorLoader=new CursorLoader(this,Uri.parse(PersonalDiaryConstants.PROVIDER_URL),from,null,null,null);
		return cursorLoader;
	}

	
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity:loader initialized with the id "+loader.getId());
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryMainActivity:Cursor count is: "+data.getCount());
		cAdapter.swapCursor(data);
		
	}

	
	public void onLoaderReset(Loader<Cursor> loader) {
		
		
	}
	
	
	private void displayStoryDetails(String storyID, String storyTitle ){
		Intent displayStoryDetailsIntent=new Intent(this.getApplicationContext(),PersonalDiaryDisplayStoryDetailActivity.class);
		displayStoryDetailsIntent.putExtra("StoryID", storyID);
		displayStoryDetailsIntent.putExtra("StoryTitle", storyTitle);
		this.startActivity(displayStoryDetailsIntent);
	}


}
