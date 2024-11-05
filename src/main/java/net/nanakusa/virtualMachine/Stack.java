package net.nanakusa.virtualMachine;

public class Stack extends SubMemory {
  private int stackPointer = 0;

  public Stack(Memory parentMemory, int basePointer) {
    super(parentMemory, basePointer, "Stack");
  }

  public int pop() {
    int value = read(stackPointer - 1);
    stackPointer--;
    System.out.println("Popped " + value + " from stack");
    return value;
  }

  public void push(int value) {
    write(stackPointer, value);
    stackPointer++;
    System.out.println("Pushed " + value + " to stack");

  }

  public int getStackPointer() {
    return stackPointer;
  }
}
