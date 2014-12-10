package com.mircobox.airshow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserFragment extends Fragment {

	private UserCallbacks mCallbacks;
	
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
		initTitleBar();
	}

	private void initTitleBar(){
		TextView title = (TextView)getView().findViewById(R.id.mainTitle);
		title.setText("用户信息");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(R.id.mainDrawer);
		drawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawerUser();
			}
		});
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			mCallbacks = (UserCallbacks)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(
					"Activity must implement UserCallbacks.");
		}
	}

	public static interface UserCallbacks {
		public void openDrawerUser();
	}
}
