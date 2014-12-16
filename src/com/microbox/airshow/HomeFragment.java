package com.microbox.airshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.adapter.CategoryListAdapter;
import com.microbox.adapter.CategoryListItem;
import com.microbox.adapter.InfoListAdapter;
import com.microbox.adapter.InfoListItem;
import com.microbox.config.ApiUrlConfig;
import com.microbox.model.GetCategoryModelThread;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.util.Utility;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	private ViewPager vPager = null;

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;

//	private ListView categoryList = null;
	private ListView infoList = null;
	private String[] infoMapping = new String[] { "infoPic", "infoTitle" };
	private int[] itemMapping = new int[] { R.id.catePicItem, R.id.cateTitle };

	private HomeCallbacks mCallbacks;

	public static Fragment newInstance(Context context) {
		HomeFragment f = new HomeFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home,
				null);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initTitleBar();
		initViewPager();
		initInfoList();

	}

	public void initTitleBar() {
		TextView title = (TextView) getView().findViewById(R.id.mainTitle);
		title.setText("主页");
		RelativeLayout drawer = (RelativeLayout) getView().findViewById(
				R.id.mainDrawer);
		drawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallbacks.openDrawer();
			}
		});

		TextView notice = (TextView) getView().findViewById(R.id.noticeBoard);
		notice.setText("距中国XX航空展开幕还有14天");
	}

	public static interface HomeCallbacks {
		public void openDrawer();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (HomeCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement HomeCallbacks.");
		}
	}

	public void initViewPager() {
		vPager = (ViewPager) getView().findViewById(R.id.viewpager);
		ViewGroup vGroup = (ViewGroup) getView().findViewById(R.id.viewGroup);

		List<View> lPics = new ArrayList<View>();

		Resources res = getResources();
		ImageView img1 = new ImageView(getActivity());
		img1.setBackgroundResource(R.drawable.test_pic);
		// img1.setBackgroundDrawable(compressImage(res.getDrawable(R.drawable.test_1)));
		lPics.add(img1);

		ImageView img2 = new ImageView(getActivity());
		img2.setBackgroundResource(R.drawable.test_pic);
		// img2.setBackgroundDrawable(compressImage(res.getDrawable(R.drawable.test_2)));
		lPics.add(img2);

		ImageView img3 = new ImageView(getActivity());
		img3.setBackgroundResource(R.drawable.test_pic);
		// img3.setBackgroundDrawable(compressImage(res.getDrawable(R.drawable.test_3)));
		lPics.add(img3);

		ImageView img4 = new ImageView(getActivity());
		img4.setBackgroundResource(R.drawable.test_pic);
		// img4.setBackgroundDrawable(compressImage(res.getDrawable(R.drawable.test_4)));
		lPics.add(img4);

		imageViews = new ImageView[lPics.size()];
		for (int i = 0; i < lPics.size(); i++) {
			imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new LayoutParams(10, 10));
			imageView.setPadding(10, 10, 10, 10);
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i]
						.setBackgroundResource(R.drawable.test_page_indicator_focused);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.test_page_indicator_unfocused);
			}
			vGroup.addView(imageViews[i]);
		}

		vPager.setAdapter(new AdvAdapter(lPics));
		vPager.setOnPageChangeListener(new GuidePageChangeListener());
		vPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
				}
				return false;
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						viewHandler.sendEmptyMessage(what.get());
						whatOption();
					}
				}
			}

		}).start();
	}

	private void whatOption() {
		what.incrementAndGet();
		if (what.get() > imageViews.length - 1) {
			what.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
	}

	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			vPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.test_page_indicator_focused);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.test_page_indicator_unfocused);
				}
			}

		}

	}

	private final class AdvAdapter extends PagerAdapter {
		private List<View> views = null;

		public AdvAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
	}

	private void initInfoList() {

		infoList = (ListView) getView().findViewById(R.id.infoList);
		HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(infoHandler,
				ApiUrlConfig.URL_GET_NEWS);
		hgjmt.start();
		
		ImageButton btnMoreInfo = (ImageButton) getView().findViewById(
				R.id.ibtnMoreInfo);
		btnMoreInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.container, Fragment.instantiate(getActivity(),
						"com.microbox.airshow.InfoFragment"));
				ft.commit();
			}
		});
	}

	private final Handler infoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<InfoListItem> newsList = new ArrayList<InfoListItem>();
			if (result != null) {
				try {
					JSONArray arr = new JSONArray(result);
					int maxLength = (arr.length()>4 ? 4 : arr.length());
					for (int i = 0; i < maxLength; i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String iconUrl = temp.getString("icon");
						String title = temp.getString("title");
						String date = temp.getString("create_time");
						String id = String.valueOf(i);
						InfoListItem ilt = new InfoListItem(iconUrl, title,
								date, id);
						newsList.add(ilt);
					}
					BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
					InfoListAdapter ilAdapter = new InfoListAdapter(
							getActivity(), newsList, bitmapUtils);
					infoList.setAdapter(ilAdapter);
					Utility.setListViewHeightBasedOnChildren(infoList);
					infoList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(),
									InfoDetailActivity.class);
							TextView tv = (TextView) arg1
									.findViewById(R.id.infoTitleItem);
							Toast.makeText(getActivity(), tv.getText(),
									Toast.LENGTH_LONG).show();
							// startActivity(intent);
						}
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "获取资讯失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
	
//	private final Handler cateHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			Bundle data = msg.getData();
//			String result = data.getString("result");
//			List<CategoryListItem> list = new ArrayList<CategoryListItem>();
//			if (result != null) {
//				try {
//					JSONArray array = new JSONArray(result);
//					for (int i = 0; i < array.length(); i++) {
//						JSONObject temp = (JSONObject) array.get(i);
//						String title = temp.getString("title");
//						String imageurl = temp.getString("images");
//						String id = temp.getString("id");
//						CategoryListItem item = new CategoryListItem(imageurl,
//								title, id);
//						list.add(item);
//					}
//					BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
//					CategoryListAdapter adapter = new CategoryListAdapter(
//							getActivity(), list, bitmapUtils);
//					infoList.setAdapter(adapter);
//					com.microbox.util.Utility
//							.setListViewHeightBasedOnChildren(infoList);
//					infoList
//							.setOnItemClickListener(new OnItemClickListener() {
//
//								@Override
//								public void onItemClick(AdapterView<?> arg0,
//										View arg1, int arg2, long arg3) {
//									// TODO Auto-generated method stub
//									TextView tvID = (TextView)arg1.findViewById(R.id.cateId);
//									Toast.makeText(getActivity(), tvID.getText(), Toast.LENGTH_SHORT).show();
//									Intent intent = new Intent();
//									intent.setClass(getActivity(), CategoryDetailActivity.class);
//									Bundle data = new Bundle();
//									data.putString("CATE_ID", tvID.getText().toString());
//									intent.putExtras(data);
//									startActivity(intent);
//								}
//							});
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			} else {
//				Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT)
//						.show();
//			}
//		}
//
//	};

	private ArrayList<HashMap<String, Object>> getCategory() {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		Integer[] imgIDs = new Integer[] { R.drawable.test_pho,
				R.drawable.test_pho, R.drawable.test_pho };
		String[] itemTitle = new String[] { "中国枭龙战机搏击长空", "中国八一跳伞队",
				"中国大型飞机低空飞行" };

		for (int i = 0; i < imgIDs.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(infoMapping[0], imgIDs[i]);
			map.put(infoMapping[1], itemTitle[i]);
			listItem.add(map);
		}
		return listItem;
	}
}
