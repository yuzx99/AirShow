package com.mircobox.airshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class LeaveMessageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.leave_message);

		Button btnSubmit = (Button) findViewById(R.id.btnSubmitMsg);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(LeaveMessageActivity.this,
//						LoginActivity.class);
//				startActivity(intent);
//				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
