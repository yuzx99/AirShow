package com.microbox.exhibition;

import java.util.ArrayList;
import java.util.HashMap;

import com.mircobox.airshow.R;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ReportFragment extends Fragment {
	private String[] infoMapping = new String[] { "reportTitle" };
	private int[] itemMapping = new int[] { R.id.reportTitle };

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

	private void initList() {
		ListView reportList = (ListView) getView()
				.findViewById(R.id.reportList);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), getDate(),
				R.layout.report_item, infoMapping, itemMapping);
		reportList.setAdapter(adapter);
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
