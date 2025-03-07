package net.nanakusa.virtualDisplay.vMDA;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MDADisplayPanel extends JPanel{
	private static final int FONT_SIZE = 30;

	private final Font font;
	private final int charWidth;
	private final int charHeight;

	private final int rows;
	private final int cols;

	private MDA mda;

	public MDADisplayPanel(int rows, int cols, MDA mda) {
		this.rows = rows;
		this.cols = cols;
		this.mda = mda;

		font = new Font("Monospaced", Font.PLAIN, FONT_SIZE);
		setFont(font);
		setBackground(Color.BLACK);
		FontMetrics fm = getFontMetrics(font);
		charWidth = fm.charWidth('W');
		charHeight = fm.getHeight();
		setPreferredSize(new Dimension(this.rows * charWidth, this.cols * charHeight));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(font);
		g.setColor(Color.WHITE);

		for (int row = 0; row < rows; row++) {
			String line = new String(mda.getScreenChars(), row * cols, cols);
			int y = row * charHeight + charHeight - 4; 
			g.drawString(line, 0, y);
		}
	}

}
