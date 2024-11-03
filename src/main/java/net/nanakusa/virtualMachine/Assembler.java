package net.nanakusa.virtualMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Assembler {
  private String code = "";

  public ArrayList<Byte> assemble(String code) {
    ArrayList<Byte> machineCode = new ArrayList<>();
    String[] lines = code.split("\n");

    for (String line : lines) {
      if (line.trim().isEmpty()) {
        continue;
      }

      String[] tokens = line.trim().split("\\s+");
      Byte[] operator = convertToMachineCode(tokens);
      machineCode.addAll(Arrays.asList(operator));
    }
    return machineCode;
  }

  private Byte[] convertToMachineCode(String[] tokens) {
    String token = tokens[0];
    token = token.toLowerCase();
    switch (token) {
      case "pop":
        return new Byte[] { Operators.POP };
      case "add":
        return new Byte[] { Operators.ADD };
      case "sub":
        return new Byte[] { Operators.SUB };
      case "mul":
        return new Byte[] { Operators.MUL };
      case "div":
        return new Byte[] { Operators.DIV };
      case "run":
        return new Byte[] { Operators.RUN };
      case "ld":
        return new Byte[] { Operators.LD };
      case "st":
        return new Byte[] { Operators.ST };
      case "jnz":
        return new Byte[] { Operators.JNZ };
      case "jz":
        return new Byte[] { Operators.JZ };
      case "jmp":
        return new Byte[] { Operators.JMP };
      case "end":
        return new Byte[] { Operators.END };
      default:
        throw new IllegalArgumentException("Unknown operator: " + token);
      case "push":
        return new Byte[] { Operators.PUSH, Byte.parseByte(tokens[1]) };
      case "lt":
        return new Byte[] { Operators.LESS };
      case "leq":
        return new Byte[] { Operators.LEQ };
      case "eq":
        return new Byte[] { Operators.EQ };
      case "neq":
        return new Byte[] { Operators.NEQ };
    }
  }

  public void loadFile(String path) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(path));
      String code = String.join("\n", lines);
      this.code = code;
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not read file: " + path);
    }
  }

  public ArrayList<Byte> loadFileAndAssemble(String path) {
    loadFile(path);
    return assemble(code);
  }
}
