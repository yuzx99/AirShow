package com.microbox.airshow;

import com.mircobox.airshow.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class CategoryDetailActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.category_detail);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
