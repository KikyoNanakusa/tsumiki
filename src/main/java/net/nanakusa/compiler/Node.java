package net.nanakusa.compiler;

enum ND_TYPE {
  ND_MUL,
  ND_DIV,
  ND_ADD,
  ND_SUB,
  ND_NUM,
  ND_LESS,
  ND_LEQ,
  ND_ASSIGN,
  ND_EQ,
  ND_NEQ,
  ND_IDENT,
  ND_FOR,
  ND_IF,
  ND_WHILE,
  ND_COND,
}

public class Node {
  private ND_TYPE type;
  private int val;
  private Node lhs;
  private Node rhs;

  public Node(ND_TYPE type) {
    this.type = type;
  }

  public Node(ND_TYPE type, int val) {
    this.type = type;
    this.val = val;
  }

  public void setLhs(Node node) {
    this.lhs = node;
  }

  public void setRhs(Node node) {
    this.rhs = node;
  }

  public Node getLhs() {
    return this.lhs;
  }

  public Node getRhs() {
    return this.rhs;
  }

  public int getVal() {
    return val;
  }

  public ND_TYPE getType() {
    return type;
  }
}
