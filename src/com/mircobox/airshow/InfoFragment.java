package com.mircobox.airshow;

import java.util.ArrayList;
import java.util.HashMap;

import com.mircobox.airshow.HomeFragment.HomeCallbacks;
import com.mircobox.util.Utility;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
		initViewCompoents();
		initInfo();
	}
	
	private void initViewCompoents(){
		ImageView drawer = (ImageView)getView().findViewById(R.id.infoDrawer);
		drawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawerInfo();
			}
		});
	}
	
	public static interface InfoCallbacks{
		public void openDrawerInfo();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
    	try{
    		mCallbacks = (InfoCallbacks)activity;
    	}catch(ClassCastException e){
    		throw new ClassCastException(
					"Activity must implement InfoCallbacks.");
    	}
	}
	
	private void initInfo() {
		infoList = (ListView) getView().findViewById(R.id.infoPageList);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), getDate(),
				R.layout.info_item, infoMapping, itemMapping);
		infoList.setAdapter(adapter);
	}

	private ArrayList<HashMap<String, Object>> getDate() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		// data
		int[] imgIDs = new int[] { R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic };
		String[] titles = new String[] { "第1届中国航空航天博览会成功落幕", "第2届中国航展飞行表演时刻表",
				"第3届中国航空航天博览会成功落幕", "第4届中国航展飞行表演时刻表", "第5届中国航空航天博览会成功落幕",
				"第6届中国航展飞行表演时刻表", "第7届中国航空航天博览会成功落幕", "第8届中国航展飞行表演时刻表",
				"第9届中国航展飞行表演时刻表", "第10届中国航展飞行表演时刻表" };
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
