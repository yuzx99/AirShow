package com.microbox.airshow;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.config.ApiUrlConfig;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.model.LoginModelThread;
import com.microbox.push.ExitApplication;
import com.microbox.util.ConnectionDetector;
import com.microbox.airshow.R;

public class SplashActivity extends Activity {

	private Timer timer;
	private long waitTime = 3000;
	private long touchTime = 0;
	private String iamgeName = "mockimage.jpeg";
	private LinearLayout llbg = null;
	private SharedPreferences spUserInfo;
	private SharedPreferences spData;
	private SharedPreferences spConfigure;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFullScreen();
		setContentView(R.layout.splash);
		ExitApplication.getInstance().addActivity(this);
		ConnectionDetector cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(this,
					getResources().getString(R.string.not_connection),
					Toast.LENGTH_SHORT).show();
		}
		// 记录登录信息
		spUserInfo = this
				.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);
		spConfigure = this.getSharedPreferences("configure",
				Context.MODE_PRIVATE);

		loadAd();
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

	private void loadAd() {
		llbg = (LinearLayout) findViewById(R.id.splash);
		llbg.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(SplashActivity.this, "touch", Toast.LENGTH_SHORT)
						.show();
				// openNextpage();
				return false;
			}
		});
		HttpGetJsonModelThread adThread = new HttpGetJsonModelThread(adHandler,
				ApiUrlConfig.URL_ADVERTISING_IMAGE);
		adThread.start();
	}

	private final Handler adHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle data = msg.getData();
			String result = data.getString("result");
			if (null != result) {
				try {
					JSONObject obj = new JSONObject(result);
					String url = obj.getString("url");
					BitmapUtils bitmapUtils = new BitmapUtils(
							SplashActivity.this);
					bitmapUtils.display(llbg, url);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.handleMessage(msg);
		}

	};

	Handler handlerLogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			String userId = data.getString("userId");
			String password = data.getString("password");
			if (result != null) {
				Toast.makeText(SplashActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				try {
					Editor editorData = spData.edit();
					JSONObject jObject = new JSONObject(result);
					String account = jObject.getString("account");
					String header_small = jObject.getString("header_small");
					String name = jObject.getString("name");
					String token = jObject.getString("token");
					String nickname = jObject.getString("nickname");
					String id = jObject.getString("id");
					editorData.putString("ACCOUNT", account);
					editorData.putString("HEADER_SMALL", header_small);
					editorData.putString("NAME", name);
					editorData.putString("TOKEN", token);
					editorData.putString("NICKNAME", nickname);
					editorData.putString("ID", id);
					editorData.commit();
					spConfigure.edit().putBoolean("ISVISITOR", false).commit();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (spUserInfo.getBoolean("ISMODIFIED", false)) {
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(SplashActivity.this,
							ProfileActivity.class);
					startActivity(intent);
					finish();
				}
			} else {
				Toast.makeText(SplashActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};

	private void openNextpage() {
		// 判断自动登录
		String userId = spUserInfo.getString("USER_ID", "");
		String password = spUserInfo.getString("PASSWORD", "");
		if (spUserInfo.getBoolean("ISCHECK", false) && null != userId
				&& null != password) {

			LoginModelThread lmt = new LoginModelThread(userId, password,
					handlerLogin);
			lmt.start();

		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			this.finish();
		}
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
			// finish();
			// System.exit(0);
			ExitApplication.getInstance().exit();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

}
