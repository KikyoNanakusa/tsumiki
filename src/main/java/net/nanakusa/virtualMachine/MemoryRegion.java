public class MemoryRegion {
  private final String name;
  private final int start;
  private final int end;

  public MemoryRegion(String name, int start, int end) {
    this.name = name;
    this.start = start;
    this.end = end;
  }

  public MemoryRegion(String name, int start) {
    this.name = name;
    this.start = start;
    this.end = -1;
  }

  public String getName() {
    return this.name;
  }

  public int getStart() {
    return this.start;
  }

  public int getEnd() {
    return this.end;
  }

  @Override
  public String toString() {
    return "MemoryRegion{" +
        "name='" + name + '\'' +
        ", start=" + start +
        ", end=" + end +
        '}';
  }
}
