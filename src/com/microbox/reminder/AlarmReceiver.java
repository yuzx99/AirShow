package com.microbox.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		arg1.setClass(arg0, AlarmActivity.class);
		arg1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		arg0.startActivity(arg1);
	}

}
