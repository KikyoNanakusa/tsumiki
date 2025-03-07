package net.nanakusa.virtualDisplay.vMDA;

public class MDA {
	private static final int ROWS = 80;	
	private static final int COLS = 25;

	private final MDAFrame frame;
	private final MDA_IO memory;

	public MDA(MDA_IO memory) {
		this.memory = memory;
		this.frame = new MDAFrame(ROWS, COLS, this);
	}

	public void updateDisplay() {
		frame.repaint();
	}

	public char[] getScreenChars() {
		char[] screenChars = new char[ROWS * COLS];
		for (int i = 0; i < ROWS * COLS; i++) {
			screenChars[i] = (char) memory.read(i);
		}
		return screenChars;
	}
}