package com.microbox.exhibition;

import com.mircobox.airshow.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroductionFragment extends Fragment {
	public  Fragment newInstance(Context context) {
		IntroductionFragment f = new IntroductionFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.introduction,
				null);
		return root;
	}
}