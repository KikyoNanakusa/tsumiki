package net.nanakusa.virtualMachine;

public class SubMemory extends Memory {
  private final Memory parentMemory;
  private final String name;
  private final int start;
  private final int end;

  public SubMemory(Memory parentMemory, int start, int end, String name) {
    super(end - start + 1);
    this.name = name;
    this.parentMemory = parentMemory;
    this.start = start;
    this.end = end;
  }

  public SubMemory(Memory parentMemory, int start, String name) {
    super(-1);
    this.name = name;
    this.parentMemory = parentMemory;
    this.start = start;
    this.end = -1;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return "SubMemory{" +
        "name='" + name + '\'' +
        "parentMemory" + parentMemory +
        ", start=" + start +
        ", end=" + end +
        '}';
  }

  @Override
  public int read(int addr) {
    int absAddr = start + addr;
    if (absAddr < start) {
      if (absAddr > end && end != 1) {
        throw new IllegalArgumentException("Invalid Address: " + absAddr + " in " + name + " region.");
      }
    }
    // System.out.println("Reading from " + name + " at address " + absAddr);
    return parentMemory.read(absAddr);
  }

  @Override
  public Boolean write(int addr, int value) {
    int absAddr = start + addr;
    if (absAddr < start) {
      if (absAddr > end && end != 1) {
        throw new IllegalArgumentException("Invalid Address: " + absAddr + " in " + name + " region.");
      }
    }
    // System.out.println("Writing to " + name + " at address " + absAddr);
    return parentMemory.write(absAddr, value);
  }

  @Override
  public void clearMemory() {
    for (int i = start; i <= end; i++) {
      parentMemory.write(i, 0);
    }
  }
}
