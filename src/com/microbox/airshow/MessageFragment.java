package com.microbox.airshow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;

import com.microbox.adapter.MessageListInfo;
import com.microbox.adapter.MessageShowListAdapter;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.config.ApiUrlConfig;
import com.microbox.util.MBDateUtils;
import com.microbox.airshow.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageFragment extends Fragment {

//	private ProgressBar progressBar;
	private ListView messageList = null;
	private int[] msgItems = new int[] { R.id.msgImageItem, R.id.msgNameItem,
			R.id.msgContentItem, R.id.msgDateItem };
	private String[] msgMapping = new String[] { "user_photo", "user_name",
			"msg_content", "msg_date" };

	private MsgCallbacks mCallbacks;

	private BitmapUtils bitmapUtils;
	private final static int LEAVE_MESSAGE = 100;

	public static Fragment newInstance(Context context) {
		MessageFragment f = new MessageFragment();

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
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
		bitmapUtils = new BitmapUtils(getActivity());
		initTitleBar();
//		initProgressBar();
		initLeaveMessage();
	}

	private void initTitleBar() {
		TextView title = (TextView) getView().findViewById(R.id.mainTitle);
		title.setText(getResources().getString(R.string.title_discuss));
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

//	private void initProgressBar(){
//		progressBar = (ProgressBar) getView().findViewById(R.id.progress);
//		progressBar.setProgress(0);
//		progressBar.setVisibility(ProgressBar.VISIBLE);
//		progressBar.setIndeterminate(true);
//	}
	
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

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LEAVE_MESSAGE && resultCode == Activity.RESULT_OK) {
			Boolean leavedMsg = data.getExtras().getBoolean("leave_message");
			if (leavedMsg) {
				HttpGetJsonModelThread gmmt = new HttpGetJsonModelThread(
						handlerMessage, ApiUrlConfig.URL_GET_MESSAGE);
				gmmt.start();
			}
		}

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
				startActivityForResult(intent, LEAVE_MESSAGE);
			}
		});

		HttpGetJsonModelThread gmmt = new HttpGetJsonModelThread(
				handlerMessage, ApiUrlConfig.URL_GET_MESSAGE);
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
						String companyName = user.getString("nickname");
						if (companyName == null || companyName.equals("")) {

						} else {
							publisher = companyName + "-" + publisher;
						}
						// long created_time = temp.getLong("created_time");
						String createdTime = temp.getString("created_time")
								.replace("T", " ");
						SimpleDateFormat sdf = new SimpleDateFormat(
								"MM/dd HH:mm");
						SimpleDateFormat dateFormate = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						// Date date = new Date(created_time);
						String dateItem = null;
						try {
							Date dateFormat = dateFormate.parse(createdTime);
							Date date = new Date(dateFormat.getTime());
							String wtoy = MBDateUtils
									.whetherTodayOrYesterday(date);

							if (wtoy != null) {
								dateItem = wtoy + " "
										+ sdf.format(date).split(" ")[1];
							} else {
								dateItem = sdf.format(date);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						MessageListInfo mli = new MessageListInfo(content,
								publisher, header_small, dateItem);
						info.add(mli);
					}
//					if(progressBar!=null){
//						progressBar.setVisibility(ProgressBar.GONE);
//					}
					
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
