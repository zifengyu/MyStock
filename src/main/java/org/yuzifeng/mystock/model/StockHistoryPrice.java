package org.yuzifeng.mystock.model;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

public class StockHistoryPrice {
	
	private TimeSeries openPriceSeries;	
	private TimeSeries highPriceSeries;
	private TimeSeries lowPriceSeries;
	private TimeSeries closePriceSeries;
	private TimeSeries volumePriceSeries;
	
	public StockHistoryPrice() {
		openPriceSeries = new TimeSeries("OPEN");
		highPriceSeries = new TimeSeries("HIGH");
		lowPriceSeries = new TimeSeries("LOW");
		closePriceSeries = new TimeSeries("CLOSE");
		volumePriceSeries = new TimeSeries("VOLUME");
	}
	
	public int addOrUpdate(Day date, double open, double high, double low, double close, double volume) {		
		int isUpdate = (openPriceSeries.addOrUpdate(date, open) == null) ? 0 : 1;
		highPriceSeries.addOrUpdate(date, high);
		lowPriceSeries.addOrUpdate(date, low);
		closePriceSeries.addOrUpdate(date, close);
		volumePriceSeries.addOrUpdate(date, volume);
		
		return isUpdate;		
	}
	
	public int addOrUpdate(StockHistoryPrice series) {		
		int overwriteCount = openPriceSeries.addAndOrUpdate(series.openPriceSeries).getItemCount();
		highPriceSeries.addAndOrUpdate(series.highPriceSeries);
		lowPriceSeries.addAndOrUpdate(series.lowPriceSeries);
		closePriceSeries.addAndOrUpdate(series.closePriceSeries);
		volumePriceSeries.addAndOrUpdate(series.volumePriceSeries);
		
		return overwriteCount;		
	}	

	public TimeSeries getOpenPriceSeries() {
		return openPriceSeries;
	}

	public TimeSeries getHighPriceSeries() {
		return highPriceSeries;
	}

	public TimeSeries getLowPriceSeries() {
		return lowPriceSeries;
	}

	public TimeSeries getClosePriceSeries() {
		return closePriceSeries;
	}

	public TimeSeries getVolumeSeries() {
		return volumePriceSeries;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
