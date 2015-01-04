package com.microbox.airshow;

import com.microbox.airshow.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutusFragment extends Fragment {

	private AboutCallbacks mCallbacks;

	public static Fragment newInstance(Context context) {
		AboutusFragment f = new AboutusFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_about,
				null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initTitleBar();
	}

	private void initTitleBar() {
		TextView title = (TextView)getView().findViewById(R.id.mainTitle);
		title.setText("关于我们");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(R.id.mainDrawer);
		drawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawerAbout();
			}
		});
	}

	public static interface AboutCallbacks {
		public void openDrawerAbout();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mCallbacks = (AboutCallbacks)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(
					"Activity must implement AboutCallbacks.");
		}
	}
}
