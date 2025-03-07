package net.nanakusa.virtualDisplay.vMDA;

import net.nanakusa.virtualMachine.SubMemory;

public class MDA_IO extends SubMemory {
	private static final int ROWS = 25;
	private static final int COLS = 80;

	private MDA mda;

	public MDA_IO(SubMemory memory) {
		super(memory, 0, ROWS * COLS, "MDAMemory");
		memory.RegisterMemoryRegion(this);
		System.out.println("MDA_IO created");
		System.out.println("Parent Memory: " + memory.toString());
		System.out.println("Parent Memory Regions: " + memory.getMemoryMap());
	}

   public void initDisplay() {
        this.mda = new MDA(this);
        clearScreen();
    }

	private void clearScreen() {
		for (int i = 0; i < ROWS * COLS; i++) {
			writeCharAt(i, ' ');
		}
	}

	private void writeCharAt(int index, char c) {
		if (index < 0 || index >= ROWS * COLS) {
			throw new IllegalAccessError("Invalid index: " + index);
		}
		write(index, c);
	}

	@Override 
	public Boolean write(int addr, int value) {
		if (value < 0 || value > 255) {
			throw new IllegalArgumentException("Invalid value: " + value);
		}
		Boolean result = super.write(addr, value);
		this.mda.updateDisplay();
		return result;
	}

	public void printMemory() {
		for (int i : this.getMemory()) {
			if (i != 32) {
				System.out.print("\u001b[00;31m" + i + "\u001b[00m");
			} else {
				System.out.print(i);
			}
		}
	}
}