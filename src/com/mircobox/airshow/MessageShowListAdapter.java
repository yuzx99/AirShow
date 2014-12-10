package com.mircobox.airshow;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.model.MessageListInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageShowListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MessageListInfo> mItems;
	private BitmapUtils bitmapUtils;

	public MessageShowListAdapter(Context context, List<MessageListInfo> data,
			BitmapUtils bitmapUtils) {
		this.mInflater = LayoutInflater.from(context);
		this.mItems = data;
		this.bitmapUtils = bitmapUtils;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
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
		MessageListInfo item = (MessageListInfo) getItem(position);
		ImageView itemIcon = null;
		TextView itemName = null;
		TextView itemMessage = null;
		TextView itemDate = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.msg_item_new, null);
		}

		itemIcon = (ImageView) convertView.findViewById(R.id.msgImageItem);
		itemName = (TextView) convertView.findViewById(R.id.msgNameItem);
		itemMessage = (TextView) convertView.findViewById(R.id.msgContentItem);
		itemDate = (TextView) convertView.findViewById(R.id.msgDateItem);
		bitmapUtils.display(itemIcon, item.getHeader_small());
		itemName.setText(item.getPublisher());
		itemMessage.setText(item.getContent());
		itemDate.setText(item.getDateItem());
		return convertView;
	}
}
