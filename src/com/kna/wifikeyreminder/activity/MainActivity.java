package com.kna.wifikeyreminder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kna.wifikeyreminder.R;
import com.kna.wifikeyreminder.WifiKeyReminder;
import com.kna.wifikeyreminder.adapter.WifiListAdapter;
import com.kna.wifikeyreminder.fragment.AddWifiFragment;
import com.kna.wifikeyreminder.fragment.EditWifiFragment;
import com.kna.wifikeyreminder.fragment.ViewWifiFragment;
import com.kna.wifikeyreminder.helper.ConnectionHelper;
import com.kna.wifikeyreminder.helper.WifiParserHelper;
import com.kna.wifikeyreminder.listener.CallBackEditWifiListener;
import com.kna.wifikeyreminder.listener.ChangeActionMenuListener;
import com.kna.wifikeyreminder.listener.SaveDataToCloudListener;
import com.kna.wifikeyreminder.model.User;
import com.kna.wifikeyreminder.model.Wifi;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;

public class MainActivity extends ActionBarActivity implements
		CallBackEditWifiListener, ChangeActionMenuListener,
		SaveDataToCloudListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private int indexViewWifi = -1;

	private String menuAction = "ADD";

	private SharedPreferences sharedPreferences;

	private User _user;

	private RelativeLayout relativeLayoutLoading;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ParseAnalytics.trackAppOpened(getIntent());

		sharedPreferences = getSharedPreferences("preferences",
				Context.MODE_PRIVATE);		
		
		relativeLayoutLoading = (RelativeLayout) findViewById(R.id.relativeLayoutMainLoading);

		relativeLayoutLoading.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				return;
			}
		});

		_user = WifiKeyReminder.user;

		if (_user == null)
			errorLogin();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		
		mDrawerList.setAdapter(new WifiListAdapter(WifiKeyReminder.wifis, this));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.mydrawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		loadNoneData();

	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			loadData(position);
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	private void loadNoneData() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, new AddWifiFragment())
				.commit();
	}

	private void loadData(int position) {
		if (position != -1) {
			Toast.makeText(MainActivity.this,
					WifiKeyReminder.wifis.get(position).getBssid(), Toast.LENGTH_SHORT)
					.show();

			indexViewWifi = position;

			ViewWifiFragment newFragment = new ViewWifiFragment();
			Bundle args = new Bundle();
			args.putInt(ViewWifiFragment.ARG_POSITION, position);
			newFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			transaction.replace(R.id.fragment_container, newFragment);

			// Commit the transaction
			transaction.commit();
		}

	}

	private void logout() {
		sharedPreferences.edit().clear().commit();
		unaccess();
	}

	private void unaccess() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void editWifi(Integer position) {
		Toast.makeText(this, "Edit wifi", Toast.LENGTH_SHORT).show();
		mDrawerLayout.closeDrawer(Gravity.LEFT);

		EditWifiFragment editWifiFragment = new EditWifiFragment();

		if (position != null) {
			Bundle args = new Bundle();
			args.putInt(ViewWifiFragment.ARG_POSITION, position);
			editWifiFragment.setArguments(args);
		}
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.addToBackStack(null);

		transaction.replace(R.id.fragment_container, editWifiFragment);

		// Commit the transaction
		transaction.commit();

	}

	private void errorLogin() {
		Toast.makeText(this, "User data error. Login again", Toast.LENGTH_SHORT)
				.show();
		logout();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (menuAction.equals("EDIT")) {
			getMenuInflater().inflate(R.menu.edit_menu, menu);
			return true;
		}

		if (menuAction.equals("ADD")) {
			getMenuInflater().inflate(R.menu.add_menu, menu);
			return true;
		}

		if (menuAction.equals("VIEW")) {
			getMenuInflater().inflate(R.menu.menu, menu);
			return true;
		}

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_add:
			editWifi(null);
			break;
		case R.id.action_edit:
			editWifi(indexViewWifi);
			break;
		case R.id.action_logout:
			logout();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	@Override
	public void doCallBack(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().remove(fragment)
				.commit();
		loadData(indexViewWifi);
	}

	@Override
	public void changeActionMenuOptions(String action) {
		menuAction = action;
		supportInvalidateOptionsMenu();
	}

	@Override
	public void save(String bssid, String key) {
		showLoading();
		
		Wifi wifi = new Wifi();
		wifi.setBssid(bssid);
		wifi.setKey(key);
		wifi.setIdUser(_user.getId());
		ConnectionHelper.saveWifi(wifi, this);
	}

	private void showLoading() {
		if (!relativeLayoutLoading.getViewTreeObserver().equals(View.VISIBLE)) {
			relativeLayoutLoading.setVisibility(View.VISIBLE);
		}
	}

	private void hideLoading() {
		if (!relativeLayoutLoading.getViewTreeObserver().equals(View.GONE)) {
			relativeLayoutLoading.setVisibility(View.GONE);
		}
	}

	@Override
	public void savedCallBackOK(ParseObject wifi) {
		Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

		hideLoading();
		
		//Save data
		WifiKeyReminder.wifis.add(WifiParserHelper.getWifi(wifi));

		//Datastore
		sharedPreferences.edit().putString("wifis", new Gson().toJson(WifiKeyReminder.wifis)).commit();

		//Show view fragment wifi
		loadData(WifiKeyReminder.wifis.size() - 1);

	}

	@Override
	public void savedCallBackKO(ParseException e) {
		Toast.makeText(this, "Data unsaved: " + e.getMessage(),
				Toast.LENGTH_SHORT).show();

		hideLoading();
	}

}
