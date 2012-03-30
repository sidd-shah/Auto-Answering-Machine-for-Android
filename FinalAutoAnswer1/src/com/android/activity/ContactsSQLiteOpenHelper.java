package com.android.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "contacts.sqlite";
	public static final String TABLE_NAME = "contacts";
	public static final int VERSION = 2;
	public static final String CONTACT_NAME="name";
	public static final String CONTACT_ID="id";
	public ContactsSQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
         
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
		
	}

	private void createTable(SQLiteDatabase db2) {
		db2.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + CONTACT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + CONTACT_NAME
				+ " TEXT);");

	}
		

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
