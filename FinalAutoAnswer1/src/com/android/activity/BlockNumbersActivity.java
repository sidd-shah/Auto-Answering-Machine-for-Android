package com.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class BlockNumbersActivity extends Activity {
	AutoCompleteTextView txtPhoneNo;

	ArrayList<Contact> contacts;

	private VoiceMailApplication vmapp;
AnsweringMachineActivity ama ;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	 ama = new AnsweringMachineActivity();

		vmapp = (VoiceMailApplication) getApplication();
		contacts = vmapp.contacts;
		setContentView(R.layout.blocknumbers);
		txtPhoneNo = (AutoCompleteTextView) findViewById(R.id.txtPhoneNo);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, VoiceMailApplication.name_Val);
		txtPhoneNo.setAdapter(adapter);

		Button add = (Button) findViewById(R.id.addbutton);
		add.setBackgroundColor(Color.rgb(255, 238, 238));
		add.setTextColor(Color.rgb(0, 0, 0));
		Button remove_selected_contacts = (Button) findViewById(R.id.viewedit);
		remove_selected_contacts.setBackgroundColor(Color.rgb(255, 238, 238));
		remove_selected_contacts.setTextColor(Color.rgb(0, 0, 0));
			add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean flag = false;
				boolean contact_found = false;
				Contact contact = new Contact(txtPhoneNo.getText().toString());
				for (String name :VoiceMailApplication.name_Val)
					if (contact.getContact_name().equalsIgnoreCase(name))
						contact_found = true;
				if (contact_found) {
					for (Contact name1 : vmapp.contacts) {
						if (contact.getContact_name().equalsIgnoreCase(
								name1.getContact_name())) {
							Toast.makeText(
									BlockNumbersActivity.this,
									"Contact " + contact.getContact_name()
											+ " has already been added!",
									Toast.LENGTH_SHORT).show();
							flag = true;
							break;
						}
					}
					if (!flag) {
						vmapp.add(contact);
						Toast.makeText(
								BlockNumbersActivity.this,
								"Contact " + contact.getContact_name()
										+ " has been added!",
								Toast.LENGTH_SHORT).show();
						adapter.notifyDataSetChanged();

					}

				}

				else {
					Toast.makeText(
							BlockNumbersActivity.this,
							"Contact "
									+ contact.getContact_name()
									+ " does not exist in your phonebook. Please enter a valid contact name.",
							Toast.LENGTH_SHORT).show();

				}
				txtPhoneNo.setText("");
			}

		});
		remove_selected_contacts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BlockNumbersActivity.this,
						ListContactsActivity.class);
				startActivity(intent);
				adapter.notifyDataSetChanged();
			}
		});

	}

}
