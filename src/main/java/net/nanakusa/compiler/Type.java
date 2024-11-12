package net.nanakusa.compiler;

class Type {
  private LVAR_TYPE type;
  private int size;

  // this can be null if the type is not a list
  private Type ListType;

  public Type(LVAR_TYPE type, int size) {
    this.type = type;
    this.size = size;
  }

  public Type(LVAR_TYPE type, Type listType, int listSize) {
    this.type = type;
    this.ListType = listType;
    this.size = listType.getSize() * listSize;
  }

  public static Type INT = new Type(LVAR_TYPE.INT, 1);

  public static Type LIST(Type type, int size) {
    return new Type(LVAR_TYPE.LIST, type, size);
  }

  public int getSize() {
    return size;
  }

  public Type getListType() {
    if (type != LVAR_TYPE.LIST) {
      throw new RuntimeException("Type is not a list");
    }

    return ListType;
  }

  public LVAR_TYPE getType() {
    return type;
  }
}
