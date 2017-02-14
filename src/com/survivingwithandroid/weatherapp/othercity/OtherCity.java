package com.survivingwithandroid.weatherapp.othercity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.survivingwithandroid.weatherapp.About;
import com.survivingwithandroid.weatherapp.JSONWeatherParser;
import com.survivingwithandroid.weatherapp.MainActivity;
import com.survivingwithandroid.weatherapp.R;
import com.survivingwithandroid.weatherapp.Setting;
import com.survivingwithandroid.weatherapp.WeatherHttpClient;
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

public class OtherCity extends Activity {
	/* menu */
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;

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

	private String lang;
	private String city;

	public void setCity(String City) {
		this.city = city;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	private static String forecastDaysNum = "3";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		city = getIntent().getStringExtra("city");

		setContentView(R.layout.activity_main);
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

		// getLocation();

		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[] { city, lang });
		// task.execute(new String[] { latitude + "", longitude + "", lang });

		JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
		task1.execute(new String[] { city, lang, forecastDaysNum });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	public void getLocation() {
		gps = new GPSTracker(this);
		gps.getLocation();
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

			Toast.makeText(
					getApplicationContext(),
					"\n Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
		} else {
			gps.showSettingsAlert();
		}

	}

	@Override
	public void onBackPressed() {
		Intent intentmain = new Intent(getApplicationContext(),
				MainActivity.class);
		startActivity(intentmain);
		
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

				weather.iconData = ((new WeatherHttpClient())
						.getImage(weather.currentCondition.getIcon()));

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
			temp.setText(""
					+ Math.round((weather.temperature.getTemp() - 275.15)));
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
			DayForecastAdapter adapter = new DayForecastAdapter(getBaseContext(),
					list);
			listWeather.setAdapter(adapter);

		}
	}
}