package com.microbox.airshow;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.microbox.model.LoginModelThread;
import com.microbox.push.ExitApplication;
import com.microbox.airshow.R;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
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
	private SharedPreferences spConfigure;

	private Boolean isAutoLogin = false;
	private long waitTime = 3000;
	private long touchTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		ExitApplication.getInstance().addActivity(this);
		initUI();
	}

	private void initUI() {
		// 记录登录信息
		spUserInfo = this
				.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);
		spConfigure = this.getSharedPreferences("configure",
				Context.MODE_PRIVATE);

		etUserID = (EditText) findViewById(R.id.etUserID);
		etPassword = (EditText) findViewById(R.id.etPassword);

		etUserID.setText(spUserInfo.getString("USER_ID", ""));
		// etPassword.setText(spUserInfo.getString("PASSWORD", ""));

		isAutoLogin = spUserInfo.getBoolean("ISCHECK", false);
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
		tgbtnSavePwd.setChecked(false);
		tgbtnSavePwd.setBackgroundResource(R.drawable.ic_unchecked_normal);
		tgbtnSavePwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				tgbtnSavePwd.setChecked(arg1);
				if (arg1) {
					tgbtnSavePwd
							.setBackgroundResource(R.drawable.ic_checked_normal);
					//spUserInfo.edit().putBoolean("ISCHECK", true).commit();
					isAutoLogin = true;
				} else {
					tgbtnSavePwd
							.setBackgroundResource(R.drawable.ic_unchecked_normal);
					//spUserInfo.edit().putBoolean("ISCHECK", false).commit();
					isAutoLogin = false;
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

		TextView visitor = (TextView) findViewById(R.id.visitorLogin);
		visitor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this,
						getResources().getString(R.string.login_as_visitor),
						Toast.LENGTH_SHORT).show();
				spConfigure.edit().putBoolean("ISVISITOR", true).commit();
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	// 登录后跳转
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
				spConfigure.edit().putBoolean("ISVISITOR", false).commit();
				spUserInfo.edit().putBoolean("ISCHECK", isAutoLogin).commit();
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
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (spUserInfo.getBoolean("ISMODIFIED", false)) {
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(LoginActivity.this,
							ProfileActivity.class);
					startActivity(intent);
					finish();
				}
			} else {
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	private void login(final String userId, final String password) {
		LoginModelThread lmt = new LoginModelThread(userId, password,
				handlerLogin);
		lmt.start();
	}

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, this.getString(R.string.exit_again),
					Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
//			finish();
//			System.exit(0);
			ExitApplication.getInstance().exit();
		}
	}
}
