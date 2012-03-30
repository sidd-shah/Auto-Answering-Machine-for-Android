package com.android.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

public class VoiceMailApplication extends Application {
	static String[] name_Val;
	static String[] phone_Val;
	
	ArrayList<Contact> contacts;
	ArrayList<Contact> all_contacts;
	private SQLiteDatabase database;
	public static final String TABLE_NAME = "contacts";
	public static final int VERSION = 2;
	public static final String CONTACT_NAME = "name";
	public static final String CONTACT_ID = "id";
	private static final String DB_PATH = "data/data/com.android.FinalAutoAnswer/databases/contacts.sqlite";
	public void onCreate() {

		super.onCreate();
		doDBCheck();
		name_Val= null;
		phone_Val=null;
		
		contacts = new ArrayList<Contact>();
		all_contacts=new ArrayList<Contact>();
		
		getContactName();
		ContactsSQLiteOpenHelper helper = new ContactsSQLiteOpenHelper(
				getApplicationContext());
		database = helper.getWritableDatabase();
		loadContacts();
	}

	private void loadContacts() {

		contacts = new ArrayList<Contact>();
		Cursor contactscursor = database.query(TABLE_NAME, new String[] {
				CONTACT_ID, CONTACT_NAME }, null, null, null, null,
				String.format("%s", CONTACT_NAME));
		contactscursor.moveToFirst();
		if (!contactscursor.isAfterLast()) {
			do {
				Long id = contactscursor.getLong(0);
				String name = contactscursor.getString(1);
				Contact newcontact = new Contact(name);
				newcontact.setContact_id(id);
				contacts.add(newcontact);

			} while (contactscursor.moveToNext());
			contactscursor.close();
		}
	}

	void add(Contact newcontact) {
		contacts.add(newcontact);
		ContentValues cv = new ContentValues();
		cv.put(CONTACT_NAME, newcontact.getContact_name());
		Long id = database.insert(TABLE_NAME, null, cv);
		newcontact.setContact_id(id);
		loadContacts();
	}

	ArrayList<Contact> read() {
		return contacts;
	}

	public void deleteContact(Contact contact) {

		Cursor contactscursor = database.rawQuery("select " + CONTACT_ID
				+ " from " + TABLE_NAME + " where " + CONTACT_NAME + " = ?",
				new String[] { contact.getContact_name() });

		contactscursor.moveToFirst();
		Long id = contactscursor.getLong(0);
		database.delete(TABLE_NAME,
				String.format("%s IN (%s)", CONTACT_ID, id.toString()), null);
		loadContacts();

	}

	private void doDBCheck() {
		try {
			File file = new File(DB_PATH);
			file.delete();
		} catch (Exception ex) {
		}
	}
	public void getContactName() {
		ContentResolver cr = getContentResolver();
		ArrayList<String> c_Name = new ArrayList<String>();
		ArrayList<String> c_Number = new ArrayList<String>();
		Cursor pCur = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (pCur.moveToNext()) {
			String finalphone = "";
			String phone = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			String name = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		
			for (int i = 0; i < phone.length(); i++){
				if (phone.charAt(i) == '-')
					;
				else
					finalphone += phone.charAt(i);
			}	c_Name.add(name);
				c_Number.add(finalphone);
				
		}
		name_Val = (String[]) c_Name.toArray(new String[c_Name.size()]);
		phone_Val = (String[]) c_Number.toArray(new String[c_Name.size()]);
		pCur.close();

	}
}
