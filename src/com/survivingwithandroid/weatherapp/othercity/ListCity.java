package com.survivingwithandroid.weatherapp.othercity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.survivingwithandroid.weatherapp.About;
import com.survivingwithandroid.weatherapp.R;
import com.survivingwithandroid.weatherapp.Setting;

public class ListCity extends Activity {
	/* menu */
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	ListView listView;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_city);
		listView = (ListView) findViewById(R.id.listcity);
		listView.setTextFilterEnabled(true);
		String[] listCity = getResources().getStringArray(R.array.location);
		ListAdapter adapter = new ArrayAdapter<String>(this,
				R.layout.text_city, listCity);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String value = (String) ((TextView) view).getText();
				OtherCity other = new OtherCity();
				Intent intent = new Intent(getApplicationContext(),
						OtherCity.class);
				Toast.makeText(getApplicationContext(), value, 4000).show();
				intent.putExtra("city", value);
				startActivity(intent);

			}
		});

		/* menu */
		drawerListViewItems = getResources().getStringArray(R.array.items);
		drawerListView = (ListView) findViewById(R.id.left_drawer);
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_listview_item, drawerListViewItems));
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close); 
		drawerLayout.setDrawerListener(actionBarDrawerToggle); 
		getActionBar().setDisplayHomeAsUpEnabled(true); 
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START); 
		drawerListView.setOnItemClickListener(new DrawerItemClickListener()); 
		
		/*endmenu*/
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	public class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				Intent intentListLocation = new Intent(getBaseContext(),
						ListCity.class);
				startActivity(intentListLocation);
				break;
			case 1:
				Intent intentSetting = new Intent(getBaseContext(), Setting.class);
				startActivity(intentSetting);
				break;
			case 2:
				Intent intentabout = new Intent(getBaseContext(), About.class);
				startActivity(intentabout);
				break;
			default:
				break;
			}

			drawerLayout.closeDrawer(drawerListView);

		}

	}
}
