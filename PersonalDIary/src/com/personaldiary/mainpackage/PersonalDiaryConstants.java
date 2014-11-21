package com.personaldiary.mainpackage;

public class PersonalDiaryConstants {
	
	public static final int CAPTURE_IMAGE=100;
	public static final int CAPTURE_VIDEO=101;
	public static final int DATABASE_VERSION=1;
	public static final String TAG="PersonalDiary";
	public static final String IMAGE_FILE="imageFile";
	public static final String VIDEO_FILE="videoFile";
	public static final String DATABASE_NAME="main";
	public static final String TABLE_NAME="Personal_Diary";
	public static final String PROVIDER_URL="content://com.personaldiary.mainpackage.PersonalDiaryContentProvider/Personal_Diary";
	public static final String CREATE_TABLE_STMT="CREATE TABLE "+DATABASE_NAME+"."+TABLE_NAME
			+"(_id INTEGER PRIMARY KEY ASC AUTOINCREMENT,"
			+"story_tile TEXT NOT NULL,"
		    +"story_body TEXT NOT NULL,"
			+"story_image_name TEXT NOT NULL,"
			+"story_image_path TEXT NOT NULL,"
			+"story_video_path TEXT NOT NULL)";
	public static final int STORY_IMAGE_PATH=0;
	public static final int STORY_VIDEO_PATH=1;

}
