package com.android.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UnplayedMessagesActivity extends ListActivity {
	File[] unplayedfiles;
	File unplayed;
File played;
File[] playedfiles;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		unplayed = new File("sdcard/call recordings/unplayed/");
		unplayedfiles = unplayed.listFiles();
		played =new File("sdcard/call recordings/played/");

		playedfiles = played.listFiles();
		ArrayList<String> playedlist = new ArrayList<String>();
		ArrayList<String> unplayedlist = new ArrayList<String>();
		
		if(playedfiles!=null)
		for (File file : playedfiles) 
			{
				
					playedlist.add(file.toString().substring(30)
							+ "                      "
							+ new Date(file.lastModified()).toString()
									.substring(0, 16));
			}
		for(File file1: unplayedfiles) {
					unplayedlist.add(file1.toString().substring(32)
							+ "                      "
							+ new Date(file1.lastModified()).toString()
									.substring(0, 16));

				}
			

		ListView listview;

		if (AnsweringMachineActivity.unplayed)
			setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
					R.layout.unplayedmessages, R.id.text, unplayedlist));

		else
			setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
					R.layout.unplayedmessages, R.id.text, playedlist));
		listview = getListView();

		listview.setTextFilterEnabled(true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Object o = arg0.getItemAtPosition(position);
				String content = o.toString();
				content = content.substring(0, content.length() - 38);
				String abc = content;
				if (AnsweringMachineActivity.unplayed)
				content = "/sdcard/call recordings/unplayed/" + content;
				else content = "/sdcard/call recordings/played/" + content;
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				File file = new File(content);
				intent.setDataAndType(Uri.fromFile(file), "audio/*");
				startActivity(intent);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				File file1 = new File(played.toString()+"/"+abc);
			}

		});

	}

}
