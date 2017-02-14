package com.survivingwithandroid.weatherapp.model;

public class DayForcastItem {
	private String wind;
	private String icon;
	private String humidity;
	private String temp;
	private String temp_max_min;
	private String presure;
	private String location ;
	private String time ;
	public DayForcastItem() {
		
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getTemp_max_min() {
		return temp_max_min;
	}
	public void setTemp_max_min(String temp_max_min) {
		this.temp_max_min = temp_max_min;
	}
	public String getPresure() {
		return presure;
	}
	public void setPresure(String presure) {
		this.presure = presure;
	}
	 
	
}
