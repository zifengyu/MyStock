package org.yuzifeng.mystock.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.jfree.data.time.Day;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.yuzifeng.mystock.model.StockHistoryPrice;

public class WebContentUtils {
	
	static public int SINA_PATTERN_ZS = 0;
	static public int SINA_PATTERN_GG = 1;
	
	static private String[] SinaPattern = {
		"http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/%s/type/S.phtml?year=%d&jidu=%d",
		"http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/%s.phtml?year=%d&jidu=%d"};	

	public static void getHistoryDataFromSina(int pattern, String id, int year, int season, StockHistoryPrice history) throws Exception {
		final int DATA_FIELD_COUNT = 7;
		//String url = "http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/" + id + "/type/S.phtml?year=" + year + "&jidu=" + season;

		//String url = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/" + id + ".phtml?year=" + year + "&jidu=" + season;
		String url = String.format(SinaPattern[pattern], id, year, season);
		
		Document doc = Jsoup.connect(url).get();
		Elements dataItems = doc.select("tr:has(td:has(div:eq(0):not(:has(strong))");

		Iterator<Element> iter = dataItems.iterator();
		while (iter.hasNext()) {
			Elements elements = iter.next().select("div[align=center]");
			if (DATA_FIELD_COUNT == elements.size()) {
				System.out.println(elements.text());
				String[] dateTokens = elements.get(0).text().split("-");
				int yyyy = Integer.parseInt(dateTokens[0]);
				int mm = Integer.parseInt(dateTokens[1]);
				int dd = Integer.parseInt(dateTokens[2]);
				Day date = new Day(dd, mm, yyyy);
				double open   = Double.parseDouble(elements.get(1).text());
				double high   = Double.parseDouble(elements.get(2).text());
				double low    = Double.parseDouble(elements.get(4).text());
				double close  = Double.parseDouble(elements.get(3).text());
				double volume = Double.parseDouble(elements.get(5).text());
				//System.out.println(volume);
				history.addOrUpdate(date, open, high, low, close, volume);
			} else {
				System.out.println("Warning: unexpected content found. Field number is not " + DATA_FIELD_COUNT + elements);
			}
		}


	}


	public static void main(String[] args) {
	}

}
