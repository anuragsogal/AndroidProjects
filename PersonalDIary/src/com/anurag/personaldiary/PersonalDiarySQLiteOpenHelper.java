package com.anurag.personaldiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PersonalDiarySQLiteOpenHelper extends SQLiteOpenHelper {

	public PersonalDiarySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiarySQLiteOpenHelper: constructor");
		
	}

	
	public void onCreate(SQLiteDatabase db) {
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiarySQLiteOpenHelper: onCreate()");
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiarySQLiteOpenHelper SQL create table :"+PersonalDiaryConstants.CREATE_TABLE_STMT);
		db.execSQL(PersonalDiaryConstants.CREATE_TABLE_STMT);

	}

	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
