package com.survivingwithandroid.weatherapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.survivingwithandroid.weatherapp.othercity.ListCity;

public class Setting extends Activity {
	/* menu */
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	ListView list;
	private EditText edit;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		edit = (EditText) findViewById(R.layout.number_forcast);
		
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_page);
		list = (ListView) findViewById(R.id.list_setting);
		list.setTextFilterEnabled(true);
		String[] listSetting = getResources().getStringArray(R.array.seting);
		ListAdapter adapter = new ArrayAdapter<String>(this,
				R.layout.text_setting, listSetting);
		list.setAdapter(adapter);
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

		/* endmenu */

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					showDialogSetNumberDay();

					break;

				default:
					break;
				}

			}
		});

	}

	public void showDialogSetNumberDay() {
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				Setting.this);
 
		alertDialog.setMessage(R.string.dialog_message).setTitle(
				R.string.dialog_title);
		LayoutInflater inflater = getLayoutInflater();

		alertDialog.setView(inflater.inflate(R.layout.number_forcast, null));
		  
		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						String number = edit.getText().toString();
						Toast.makeText(getBaseContext(), number, 4000).show();
					}
				});
 
		alertDialog.setNegativeButton("EXIT",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.cancel();
					}
				});


		alertDialog.show();
		
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
				Intent intentListLocation = new Intent(getApplicationContext(),
						ListCity.class);
				startActivity(intentListLocation);
				break;
			case 1:
				Intent intentSetting = new Intent(getApplicationContext(),
						Setting.class);
				startActivity(intentSetting);
				break;
			case 2:
				Intent intentabout = new Intent(getApplicationContext(),
						About.class);
				startActivity(intentabout);
				break;
			default:
				break;
			}

			drawerLayout.closeDrawer(drawerListView);

		}

	}

}
