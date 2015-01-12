package com.microbox.airshow;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.microbox.config.ApiUrlConfig;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.airshow.R;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InfoDetailActivity extends Activity {

	private String infoId;
	private boolean hasVideo = false;
	private String videoUrl;

	ImageView videoImage;
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_detail);
		Intent intent = getIntent();
		infoId = intent.getStringExtra("INFO_ID");
		// Toast.makeText(InfoDetailActivity.this, "id:" + infoId,
		// Toast.LENGTH_SHORT).show();
		initViewCompoent();
	}

	private void initViewCompoent() {
		TextView title = (TextView) findViewById(R.id.pageTitle);
		title.setText("资讯");
		RelativeLayout back = (RelativeLayout) findViewById(R.id.pageBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		videoImage = (ImageView) findViewById(R.id.infoDetPageVideo);
		webView = (WebView) findViewById(R.id.infoDetWeb);
		new HttpGetJsonModelThread(infoDethandler, ApiUrlConfig.URL_GET_NEWS
				+ "/" + infoId).start();
	}

	private final Handler infoDethandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			if (result != null) {
				try {
					JSONObject obj = new JSONObject(result);
					hasVideo = obj.getBoolean("has_video");
					if (hasVideo) {
						videoImage.setVisibility(ImageView.VISIBLE);
						videoUrl = obj.getString("video_link");
						videoImage.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (videoUrl != null) {
									Intent intent = new Intent(
											Intent.ACTION_VIEW);
									String type = "video/*";
									Uri uri = Uri.parse(videoUrl);
									// intent.setData(uri);
									intent.setDataAndType(uri, type);
									startActivity(intent);
								}
							}
						});
					}
					String content = obj.getString("content");
					webView.loadDataWithBaseURL(null, content, "text/html",
							"utf-8", null);
					webView.setVerticalScrollBarEnabled(false);
					webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
					webView.setScrollContainer(false);
					webView.setScrollbarFadingEnabled(false);
					// webView.getSettings().setJavaScriptEnabled(true);
					// webView.setWebChromeClient(new WebChromeClient());
					webView.getSettings().setLayoutAlgorithm(
							LayoutAlgorithm.SINGLE_COLUMN);
					webView.getSettings().setDefaultTextEncodingName("UTF-8");
					// webView.getSettings().setBuiltInZoomControls(false);
					webView.getSettings().setSupportZoom(false);

					webView.getSettings().setBuiltInZoomControls(true);
					webView.getSettings().setDisplayZoomControls(false);
					webView.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							// TODO Auto-generated method stub

							return false;
						}
					});
					// cache
					// WebSettings webSettings = webView.getSettings();
					// webSettings.setAppCachePath(getContext()
					// .getApplicationContext().getCacheDir()
					// .getAbsolutePath());
					// webSettings.setAllowFileAccess(true);
					// webSettings.setAppCacheEnabled(true);
					// webSettings
					// .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
					// webSettings.setDomStorageEnabled(true);
					// webSettings.setAppCacheMaxSize(1024 * 1024 * 10);
					// webSettings.setDatabaseEnabled(true);
					// webSettings
					// .setDatabasePath(getContext()
					// .getApplicationContext()
					// .getDir("database", Context.MODE_PRIVATE)
					// .getPath());

					// webView.getSettings().setUseWideViewPort(true);
					// webSettings.setLoadWithOverviewMode(true);
					// webView.setOnTouchListener(new View.OnTouchListener() {
					// @Override
					// public boolean onTouch(View v, MotionEvent event) {
					//
					// }
					//
					// });
					// webView.getSettings().setLoadWithOverviewMode(true);
					// DisplayMetrics dm = getResources().getDisplayMetrics();
					// int scale = dm.densityDpi;
					// if (scale == 240) { //
					// webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
					// } else if (scale == 160) {
					// webView.getSettings()
					// .setDefaultZoom(ZoomDensity.MEDIUM);
					// } else {
					// webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
					// }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	protected ContextWrapper getContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
