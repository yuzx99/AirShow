package com.microbox.airshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.adapter.InfoListAdapter;
import com.microbox.adapter.InfoListItem;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.util.Utility;
import com.microbox.config.ApiUrlConfig;
import com.microbox.airshow.R;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InfoFragment extends Fragment {
	private ListView infoList = null;
	private List<InfoListItem> newsList;
	private InfoListAdapter ilAdapter;
	private String[] infoMapping = new String[] { "infoPic", "infoTitle",
			"infoDate" };
	private int[] itemMapping = new int[] { R.id.infoPicItem,
			R.id.infoTitleItem, R.id.infoDateItem };

	private InfoCallbacks mCallbacks;

	private BitmapUtils bitmapUtils;

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
		bitmapUtils = new BitmapUtils(getActivity());
		initTitleBar();
		initInfo();
	}

	private void initTitleBar() {
		TextView title = (TextView) getView().findViewById(R.id.mainTitle);
		title.setText(getActivity().getResources().getString(
				R.string.title_info));
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
			if (result != null) {
				newsList.clear();
				try {
					JSONArray arr = new JSONArray(result);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String iconUrl = temp.getString("icon");
						String title = temp.getString("title");
						String date = temp.getString("create_time").replace(
								"T", " ");
						String id = temp.getString("id");
						Boolean hasVideo = temp.getBoolean("has_video");
						InfoListItem ilt = new InfoListItem(iconUrl, title,
								date, id, hasVideo);
						newsList.add(ilt);
					}
					ilAdapter.notifyDataSetChanged();
					Utility.setListViewHeightBasedOnChildren(infoList);
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
		newsList = new ArrayList<InfoListItem>();
		ilAdapter = new InfoListAdapter(getActivity(), newsList, bitmapUtils);
		infoList.setAdapter(ilAdapter);
		infoList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						InfoDetailActivity.class);
				TextView tvId = (TextView) arg1.findViewById(R.id.infoIdItem);
				Bundle bundle = new Bundle();
				bundle.putString("INFO_ID", tvId.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(handlerNews,
				ApiUrlConfig.URL_GET_SIMPLE_NEWS);
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
