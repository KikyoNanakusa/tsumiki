package net.nanakusa.assembler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import net.nanakusa.virtualMachine.Operators;

public class Assembler {
  private String code = "";
  private Map<String, Integer> labelMap = new HashMap<>();

  private List<List<String>> preprocess(String code) {
    String[] lines = code.split("\n");
    List<List<String>> tokenizedLines = new ArrayList<>();

    int totalBytes = 0;
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];

      if (line.trim().isEmpty()) {
        continue;
      }

      ArrayList<String> tokenizedLine = new ArrayList<>(Arrays.asList(line.trim().split("\\s+")));
      enumrateLabel(tokenizedLine, totalBytes);

      if (tokenizedLine.size() == 0) {
        continue;
      }
      tokenizedLines.add(tokenizedLine);
      totalBytes += tokenizedLine.size();
    }

    // for (String label : labelMap.keySet()) {
    // System.out.println(label + " -> " + labelMap.get(label));
    // }

    return tokenizedLines;
  }

  private void enumrateLabel(List<String> tokens, int lineIndex) {
    String token = tokens.get(0);
    if (token.endsWith(":")) {
      labelMap.put(token.substring(0, token.length() - 1), lineIndex);
      tokens.remove(0);
    }
  }

  public ArrayList<Byte> assemble(String code) {
    List<List<String>> tokenizedLines = preprocess(code);
    ArrayList<Byte> machineCode = new ArrayList<>();

    for (List<String> line : tokenizedLines) {
      Byte[] stmt = convertToMachineCode(line);
      machineCode.addAll(Arrays.asList(stmt));
    }
    writeToFile("output.bin", machineCode);
    return machineCode;
  }

  private Byte[] convertToMachineCode(List<String> tokens) {
    String token = tokens.get(0);
    token = token.toLowerCase();
    String label = "";
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
        label = tokens.get(1).substring(0, tokens.get(1).length() - 1);
        if (labelMap.containsKey(label)) {
          return new Byte[] { Operators.JNZ, labelMap.get(label).byteValue() };
        } else {
          return new Byte[] { Operators.JNZ, Byte.parseByte(tokens.get(1)) };
        }
      case "jz":
        label = tokens.get(1).substring(0, tokens.get(1).length() - 1);
        if (labelMap.containsKey(label)) {
          return new Byte[] { Operators.JZ, labelMap.get(label).byteValue() };
        } else {
          return new Byte[] { Operators.JZ, Byte.parseByte(tokens.get(1)) };
        }
      case "jmp":
        label = tokens.get(1);
        if (labelMap.containsKey(label)) {
          return new Byte[] { Operators.JMP, labelMap.get(label).byteValue() };
        } else {
          return new Byte[] { Operators.JMP, Byte.parseByte(tokens.get(1)) };
        }
      case "end":
        return new Byte[] { Operators.END };
      case "push":
        return new Byte[] { Operators.PUSH, Byte.parseByte(tokens.get(1)) };
      case "lt":
        return new Byte[] { Operators.LESS };
      case "leq":
        return new Byte[] { Operators.LEQ };
      case "eq":
        return new Byte[] { Operators.EQ };
      case "neq":
        return new Byte[] { Operators.NEQ };
      default:
        throw new IllegalArgumentException("Unknown operator: " + token);
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

  public void writeToFile(String outputPath, ArrayList<Byte> machineCode) {
    try (FileOutputStream fos = new FileOutputStream(outputPath)) {
      for (Byte b : machineCode) {
        fos.write(b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not write to file: " + outputPath, e);
    }
  }
}
