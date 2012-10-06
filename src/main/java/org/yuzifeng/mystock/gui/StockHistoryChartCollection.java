package org.yuzifeng.mystock.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.data.time.TimeSeries;

public class StockHistoryChartCollection {
	
	private long minTime;
	private long maxTime;
	private long startTime;
	private int pageTimeInMonth;
	
	private ArrayList<StockHistoryChart> chartList;
	
	public StockHistoryChartCollection() {
		chartList = new ArrayList<StockHistoryChart>();
		minTime = Long.MAX_VALUE;
		maxTime = Long.MIN_VALUE;
		startTime = Long.MAX_VALUE;
		pageTimeInMonth = 12;
	}
	
	public int createNewChart(String name) {
		StockHistoryChart chart = new StockHistoryChart(name);		
		chartList.add(chart);			
		return chartList.size() - 1;
	}
	
	public StockHistoryChart getChart(int index) {
		return chartList.get(index);
	}
	
	public long getMinTime() {
		return minTime;
	}
	
	public long getMaxTime() {
		return maxTime;
	}
	
	public void setData(int index, TimeSeries priceSeries, TimeSeries volumeSeries) {
		StockHistoryChart chart = chartList.get(index);
		chart.setDataSet(priceSeries, volumeSeries);
		chart.setPageRange(pageTimeInMonth);
		long time = chart.getMinTime();
		if (minTime > time)
			minTime = time;
		time = chart.getMaxTime();
		if (maxTime < time)
			maxTime = time;
		
		
	}
	
	public void setPageRange(int month) {
		this.pageTimeInMonth = month;
		Iterator<StockHistoryChart> iter = chartList.iterator();
		while (iter.hasNext()) {
			StockHistoryChart chart = iter.next();
			chart.setPageRange(pageTimeInMonth);							
		}
	}
	
	public void prev(int month) {
		if (startTime > minTime) {
			startTime -= 365L * month * 2 * 3600 * 1000; 
			refreshAll();
		}
	}
	
	public void next(int month) {
		long time = startTime + 365L * month * 2 * 3600 * 1000;
		if (time < maxTime) {
			startTime = time; 
			refreshAll();
		}
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
		refreshAll();
	}
	
	public void refreshAll() {		
		if (minTime <= maxTime) {
			if (startTime == Long.MAX_VALUE)
				startTime = maxTime - 360L * 24 * 3600 * 1000;
			
			Iterator<StockHistoryChart> iter = chartList.iterator();
			while (iter.hasNext()) {
				StockHistoryChart chart = iter.next();
				//chart.setPageRange(pageTimeInMonth);
				chart.setStartTime(startTime);				
			}
		}
	}
	
	


}
