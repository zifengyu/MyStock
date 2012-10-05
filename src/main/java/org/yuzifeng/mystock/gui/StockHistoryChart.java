package org.yuzifeng.mystock.gui;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;

public class StockHistoryChart {

	private JFreeChart chart;
	private TimeSeries priceSeries;
	private TimeSeriesCollection priceDataSet;
	private TimeSeries volumeSeries;
	private TimeSeriesCollection volumeDataSet;
	
	private DateAxis dateAxis;
	private NumberAxis priceAxis;
	private NumberAxis volumeAxis;
	
	private int year = 2000;
	private int period = 365;

	public StockHistoryChart() {		

		priceDataSet = new TimeSeriesCollection();
		volumeDataSet = new TimeSeriesCollection();		

		dateAxis = new DateAxis("Date");		
		priceAxis = new NumberAxis("Price");
		volumeAxis = new NumberAxis("Volume");		

		XYItemRenderer renderer = new StandardXYItemRenderer();
		XYPlot pricePlot = new XYPlot(priceDataSet, null, priceAxis, renderer);

		XYBarRenderer volumeBarrenderer = new XYBarRenderer(0.01);
		XYBarPainter volumeBarPainter = new StandardXYBarPainter();
		volumeBarrenderer.setBarPainter(volumeBarPainter);
		volumeBarrenderer.setShadowVisible(false);		
		XYPlot volumePlot = new XYPlot(volumeDataSet, null, volumeAxis, volumeBarrenderer);		

		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(dateAxis);
		plot.setGap(10.0);
		plot.add(pricePlot, 4);
		plot.add(volumePlot, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);
		chart = new JFreeChart(
				null,
				JFreeChart.DEFAULT_TITLE_FONT, 
				plot, 
				false
				);
	}

	public void setRange(Day startDay, Day endDay) {
		//dateAxis.setR.setR.setRange(startDay, endDay);
	}

	public void setDataSet(TimeSeries priceSeries, TimeSeries volumeSeries) {
		//this.priceSeries = null == priceSeries ? new TimeSeries("PRICE") : priceSeries;
		//this.volumeSeries = null == volumeSeries ? new TimeSeries("VOLUME") : volumeSeries;

		//priceDataSet = new TimeSeriesCollection();
		this.priceSeries = priceSeries;
		this.volumeSeries = volumeSeries;
		priceDataSet.removeAllSeries();
		priceDataSet.addSeries(priceSeries);
		volumeDataSet.removeAllSeries();
		volumeDataSet.addSeries(volumeSeries);
		//volumeDataSet = new TimeSeriesCollection();
		//volumeDataSet.addSeries(this.volumeSeries);		
	}

	public JFreeChart getChart() {
		return chart;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
