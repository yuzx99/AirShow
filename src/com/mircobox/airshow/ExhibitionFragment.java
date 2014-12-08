package com.mircobox.airshow;

import com.mircobox.airshow.HomeFragment.HomeCallbacks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ExhibitionFragment extends Fragment {

	private ExhiCallbacks mCallbacks;

	public static Fragment newInstance(Context context) {
		ExhibitionFragment f = new ExhibitionFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_exhi,
				null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViewCompoent();
	}

	private void initViewCompoent() {
		ImageView drawer = (ImageView) getView().findViewById(R.id.exhiDrawer);
		drawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawerExhi();
			}
		});
	}

	public static interface ExhiCallbacks {
		public void openDrawerExhi();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (ExhiCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement ExhiCallbacks.");
		}
	}
}
