package org.yuzifeng.mystock.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PlotEntity;
import org.jfree.chart.entity.XYItemEntity;

import org.yuzifeng.mystock.model.StockHistoryPrice;
import org.yuzifeng.mystock.utils.*;

import java.awt.Font;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class MainFrame extends JFrame {

	private JTextField commandInputComp;
	private JTextArea commandHistoryComp;
	private JTextArea stockInfoComp;	
	private JTabbedPane tabbedPane;

	//private StockHistoryChart stockChart;
	private boolean enableCursor = false;
	private JTextField textField;

	private ChartPanel[] chartPanelList = new ChartPanel[5];
	private StockHistoryChart[] stockHistoryChartList = new StockHistoryChart[5];
	private String BASE_PATH = "C:\\Users\\yua2\\workspace\\mystock\\src\\";

	public MainFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// get screen dimensions
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		// set frame width, height and let platform pick screen location
		setSize(screenWidth / 2, screenHeight / 2);
		setLocationByPlatform(true);

		// set frame icon and title
		setTitle("MyStock");		

		createMenu();
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1108px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("264px"),},
				new RowSpec[] {
				RowSpec.decode("816px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("26px"),}));


		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, "1, 1, fill, fill");
		
	
		for (int i = 0; i < 5; ++i) {

			stockHistoryChartList[i] = new StockHistoryChart("ZS000001");
			chartPanelList[i] = new ChartPanel(stockHistoryChartList[i].getChart());
			stockHistoryChartList[i].chartPanel = chartPanelList[i];			
			chartPanelList[i].addChartMouseListener(new ChartMouseAdaptor(i));
			chartPanelList[i].setDomainZoomable(false);
			chartPanelList[i].setRangeZoomable(false);		
			chartPanelList[i].setHorizontalAxisTrace(false);
			chartPanelList[i].setVerticalAxisTrace(false);
			tabbedPane.addTab("New tab", null, chartPanelList[i], null);

			StockHistoryPrice history = new StockHistoryPrice();
			try {
				StockHistoryFileUtils.loadFromFile(history, BASE_PATH + "data\\ZS000001.SHP");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			stockHistoryChartList[i].setDataSet(history.getClosePriceSeries(), history.getVolumeSeries());


		}
		
		

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, "3, 1, 1, 3, fill, fill");
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWeights = new double[]{1.0};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0};
		panel_2.setLayout(gbl_panel_2);

		stockInfoComp = new JTextArea();
		stockInfoComp.setFont(new Font("Monospaced", Font.PLAIN, 20));
		JScrollPane scrollPane = new JScrollPane(stockInfoComp);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 100.0;
		gbc_scrollPane.weightx = 100.0;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.anchor = GridBagConstraints.NORTH;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_2.add(scrollPane, gbc_scrollPane);
		stockInfoComp.setLineWrap(true);
		stockInfoComp.setBackground(Color.BLACK);
		stockInfoComp.setEditable(false);
		stockInfoComp.setForeground(Color.ORANGE);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.anchor = GridBagConstraints.NORTH;
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		gbc_textArea.weightx = 0.0;
		gbc_textArea.weighty = 100.0;		
		//panel_2.add(stockInfoComp, gbc_textArea);

		commandHistoryComp = new JTextArea();
		commandHistoryComp.setLineWrap(true);
		JScrollPane scrollpanel_2 = new JScrollPane(commandHistoryComp);
		GridBagConstraints gbc_scrollpanel_2 = new GridBagConstraints();
		gbc_scrollpanel_2.anchor = GridBagConstraints.SOUTH;
		gbc_scrollpanel_2.fill = GridBagConstraints.BOTH;
		gbc_scrollpanel_2.gridx = 0;
		gbc_scrollpanel_2.gridy = 1;
		gbc_scrollpanel_2.weightx = 0.0;
		gbc_scrollpanel_2.weighty = 100.0;
		panel_2.add(scrollpanel_2, gbc_scrollpanel_2);
		scrollpanel_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpanel_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		commandHistoryComp.setForeground(Color.ORANGE);
		commandHistoryComp.setBackground(Color.BLACK);

		JPanel panel_4 = new JPanel();
		getContentPane().add(panel_4, "1, 3, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		commandInputComp = new JTextField();
		commandInputComp.setFont(new Font("Courier New", Font.BOLD, 14));
		commandInputComp.addKeyListener(new CMDInputAdaptor());

		JButton commandButton = new JButton("CMD>");
		commandButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		commandButton.setForeground(Color.BLACK);
		panel_4.add(commandButton, BorderLayout.WEST);
		commandInputComp.setForeground(Color.ORANGE);
		commandInputComp.setBackground(Color.BLACK);
		panel_4.add(commandInputComp);


		//Focus on command line input component
		pack();
		commandInputComp.requestFocusInWindow();
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}	


	private void createMenu() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		setJMenuBar(menuBar);

	}

	private class CMDInputAdaptor extends KeyAdapter {		
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {  
				String command = commandInputComp.getText().trim();
				commandHistoryComp.append("<CMD>" + command + "\n");
				commandInputComp.selectAll();	
				commandHistoryComp.append(processCommand(command) + "\n\n");
			}  
		}

		private String processCommand(String cmd) {
			String res = "Error in processing command.";

			StockHistoryChart stockChart = stockHistoryChartList[0];

			String[] tokens = cmd.split("\\s");
			if (tokens[0].equalsIgnoreCase("p")) {
				stockChart.prevPage();
				res = "OK";				
			} else if (tokens[0].equalsIgnoreCase("p2")) {
				stockChart.prevPage2();
				res = "OK";
			} else if (tokens[0].equalsIgnoreCase("n")) {
				stockChart.nextPage();
				res = "OK";
			} else if (tokens[0].equalsIgnoreCase("n2")) {
				stockChart.nextPage2();
				res = "OK";
			} else if (tokens[0].equalsIgnoreCase("y")) {
				int index = 1;
				while (index < tokens.length && tokens[index].length() == 0)
					++index;
				if (index < tokens.length) {
					double low = Double.parseDouble(tokens[index]);
					++index;
					while (index < tokens.length && tokens[index].length() == 0)
						++index;
					if (index < tokens.length) {
						double high = Double.parseDouble(tokens[index]);
						if (high > low) {
							stockChart.setPriceAxisRange(low, high);
							res = "Set Y axis range : " + low + "-" + high;
						}
					}
				}

			} else if (tokens[0].equalsIgnoreCase("r")) {
				int index = 1;
				while (index < tokens.length && tokens[index].length() == 0)
					++index;
				if (index < tokens.length) {
					int m = Integer.parseInt((tokens[index]));
					if (m >= 1) {
						stockChart.setPageRange(m);
						res = "Set page range : " + tokens[index] + " months";
					}

				}
			}

			return res;

		}
	}

	private class ChartMouseAdaptor implements ChartMouseListener {
		
		private int index;
		
		public ChartMouseAdaptor(int index) {
			this.index = index;
		}

		public void chartMouseClicked(ChartMouseEvent event) {
			if(event.getTrigger().getButton() == MouseEvent.BUTTON1 && event.getTrigger().getClickCount() >= 2){
				enableCursor = !enableCursor;
				for (int i = 0; i < 5; ++i) {
					chartPanelList[i].setHorizontalAxisTrace(enableCursor);
					chartPanelList[i].setVerticalAxisTrace(enableCursor);
				}

			}

		}

		public void chartMouseMoved(ChartMouseEvent event) {
			if (enableCursor) {
				ChartEntity e = event.getEntity();

				if (e != null) {
					String display = "";
					/*if (e instanceof XYItemEntity) {
						int series = ((XYItemEntity)e).getSeriesIndex();
						int item = ((XYItemEntity)e).getItem();
						Date date = new Date(((XYItemEntity)e).getDataset().getX(series, item).longValue());
						double price = ((XYItemEntity)e).getDataset().getX(series, item).doubleValue();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						display = "Date\t: " + format.format(date) + "\nPrice\t: " + price;

					} else {*/
					if (e instanceof PlotEntity) {
						PlotEntity en = (PlotEntity)e;
						
						
						Rectangle bounds = e.getArea().getBounds();
						int minX = bounds.x;			
						
						int minY = bounds.y;
						int mouseX = event.getTrigger().getX();
						int mouseY = event.getTrigger().getY();
						Point2D mousePoint2 = chartPanelList[index].translateScreenToJava2D(new Point(mouseX, mouseY));						
						double posX = (mousePoint2.getX() - bounds.x) / bounds.width;
						double posY = 1 - (mousePoint2.getY() - bounds.y) / bounds.height;						
						display = stockHistoryChartList[index].getStockInfoInPage(mouseX, mouseY);

						
						stockInfoComp.setText(display + "\nminX:" + minX + " mouseX:" + mouseX + " minY:" + minY + " mouseY:" + mouseY
								+ "\n" + en.getArea() + "\n" + posX + "," + posY);
						
					}

				}
			}
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				MainFrame frame = new MainFrame();				
				frame.setVisible(true);
			}});

	}

}
