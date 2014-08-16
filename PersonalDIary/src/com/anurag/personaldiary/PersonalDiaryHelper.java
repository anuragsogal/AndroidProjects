package com.anurag.personaldiary;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.net.Uri;
import android.os.Environment;

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
			String imageFileText = imageFileDirectory.toString()+"/"+"PersonalDiaryIMG_"+dateOfCreation+".jpg";
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

}
