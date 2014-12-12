package com.microbox.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.mircobox.airshow.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<InfoListItem> mItems;
	private BitmapUtils bitmapUtils;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public InfoListItem getItem(int position) {
		// TODO Auto-generated method stub
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		InfoListItem item = getItem(position);
		ImageView icon = null;
		TextView title = null;
		TextView date = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.info_item, null);
		}
		icon = (ImageView)convertView.findViewById(R.id.infoPicItem);
		title = (TextView)convertView.findViewById(R.id.infoTitleItem);
		date = (TextView)convertView.findViewById(R.id.infoDateItem);
		bitmapUtils.display(icon, item.getIconUrl());
		title.setText(item.getTitle());
		date.setText(item.getDate());
		return convertView;
	}

}
