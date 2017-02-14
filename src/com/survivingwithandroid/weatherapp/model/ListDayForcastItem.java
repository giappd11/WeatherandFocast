package com.survivingwithandroid.weatherapp.model;

import java.util.ArrayList;
import java.util.List;

public class ListDayForcastItem  {

	
	private ArrayList<DayForcastItem> listForcast;
	public ListDayForcastItem(){
		listForcast = new ArrayList<DayForcastItem>();
		
	} 
	 
	public int countCast (){
		return listForcast.size();
		
	}
	public void addForcast(DayForcastItem item) {
		this.listForcast.add(item);
		
	}

	public DayForcastItem getFocast(int daynum) {
		return this.listForcast.get(daynum);
	}
	
	public List getListFocast(){
		return   this.listForcast;
	}

	 
}

