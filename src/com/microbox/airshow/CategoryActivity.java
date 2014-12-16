package com.microbox.airshow;

import java.util.ArrayList;
import java.util.HashMap;

import com.microbox.util.Utility;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends Activity {

	private ListView cateInfoList = null;
	private String[] infoMapping = new String[] { "infoPic", "infoTitle",
			"infoDate" };
	private int[] itemMapping = new int[] { R.id.infoPicItem,
			R.id.infoTitleItem, R.id.infoDateItem };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.category);
		initTitleBar();
		initInfo();
	}

	private void initTitleBar(){
		TextView title = (TextView)findViewById(R.id.pageTitle);
		title.setText("专题");
		RelativeLayout back = (RelativeLayout) findViewById(R.id.pageBack);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void initInfo() {
		cateInfoList = (ListView) findViewById(R.id.cateInfoList);
		SimpleAdapter adapter = new SimpleAdapter(this, getDate(),
				R.layout.info_item, infoMapping, itemMapping);
		cateInfoList.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(cateInfoList);

	}

	private ArrayList<HashMap<String, Object>> getDate() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		// data
		int[] imgIDs = new int[] { R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic,
				R.drawable.test_pic, R.drawable.test_pic };
		String[] titles = new String[] { "第十届中国航空展成功落幕", "第十届中国航空展成功落幕",
				"第十届中国航空展成功落幕", "第十届中国航空展成功落幕", "第十届中国航空展成功落幕", "第十届中国航空展成功落幕",
				"第十届中国航空展成功落幕", "第十届中国航空展成功落幕", "第十届中国航空展成功落幕", "第十届中国航空展成功落幕" };
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

	@Override
	public void onBackPressed() {
		finish();
	}
}
