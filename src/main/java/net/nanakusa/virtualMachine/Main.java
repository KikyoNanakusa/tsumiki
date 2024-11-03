class Main {
  static final int stackSize = 30;
  static final int codeSize = 30;
  static final int IOSize = 100;

  public static void main(String[] args) {
    Memory memory = new Memory(stackSize + codeSize + IOSize);

    MemoryViewer viewer = new MemoryViewer(memory, 30);

    Processor processor = new Processor();
    processor.start(memory);

  }
}
