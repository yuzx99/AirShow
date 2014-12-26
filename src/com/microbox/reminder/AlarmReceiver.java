package com.microbox.reminder;

import com.mircobox.airshow.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		// arg1.setClass(arg0, AlarmActivity.class);
		// arg1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// arg0.startActivity(arg1);
		NotificationManager nm = (NotificationManager) arg0
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(arg0);
		builder.setSmallIcon(R.drawable.galleon_launcher)
				.setTicker("Notification")
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_SOUND)
				.setAutoCancel(true)
				.setContentTitle("This is title")
				.setContentText("this is content")
				.setLights(Color.RED, 0, 1);
		Notification note = builder.getNotification();
		nm.notify(R.string.app_name, note);
	}
}
