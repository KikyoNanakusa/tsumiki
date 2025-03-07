package net.nanakusa.virtualDisplay.vMDA;

import javax.swing.*;

public class MDAFrame extends JFrame {
	private final MDADisplayPanel displayPanel;

	public MDAFrame(int rows, int cols, MDA mda) {
		setTitle(" vMDA Frame ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		displayPanel = new MDADisplayPanel(rows, cols, mda);
		add(displayPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}