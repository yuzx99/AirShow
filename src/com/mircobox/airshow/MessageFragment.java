package com.mircobox.airshow;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;

import com.microbox.adapter.MessageListInfo;
import com.microbox.adapter.MessageShowListAdapter;
import com.microbox.model.GetMessageModelThread;
import com.mircobox.config.ApiUrlConfig;
import com.mircobox.util.MBDateUtils;
import com.mircobox.util.MBHttpUtils;
import com.mircobox.util.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

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
		TextView title = (TextView) getView().findViewById(R.id.mainTitle);
		title.setText("留言");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(
				R.id.mainDrawer);
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		GetMessageModelThread gmmt = new GetMessageModelThread(handlerMessage);
		gmmt.start();
	}

	private void initLeaveMessage() {

		messageList = (ListView) getView().findViewById(R.id.msgList);

		ImageButton ibtnLeaveMsg = (ImageButton) getView().findViewById(
				R.id.ibtnLeaveMsg);

		ibtnLeaveMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						LeaveMessageActivity.class);
				startActivity(intent);
			}
		});
		GetMessageModelThread gmmt = new GetMessageModelThread(handlerMessage);
		gmmt.start();
	}

	Handler handlerMessage = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<MessageListInfo> info = new ArrayList<MessageListInfo>();
			if (result != null) {
				try {
					JSONArray arr = new JSONArray(result);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String content = temp.getString("content");
						String publisher = temp.getString("publisher");
						JSONObject user = temp.getJSONObject("user");
						String header_small = user.getString("header_small");
						long created_time = temp.getLong("created_time");
						SimpleDateFormat sdf = new SimpleDateFormat(
								"MM/dd HH:mm");
						Date date = new Date(created_time);
						String wtoy = MBDateUtils.whetherTodayOrYesterday(date);
						String dateItem;
						if (wtoy != null) {
							dateItem = wtoy + " "
									+ sdf.format(date).split(" ")[1];
						} else {
							dateItem = sdf.format(date);
						}
						MessageListInfo mli = new MessageListInfo(content,
								publisher, header_small, dateItem);
						info.add(mli);
					}
					BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
					MessageShowListAdapter mslAdapter = new MessageShowListAdapter(
							getActivity(), info, bitmapUtils);
					messageList.setAdapter(mslAdapter);
					messageList.setDividerHeight(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "获取留言失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
}
