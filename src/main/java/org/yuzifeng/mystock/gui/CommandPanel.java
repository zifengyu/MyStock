package org.yuzifeng.mystock.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class CommandPanel extends JPanel {
	
	public CommandPanel() {
		
		setLayout(new GridLayout(1, 1));
		commandLineComponent = new JTextField();
		add(commandLineComponent);
		
	}
	
	private JTextComponent commandLineComponent;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
