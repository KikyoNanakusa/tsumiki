package net.nanakusa.virtualMachine;

public class Main {

  public static void main(String[] args) {
    Memory memory = new Memory(300);

    // MemoryViewer viewer = new MemoryViewer(memory, 30);

    Processor processor = new Processor();
    int result = processor.start(memory);

    // int[] mem = memory.getMemory();
    // for (int i = 0; i < mem.length; i++) {
    // System.out.println(i + ": " + mem[i]);
    // }

    System.out.println("\nResult(Top of Stack)");
    if (result == -1) {
      System.out.println("Stack has no elements left");
    } else {
      System.out.println(result);
    }
  }
}
