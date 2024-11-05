package net.nanakusa.compiler;

import java.util.List;

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
  ND_LVAR,
  ND_RETURN,
  ND_FOR,
  ND_IF,
  ND_ELSE,
  ND_WHILE,
}

public class Node {
  private ND_TYPE type;
  private int val;
  private Node lhs;
  private Node rhs;

  private String name;
  private int offset;

  private Node cond;
  private List<Node> then;
  private Node els = null;

  private Node init;
  private Node inc;

  public void printTree(String indent) {
    System.out.println(indent + "Node Type: " + type);

    if (type == ND_TYPE.ND_NUM) {
      System.out.println(indent + "  Value: " + val);
    } else if (type == ND_TYPE.ND_LVAR) {
      System.out.println(indent + "  Variable Name: " + name);
    }

    if (lhs != null) {
      System.out.println(indent + "  Left:");
      lhs.printTree(indent + "    ");
    }

    if (rhs != null) {
      System.out.println(indent + "  Right:");
      rhs.printTree(indent + "    ");
    }
  }

  public Node(ND_TYPE type) {
    this.type = type;
  }

  public Node(ND_TYPE type, int val) {
    this.type = type;
    this.val = val;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLhs(Node node) {
    this.lhs = node;
  }

  public void setRhs(Node node) {
    this.rhs = node;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public void setCond(Node cond) {
    this.cond = cond;
  }

  public void setThen(List<Node> then) {
    this.then = then;
  }

  public void setEls(Node els) {
    this.els = els;
  }

  public void setInit(Node init) {
    this.init = init;
  }

  public void setInc(Node inc) {
    this.inc = inc;
  }

  public int getOffset() {
    return this.offset;
  }

  public String getName() {
    return this.name;
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

  public Node getCond() {
    return cond;
  }

  public List<Node> getThen() {
    return then;
  }

  public Node getEls() {
    return els;
  }

  public Node getInit() {
    return init;
  }

  public Node getInc() {
    return inc;
  }
}
