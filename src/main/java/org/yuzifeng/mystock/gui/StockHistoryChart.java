package org.yuzifeng.mystock.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
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
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;

public class StockHistoryChart {
	
	private String name;

	private JFreeChart chart;
	private TimeSeries priceSeries;
	private TimeSeriesCollection priceDataSet;
	private TimeSeries volumeSeries;
	private TimeSeriesCollection volumeDataSet;
	public ChartPanel chartPanel;

	private DateAxis dateAxis;
	private NumberAxis priceAxis;
	private NumberAxis volumeAxis;
	
	private XYPlot pricePlot;
	CombinedDomainXYPlot plot;

	private long minTime;
	private long maxTime;
	private long timeRangePerPage = 365L * 24 * 3600 * 1000;
	private long pageMin;
	private long pageMax;
	private double minPriceAxis;
	private double maxPriceAxis;	

	public StockHistoryChart(String name) {
		
		this.name = name;
		
		

		priceDataSet = new TimeSeriesCollection();
		volumeDataSet = new TimeSeriesCollection();		

		dateAxis = new DateAxis("Date");		
		priceAxis = new NumberAxis("Price");
		volumeAxis = new NumberAxis("Volume");		

		XYItemRenderer renderer = new StandardXYItemRenderer();		

		pricePlot = new XYPlot(priceDataSet, dateAxis, priceAxis, renderer);
		pricePlot.setBackgroundPaint(Color.BLACK);

		XYBarRenderer volumeBarrenderer = new XYBarRenderer(0.01);
		XYBarPainter volumeBarPainter = new StandardXYBarPainter();
		volumeBarrenderer.setBarPainter(volumeBarPainter);
		volumeBarrenderer.setShadowVisible(false);		
		XYPlot volumePlot = new XYPlot(volumeDataSet, dateAxis, volumeAxis, volumeBarrenderer);	
		volumePlot.setBackgroundPaint(Color.BLACK);

		plot = new CombinedDomainXYPlot(dateAxis);
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
	
	public String getName() {
		return name;
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

		minTime = priceSeries.getDataItem(0).getPeriod().getFirstMillisecond();
		maxTime = priceSeries.getDataItem(priceSeries.getItemCount() - 1).getPeriod().getFirstMillisecond();
		timeRangePerPage = 365L * 24 * 3600 * 1000;
		setDateAxisRange(maxTime - timeRangePerPage, maxTime);
		//volumeDataSet = new TimeSeriesCollection();
		//volumeDataSet.addSeries(this.volumeSeries);		
	}

	public String getStockInfoInPage(int posX, int posY) {
		/*
		int index = getIndexInPage(posX);
		
		if (index < 0)
			return null;	

		TimeSeriesDataItem item = priceSeries.getDataItem(index);
		Date date = item.getPeriod().getStart();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		String res = "Date\t: " + format.format(date) + "\nPrice\t: " + item.getValue().toString();
		item = volumeSeries.getDataItem(index);
		res += "\nVolume\t: " + item.getValue().toString();
		//res += "\nxPos\t: " + format.format((new Date((long)((pageMax - pageMin) * posX + pageMin))));
		//res += "\nyPos\t: " + ((maxPriceAxis - minPriceAxis) * posY + minPriceAxis);
		}
		*/
		
		Point2D mousePoint2 = chartPanel.translateScreenToJava2D(new Point(posX, posY));
		System.out.println("mouseX=" + posX + " mouseY=" + posY + "\n");
		System.out.println("point2=" + mousePoint2 + "\n");
		System.out.println("Area=" + chartPanel.getScreenDataArea(500, 200) + "\n");
		long date = (long)dateAxis.java2DToValue(mousePoint2.getX(), chartPanel.getScreenDataArea(posX, posY), pricePlot.getDomainAxisEdge());
		double price = priceAxis.java2DToValue(mousePoint2.getY(), chartPanel.getScreenDataArea(), plot.getRangeAxisEdge());
		Date date2 = new Date(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		String res = format.format(date2) + "\n" + price + "\n";
		//res = chartPanel.getChartRenderingInfo().getPlotInfo().getPlotArea().toString();
		return res;
	}
	
	public int getIndexInPage(double pos) {
		long pTime = pageMin + (long)((pageMax - pageMin) * pos);

		int index1 = -1;
		int index2 = 0;
		int index = 0;

		int count = priceSeries.getItemCount();

		if (0 == count)
			return -1;

		if (count > 1) {
			while (index2 < count && priceSeries.getDataItem(index2).getPeriod().getFirstMillisecond() < pTime) {
				index1 = index2++;
			}

			if (index2 == count) {
				index = count - 1;
			} else {
				long time1 = priceSeries.getDataItem(index1).getPeriod().getFirstMillisecond();
				long time2 = priceSeries.getDataItem(index2).getPeriod().getFirstMillisecond();

				index = (pTime - time1 < time2 - pTime) ? index1 : index2;
			}
		}
		
		return index;
	}
	
	public void setPageRange(int month) {
		timeRangePerPage = ((long)month) * 365 / 12 * 24 * 3600 * 1000;
		setDateAxisRange(pageMax - timeRangePerPage, pageMax);
	}

	public void setDateAxisRange(long startTime, long endTime) {
		
		pageMin = startTime;
		pageMax = endTime;
		
		int startIndex = getIndexInPage(0);
		int endIndex = getIndexInPage(1);
		
		double minPrice = Double.MAX_VALUE;
		double maxPrice = Double.MIN_VALUE;
		if (startIndex != -1 && endIndex != -1) {
			for (int i = startIndex; i <= endIndex; ++i) {
				double price = priceSeries.getDataItem(i).getValue().doubleValue();
				if (price > maxPrice)
					maxPrice = price;
				if (price < minPrice)
					minPrice = price;
			}
		}
		setPriceAxisRange(minPrice - (maxPrice - minPrice) / 3, maxPrice + (maxPrice - minPrice) / 3);
			
		dateAxis.setRange(pageMin, pageMax);
		
	}
	
	public void setPriceAxisRange(double lower, double upper) {
		minPriceAxis = lower;
		maxPriceAxis = upper;
		priceAxis.setRange(lower, upper);
	}

	public void prevPage() {
		if (pageMin > minTime) {
			pageMax = pageMin;
			pageMin = pageMin - timeRangePerPage;
			setDateAxisRange(pageMin, pageMax);
		}
	}
	
	public void prevPage2() {
		if (pageMin > minTime) {
			pageMax = pageMin;
			pageMin = pageMin - timeRangePerPage / 2;
			setDateAxisRange(pageMin, pageMax);
		}
	}

	public void nextPage() {
		if (pageMax < maxTime) {
			pageMin = pageMax;
			pageMax = pageMax + timeRangePerPage;
			setDateAxisRange(pageMin, pageMax);
		}
	}
	
	public void nextPage2() {
		if (pageMax < maxTime) {
			pageMin = pageMax;
			pageMax = pageMax + timeRangePerPage / 2;
			setDateAxisRange(pageMin, pageMax);
		}
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
