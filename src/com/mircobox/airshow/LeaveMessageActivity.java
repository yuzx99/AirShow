package com.mircobox.airshow;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.microbox.model.SubmitMessageModelThread;
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
		final String result = spData.getString("RESULT", "");
		Button btnSubmit = (Button) findViewById(R.id.btnSubmitMsg);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject(result);
					final String token = jsonObject.getString("token");
					final String content = etMessage.getText().toString();
					if (content.isEmpty()) {
						Toast.makeText(LeaveMessageActivity.this, "留言不能为空",
								Toast.LENGTH_SHORT).show();
					} else {
						SubmitMessageModelThread smmt = new SubmitMessageModelThread(
								token, content, handlerSentMessage);
						smmt.start();
					}

					// new Thread(new Runnable() {
					// @Override
					// public void run() {
					// try {
					// MBHttpUtils ru = new MBHttpUtils();
					// JSONObject param = new JSONObject();
					// param.put("token", token);
					// param.put("content", content);
					// String result = ru.restHttpPostJson(
					// ApiUrlConfig.URL_SEND_MESSAGE, param);
					// Message msg = new Message();
					// Bundle data = new Bundle();
					// data.putString("result", result);
					// msg.setData(data);
					// handlerSentMessage.sendMessage(msg);
					// } catch (JSONException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// } catch (ClientProtocolException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					// }).start();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
