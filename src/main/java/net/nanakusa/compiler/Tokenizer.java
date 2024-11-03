package net.nanakusa.compiler;

import java.util.ArrayList;

enum TK_TYPE {
  TK_NUM,
  TK_RESERVED,
};

class Token {
  private TK_TYPE type;
  private String str;

  public Token(TK_TYPE type, String str) {
    this.type = type;
    this.str = str;
  }

  public Token(TK_TYPE type, char c) {
    this.type = type;
    this.str = Character.toString(c);
  }

  public TK_TYPE getType() {
    return this.type;
  }

  public String getStr() {
    return this.str;
  }
}

class Tokenizer {
  private static String isReservedString(String str) {
    String[] reservedTokens = { "for", "while", "if", "<=", ">=", "==", "!=" };

    for (String token : reservedTokens) {
      if (str.length() >= token.length() && str.substring(0, token.length()).equals(token)) {
        return token;
      }
    }
    return "";
  }

  private static boolean isReservedChar(char ch) {
    char[] reservedTokens = { '+', '-', '*', '/', '(', ')', '<', '>' };

    for (char token : reservedTokens) {
      if (ch == token) {
        return true;
      }
    }
    return false;
  }

  static void tokenize(String code, ArrayList<Token> token) {
    int p = 0;
    while (p < code.length()) {

      // 空白はスキップ
      if (code.charAt(p) == ' ') {
        p++;
        continue;
      }

      if (!isReservedString(code.substring(p)).isBlank()) {
        String tokenStr = isReservedString(code.substring(p));
        Token tok = new Token(TK_TYPE.TK_RESERVED, tokenStr);
        token.add(tok);
        p += tokenStr.length();
        continue;
      }

      if (isReservedChar(code.charAt(p))) {
        Token tok = new Token(TK_TYPE.TK_RESERVED, code.charAt(p));
        token.add(tok);
        p++;
        continue;
      }

      // number
      if (Character.isDigit(code.charAt(p))) {
        String num = "";

        while (p < code.length() && Character.isDigit(code.charAt(p))) {
          num += code.charAt(p);
          p++;
        }
        Token tok = new Token(TK_TYPE.TK_NUM, String.valueOf(num));
        token.add(tok);
        continue;
      }
    }
  }

  static boolean consumeToken(ArrayList<Token> token, String expected) {
    if (token.size() > 0 && token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals(expected)) {
      token.remove(0);
      return true;
    } else {
      return false;
    }
  }

  static boolean expectToken(ArrayList<Token> token, String expected) {
    if (token.size() > 0 && token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals(expected)) {
      return true;
    } else {
      return false;
    }
  }
}
