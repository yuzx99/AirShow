package com.mircobox.airshow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExhibitionFragment extends Fragment {
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
}
