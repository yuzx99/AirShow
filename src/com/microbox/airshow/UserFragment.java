package com.microbox.airshow;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.airshow.R;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserFragment extends Fragment {

	private UserCallbacks mCallbacks;
	private SharedPreferences spUserInfo;
	private SharedPreferences spData;

	private ImageView ivUserPhoto;
	private TextView etRealName;
	private TextView etUserName;

	private static final int EDIT_PROFILE = 1;

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
		initTitleBar();
		initViewCompoent();
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

	private void initViewCompoent() {
		String urlHeaderSmall = spData.getString("HEADER_SMALL", "");
		String name = spData.getString("NAME", "");
		String nickName = spData.getString("NICKNAME", "");

		ivUserPhoto = (ImageView) getView().findViewById(R.id.userPhoto);
		BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
		bitmapUtils.display(ivUserPhoto, urlHeaderSmall);
		etRealName = (TextView) getView().findViewById(R.id.userRealName);
		etRealName.setText(name);
		etUserName = (TextView) getView().findViewById(R.id.userNickName);
		etUserName.setText(nickName);
		Button btnExit = (Button) getView().findViewById(R.id.userExit);
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Editor editorUserInfo = spUserInfo.edit();
				// editorUserInfo.remove("USER_ID");
				editorUserInfo.remove("PASSWORD").commit();
				editorUserInfo.putBoolean("ISCHECK", false).commit();
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
				startActivityForResult(intent, EDIT_PROFILE);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK
				&& requestCode == EDIT_PROFILE) {
			Boolean isEdit = data.getExtras().getBoolean("profile_modified");
			String newname = data.getExtras().getString("new_name");
			String nickname = data.getExtras().getString("new_nickname");
			String photourl = data.getExtras().getString("new_photo");
			if (isEdit) {
				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
				bitmapUtils.display(ivUserPhoto, photourl);
				etRealName.setText(newname);
				etUserName.setText(nickname);
				mCallbacks.updateDrawerProfile();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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

		public void updateDrawerProfile();
	}
}
