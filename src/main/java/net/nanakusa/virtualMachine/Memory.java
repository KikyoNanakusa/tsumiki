package net.nanakusa.virtualMachine;

import java.util.List;
import java.util.ArrayList;

public class Memory {
  private final int MEM_SIZE;
  private int memory[];
  private List<MemoryRegion> memoryMap;
  private final List<MemoryChangeListener> listeners = new ArrayList<>();

  public Memory(int memSize) {
    this.MEM_SIZE = memSize;

    if (MEM_SIZE > 0) {
      this.memory = new int[MEM_SIZE];
    } else {
      this.memory = null;
    }

    this.memoryMap = new ArrayList<>();

    for (int i = 0; i < MEM_SIZE; i++) {
      this.memory[i] = 0;
    }
  }

  protected Memory() {
    MEM_SIZE = -1;
    this.memoryMap = new ArrayList<>();
  }

  public int[] getMemory() {
    return this.memory.clone();
  }

  public int getSize() {
    return this.MEM_SIZE;
  }

  public void clearMemory() {
    for (int i = 0; i < MEM_SIZE; i++) {
      this.memory[i] = 0;
    }
  }

  public int read(int addr) {
    if (addr < 0 || addr >= MEM_SIZE) {
      throw new IllegalArgumentException("Invalid Address: " + addr);
    }
    return this.memory[addr];
  }

  public Boolean write(int addr, int value) {
    if (addr < 0 || addr >= MEM_SIZE) {
      return false;
    }
    this.memory[addr] = value;
    notifyListeners();
    return true;
  }

  private void notifyListeners() {
    for (MemoryChangeListener listener : listeners) {
      listener.memoryChanged();
    }
  }

  public void addChangeListener(MemoryChangeListener listener) {
    listeners.add(listener);
  }

  public SubMemory addMemoryRegion(String name, int start, int end) {
    if (start < 0 || start >= MEM_SIZE || end < 0 || end >= MEM_SIZE || start > end) {
      throw new IllegalArgumentException("Invalid Address");
    }
    this.memoryMap.add(new MemoryRegion(name, start, end));
    return this.getSubMemory(start, end, name);
  }

  public List<MemoryRegion> getMemoryMap() {
    return this.memoryMap;
  }

  public Stack addStack(int start) {
    if (start < 0 || start >= MEM_SIZE) {
      throw new IllegalArgumentException("Invalid Address");
    }

    this.memoryMap.add(new MemoryRegion("Stack", start));
    return new Stack(this, start);
  }

  private SubMemory getSubMemory(int start, int end, String name) {
    if (start < 0 || end >= MEM_SIZE || start > end) {
      throw new IllegalArgumentException("Invalid Address");
    }

    return new SubMemory(this, start, end, name);
  }
}
