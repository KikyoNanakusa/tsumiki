package net.nanakusa.compiler;

public class LVar {
  private String name;
  private int offset;

  public LVar(String name, int offset) {
    this.name = name;
    this.offset = offset;
  }

  public String getName() {
    return name;
  }

  public int getOffset() {
    return offset;
  }
}
