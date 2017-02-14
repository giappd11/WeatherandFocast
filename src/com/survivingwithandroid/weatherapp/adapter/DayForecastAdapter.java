package com.survivingwithandroid.weatherapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.survivingwithandroid.weatherapp.R;
import com.survivingwithandroid.weatherapp.model.DayForcastItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DayForecastAdapter extends ArrayAdapter<DayForcastItem> {

	Context context = null;
	ArrayList<DayForcastItem> listItem = null;
	int layoutId;

	public DayForecastAdapter(Context context,
			ArrayList<DayForcastItem> listItem) {
		super(context, R.layout.day_forcast_item, listItem);
		this.context = context;
		this.listItem = new ArrayList<DayForcastItem>(listItem);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater
				.inflate(R.layout.day_forcast_item, parent, false);
		final TextView humidity = (TextView) convertView
				.findViewById(R.id.humidity);
		humidity.setText(listItem.get(position).getHumidity());

		final TextView presure = (TextView) convertView
				.findViewById(R.id.presure);
		presure.setText(listItem.get(position).getPresure());

		final TextView location = (TextView) convertView
				.findViewById(R.id.location);
		location.setText(listItem.get(position).getLocation());

		final TextView time = (TextView) convertView.findViewById(R.id.time);
		time.setText(listItem.get(position).getTime());

		final TextView wind = (TextView) convertView.findViewById(R.id.wind);
		wind.setText(listItem.get(position).getWind());

		final TextView tempView = (TextView) convertView
				.findViewById(R.id.temprature);
		tempView.setText(listItem.get(position).getTemp());

		final TextView temp_max_min = (TextView) convertView
				.findViewById(R.id.max_min);
		temp_max_min.setText(listItem.get(position).getTemp_max_min());

		final ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

		try {
			InputStream ims = getContext().getAssets().open(
					"n" + listItem.get(position).getIcon() + ".png");
			Drawable d = Drawable.createFromStream(ims, null);
			icon.setImageDrawable(d);
		} catch (IOException ex) {

		}
		return convertView;

	}

}
