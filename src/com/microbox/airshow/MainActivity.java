package com.microbox.airshow;

import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.airshow.R;
import com.microbox.push.ExitApplication;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		HomeFragment.HomeCallbacks, InfoFragment.InfoCallbacks,
		ExhibitionFragment.ExhiCallbacks, MessageFragment.MsgCallbacks,
		AboutusFragment.AboutCallbacks, UserFragment.UserCallbacks {

	private SharedPreferences spData;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	final String[] fragments = new String[] {
			"com.microbox.airshow.UserFragment",
			"com.microbox.airshow.HomeFragment",
			"com.microbox.airshow.InfoFragment",
			"com.microbox.airshow.ExhibitionFragment",
			"com.microbox.airshow.MessageFragment",
			"com.microbox.airshow.AboutusFragment" };
	private long waitTime = 3000;
	private long touchTime = 0;

	// private UserFragment mUserFragment;
	// private HomeFragment mHomeFragment;
	// private InfoFragment mInfoFragment;
	// private ExhibitionFragment mExhibitionFragment;
	// private MessageFragment mMessageFragment;
	// private AboutusFragment mAboutusFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ExitApplication.getInstance().addActivity(this);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		getActionBar().hide();

		TextView tv = (TextView) findViewById(R.id.userName);
		ImageView iv = (ImageView) findViewById(R.id.headIcon);
		spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (hasAccessRight()) {
			String urlHeaderSmall = spData.getString("HEADER_SMALL", "");
			String name = spData.getString("NAME", "");
			BitmapUtils bitmapUtils = new BitmapUtils(this);
			bitmapUtils.display(iv, urlHeaderSmall);
			tv.setText(name);
		} else {
			tv.setText(getResources().getString(R.string.visitor));
			iv.setBackgroundResource(R.drawable.pic_default_face);
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		
		if (hasAccessRight()) {
			tx.replace(R.id.container, Fragment.instantiate(MainActivity.this,
					fragments[position]));
			tx.commit();
			onSectionAttached(position);
		} else {
			switch (position) {
			case 0:
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
				break;
			case 4:
				Toast toast = Toast.makeText(
						MainActivity.this,
						getResources().getString(R.string.have_no_access_right),
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				break;
			default:
				tx.replace(R.id.container, Fragment.instantiate(
						MainActivity.this, fragments[position]));
				tx.commit();
				onSectionAttached(position);
				break;
			}
		}

	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_home);
			break;
		case 2:
			mTitle = getString(R.string.title_info);
			break;
		case 3:
			mTitle = getString(R.string.title_exhi);
			break;
		case 4:
			mTitle = getString(R.string.title_msg);
			break;
		case 5:
			mTitle = getString(R.string.title_about);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	private boolean hasAccessRight() {
		boolean authorized = true;
		SharedPreferences spConfigure = this.getSharedPreferences("configure",
				Context.MODE_PRIVATE);
		SharedPreferences spUserInfo = this.getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		if (spUserInfo.getBoolean("ISCHECK", false)) {
			authorized = true;
		}else{
			if (spConfigure.getBoolean("ISVISITOR", true)) {
				authorized = false;
			}
		}

		return authorized;
	}

	// set menu disabled
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// if (!mNavigationDrawerFragment.isDrawerOpen()) {
	// // Only show items in the action bar relevant to this screen
	// // if the drawer is not showing. Otherwise, let the drawer
	// // decide what to show in the action bar.
	// getMenuInflater().inflate(R.menu.main, menu);
	// restoreActionBar();
	// return true;
	// }
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, this.getString(R.string.exit_again),
					Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			//spData.edit().clear().commit();
			SharedPreferences spConfigure = this.getSharedPreferences(
					"configure", Context.MODE_PRIVATE);
			spConfigure.edit().putBoolean("ISVISITOR", true).commit();
			// finish();
			// System.exit(0);
			ExitApplication.getInstance().exit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void openDrawer() {
		// TODO Auto-generated method stub
		mNavigationDrawerFragment.openDrawer();
	}

	@Override
	public void openDrawerInfo() {
		// TODO Auto-generated method stub
		mNavigationDrawerFragment.openDrawer();
	}

	@Override
	public void openDrawerExhi() {
		// TODO Auto-generated method stub
		mNavigationDrawerFragment.openDrawer();
	}

	@Override
	public void openDrawerMsg() {
		// TODO Auto-generated method stub
		mNavigationDrawerFragment.openDrawer();
	}

	@Override
	public void openDrawerAbout() {
		// TODO Auto-generated method stub
		mNavigationDrawerFragment.openDrawer();
	}

	@Override
	public void openDrawerUser() {
		// TODO Auto-generated method stub
		mNavigationDrawerFragment.openDrawer();
	}

	@Override
	public void updateDrawerProfile() {
		// TODO Auto-generated method stub
		spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);
		String urlHeaderSmall = spData.getString("HEADER_SMALL", "");
		String name = spData.getString("NAME", "");
		TextView tv = (TextView) findViewById(R.id.userName);
		ImageView iv = (ImageView) findViewById(R.id.headIcon);
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(iv, urlHeaderSmall);
		tv.setText(name);
	}

}
