package com.microbox.adapter;

import java.util.List;

import com.microbox.airshow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ReportListItem> mItems;

	public ReportListAdapter(Context context, List<ReportListItem> data) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.mItems = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public ReportListItem getItem(int position) {
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
		ReportListItem item = getItem(position);
		TextView itemTitle = null;
		TextView itemUrl = null;
		TextView itemId = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.report_item, null);
		}
		itemTitle = (TextView) convertView.findViewById(R.id.reportTitle);
		itemId = (TextView) convertView.findViewById(R.id.reportIdItem);
		itemUrl = (TextView) convertView.findViewById(R.id.reportUrlItem);
		itemTitle.setText(item.getTitle());
		itemId.setText(item.getId());
		itemUrl.setText(item.getUrl());
		return convertView;
	}

}
