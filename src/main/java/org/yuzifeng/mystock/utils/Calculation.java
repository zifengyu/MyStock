package org.yuzifeng.mystock.utils;

import java.io.File;
import java.io.IOException;

import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.yuzifeng.mystock.model.StockHistoryPrice;

public class Calculation {

	private static double[] minorMagnitude = {0.201123387
		,0.253196128
		,0.257957008
		,0.277268509
		,0.283926009
		,0.287172272
		,0.34500866
		,0.356812281
		,0.386150078
		,0.392304718
		,0.420429915
		,0.456154834
		,0.475035825
		,0.495565759
		,0.519080302
		,0.520489641
		,0.526453298
		,0.528388775
		,0.538023273
		,0.538935492
		,0.560570627
		,0.569530843
		,0.570890797
		,0.576045533
		,0.584339214
		,0.593294978
		,0.619806033
		,0.628867676
		,0.62890737
		,0.66639188
		,0.676277913
		,0.727654987
		,0.728432273
		,0.753917051
		,0.779684771
		,0.794009587
		,0.82112239
		,0.844081828
		,0.862937402
		,0.874454367
		,0.924888101
		,0.958087565
		,1.197083978
		,1.214823783
		,1.391057649
		,1.481425322
		,1.682156703
	};

	private static double[] minorDays = {6
		,7
		,7
		,8
		,9
		,12
		,13
		,13
		,13
		,14
		,15
		,16
		,17
		,20
		,23
		,23
		,24
		,25
		,25
		,26
		,26
		,28
		,30
		,31
		,33
		,34
		,34
		,36
		,36
		,37
		,38
		,42
		,42
		,46
		,57
		,59
		,61
		,67
		,68
		,76
		,83
		,84
		,89
		,102
		,105
		,108		
		,139
	};
	
	private static double[] bearDays = {144
			,41
			,131
			,10
			,7
			,13
			,21
			,12
			,75
			,81
			,38
			,129
			,48
			,74
			,174
			,213
			,157
			,48
			,69
			,22
			,88
			,42
			,94
			,132
			,38
			,28
			,75
			,80
			,76
			,62
			,96
			,51
			,27
			,139
			,41
	};
	
	private static double[] bullDays = {42
			,31
			,69
			,46
			,36
			,138
			,34
			,34
			,28
			,81
			,42
			,50
			,113
			,96
			,105
			,111
			,68
			,180
			,27
			,292
			,101
			,35
			,46
			,151
			,123

	};
	

	public static double calPro(double[] hist, double val) {
		double pro = 0;
		for (int i = 0; i < hist.length; ++i) {
			if (hist[i] > val)
				pro++;
		}
		return pro / hist.length;
	}

	public static TimeSeries EXPMA(TimeSeries sourceData, int N) {
		int count = sourceData.getItemCount();

		double expma = 0;
		
		TimeSeries expmaRes = new TimeSeries("EXPMA" + N);

		for (int i = 0; i < count; ++i) {
			TimeSeriesDataItem data = sourceData.getDataItem(i);
			RegularTimePeriod date = data.getPeriod();
			double value = data.getValue().doubleValue();
			expma = (2 * value + expma * (N - 1)) / (N + 1);
			expmaRes.add(date, expma);
		}

		return expmaRes;
	}
	
	public static void Analyze2(TimeSeries sourceData, TimeSeries index1, TimeSeries index2) {
		int count = sourceData.getItemCount();
		
		double buyPrice = 0;
		double sellPrice = 0;
		double total = 1;
		
		for (int i = 0; i < count - 1; ++i) {
			RegularTimePeriod p1 = sourceData.getDataItem(i).getPeriod();
			RegularTimePeriod p2 = sourceData.getDataItem(i + 1).getPeriod();
			double v11 = index1.getDataItem(p1).getValue().doubleValue();
			double v12 = index2.getDataItem(p1).getValue().doubleValue();
			double v21 = index1.getDataItem(p2).getValue().doubleValue();
			double v22 = index2.getDataItem(p2).getValue().doubleValue();
			//System.out.println(v11 + " " + v12 + " " + v21 + " " + v22);
			if (v11 <= v12 && v21 > v22) {
				//System.out.println("BUY," + p2.getStart() + "," + sourceData.getDataItem(p2).getValue().doubleValue());
				buyPrice = sourceData.getDataItem(p2).getValue().doubleValue();
			}
			
			if (v11 >= v12 && v21 < v22) {
				sellPrice = sourceData.getDataItem(p2).getValue().doubleValue();
				if (buyPrice != 0) {
					double rate = sellPrice / buyPrice - 1;
					total *= (1 + rate);
					System.out.println(p2.getStart() + "\t" + (int)(rate * 100) +  "\t" + total);
				}
				
				//System.out.println("SEL," + p2.getStart() + "," + sourceData.getDataItem(p2).getValue().doubleValue());
			}
		}
	}

	public static void main(String[] args) {
		
		//System.out.println(calPro(bullDays, 28));
		
		StockHistoryPrice history = new StockHistoryPrice();


		try {
			StockHistoryFileUtils.loadFromFile(history, "C:\\Users\\yua2\\git\\MyStock\\src\\data\\002024\\002024.SHP");
			TimeSeries ts1 = EXPMA(history.getClosePriceSeries(), 1);
			TimeSeries ts2 = EXPMA(history.getClosePriceSeries(), 30);
			Analyze2(history.getClosePriceSeries(), ts1, ts2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
