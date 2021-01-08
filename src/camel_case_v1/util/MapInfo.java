package camel_case_v1.util;

public class MapInfo {
  public final int size;
  public final int xOffset;
  public final int yOffset;

  public final int minX;
  public final int maxX;
  public final int minY;
  public final int maxY;

  public MapInfo(int size, int xOffset, int yOffset, int minX, int maxX, int minY, int maxY) {
    this.size = size;
    this.xOffset = xOffset;
    this.yOffset = yOffset;

    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }

  public static MapInfo parseBoundaries(int minX, int maxX, int minY, int maxY) {
    if (maxX == -1 || maxY == -1) {
      return null;
    }

    if (minX == -1 && minY == -1) {
      return null;
    }

    int size;
    if (minX == -1) {
      size = maxY - minY + 1;
    } else {
      size = maxX - minX + 1;
    }

    int xOffset = maxX - size + 1;
    int yOffset = maxY - size + 1;

    return new MapInfo(size, xOffset, yOffset, minX, maxX, minY, maxY);
  }
}
