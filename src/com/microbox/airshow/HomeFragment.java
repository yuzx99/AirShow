package com.microbox.airshow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.microbox.adapter.ImageItem;
import com.microbox.adapter.InfoListAdapter;
import com.microbox.adapter.InfoListItem;
import com.microbox.config.ApiUrlConfig;
import com.microbox.model.GetCategoryModelThread;
import com.microbox.model.GetDataModelThread;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.reminder.AlarmReceiver;
import com.microbox.util.Utility;
import com.mircobox.airshow.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	private ViewPager vPager = null;

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewGroup vGroup = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;

	// private ListView categoryList = null;
	private ListView infoList = null;
	private String[] infoMapping = new String[] { "infoPic", "infoTitle" };
	private int[] itemMapping = new int[] { R.id.catePicItem, R.id.cateTitle };

	List<View> lPics = null;

	private TextView noticeBoard1 = null;
	private TextView noticeBoard2 = null;
	private TextView noticeBoard3 = null;

//	private SharedPreferences spConfigure;
	private HomeCallbacks mCallbacks;

	private static final String NEW_ALARM = "com.microbox.airshow.action.NEW_ALARM";

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
//		spConfigure = getActivity().getSharedPreferences("configure",Context.MODE_PRIVATE);
		initTitleBar();
		initViewPager();
		initInfoList();
		initNoticeBoard();
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

		new HttpGetJsonModelThread(remindHandler,
				ApiUrlConfig.URL_GET_SIMPLE_CONF).start();
	}

	private final Handler remindHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			if (result != null) {
				try {
					JSONObject obj = new JSONObject(result);
					String temp = obj.getString("started_time");
					String startTime = temp.replace("T", " ");
					System.out.println(startTime);
					String conf = obj.getString("title");
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					TextView notice = (TextView) getView().findViewById(
							R.id.noticeBoard);
					try {
						Date startDate = sdf.parse(startTime);
						Date curDate = new Date(System.currentTimeMillis());
						// 24*60*60*1000 = 86400000
						long days = startDate.getTime() / 86400000
								- curDate.getTime() / 86400000;
						System.out.println(startDate);
						if (days > 0) {
							notice.setText("距" + conf + "开幕还有"
									+ String.valueOf(days) + "天");
						}
						
//						 setRepeatingAlarm();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	};

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
		vGroup = (ViewGroup) getView().findViewById(R.id.viewGroup);
		lPics = new ArrayList<View>();

		new GetDataModelThread(adHandler, ApiUrlConfig.URL_GET_AD_IMAGES)
				.start();

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
	}

	private final Handler adHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<ImageItem> imageList = new ArrayList<ImageItem>();
			if (result != null) {
				try {
					JSONArray array = new JSONArray(result);
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						String link = obj.getString("link");
						String newsId = obj.getString("news_id");
						ImageItem item = new ImageItem(newsId, link);
						imageList.add(item);
					}
					vPager.setAdapter(new AdvAdapter(imageList));

					imageViews = new ImageView[imageList.size()];
					for (int i = 0; i < imageList.size(); i++) {
						imageView = new ImageView(getActivity());
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								10, 10);
						lp.rightMargin = 15;
						imageView.setLayoutParams(lp);
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

					new Thread(new Runnable() {
						@Override
						public void run() {
							while (true) {
								if (isContinue) {
									viewHandler.sendEmptyMessage(what.get());
									// whatOption();
									what.incrementAndGet();
									if (what.get() > imageViews.length - 1) {
										what.getAndAdd(-4);
									}
									try {
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}).start();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	};

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
		private List<ImageItem> imageList = null;
		private List<View> views = null;

		public AdvAdapter(List<ImageItem> list) {
			this.imageList = list;
			BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
			views = new ArrayList<View>();
			for (int i = 0; i < imageList.size(); i++) {

				ImageView image = new ImageView(getActivity());
				bitmapUtils.display(image, imageList.get(i).getImageUrl());
				this.views.add(image);
			}
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
			View view = views.get(arg1);
			final String newsId = imageList.get(arg1).getNewsId();
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (newsId != null) {
						Intent intent = new Intent(getActivity(),
								InfoDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("INFO_ID", newsId);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			});
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

	private void initNoticeBoard() {
		noticeBoard1 = (TextView) getView().findViewById(R.id.noticeBoard1);
		noticeBoard2 = (TextView) getView().findViewById(R.id.noticeBoard2);
		noticeBoard3 = (TextView) getView().findViewById(R.id.noticeBoard3);
		new HttpGetJsonModelThread(noticeHandler, ApiUrlConfig.URL_GET_NOTICE)
				.start();
	}

	private final Handler noticeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<String> noticeList = new ArrayList<String>();
			if (result != null) {
				try {
					JSONArray array = new JSONArray(result);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String title = object.getString("title");
						noticeList.add(title);
					}
					TextView[] tvs = new TextView[] { noticeBoard1,
							noticeBoard2, noticeBoard3 };
					int noticeNumber = (noticeList.size() < 3 ? noticeList
							.size() : 3);
					for (int index = 0; index < noticeNumber; index++) {
						tvs[index].setText(noticeList.get(index));
						tvs[index].setVisibility(TextView.VISIBLE);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	};

	private void initInfoList() {

		infoList = (ListView) getView().findViewById(R.id.infoList);
		HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(infoHandler,
				ApiUrlConfig.URL_GET_SIMPLE_NEWS);
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
					int maxLength = (arr.length() > 4 ? 4 : arr.length());
					for (int i = 0; i < maxLength; i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String iconUrl = temp.getString("icon");
						String title = temp.getString("title");
						String date = temp.getString("create_time");
						String id = temp.getString("id");
						Boolean hasVideo = temp.getBoolean("has_video");
						InfoListItem ilt = new InfoListItem(iconUrl, title,
								date, id, hasVideo);
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
							TextView tvId = (TextView) arg1
									.findViewById(R.id.infoIdItem);
							Bundle bundle = new Bundle();
							bundle.putString("INFO_ID", tvId.getText()
									.toString());
							intent.putExtras(bundle);
							startActivity(intent);
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

	private void setRepeatingAlarm() {
		AlarmManager alarmManager = (AlarmManager) getActivity()
				.getSystemService(Context.ALARM_SERVICE);
		int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		long lengthofWait = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
		Intent intent = new Intent();
		intent.setAction(NEW_ALARM);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(),
				0, intent, 0);
		// alarmManager.setInexactRepeating(alarmType, lengthofWait,
		// lengthofWait,
		// alarmIntent);
		alarmManager.setRepeating(alarmType, SystemClock.elapsedRealtime(),
				10 * 1000, alarmIntent);
		// alarmManager.cancel(alarmIntent);
	}

	// private final Handler cateHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// super.handleMessage(msg);
	// Bundle data = msg.getData();
	// String result = data.getString("result");
	// List<CategoryListItem> list = new ArrayList<CategoryListItem>();
	// if (result != null) {
	// try {
	// JSONArray array = new JSONArray(result);
	// for (int i = 0; i < array.length(); i++) {
	// JSONObject temp = (JSONObject) array.get(i);
	// String title = temp.getString("title");
	// String imageurl = temp.getString("images");
	// String id = temp.getString("id");
	// CategoryListItem item = new CategoryListItem(imageurl,
	// title, id);
	// list.add(item);
	// }
	// BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
	// CategoryListAdapter adapter = new CategoryListAdapter(
	// getActivity(), list, bitmapUtils);
	// infoList.setAdapter(adapter);
	// com.microbox.util.Utility
	// .setListViewHeightBasedOnChildren(infoList);
	// infoList
	// .setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0,
	// View arg1, int arg2, long arg3) {
	// // TODO Auto-generated method stub
	// TextView tvID = (TextView)arg1.findViewById(R.id.cateId);
	// Toast.makeText(getActivity(), tvID.getText(), Toast.LENGTH_SHORT).show();
	// Intent intent = new Intent();
	// intent.setClass(getActivity(), CategoryDetailActivity.class);
	// Bundle data = new Bundle();
	// data.putString("CATE_ID", tvID.getText().toString());
	// intent.putExtras(data);
	// startActivity(intent);
	// }
	// });
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// } else {
	// Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// };

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
