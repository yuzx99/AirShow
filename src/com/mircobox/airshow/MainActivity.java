package com.mircobox.airshow;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		HomeFragment.HomeCallbacks,
		InfoFragment.InfoCallbacks,
		ExhibitionFragment.ExhiCallbacks,
		MessageFragment.MsgCallbacks,
		AboutusFragment.AboutCallbacks,
		UserFragment.UserCallbacks{

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
			"com.mircobox.airshow.UserFragment",
			"com.mircobox.airshow.HomeFragment",
			"com.mircobox.airshow.InfoFragment",
			"com.mircobox.airshow.ExhibitionFragment",
			"com.mircobox.airshow.MessageFragment",
			"com.mircobox.airshow.AboutusFragment" };
	private long waitTime = 3000;
	private long touchTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		getActionBar().hide();

		TextView tv = (TextView) findViewById(R.id.userName);
		if (tv != null) {
			tv.setText(mTitle);
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		// FragmentManager fragmentManager = getSupportFragmentManager();
		// fragmentManager
		// .beginTransaction()
		// .replace(R.id.container,
		// PlaceholderFragment.newInstance(position + 1)).commit();

		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.container,
				Fragment.instantiate(MainActivity.this, fragments[position]));
		tx.commit();
		onSectionAttached(position);

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
//  set menu disabled 
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		if (!mNavigationDrawerFragment.isDrawerOpen()) {
//			// Only show items in the action bar relevant to this screen
//			// if the drawer is not showing. Otherwise, let the drawer
//			// decide what to show in the action bar.
//			getMenuInflater().inflate(R.menu.main, menu);
//			restoreActionBar();
//			return true;
//		}
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, this.getString(R.string.exit_again),
					Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			finish();
			System.exit(0);
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

}
