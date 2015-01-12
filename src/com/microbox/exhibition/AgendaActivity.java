package com.microbox.exhibition;

import com.microbox.airshow.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class AgendaActivity extends Activity {

	private WebView wbContent;
	private SharedPreferences spInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.agenda);
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
		title.setText(getResources().getString(R.string.title_exhi_agenda));
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		wbContent = (WebView) findViewById(R.id.agenda);
		spInfo = getSharedPreferences("loaded_info", Context.MODE_PRIVATE);
		String content = spInfo.getString("agenda_content", "");
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

		// wbContent.getSettings().setJavaScriptEnabled(true);
		// wbContent.addJavascriptInterface(new JavascriptInterface(this),
		// "imagelistner");
		// wbContent.setWebViewClient(new MyWebViewClient());
		wbContent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String img) {
			// Intent intent = new Intent();
			// intent.putExtra("image", img);
			// intent.setClass(context, ShowWebImageActivity.class);
			// context.startActivity(intent);
			Toast.makeText(context, "Image " + img, Toast.LENGTH_SHORT).show();
		}
	}

	private void addImageClickListner() {
		wbContent.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\"); "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(this.src);  "
				+ "    }  " + "}" + "})()");
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);

			addImageClickListner();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			return super.shouldOverrideUrlLoading(view, url);
		}
	}
}
