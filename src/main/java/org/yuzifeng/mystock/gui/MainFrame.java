package org.yuzifeng.mystock.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
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
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.Font;
import javax.swing.JButton;

public class MainFrame extends JFrame {

	private JTextField commandInputComp;
	private JTextArea commandHistoryComp;

	private StockHistoryChart stockChart;

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

		GridBagLayout gridBagLayout = new GridBagLayout();		
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0};
		getContentPane().setLayout(gridBagLayout);
	
		stockChart = new StockHistoryChart();

		JPanel panel_1 = new ChartPanel(stockChart.getChart());
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridheight = 2;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.weighty = 200.0;
		gbc_panel_1.weightx = 300.0;
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridheight = 3;
		gbc_panel_2.weightx = 100.0;
		gbc_panel_2.anchor = GridBagConstraints.EAST;
		gbc_panel_2.weighty = 100.0;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		getContentPane().add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWeights = new double[]{0.0};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0};
		panel_2.setLayout(gbl_panel_2);

		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.BLACK);
		textArea.setEditable(false);
		textArea.setForeground(Color.ORANGE);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.anchor = GridBagConstraints.NORTH;
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		gbc_textArea.weightx = 100.0;
		gbc_textArea.weighty = 100.0;		
		panel_2.add(textArea, gbc_textArea);

		commandHistoryComp = new JTextArea();
		commandHistoryComp.setLineWrap(true);
		JScrollPane scrollpanel_2 = new JScrollPane(commandHistoryComp);
		GridBagConstraints gbc_scrollpanel_2 = new GridBagConstraints();
		gbc_scrollpanel_2.anchor = GridBagConstraints.SOUTH;
		gbc_scrollpanel_2.fill = GridBagConstraints.BOTH;
		gbc_scrollpanel_2.gridx = 0;
		gbc_scrollpanel_2.gridy = 1;
		gbc_scrollpanel_2.weightx = 100.0;
		gbc_scrollpanel_2.weighty = 100.0;
		panel_2.add(scrollpanel_2, gbc_scrollpanel_2);
		scrollpanel_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpanel_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		commandHistoryComp.setForeground(Color.ORANGE);
		commandHistoryComp.setBackground(Color.BLACK);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(null);
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.anchor = GridBagConstraints.SOUTH;
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 2;
		getContentPane().add(panel_4, gbc_panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		commandInputComp = new JTextField();
		commandInputComp.setFont(new Font("Courier New", Font.BOLD, 14));
		commandInputComp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {  
					String command = commandInputComp.getText();
					commandHistoryComp.append(">" + command + "\n");
					commandInputComp.setText("");  
					stockChart.addDataPoint(Double.parseDouble(command));
				}  

			}
		});

		JButton commandButton = new JButton("CMD>");
		commandButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		commandButton.setForeground(Color.BLACK);
		panel_4.add(commandButton, BorderLayout.WEST);
		commandInputComp.setForeground(Color.ORANGE);
		commandInputComp.setBackground(Color.BLACK);
		panel_4.add(commandInputComp);

		//Focus on command line input component
		pack();
		setExtendedState(Frame.MAXIMIZED_BOTH);
		commandInputComp.requestFocusInWindow();

	}

	private void createMenu() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		setJMenuBar(menuBar);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				JFrame frame = new MainFrame();				
				frame.setVisible(true);
			}});

	}

}
