package net.nanakusa.virtualMachine;

import javax.swing.SwingUtilities;

import net.nanakusa.virtualDisplay.vMDA.MDA_IO;

public class Main {

  public static void main(String[] args) {
    Memory memory = new Memory(4000);

    // MemoryViewer viewer = new MemoryViewer(memory, 30);

    Processor processor = new Processor();
    int result = processor.start(memory);

    // int[] mem = memory.getMemory();
    // for (int i = 0; i < mem.length; i++) {
    // System.out.println(i + ": " + mem[i]);
    // }

    // SubMemory mdaMem = memory.addMemoryRegion("MDA memory", 1000, 3999);
    // MDA_IO mda_io = new MDA_IO(mdaMem);
    // mda_io.initDisplay();
    // SwingUtilities.invokeLater(() -> {
    //   String message = "Hello, World!";
    //   for (int i = 0; i < message.length(); i++) {
    //     mda_io.write(i, message.charAt(i));
    //   }
    // });

    System.out.println("\nResult(Top of Stack)");
    if (result == -1) {
      System.out.println("Stack has no elements left");
    } else {
      System.out.println(result);
    }
  }
}
