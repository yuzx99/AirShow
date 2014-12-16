package com.microbox.airshow;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.adapter.CategoryListAdapter;
import com.microbox.adapter.CategoryListItem;
import com.microbox.adapter.InfoListAdapter;
import com.microbox.adapter.InfoListItem;
import com.microbox.config.ApiUrlConfig;
import com.microbox.model.GetDataModelThread;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryDetailActivity extends Activity {

	private String mCateId;
	private String mCateTitle;
	private ListView cateDetList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.category_detail);
		Bundle data = getIntent().getExtras();
		if (data != null) {
			mCateId = data.getString("CATE_ID");
		}

		cateDetList = (ListView) findViewById(R.id.cateDetList);
		 String url = "http://10.60.43.10:5000/news/topic/" + mCateId;
		new GetDataModelThread(cateDetHandler, url).start();
	}

	private final Handler cateDetHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<InfoListItem> list = new ArrayList<InfoListItem>();
			if (result != null) {
				try {
					JSONObject object = new JSONObject(result);
					JSONArray array = object.getJSONArray("newses");
					for (int i = 0; i < array.length(); i++) {
						JSONObject temp = (JSONObject) array.get(i);
						String imageurl = temp.getString("icon");
						String title = temp.getString("title");
						String date = temp.getString("create_time");
						String id = temp.getString("id");

						InfoListItem item = new InfoListItem(imageurl, title,
								date, id);
						list.add(item);
					}

					BitmapUtils bitmapUtils = new BitmapUtils(
							CategoryDetailActivity.this);
					InfoListAdapter adapter = new InfoListAdapter(
							CategoryDetailActivity.this, list, bitmapUtils);
					cateDetList.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(CategoryDetailActivity.this, "获取信息失败",
						Toast.LENGTH_SHORT).show();
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
