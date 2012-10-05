package org.yuzifeng.mystock.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.yuzifeng.mystock.model.StockHistoryPrice;

public class StockHistoryFileUtils {

	public static void loadFromFile(StockHistoryPrice history, String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		Pattern datePattern = Pattern.compile("(\\d{4})/(\\d{2})/(\\d{2})[\\s,]*(\\S*)[\\s,]+(\\S*)[\\s,]+(\\S*)[\\s,]+(\\S*)[\\s,]+(\\S*)");
		try {
			while ((line = reader.readLine()) != null) {
				line = line.trim();				
				Matcher m = datePattern.matcher(line);
				if (m.find()) {
					int yyyy = Integer.parseInt(m.group(1));
					int mm = Integer.parseInt(m.group(2));
					int dd = Integer.parseInt(m.group(3));
					Day date = new Day(dd, mm, yyyy);
					double open = Double.parseDouble(m.group(4));
					double high = Double.parseDouble(m.group(5));
					double low = Double.parseDouble(m.group(6));
					double close = Double.parseDouble(m.group(7));
					double volume = Double.parseDouble(m.group(8));
					history.addOrUpdate(date, open, high, low, close, volume);
				}
			}
		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}

	public static void saveToFile(StockHistoryPrice history, String filePath) throws IOException {
		PrintStream printer = new PrintStream(new FileOutputStream(filePath));

		TimeSeries openPriceSeries = history.getOpenPriceSeries();
		TimeSeries highPriceSeries = history.getHighPriceSeries();
		TimeSeries lowPriceSeries = history.getLowPriceSeries();
		TimeSeries closePriceSeries = history.getClosePriceSeries();
		TimeSeries volumeSeries = history.getVolumeSeries();

		int count = openPriceSeries.getItemCount();

		for (int i = 0; i < count; ++i) {
			String outputLine = "";
			TimeSeriesDataItem item = openPriceSeries.getDataItem(i);
			Day date = (Day)item.getPeriod();

			//Add date
			outputLine += date.getYear() + "/";

			if (date.getMonth() < 10)
				outputLine += "0";
			outputLine += date.getMonth() + "/";

			if (date.getDayOfMonth() < 10)
				outputLine += "0";
			outputLine += date.getDayOfMonth();

			//Add open price
			outputLine += "," + item.getValue();

			//Add high price
			item = highPriceSeries.getDataItem(date);
			outputLine += ",";
			if (item != null)
				outputLine += item.getValue();

			//Add low price
			item = lowPriceSeries.getDataItem(date);
			outputLine += ",";
			if (item != null)
				outputLine += item.getValue();

			//Add close price
			item = closePriceSeries.getDataItem(date);
			outputLine += ",";
			if (item != null)
				outputLine += item.getValue();

			//Add volume
			item = volumeSeries.getDataItem(date);
			outputLine += ",";
			if (item != null)
				outputLine += item.getValue();

			printer.println(outputLine);
		}

		printer.close();	

	}

	public static void mergeFilesInFolder(String folderPath, String targetFileName) {
		File directory = new File(folderPath);
		if (directory.isDirectory()) {
			StockHistoryPrice history = new StockHistoryPrice();
			File[] sourceList = directory.listFiles();
			for (int i = 0; i < sourceList.length; ++i) {
				if (sourceList[i].isFile()) {
					try {
						loadFromFile(history, sourceList[i].getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			try {
				saveToFile(history, directory.getAbsolutePath() + "\\" + targetFileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		mergeFilesInFolder("C:\\Temp", "ZS000001.SHP");
	}

}
