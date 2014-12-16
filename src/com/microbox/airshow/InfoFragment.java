package com.microbox.airshow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.adapter.InfoListAdapter;
import com.microbox.adapter.InfoListItem;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.airshow.HomeFragment.HomeCallbacks;
import com.microbox.config.ApiUrlConfig;
import com.microbox.util.Utility;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class InfoFragment extends Fragment {
	private ListView infoList = null;
	private String[] infoMapping = new String[] { "infoPic", "infoTitle",
			"infoDate" };
	private int[] itemMapping = new int[] { R.id.infoPicItem,
			R.id.infoTitleItem, R.id.infoDateItem };

	private InfoCallbacks mCallbacks;

	public static Fragment newInstance(Context context) {
		InfoFragment f = new InfoFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_info,
				null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initTitleBar();
		initInfo();
	}

	private void initTitleBar() {
		TextView title = (TextView) getView().findViewById(R.id.mainTitle);
		title.setText("资讯");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(
				R.id.mainDrawer);
		drawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawerInfo();
			}
		});
	}

	public static interface InfoCallbacks {
		public void openDrawerInfo();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (InfoCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement InfoCallbacks.");
		}
	}

	Handler handlerNews = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<InfoListItem> newsList = new ArrayList<InfoListItem>();
			if (result != null) {
				try {
					JSONArray arr = new JSONArray(result);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String iconUrl = temp.getString("icon");
						String title = temp.getString("title");
						String date = temp.getString("create_time");
						String id = temp.getString("id");
						InfoListItem ilt = new InfoListItem(iconUrl, title,
								date, id);
						newsList.add(ilt);
					}
					BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
					InfoListAdapter ilAdapter = new InfoListAdapter(
							getActivity(), newsList, bitmapUtils);
					infoList.setAdapter(ilAdapter);
					infoList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(),
									InfoDetailActivity.class);
							TextView tvId = (TextView) arg1
									.findViewById(R.id.infoIdItem);
							Bundle bundle = new Bundle();
							bundle.putString("id", tvId.getText().toString());
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "获取资讯失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	private void initInfo() {
		infoList = (ListView) getView().findViewById(R.id.infoPageList);
		HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(handlerNews,
				ApiUrlConfig.URL_GET_NEWS);
		hgjmt.start();
	}

	private ArrayList<HashMap<String, Object>> getDate() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		// data
		int[] imgIDs = new int[] { R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic };
		String[] titles = new String[] { "第1届中国航空航天博览会", "第2届中国航空航天博览会",
				"第3届中国航空航天博览会", "第4届中国航空航天博览会", "第5届中国航空航天博览会", "第6届中国航空航天博览会",
				"第7届中国航空航天博览会", "第8届中国航空航天博览会", "第9届中国航空航天博览会", "第10届中国航空航天博览会" };
		String[] dates = new String[] { "2014-11-9", "2014-11-8", "2014-11-9",
				"2014-11-8", "2014-11-9", "2014-11-8", "2014-11-9",
				"2014-11-8", "2014-11-8", "2014-11-8" };
		for (int i = 0; i < imgIDs.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(infoMapping[0], imgIDs[i]);
			map.put(infoMapping[1], titles[i]);
			map.put(infoMapping[2], dates[i]);
			list.add(map);
		}
		return list;
	}
}
