package com.mircobox.airshow;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.mircobox.config.ApiUrlConfig;
import com.mircobox.model.*;

public class SplashActivity extends Activity {

	private Timer timer;
	private long waitTime = 3000;
	private long touchTime = 0;
	private String iamgeName = "mockimage.jpeg";
	private LinearLayout llbg = null;

	// Handler handlerBackground = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// Bundle data = msg.getData();
	// String dir = data.getString("dir");
	// String filePath = dir + "/" + iamgeName;
	// Bitmap bitmap = BitmapFactory.decodeFile(filePath);
	// BitmapDrawable bDrawable = new BitmapDrawable(bitmap);
	// // set advertisement page background
	// llbg.setBackgroundDrawable(bDrawable);
	// startTimer();
	// }
	// };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFullScreen();
		setContentView(R.layout.splash);

		llbg = (LinearLayout) findViewById(R.id.splash);
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(llbg, ApiUrlConfig.URL_ADVERTISING_IMAGE);
		// Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		// BitmapDrawable bDrawable = new BitmapDrawable(bitmap);
		// llbg.setBackgroundDrawable(bDrawable);
		startTimer();
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// MBDownloadFile httpDownLoader = new MBDownloadFile();
		// try {
		// int result = httpDownLoader.downFiletoDecive(urlMockimage,
		// iamgeName);
		// if (result == -1) {
		// finish();
		// System.exit(0);
		// } else {
		// Message msg = new Message();
		// Bundle data = new Bundle();
		// data.putString("dir", httpDownLoader.getDirPath());
		// msg.setData(data);
		// handlerBackground.sendMessage(msg);
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }).start();
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

	private void setFullScreen() {
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
