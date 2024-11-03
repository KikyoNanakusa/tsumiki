package net.nanakusa.virtualMachine;

import java.util.ArrayList;

class Processor {
  static final int stackSize = 30;
  static final int codeSize = 30;
  static final int IOSize = 100;
  int pc = 0;

  private void loadCode(SubMemory code) {
    Assembler assembler = new Assembler();
    ArrayList<Byte> machineCode = assembler.loadFileAndAssemble("code.txt");

    for (byte b : machineCode) {
      code.write(pc, b);
      pc++;
    }
  }

  public void start(Memory memory) {
    Stack stack = memory.addStack(0);
    SubMemory code = memory.addMemoryRegion("code", stackSize, stackSize + codeSize);
    SubMemory IO = memory.addMemoryRegion("IO", stackSize + codeSize, stackSize + codeSize + IOSize - 1);

    boolean endFlag = false;
    int cmd = 0, op = 0;
    int operand1 = 0, operand2 = 0;

    loadCode(code);

    while (endFlag == false) {
      pc = 0;
      cmd = code.read(pc);
      pc++;
      while (cmd != -1 && cmd != 0) {
        switch (cmd) {
          case Operators.POP:
            op = stack.pop();
            System.out.println("POP " + op);
            break;
          case Operators.PUSH:
            op = code.read(pc);
            pc += 1;
            stack.push(op);
            System.out.println("PUSH " + op);
            break;
          case Operators.ADD:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 + operand2;
            stack.push(op);
            System.out.println("ADD (" + operand1 + " " + operand2 + ")");
            break;
          case Operators.SUB:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 - operand2;
            stack.push(op);
            System.out.println("SUB (" + operand1 + " " + operand2 + ")");
            break;
          case Operators.MUL:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 * operand2;
            stack.push(op);
            System.out.println("MUL (" + operand1 + " " + operand2 + ")");
            break;
          case Operators.DIV:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 / operand2;
            stack.push(op);
            System.out.println("DIV (" + operand1 + " " + operand2 + ")");
            break;
          case Operators.LD:
            operand1 = stack.pop();
            op = memory.read(operand1);
            stack.push(op);
            System.out.println("LD (" + operand1 + "= " + op + ")");
            break;
          case Operators.ST:
            operand2 = stack.pop();
            operand1 = stack.pop();
            memory.write(operand2, operand1);
            System.out.println("ST (" + operand1 + " " + operand2 + ")");
            break;
          case Operators.JNZ:
            operand2 = stack.pop(); // jump adress
            operand1 = stack.pop(); // flag
            System.out.println("JNZ (" + operand1 + " " + operand2 + ")");
            if (operand1 != 0) {
              pc = operand2;
            }
            break;
          case Operators.JZ:
            operand2 = stack.pop();
            operand1 = stack.pop(); // flag
            System.out.println("JZ (" + operand1 + " " + operand2 + ")");
            if (operand1 == 0) {
              pc = operand2;
            }
            break;
          case Operators.JMP:
            operand1 = stack.pop();
            System.out.println("JMP (" + operand1 + ")");
            pc = operand1;
            break;
          case Operators.LESS:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 < operand2 ? 1 : 0;
            stack.push(op);
            System.out.println("LESS (" + operand1 + ", " + operand2 + ")");
            break;
          case Operators.LEQ:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 <= operand2 ? 1 : 0;
            stack.push(op);
            System.out.println("LEQ (" + operand1 + ", " + operand2 + ")");
            break;
          case Operators.EQ:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 == operand2 ? 1 : 0;
            stack.push(op);
            System.out.println("EQ (" + operand1 + ", " + operand2 + ")");
            break;
          case Operators.NEQ:
            operand2 = stack.pop();
            operand1 = stack.pop();
            op = operand1 != operand2 ? 1 : 0;
            stack.push(op);
            System.out.println("NEQ (" + operand1 + ", " + operand2 + ")");
            break;
          default:
            System.out.println("Invalid command");
            break;
        }
        cmd = code.read(pc);
        pc++;
      }
      endFlag = true;
    }
  }
}
