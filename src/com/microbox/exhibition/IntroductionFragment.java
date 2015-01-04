package com.microbox.exhibition;

import org.json.JSONException;
import org.json.JSONObject;

import com.microbox.airshow.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;

public class IntroductionFragment extends Fragment {
	private WebView wbContent;
	private SharedPreferences spData;

	public Fragment newInstance(Context context) {
		IntroductionFragment f = new IntroductionFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.introduction,
				null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initWebView();
	}

	Handler handlerContent = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			try {
				JSONObject joObject = new JSONObject(result);
				String content = joObject.getString("intro_content");
				wbContent.loadDataWithBaseURL(null, content, "text/html",
						"utf-8", null);
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
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private void initWebView() {
		// TODO Auto-generated method stub
		wbContent = (WebView) getView().findViewById(R.id.exhi_intro);
		spData = getActivity().getSharedPreferences("data",
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
		// HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(
		// handlerContent, ApiUrlConfig.URL_CONFERENCE_CONTENT);
		// hgjmt.start();
	}

}
