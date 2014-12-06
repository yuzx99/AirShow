package com.mircobox.airshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		etUserID = (EditText) findViewById(R.id.etUserID);
		etPassword = (EditText) findViewById(R.id.etPassword);
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
		tgbtnSavePwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				tgbtnSavePwd.setChecked(arg1);
				if (arg1) {
					tgbtnSavePwd
							.setBackgroundResource(R.drawable.ic_checked_pressed);
				} else {
					tgbtnSavePwd
							.setBackgroundResource(R.drawable.ic_checked_normal);
				}
			}
		});
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isValid()) {
					Intent intent = new Intent(LoginActivity.this,
							ProfileActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	private boolean isValid() {
		return true;
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
