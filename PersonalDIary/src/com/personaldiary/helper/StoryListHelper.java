package com.personaldiary.helper;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.anurag.personaldiary.R;
import com.personaldiary.mainpackage.PersonalDiaryConstants;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.TextView;

public class StoryListHelper implements MultiChoiceModeListener {
	
	private Activity mainActivityRef;
	private ListView listView;
	private Resources res;
	private List<Integer> storyIDListToBeDeleted;
	private ContentResolver mainActivityContentRes;
	private String[] selectProjection={"story_image_path","story_video_path"};
	private String selectSelection="_id=?";
	
	public StoryListHelper(){
		storyIDListToBeDeleted= new ArrayList<Integer>();
	}


	public ListView getListView() {
		return listView;
	}


	public void setListView(ListView listView) {
		this.listView = listView;
	}


	public Resources getRes() {
		return res;
	}


	public void setRes(Resources res) {
		this.res = res;
	}
	
	


	public Activity getMainActivityRef() {
		return mainActivityRef;
	}


	public void setMainActivityRef(Activity mainActivityRef) {
		this.mainActivityRef = mainActivityRef;
	}


	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

	}

	
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		
		return false;
	}

	
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		Log.d(PersonalDiaryConstants.TAG,"Deleting the selected items");
		mainActivityContentRes=mainActivityRef.getContentResolver();
		for(int i=0;i<storyIDListToBeDeleted.size();i++){
			Integer idToBeDeleted=storyIDListToBeDeleted.get(i);
			String[] selectionArgs={idToBeDeleted.toString()};
			Cursor cursorObj=mainActivityContentRes.query(Uri.parse(PersonalDiaryConstants.PROVIDER_URL), selectProjection, selectSelection, selectionArgs, null);
			deleteData(cursorObj);
			mainActivityContentRes.delete(Uri.parse(PersonalDiaryConstants.PROVIDER_URL), selectSelection, selectionArgs);
			Log.d(PersonalDiaryConstants.TAG,"Record deleted from the database");
		}
		return false;
	}

	
	public void onDestroyActionMode(ActionMode mode) {
		

	}

	
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		Log.d(PersonalDiaryConstants.TAG,"StoryListHelper:Position of item checked or unchecked is: "+position);
		int positionInTheList= position;
		boolean checkedStatus=checked;
		View listItemView=listView.getChildAt(positionInTheList);
		TextView storyIDTxtView=(TextView)listItemView.findViewById(R.id.storyIDTextView);
		Integer idToBeDeleted=Integer.parseInt(storyIDTxtView.getText().toString());
		ImageView checkedIndicatorImage= (ImageView)listItemView.findViewById(R.id.imageViewTransition);
		if(checkedStatus){
			checkedIndicatorImage.setImageDrawable(res.getDrawable(R.drawable.ic_launcher));
			storyIDListToBeDeleted.add(idToBeDeleted);
		}
		else{
			checkedIndicatorImage.setImageDrawable(res.getDrawable(R.drawable.trash_icon));
			for(int i=0;i<storyIDListToBeDeleted.size();i++){
				int idsInTheList=storyIDListToBeDeleted.get(i);
				if(idsInTheList==idToBeDeleted)
					storyIDListToBeDeleted.remove(i);
			}
		}
			
			
	}
	
	private boolean deleteData(Cursor cursorObj){
		if(cursorObj.getCount()==1){
			//File dir = mainActivityRef.getFilesDir();
			cursorObj.moveToNext();
			String storyImagePath=cursorObj.getString(PersonalDiaryConstants.STORY_IMAGE_PATH);
			String storyVideoPath=cursorObj.getString(PersonalDiaryConstants.STORY_VIDEO_PATH);
			File vidoeFileToBeDeleted,imageFileToBeDeleted;
			try {
				vidoeFileToBeDeleted = new File(new URI(storyVideoPath));
				imageFileToBeDeleted = new File(new URI(storyImagePath));
				boolean isDeletedVideoFile = vidoeFileToBeDeleted.delete();
				boolean isDeletedImageFile= imageFileToBeDeleted.delete();
				if(isDeletedVideoFile && isDeletedImageFile){
					Log.d(PersonalDiaryConstants.TAG,"The video and image file have been deleted");			
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
