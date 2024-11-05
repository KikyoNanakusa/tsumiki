package net.nanakusa.compiler;

enum TK_TYPE {
  TK_NUM,
  TK_RESERVED,
  TK_IDENT,
  TL_EOF,
};

public class Token {
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
