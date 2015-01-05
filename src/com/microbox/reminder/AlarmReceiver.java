package com.microbox.reminder;

import java.util.Date;

import com.microbox.airshow.MainActivity;
import com.microbox.airshow.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
		String conf = arg1.getStringExtra("notice_conf");
		long startDays = arg1.getLongExtra("notice_startDays", 0);
		Date curDate = new Date(System.currentTimeMillis());		
		long days = startDays - curDate.getTime()/86400000;
		String content = "距" + conf + "开幕还有"
				+ String.valueOf(days) + "天";
		
		NotificationManager nm = (NotificationManager) arg0
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(arg0);
		builder.setSmallIcon(R.drawable.galleon_launcher).setTicker("AirShow")
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
				.setContentTitle("会展提醒").setContentText(content)
				.setLights(Color.RED, 0, 1);
		Intent intent = new Intent(arg0, MainActivity.class);
		PendingIntent pendingIntetn = PendingIntent.getActivity(arg0, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntetn);
		Notification note = builder.getNotification();
		nm.notify(R.string.app_name, note);
	}
}
