package net.nanakusa.virtualMachine;

import java.util.ArrayList;
import net.nanakusa.assembler.Assembler;

class Processor {
  static final int stackSize = 30;
  static final int codeSize = 100;
  static final int IOSize = 100;
  int pc = 0;

  private void loadCode(SubMemory code) {
    Assembler assembler = new Assembler();
    ArrayList<Byte> machineCode = assembler.loadFileAndAssemble("output.s");

    for (byte b : machineCode) {
      code.write(pc, b);
      pc++;
    }
  }

  public int start(Memory memory) {
    Stack stack = memory.registerStack(0);
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
        // System.out.println("PC: " + pc + " CMD: " + cmd);
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
            System.out.println("LD (addr: " + operand1 + " value: " + op + ")");
            break;
          case Operators.ST:
            operand2 = stack.pop();
            operand1 = stack.pop();
            memory.write(operand1, operand2);
            System.out.println("ST (addr: " + operand1 + " value: " + operand2 + ")");
            break;
          case Operators.JNZ:
            operand1 = stack.pop();
            operand2 = code.read(pc);
            pc += 1;
            System.out.println("JNZ flag = " + operand1 + " addr = " + operand2);
            if (operand1 != 0) {
              pc = operand2;
            }
            break;
          case Operators.JZ:
            operand1 = stack.pop();
            operand2 = code.read(pc);
            pc += 1;
            System.out.println("JZ flag = " + operand1 + " addr = " + operand2);
            if (operand1 == 0) {
              pc = operand2;
            }
            break;
          case Operators.JMP:
            operand1 = code.read(pc);
            pc += 1;
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

    // Return the top of the stack
    if (stack.getStackPointer() == 0) {
      return -1;
    } else {
      return stack.pop();
    }
  }
}
