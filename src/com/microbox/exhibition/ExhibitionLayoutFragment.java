package com.microbox.exhibition;

import com.mircobox.airshow.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExhibitionLayoutFragment extends Fragment {
	public  Fragment newInstance(Context context) {
		ExhibitionLayoutFragment f = new ExhibitionLayoutFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.exhibition_layout,
				null);
		return root;
	}
}
