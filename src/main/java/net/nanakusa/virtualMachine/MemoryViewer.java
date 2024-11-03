package net.nanakusa.virtualMachine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

class MemoryViewer extends JPanel implements MemoryChangeListener {
  private final Memory memory;
  private final int cellSize = 20;
  private final int columns;
  private final JFrame frame;

  public MemoryViewer(Memory memory, int columns) {
    this.memory = memory;
    this.columns = columns;
    int rows = (int) Math.ceil((double) memory.getSize() / columns);
    this.setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));

    this.frame = new JFrame("Memory Map Viewer");
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.add(this);
    this.frame.pack();
    this.frame.setLocationRelativeTo((Component) null);
    this.frame.setVisible(true);

    memory.addChangeListener(this);
  }

  public JFrame getFrame() {
    return this.frame;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int[] memoryData = this.memory.getMemory();

    for (int i = 0; i < memoryData.length; ++i) {
      g.setColor(this.getColorForValue(memoryData[i]));

      int x = (i % columns) * cellSize;
      int y = (i / columns) * cellSize;
      g.fillRect(x, y, cellSize, cellSize);

      g.setColor(Color.BLACK);
      g.drawRect(x, y, cellSize, cellSize);
    }
  }

  private Color getColorForValue(int value) {
    if (value == 0) {
      return Color.BLACK;
    } else if (value > 0 && value <= 255) {
      int greenValue = Math.min(value, 255);
      return new Color(0, greenValue, 0);
    } else {
      return Color.RED;
    }
  }

  @Override
  public void memoryChanged() {
    this.repaint();
  }
}
