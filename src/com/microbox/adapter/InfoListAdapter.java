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
		ImageView itemIcon = null;
		TextView itemTitle = null;
		TextView itemDate = null;
		TextView itemId = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.info_item, null);
		}
		itemIcon = (ImageView)convertView.findViewById(R.id.infoPicItem);
		itemTitle = (TextView)convertView.findViewById(R.id.infoTitleItem);
		itemDate = (TextView)convertView.findViewById(R.id.infoDateItem);
		itemId = (TextView)convertView.findViewById(R.id.infoIdItem);
		bitmapUtils.display(itemIcon, item.getIconUrl());
		itemTitle.setText(item.getTitle());
		itemDate.setText(item.getDate());
		itemId.setText(item.getId());
		return convertView;
	}

}
