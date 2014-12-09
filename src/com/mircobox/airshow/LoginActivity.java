package com.mircobox.airshow;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mircobox.config.ApiUrlConfig;
import com.mircobox.util.MBHttpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

public class LoginActivity extends Activity {
	private EditText etUserID;
	private EditText etPassword;
	private ToggleButton tgbtnShowPwd;
	private ToggleButton tgbtnSavePwd;
	private Button btnLogin;
	private SharedPreferences spUserInfo;
	private SharedPreferences spData;

	private long waitTime = 3000;
	private long touchTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		initUI();
	}

	private void initUI() {
		// 记录登录信息
		spUserInfo = this
				.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);

		etUserID = (EditText) findViewById(R.id.etUserID);
		etPassword = (EditText) findViewById(R.id.etPassword);

		etUserID.setText(spUserInfo.getString("USER_ID", ""));
		etPassword.setText(spUserInfo.getString("PASSWORD", ""));

		tgbtnShowPwd = (ToggleButton) findViewById(R.id.tgbtnShowPwd);
		tgbtnShowPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				tgbtnShowPwd.setChecked(arg1);

				if (arg1) {
					tgbtnShowPwd
							.setBackgroundResource(R.drawable.ic_input_box_visible);
					etPassword
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					tgbtnShowPwd
							.setBackgroundResource(R.drawable.ic_input_box_invisible);
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		tgbtnSavePwd = (ToggleButton) findViewById(R.id.tgbtnSavePwd);
		// 判断自动登录
		if (spUserInfo.getBoolean("ISCHECK", false)) {
			tgbtnSavePwd.setChecked(true);
			tgbtnSavePwd.setBackgroundResource(R.drawable.ic_checked_pressed);
			login(spUserInfo.getString("USER_ID", ""),
					spUserInfo.getString("PASSWORD", ""));
		}
		tgbtnSavePwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				tgbtnSavePwd.setChecked(arg1);
				if (arg1) {
					tgbtnSavePwd
							.setBackgroundResource(R.drawable.ic_checked_pressed);
					spUserInfo.edit().putBoolean("ISCHECK", true).commit();
				} else {
					tgbtnSavePwd
							.setBackgroundResource(R.drawable.ic_checked_normal);
					spUserInfo.edit().putBoolean("ISCHECK", false).commit();
				}
			}
		});
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String userId = etUserID.getText().toString();
				String password = etPassword.getText().toString();
				login(userId, password);
			}
		});
	}

	Handler handlerLogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			String userId = data.getString("userId");
			String password = data.getString("password");
			if (result != null) {
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				Editor editorUserInfo = spUserInfo.edit();
				editorUserInfo.putString("USER_ID", userId);
				editorUserInfo.putString("PASSWORD", password);
				editorUserInfo.commit();
				Editor editorData = spData.edit();
				editorData.putString("RESULT", result);
				editorData.commit();
				Intent intent = new Intent(LoginActivity.this,
						ProfileActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	private void login(final String userId, final String password) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MBHttpUtils ru = new MBHttpUtils();
					JSONObject param = new JSONObject();
					param.put("account", userId);
					param.put("password", password);
					String result = ru.restHttpPostJson(ApiUrlConfig.URL_LOGIN,
							param);
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("result", result);
					data.putString("userId", userId);
					data.putString("password", password);
					msg.setData(data);
					handlerLogin.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
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
