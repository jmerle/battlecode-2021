package camel_case_v2.util;

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
    int size = -1;
    int xOffset = -1;
    int yOffset = -1;

    if (minX != -1 && maxX != -1) {
      size = maxX - minX + 1;
    } else if (minY != -1 && maxY != -1) {
      size = maxY - minY + 1;
    }

    if (minX != -1) {
      xOffset = minX;
    } else if (maxX != -1 && size != -1) {
      xOffset = maxX - size + 1;
    }

    if (minY != -1) {
      yOffset = minY;
    } else if (maxY != -1 && size != -1) {
      yOffset = maxY - size + 1;
    }

    if (size == -1 || xOffset == -1 || yOffset == -1) {
      return null;
    }

    return new MapInfo(size, xOffset, yOffset, minX, maxX, minY, maxY);
  }
}
