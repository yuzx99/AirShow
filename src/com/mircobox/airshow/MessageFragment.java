package com.mircobox.airshow;

import java.util.ArrayList;
import java.util.HashMap;

import com.mircobox.util.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MessageFragment extends Fragment {

	private ListView messageList = null;
	private int[] msgItems = new int[] { R.id.msgImageItem, R.id.msgNameItem,
			R.id.msgContentItem, R.id.msgDateItem };
	private String[] msgMapping = new String[] { "user_photo", "user_name",
			"msg_content", "msg_date" };

	private MsgCallbacks mCallbacks;

	public static Fragment newInstance(Context context) {
		MessageFragment f = new MessageFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_message, null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initTitleBar();
		initLeaveMessage();
	}

	private void initTitleBar() {
		TextView title = (TextView)getView().findViewById(R.id.mainTitle);
		title.setText("留言");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(R.id.mainDrawer);
		drawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawerMsg();
			}
		});
	}

	public static interface MsgCallbacks {
		public void openDrawerMsg();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (MsgCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement MsgCallbacks.");
		}
	}

	private void initLeaveMessage() {


		messageList = (ListView) getView().findViewById(R.id.msgList);
		SimpleAdapter msgAdapter = new SimpleAdapter(getActivity(),
				getLeaveMessage(), R.layout.msg_item_new, msgMapping, msgItems);
		ImageButton ibtnLeaveMsg = (ImageButton) getView().findViewById(
				R.id.ibtnLeaveMsg);
		messageList.setAdapter(msgAdapter);
		messageList.setDividerHeight(0);
		
		ibtnLeaveMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						LeaveMessageActivity.class);
				startActivity(intent);
			}
		});
	}

	private ArrayList<HashMap<String, Object>> getLeaveMessage() {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		Integer[] imgIDs = new Integer[] { R.drawable.test_pho,
				R.drawable.test_pho, R.drawable.test_pho };
		String[] itemNames = new String[] { getString(R.string.test_name),
				getString(R.string.test_name), getString(R.string.test_name) };
		String[] itemContents = new String[] {
				getString(R.string.test_msg_content),
				getString(R.string.test_msg_content),
				getString(R.string.test_msg_content) };
		String[] itemDates = new String[] { "10:10", "10:30", "11:40" };

		for (int i = 0; i < imgIDs.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(msgMapping[0], imgIDs[i]);
			map.put(msgMapping[1], itemNames[i]);
			map.put(msgMapping[2], itemContents[i]);
			map.put(msgMapping[3], itemDates[i]);
			listItem.add(map);
		}
		return listItem;
	}
}
