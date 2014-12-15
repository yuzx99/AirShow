package com.microbox.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.mircobox.airshow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CategoryListItem> mItems;
	private BitmapUtils bitmapUtils;
	
	public CategoryListAdapter(Context context, List<CategoryListItem> data) {
		this.mInflater = LayoutInflater.from(context);
		this.mItems = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public CategoryListItem getItem(int position) {
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
		CategoryListItem item = getItem(position);
		ImageView itemIcon = null;
		TextView itemTitle = null;
		TextView itemId = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.cate_item, null);
		}
		itemIcon = (ImageView)convertView.findViewById(R.id.catePicItem);
		itemTitle = (TextView)convertView.findViewById(R.id.cateTitle);
		itemId = (TextView) convertView.findViewById(R.id.cateId);
		bitmapUtils.display(itemIcon, item.getIconUrl());
		itemTitle.setText(item.getTitle());
		itemId.setText(item.getId());
		return convertView;
	}

}
