package com.anurag.personaldiary;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PersonalDiaryContentProvider extends ContentProvider {

	PersonalDiarySQLiteOpenHelper dbHelper;
	SQLiteDatabase dataBase;
	
	public boolean onCreate() {
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryContentProvider: onCreate()");
		try{
		dbHelper=new PersonalDiarySQLiteOpenHelper(this.getContext(),PersonalDiaryConstants.DATABASE_NAME,null,PersonalDiaryConstants.DATABASE_VERSION);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryContentProvider: Database created");
		dataBase=dbHelper.getWritableDatabase();
		}
		catch(Exception e){
			Log.e(PersonalDiaryConstants.TAG,"PersonalDiaryContentProvider: there was an error in creating the database or in getting a writable: "+e);
			return false;
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String tableName=uri.getPath().replace("/","");
		Cursor cursorObj=dataBase.query(tableName, projection, selection, selectionArgs, null, null, null);
		return cursorObj;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tableName=uri.getPath().replace("/","");
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryContentProvider: Inserting a row of data in the table");
		Long rowNumber=dataBase.insert(tableName, null, values);
		Log.d(PersonalDiaryConstants.TAG,"PersonalDiaryContentProvider: Row number of the row inserted is "+rowNumber);
		String uriString=uri.toString()+"/"+rowNumber.toString();
		return Uri.parse(uriString);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
