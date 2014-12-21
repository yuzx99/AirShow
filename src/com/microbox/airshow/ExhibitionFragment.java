package com.microbox.airshow;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.microbox.airshow.HomeFragment.HomeCallbacks;
import com.microbox.config.ApiUrlConfig;
import com.microbox.exhibition.AgendaFragment;
import com.microbox.exhibition.ExhibitionLayoutFragment;
import com.microbox.exhibition.IntroductionFragment;
import com.microbox.exhibition.ReportFragment;
import com.microbox.exhibition.SponsorFragment;
import com.microbox.exhibition.TransportationFragment;
import com.microbox.model.HttpGetJsonModelThread;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExhibitionFragment extends Fragment {

	private ExhiCallbacks mCallbacks;
	private List<Fragment> fragmentList;
	private List<String> titleList;
	private SharedPreferences spData;

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

	Handler handlerContent = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			try {
				JSONObject joObject = new JSONObject(result);
				String intro_content = joObject.getString("intro_content");
				String logistics_content = joObject
						.getString("logistics_content");
				String group_content = joObject.getString("group_content");
				String agenda_content = joObject.getString("agenda_content");
				String layout_content = joObject.getString("layout_content");
				Editor editorData = spData.edit();
				editorData.putString("intro_content", intro_content);
				editorData.putString("logistics_content", logistics_content);
				editorData.putString("group_content", group_content);
				editorData.putString("agenda_content", agenda_content);
				editorData.putString("layout_content", layout_content);
				editorData.putBoolean("is_loaded", true);
				editorData.commit();
				initTitleBar();
				initViewPager();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		spData = getActivity().getSharedPreferences("data",
				Context.MODE_PRIVATE);
		if (spData.getBoolean("is_loaded", false)) {
			initTitleBar();
			initViewPager();
		} else {
			HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(
					handlerContent, ApiUrlConfig.URL_CONFERENCE_CONTENT);
			hgjmt.start();
		}
	}

	private void initTitleBar() {
		TextView title = (TextView) getView().findViewById(R.id.mainTitle);
		title.setText("第十届中国航空博览会");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(
				R.id.mainDrawer);
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
		PagerTabStrip pagerTabStrip = (PagerTabStrip) getView().findViewById(
				R.id.exhiPagerTab);
		pagerTabStrip.setTextColor(getResources().getColor(R.color.white));
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(
				R.color.title_bar_color));

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new IntroductionFragment());
		fragmentList.add(new TransportationFragment());
		fragmentList.add(new SponsorFragment());
		fragmentList.add(new AgendaFragment());
		fragmentList.add(new ExhibitionLayoutFragment());
		fragmentList.add(new ReportFragment());

		titleList = new ArrayList<String>();
		String[] titles = getResources().getStringArray(
				R.array.exhi_pager_title);
		for (int i = 0; i < titles.length; i++) {
			titleList.add(titles[i]);
		}
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
			return (fragments == null || fragments.size() == 0) ? null
					: fragments.get(arg0);
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
