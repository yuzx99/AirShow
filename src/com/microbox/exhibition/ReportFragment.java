package com.microbox.exhibition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.adapter.InfoListAdapter;
import com.microbox.adapter.InfoListItem;
import com.microbox.adapter.ReportListAdapter;
import com.microbox.adapter.ReportListItem;
import com.microbox.airshow.InfoDetailActivity;
import com.microbox.config.ApiUrlConfig;
import com.microbox.model.HttpGetJsonModelThread;
import com.mircobox.airshow.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReportFragment extends Fragment {
	private String[] infoMapping = new String[] { "reportTitle" };
	private int[] itemMapping = new int[] { R.id.reportTitle };
	private ListView reportList;

	public Fragment newInstance(Context context) {
		ReportFragment f = new ReportFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.report, null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initList();
	}

	Handler handlerReport = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<ReportListItem> reportsList = new ArrayList<ReportListItem>();
			if (result != null) {
				try {
					JSONArray arr = new JSONArray(result);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String file_path = temp.getString("file_path");
						String file_name = temp.getString("file_name");
						String id = temp.getString("id");
						ReportListItem rlt = new ReportListItem(file_name, id,
								file_path);
						reportsList.add(rlt);
					}
					ReportListAdapter rlAdapter = new ReportListAdapter(
							getActivity(), reportsList);
					reportList.setAdapter(rlAdapter);
					reportList
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									TextView tvTitle = (TextView) view
											.findViewById(R.id.reportTitle);
									Toast.makeText(
											getActivity(),
											"Title:"
													+ tvTitle.getText()
															.toString(),
											Toast.LENGTH_SHORT).show();
								}

							});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "获取pdf列表失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	private void initList() {
		reportList = (ListView) getView().findViewById(R.id.reportList);
		HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(
				handlerReport, ApiUrlConfig.URL_GET_REPORT);
		hgjmt.start();

	}

	private ArrayList<HashMap<String, Object>> getDate() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		// data
		String[] titles = new String[] { "第1届中国航空航天博览会", "第2届中国航空航天博览会",
				"第3届中国航空航天博览会", "第4届中国航空航天博览会", "第5届中国航空航天博览会", "第6届中国航空航天博览会",
				"第7届中国航空航天博览会", "第8届中国航空航天博览会", "第9届中国航空航天博览会", "第10届中国航空航天博览会" };

		for (int i = 0; i < titles.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(infoMapping[0], titles[i]);
			list.add(map);
		}
		return list;
	}
}
