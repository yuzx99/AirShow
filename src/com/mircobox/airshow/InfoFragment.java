package com.mircobox.airshow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment {
	  public static Fragment newInstance(Context context) {
	    	InfoFragment f = new InfoFragment();
	 
	        return f;
	    }
	 
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_info, null);
	        return root;
	    }
}
