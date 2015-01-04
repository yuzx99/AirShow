package com.microbox.reminder;

import com.microbox.airshow.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class AlarmActivity extends Activity {
	
	private static final String NEW_ALARM = "com.microbox.airshow.action.NEW_ALARM";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String content = "日程提醒";
		if(intent!=null){
			content=intent.getStringExtra("REMIND_CONTENT");
		}
		new AlertDialog.Builder(AlarmActivity.this,AlertDialog.THEME_HOLO_DARK)
		.setIcon(R.drawable.alarms)
		.setTitle("提醒").setMessage(content)
		.setNegativeButton("不再提醒", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(NEW_ALARM);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmActivity.this,
						0, intent, 0);
				AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				alarmManager.cancel(alarmIntent);
				AlarmActivity.this.finish();
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				AlarmActivity.this.finish();
			}
		}).show();
		
		 
	}
}
