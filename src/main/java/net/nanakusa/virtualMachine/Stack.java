package net.nanakusa.virtualMachine;

public class Stack extends SubMemory {
  private int stackPointer = 0;

  public Stack(Memory parentMemory, int basePointer) {
    super(parentMemory, basePointer, "Stack");
  }

  public int pop() {
    int value = read(stackPointer - 1);
    stackPointer--;
    return value;
  }

  public void push(int value) {
    write(stackPointer, value);
    stackPointer++;
  }

  public int getStackPointer() {
    return stackPointer;
  }
}
