package com.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class AutoAnswerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
			if(AnsweringMachineActivity.flag||AnsweringMachineActivity.blockflag){
			String phone_state = intent
				.getStringExtra(TelephonyManager.EXTRA_STATE);
		String number = intent
				.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			Intent i = new Intent(context, AutoAnswerIntentService.class);
			i.putExtra("phone_no", number);
			context.startService(i);
		}
		
		}else ;
			
		
	}


}