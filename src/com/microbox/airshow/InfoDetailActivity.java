package com.microbox.airshow;

import org.json.JSONException;
import org.json.JSONObject;

import com.microbox.config.ApiUrlConfig;
import com.microbox.model.HttpGetJsonModelThread;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InfoDetailActivity extends Activity {

	private String infoId;
	private boolean hasVideo = false;
	private String videoUrl;

	ImageView videoImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_detail);
		Intent intent = getIntent();
		infoId = intent.getStringExtra("INFO_ID");
		Toast.makeText(InfoDetailActivity.this, "id:" + infoId,
				Toast.LENGTH_SHORT).show();
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
		new HttpGetJsonModelThread(infoDethandler, ApiUrlConfig.URL_GET_NEWS
				+ "/" + infoId).start();

		videoImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(videoUrl!=null){
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String type = "video/*";
					Uri uri = Uri
							.parse(videoUrl);
					//intent.setData(uri);
					intent.setDataAndType(uri, type);
					startActivity(intent);
				}
			}
		});
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
					}
					videoUrl = obj.getString("video_link");

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

}
