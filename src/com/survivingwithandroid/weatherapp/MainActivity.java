package com.survivingwithandroid.weatherapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.survivingwithandroid.weatherapp.adapter.DayForecastAdapter;
import com.survivingwithandroid.weatherapp.model.DayForcastItem;
import com.survivingwithandroid.weatherapp.model.ListDayForcastItem;
import com.survivingwithandroid.weatherapp.model.Weather;
import com.survivingwithandroid.weatherapp.othercity.ListCity;
import com.survivingwithandroid.weatherapp.update.GPSTracker;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

	/* menu */
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;

	private DayForecastAdapter adapter;
	private GPSTracker gps;
	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	private TextView unitTemp;

	private ListView listWeather;
	private TextView hum;
	private ImageView imgView;

	private double latitude;
	private double longitude;
	private String adress;
	private ArrayList<ListDayForcastItem> listfocast;

	private String forecastDaysNum;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		forecastDaysNum = getResources().getString(R.string.number_day);
		setContentView(R.layout.activity_main);

		String lang = "en";
		listWeather = (ListView) findViewById(R.id.listWeather);
		listWeather.setTextFilterEnabled(true);
		cityText = (TextView) findViewById(R.id.cityText);
		temp = (TextView) findViewById(R.id.temprature);
		unitTemp = (TextView) findViewById(R.id.unittemp);
		unitTemp.setText("�C");
		condDescr = (TextView) findViewById(R.id.skydesc);
		imgView = (ImageView) findViewById(R.id.condIcon);

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

		getLocation();

		if (getStatusInternet()) {
			JSONWeatherTask task = new JSONWeatherTask();
			task.execute(new String[] { adress, lang });

			JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
			task1.execute(new String[] { adress, lang, forecastDaysNum });

		}
	}

	public boolean getStatusInternet() {
		ConnectivityManager connec = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return wifi.isConnected() || mobile.isConnected();
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
			case 3:
				Intent intenAchart = new Intent(getApplicationContext(),
						Achart.class);
				startActivity(intenAchart);
				break;
			default:
				break;
			}

			drawerLayout.closeDrawer(drawerListView);

		}

	}

	public void getLocation() {

		ConnectivityManager connec = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		gps = new GPSTracker(this);
		gps.getLocation();
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			getCityAndCountry(latitude, longitude);

		} else {
			gps.showSettingsAlert();
		}
		if (wifi.isConnected() && mobile.isConnected()) {

			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					getApplicationContext());
			builder1.setMessage("Network not Availble");
			builder1.setCancelable(true);
			builder1.setNegativeButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			builder1.setNeutralButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			AlertDialog alert11 = builder1.create();
			alert11.show();

		}

	}

	public void getCityAndCountry(double lat, double lon) {

		Geocoder geoCoder = new Geocoder(getApplicationContext(),
				Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(lat, lon, 1);

			if (addresses.size() > 0) {
				adress = addresses.get(0).getAdminArea() + ","
						+ addresses.get(0).getCountryCode();

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(getApplicationContext(), "resum", 4000).show();

	}

	@Override
	protected void onRestart() {
		super.onRestart();

		Toast.makeText(getApplicationContext(), "restart", 4000).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.showlistcity:
			Intent intentnext = new Intent(this, ListCity.class);
			startActivity(intentnext);
			break;
		default:
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();

			String data = ((new WeatherHttpClient()).getWeatherData(params[0],
					params[1]));

			try {
				weather = JSONWeatherParser.getWeather(data);
				System.out.println("Weather [" + weather + "]");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return weather;

		}

		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);
			try {
				InputStream ims = getAssets().open(
						"n" + weather.currentCondition.getIcon() + ".png");
				Drawable d = Drawable.createFromStream(ims, null);
				imgView.setImageDrawable(d);
			} catch (IOException ex) {
				return;
			}

			cityText.setText(weather.location.getCity() + ","
					+ weather.location.getCountry());
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273)));
			condDescr.setText(weather.currentCondition.getCondition() + "("
					+ weather.currentCondition.getDescr() + ")");

		}
	}

	private class JSONForecastWeatherTask extends
			AsyncTask<String, Void, ListDayForcastItem> {
		@Override
		protected ListDayForcastItem doInBackground(String... params) {

			String data = ((new WeatherHttpClient()).getForecastWeatherData(
					params[0], params[1], params[2]));
			Log.d("focast", data);
			ListDayForcastItem forecast = new ListDayForcastItem();
			try {
				forecast = JSONWeatherParser.getForecastWeather(data);
				System.out.println("Weather [" + forecast + "]");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return forecast;

		}

		@Override
		protected void onPostExecute(ListDayForcastItem forecastWeather) {
			super.onPostExecute(forecastWeather);

			ArrayList<DayForcastItem> list = new ArrayList<DayForcastItem>();
			for (int i = 0; i < forecastWeather.countCast(); i++) {
				list.add(forecastWeather.getFocast(i));
			}

			adapter = new DayForecastAdapter(getBaseContext(), list);
			listWeather.setAdapter(adapter);

		}

	}
}