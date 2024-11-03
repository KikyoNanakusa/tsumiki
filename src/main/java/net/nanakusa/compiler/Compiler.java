package net.nanakusa.compiler;

import java.io.*;
import java.util.ArrayList;

public class Compiler {
  static ArrayList<Token> token;

  public static void main(String[] args) {
    token = new ArrayList<Token>();
    String code = getCode();
    Tokenizer.tokenize(code, token);
    Parser.token = token;
    Node node = Parser.parse();
    CodeGenerator.codegen(node);
  }

  private static String getCode() {
    String buf = "";
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      System.err.print("expr:");
      buf = br.readLine();
    } catch (Exception e) {
      System.err.println(e);
    }

    return buf;
  }
}
