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
	private int year = 2000;
	private int period = 365;

	public StockHistoryChart() {		

		setDataSet(null, null);

		DateAxis dateAxis = new DateAxis("Date");		
		NumberAxis priceAxis = new NumberAxis("Price");
		NumberAxis volumeAxis = new NumberAxis("Volume");

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

	public void addDataPoint(double data) {
		priceSeries.add(new Year(year++), data);

	}

	public void setDataSet(TimeSeries priceSeries, TimeSeries volumeSeries) {
		this.priceSeries = null == priceSeries ? new TimeSeries("PRICE") : priceSeries;
		this.volumeSeries = null == volumeSeries ? new TimeSeries("VOLUME") : volumeSeries;

		priceDataSet = new TimeSeriesCollection();
		priceDataSet.addSeries(this.priceSeries);

		volumeDataSet = new TimeSeriesCollection();
		volumeDataSet.addSeries(this.volumeSeries);		
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
