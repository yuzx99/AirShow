package com.microbox.airshow;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.microbox.config.ApiUrlConfig;
import com.microbox.model.SubmitMessageModelThread;
import com.microbox.util.MBHttpUtils;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeaveMessageActivity extends Activity {

	private SharedPreferences spData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.leave_message);
		spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);
		initViewCompoent();

	}

	//
	Handler handlerSentMessage = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			if (result != null) {
				Toast.makeText(LeaveMessageActivity.this, "提交成功",
						Toast.LENGTH_SHORT).show();
				Intent resultIntent= new Intent();
				resultIntent.putExtra("leave_message", true);
				setResult(RESULT_OK, resultIntent);
				finish();
			} else {
				Toast.makeText(LeaveMessageActivity.this, "提交留言失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void initViewCompoent() {
		TextView title = (TextView) findViewById(R.id.pageTitle);
		title.setText("撰写留言");

		RelativeLayout back = (RelativeLayout) findViewById(R.id.pageBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		final EditText etMessage = (EditText) findViewById(R.id.textContentMessage);

		Button btnSubmit = (Button) findViewById(R.id.btnSubmitMsg);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String token = spData.getString("TOKEN", "");
				String content = etMessage.getText().toString();
				if (content.isEmpty()) {
					Toast.makeText(LeaveMessageActivity.this, "留言不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					SubmitMessageModelThread smmt = new SubmitMessageModelThread(
							token, content, handlerSentMessage);
					smmt.start();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
