package com.android.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListContactsActivity extends ListActivity {
	VoiceMailApplication vmapp;
	private Builder unsavedchangesdialog;
	ArrayList<String>contact_names;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ListView listview;
		vmapp = (VoiceMailApplication) getApplication();
        contact_names=new ArrayList<String>();
        for(Contact contact: vmapp.contacts)
        	contact_names.add(contact.getContact_name());
		setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
				R.layout.list_contacts, R.id.text, contact_names));
		listview = getListView();
                listview.setBackgroundColor(Color.rgb(255, 238, 238));
		listview.setTextFilterEnabled(true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			private String contact_name;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				contact_name = (String) arg0.getItemAtPosition(position);

				unsavedchangesdialog = new AlertDialog.Builder(
						ListContactsActivity.this);
				unsavedchangesdialog.setTitle("Remove Contact from List.");
				unsavedchangesdialog
						.setMessage(
								"Do you want to remove this contact from the list of blocked contacts?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										vmapp.deleteContact(new Contact(contact_name));
										Toast.makeText(
												ListContactsActivity.this,
												"Contact "
														+ contact_name
																
														+ " has been deleted!",
												Toast.LENGTH_SHORT).show();

										finish();

									}

								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();

									}
								})
						.setNeutralButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										return;

									}
								});
				AlertDialog dialog = unsavedchangesdialog.create();
				dialog.show();
			}

		});

	}

}
