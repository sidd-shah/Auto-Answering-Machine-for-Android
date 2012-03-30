package com.android.activity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

public class AutoAnswerIntentService extends IntentService {
	MediaRecorder recorder;
	String phone_no;
	String file_name;
	VoiceMailApplication vmap;

	public AutoAnswerIntentService() {
		super("AutoAnswerIntentService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		phone_no = null;
		file_name = null;
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Context context = getBaseContext();
		phone_no = intent.getStringExtra("phone_no");
		
		if (getContactName(phone_no).equals(""))
			file_name = phone_no;
		else
			file_name = getContactName(phone_no);
		
		try {
			Thread.sleep(4 * 1000);
		} catch (InterruptedException e) {
		}

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING) {

			return;
		} else {
			try {
				vmap = (VoiceMailApplication) getApplication();
				String name = getContactName(phone_no);
				boolean contact_found = false;
				for (Contact contact : vmap.contacts) {
					if (name.equalsIgnoreCase(contact.getContact_name())) {
						contact_found = true;
						break;
					}
				}
				if (contact_found && AnsweringMachineActivity.blockflag) {
					answerPhoneHeadsethook(context);
				}
				else if( AnsweringMachineActivity.flag) {
					Thread.sleep(15000);
					answerPhoneHeadsethook(context);
				}
				

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	private void answerPhoneHeadsethook(final Context context)
			throws IllegalStateException, IOException {

		final Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				recorder.stop();
				buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
						KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
				context.sendOrderedBroadcast(buttonUp,
						"android.permission.CALL_PRIVILEGED");

			}
		};
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp,
				"android.permission.CALL_PRIVILEGED");
		startRecording();
		timer.schedule(task, 30000);

		notifyUser();

	}

	private void notifyUser() {

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		final Notification notifyDetails = new Notification(
				R.drawable.phone_receiver, "New Voicemail alert!, Click Me!",
				System.currentTimeMillis());
		notifyDetails.flags = Notification.FLAG_AUTO_CANCEL;
		Intent notificationIntent = new Intent(this,
				AnsweringMachineActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notifyDetails.setLatestEventInfo(getBaseContext(),
				"you have a message from", file_name, contentIntent);
		final int NOTIFY_ID = 1;
		mNotificationManager.notify(NOTIFY_ID, notifyDetails);

	}

	public String getContactName(final String phoneNumber) {

		boolean nofound = false;
		int i = 0;
		for (i = 0; i < VoiceMailApplication.phone_Val.length; i++) {
			if (phoneNumber.equalsIgnoreCase(VoiceMailApplication.phone_Val[i])) {

				nofound = true;
				break;
			}
		}

		String contactName;
		if (nofound)
			contactName = VoiceMailApplication.name_Val[i];
		else
			contactName = "";
		return contactName;
	}

	private void startRecording() throws IllegalArgumentException,
			IllegalStateException, IOException {
		recorder = new MediaRecorder();
		int audioSource = MediaRecorder.AudioSource.VOICE_CALL;
		recorder.setAudioSource(audioSource);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		String filePath = Environment.getExternalStorageDirectory()
				+ "/call recordings/unplayed/" + file_name + ".mp3";
		File file = new File(filePath);
		int count=0;
		String filepathname;
		filepathname=file_name;
		boolean flag = false;
		if(file.exists()){
			filepathname+="0";
			count++;
			filepathname+=count;
		    filePath = Environment.getExternalStorageDirectory()
			+ "/call recordings/unplayed/" + filepathname + ".mp3";
			file=new File(filePath);
			flag = true;
		}	
		while(true){
		if(file.exists()){
			filepathname=filepathname.substring(0,filepathname.length()-1);
			count++;
			filepathname+=count;
			filePath = Environment.getExternalStorageDirectory()
				+ "/call recordings/unplayed/" + filepathname + ".mp3";
			file=new File(filePath);
		}
		else break;
		String fileplayedPath = Environment.getExternalStorageDirectory()
				+ "/call recordings/played/" + filepathname + ".mp3";
		File file1 = new File(fileplayedPath);
		String filepathname1 = null;
		filepathname1=filepathname;
		if(file1.exists()&& !flag){
			filepathname1+="0";
			count++;
			filepathname1+=count;
			fileplayedPath = Environment.getExternalStorageDirectory()
			+ "/call recordings/played/" + filepathname1 + ".mp3";
			file1=new File(fileplayedPath);
		}	
		while(true){
		if(file1.exists()){
			filepathname1=filepathname1.substring(0,filepathname1.length()-1);
			count++;
			filepathname1+=count;
			fileplayedPath = Environment.getExternalStorageDirectory()
				+ "/call recordings/played/" + filepathname1 + ".mp3";
			file1=new File(fileplayedPath);
		}
		else break;
		}
		file.getParentFile().mkdirs();
		recorder.setOutputFile(filePath);
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		recorder.start();

	}

}
}