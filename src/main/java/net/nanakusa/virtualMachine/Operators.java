package net.nanakusa.virtualMachine;

public class Operators {
  public static final byte POP = 0x01;
  public static final byte PUSH = 0x02;
  public static final byte ADD = 0x03;
  public static final byte SUB = 0x04;
  public static final byte MUL = 0x05;
  public static final byte DIV = 0x06;
  public static final byte RUN = 0x07;
  public static final byte LD = 0x08;
  public static final byte ST = 0x09;
  public static final byte JNZ = 0x0A;
  public static final byte JZ = 0x0B;
  public static final byte JMP = 0x0C;
  public static final byte LESS = 0x0D;
  public static final byte LEQ = 0x0E;
  public static final byte END = -0x01;
  public static final byte EQ = 0x0F;
  public static final byte NEQ = 0x10;
}
