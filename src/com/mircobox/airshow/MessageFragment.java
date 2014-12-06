package com.mircobox.airshow;

import java.util.ArrayList;
import java.util.HashMap;

import com.mircobox.util.Utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MessageFragment extends Fragment {
	
	private ListView todayMsgList = null;
	private ListView ytdMsgList = null;
	private ListView earlierMsgList = null;
	private int[] msgItems = new int[] { R.id.msgImageItem, R.id.msgNameItem,
			R.id.msgContentItem };
	private String[] msgMapping = new String[] { "user_photo", "user_name",
			"msg_content" };
    public static Fragment newInstance(Context context) {
    	MessageFragment f = new MessageFragment();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_message, null);
        return root;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	initLeaveMessage();
    }
    
    private void initLeaveMessage() {
		todayMsgList = (ListView) getView().findViewById(R.id.todayMsgList);
		ytdMsgList = (ListView) getView().findViewById(R.id.ytdMsgList);
		earlierMsgList = (ListView) getView().findViewById(R.id.earlierMsgList);
		SimpleAdapter todayAdapter = new SimpleAdapter(getActivity(), getLeaveMessage(),
				R.layout.msg_item, msgMapping, msgItems);
		todayMsgList.setAdapter(todayAdapter);
		todayMsgList.setDividerHeight(0);
		Utility.setListViewHeightBasedOnChildren(todayMsgList);
		ytdMsgList.setAdapter(todayAdapter);
		Utility.setListViewHeightBasedOnChildren(ytdMsgList);
		earlierMsgList.setAdapter(todayAdapter);
		Utility.setListViewHeightBasedOnChildren(earlierMsgList);

		ImageButton ibtnLeaveMsg = (ImageButton) getView().findViewById(R.id.ibtnLeaveMsg);
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
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < imgIDs.length; i++) {
			map.put(msgMapping[0], imgIDs[i]);
			map.put(msgMapping[1], itemNames[i]);
			map.put(msgMapping[2], itemContents[i]);
			listItem.add(map);
		}
		return listItem;
	}
}
