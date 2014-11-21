package com.personaldiary.mainpackage;

import android.os.Parcel;
import android.os.Parcelable;

public class DateToParcel implements Parcelable {

	int age;
	String name;
	int[] scores;
	String[] courses;
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(age);
		dest.writeString(name);
		dest.writeIntArray(scores);
		dest.writeStringArray(courses);
	}
	
	public static final Parcelable.Creator<DateToParcel> CREATOR=new Parcelable.Creator<DateToParcel>(){

		@Override
		public DateToParcel createFromParcel(Parcel source) {
			DateToParcel dataToParcel=new DateToParcel();
			dataToParcel.setAge(source.readInt());
			dataToParcel.setName(source.readString());
			dataToParcel.setScores(source.createIntArray());
			dataToParcel.setCourses(source.createStringArray());
			return dataToParcel;
		}

		@Override
		public DateToParcel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DateToParcel[2];
		}

		
	};


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int[] getScores() {
		return scores;
	}


	public void setScores(int[] scores) {
		this.scores = scores;
	}


	public String[] getCourses() {
		return courses;
	}


	public void setCourses(String[] courses) {
		this.courses = courses;
	}

	
	
	
}
