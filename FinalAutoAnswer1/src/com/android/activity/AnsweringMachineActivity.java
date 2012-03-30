package com.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

public class AnsweringMachineActivity extends Activity {
	AutoAnswerReceiver receiver;
	static boolean flag = true;
	static boolean blockflag = true;
	VoiceMailApplication vmapp;
	private Spinner spinner;
	ArrayList<String> spinnerList;
	static boolean unplayed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		vmapp = (VoiceMailApplication) getApplication();
		spinner = (Spinner) findViewById(R.id.spinner1);
		spinnerList = new ArrayList<String>();
		spinnerList.add("view played messages");
		spinnerList.add("view unplayed messages");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (arg0.getItemAtPosition(position).toString()
						.equalsIgnoreCase("view played messages"))
					unplayed = false;
				else
					unplayed = true;
				Intent intent = new Intent(getBaseContext(),
						UnplayedMessagesActivity.class);
				startActivity(intent);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		final CheckBox service = (CheckBox) findViewById(R.id.checkBox);
		service.setBackgroundColor(Color.rgb(255, 238, 238));
		service.setTextColor(Color.rgb(0, 0, 0));
		final CheckBox blockservice = (CheckBox) findViewById(R.id.blockedcheckBox);
		blockservice.setBackgroundColor(Color.rgb(255, 238, 238));
		blockservice.setTextColor(Color.rgb(0, 0, 0));
		receiver = new AutoAnswerReceiver();

		service.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean status) {
				flag = status;
				
			
			}
		});
		blockservice.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean status) {
				blockflag = status;
				
			}
		});
		Button button = (Button) findViewById(R.id.mainaddbutton);
		button.setBackgroundColor(Color.rgb(255, 150, 10));
		button.setTextColor(Color.rgb(0, 0, 0));
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),
						BlockNumbersActivity.class);
				startActivity(i);

			}
		});
		
	}

}
