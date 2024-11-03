package net.nanakusa.virtualMachine;

public class Main {
  static final int stackSize = 30;
  static final int codeSize = 30;
  static final int IOSize = 100;

  public static void main(String[] args) {
    Memory memory = new Memory(stackSize + codeSize + IOSize);

    // MemoryViewer viewer = new MemoryViewer(memory, 30);

    Processor processor = new Processor();
    int result = processor.start(memory);
    if (result == -1) {
      System.out.println("Stack has no elements left");
    } else {
      System.out.println(result);
    }
  }
}
