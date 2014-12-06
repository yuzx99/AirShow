package com.mircobox.airshow;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private Timer timer;
	private long waitTime = 3000;
	private long touchTime = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFullScreen();
		setContentView(R.layout.splash);

		// set advertisement page background
		LinearLayout llbg = (LinearLayout) findViewById(R.id.splash);
		llbg.setBackgroundResource(R.drawable.ad);
		startTimer();
	}

	public void startTimer() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new FlashTask(), 3000);
		}
	}

	private class FlashTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			openNextpage();
		}

	}

	private void openNextpage() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);		
		this.finish();
	}

	private void setFullScreen(){
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,   
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); 
	}

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, this.getString(R.string.exit_again),
					Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			finish();
			System.exit(0);
		}
	}
}
