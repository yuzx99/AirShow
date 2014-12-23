package com.microbox.exhibition;

import com.mircobox.airshow.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IntroductionActivity extends Activity {

	private WebView wbContent;
	private SharedPreferences spData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.introduction);
		initTitleBar();
		initWebView();
	}

	private void initTitleBar() {
		RelativeLayout back = (RelativeLayout) findViewById(R.id.pageBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView title = (TextView) findViewById(R.id.pageTitle);
		title.setText(getResources().getString(R.string.title_exhi_intro));
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		wbContent = (WebView) findViewById(R.id.exhi_intro);
		spData = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		String content = spData.getString("intro_content", "");
		wbContent
				.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
		wbContent.setVerticalScrollBarEnabled(false);
		wbContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		wbContent.setScrollContainer(false);
		wbContent.setScrollbarFadingEnabled(false);
		wbContent.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		wbContent.getSettings().setDefaultTextEncodingName("UTF-8");
		wbContent.getSettings().setSupportZoom(false);

		wbContent.getSettings().setBuiltInZoomControls(true);
		wbContent.getSettings().setDisplayZoomControls(false);
		wbContent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				return false;
			}
		});
	}
}
