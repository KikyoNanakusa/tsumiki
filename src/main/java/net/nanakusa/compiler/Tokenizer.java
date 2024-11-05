package net.nanakusa.compiler;

import java.util.List;

enum TK_TYPE {
  TK_NUM,
  TK_RESERVED,
  TK_IDENT,
  TL_EOF,
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
    String[] reservedTokens = { "for", "while", "if", "else", "<=", ">=", "==", "!=", "return" };

    for (String token : reservedTokens) {
      if (str.length() >= token.length() && str.substring(0, token.length()).equals(token)) {
        return token;
      }
    }
    return "";
  }

  private static boolean isReservedChar(char ch) {
    char[] reservedTokens = { '+', '-', '*', '/', '(', ')', '<', '>', ';', '=', '{', '}' };

    for (char token : reservedTokens) {
      if (ch == token) {
        return true;
      }
    }
    return false;
  }

  private static boolean isAlpha(char ch) {
    return Character.isAlphabetic(ch);
  }

  private static boolean isAlphaNum(char ch) {
    return Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_';
  }

  static void tokenize(String code, List<Token> token) {
    int p = 0;
    while (p < code.length()) {

      // 空白はスキップ
      if (code.charAt(p) == ' ' || code.charAt(p) == '\n' || code.charAt(p) == '\t') {
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

      if (isAlpha(code.charAt(p))) {
        String ident = "";
        while (p < code.length() && isAlphaNum(code.charAt(p))) {
          ident += code.charAt(p);
          p++;
        }
        Token tok = new Token(TK_TYPE.TK_IDENT, ident);
        token.add(tok);
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

      System.err.println("Unknown token at position " + p + ": " + code.charAt(p));
      throw new Error("Unknown token" + code.charAt(p));
    }

    token.add(new Token(TK_TYPE.TL_EOF, ""));
  }

  static boolean consumeToken(List<Token> token, String expected) {
    if (token.size() > 0 && token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals(expected)) {
      token.remove(0);
      return true;
    } else {
      return false;
    }
  }

  static void expectToken(List<Token> token, String expected) {
    if (token.size() > 0 && token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals(expected)) {
      token.remove(0);
    } else {
      throw new Error("Expected '" + expected + "'");
    }
  }

  public static boolean atEof(List<Token> token) {
    return token.size() == 0 || token.get(0).getType() == TK_TYPE.TL_EOF;
  }
}
