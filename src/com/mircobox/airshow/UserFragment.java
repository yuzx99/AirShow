package com.mircobox.airshow;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserFragment extends Fragment {

	private UserCallbacks mCallbacks;
	private SharedPreferences spUserInfo;
	private SharedPreferences spData;

	public static Fragment newInstance(Context context) {
		UserFragment f = new UserFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_user,
				null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		spUserInfo = getActivity().getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		spData = getActivity().getSharedPreferences("data",
				Context.MODE_PRIVATE);
		String result = spData.getString("RESULT", "");
		initTitleBar();
		try {
			initViewCompoent(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initTitleBar() {
		TextView title = (TextView) getView().findViewById(R.id.mainTitle);
		title.setText("用户信息");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(
				R.id.mainDrawer);
		drawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawerUser();
			}
		});
	}

	private void initViewCompoent(String result) throws JSONException {
		JSONObject jsonObject = null;
		String urlHeaderSmall = null;
		final String name;
		final String nickName;
		jsonObject = new JSONObject(result);
		urlHeaderSmall = jsonObject.getString("header_small");
		name = jsonObject.getString("name");
		nickName = jsonObject.getString("nickname");
		ImageView ivUserPhoto = (ImageView) getView().findViewById(
				R.id.userPhoto);
		BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
		bitmapUtils.display(ivUserPhoto, urlHeaderSmall);
		TextView etRealName = (TextView) getView().findViewById(
				R.id.userRealName);
		etRealName.setText(name);
		TextView etUserName = (TextView) getView().findViewById(
				R.id.userNickName);
		etUserName.setText(nickName);
		Button btnExit = (Button) getView().findViewById(R.id.userExit);
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Editor editorUserInfo = spUserInfo.edit();
				// editorUserInfo.remove("USER_ID");
				editorUserInfo.remove("PASSWORD");
				editorUserInfo.putBoolean("ISCHECK", false);
				editorUserInfo.commit();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});

		Button btnEdit = (Button) getView().findViewById(R.id.userEditProfile);
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ProfileActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallbacks = (UserCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement UserCallbacks.");
		}
	}

	public static interface UserCallbacks {
		public void openDrawerUser();
	}
}