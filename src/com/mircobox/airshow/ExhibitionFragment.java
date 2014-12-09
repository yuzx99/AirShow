package com.mircobox.airshow;

import java.util.ArrayList;
import java.util.List;

import com.mircobox.airshow.HomeFragment.HomeCallbacks;
import com.mircobox.exhibition.IntroductionFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ExhibitionFragment extends Fragment {

	private ExhiCallbacks mCallbacks;
	private List<Fragment> fragmentList;
	private List<String> titleList;

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
		initViewPager();
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

	private void initViewPager() {
		ViewPager vp = (ViewPager) getView().findViewById(R.id.exhiPager);
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new IntroductionFragment());
		fragmentList.add(new IntroductionFragment());
		fragmentList.add(new IntroductionFragment());
		fragmentList.add(new IntroductionFragment());
		titleList = new ArrayList<String>();
		titleList.add("会展介绍");
		titleList.add("交通住宿");
		titleList.add("会展介绍1");
		titleList.add("交通住宿2");
		vp.setAdapter(new SectionPagerAdapter(getFragmentManager(),
				fragmentList, titleList));
	}

	public class SectionPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragments;
		private List<String> titles;

		public SectionPagerAdapter(FragmentManager fm,
				List<Fragment> fragmentList, List<String> titleList) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.fragments = fragmentList;
			this.titles = titleList;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return (fragments == null || fragments.size() == 0) ? null : fragments.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return (fragments == null ? 0 : fragments.size()); 
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return (titles.size() > position) ? titles.get(position) : "";
		}
	}
}
