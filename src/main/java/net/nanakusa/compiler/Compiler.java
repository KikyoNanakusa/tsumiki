package net.nanakusa.compiler;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
  static ArrayList<Token> token;

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: java net.nanakusa.compiler.Compiler <file path>");
      System.exit(1);
    }

    token = new ArrayList<Token>();
    String code = getCode(args[0]);
    Tokenizer.tokenize(code, token);
    Parser.token = token;
    Node node = Parser.parse();
    CodeGenerator.codegen(node);
  }

  private static String getCode(String filePath) {
    StringBuilder code = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        code.append(line).append("\n");
      }
    } catch (IOException e) {
      System.err.println("Failed to read file: " + e.getMessage());
    }
    return code.toString().trim();
  }
}
