package camel_case.util;

import battlecode.common.MapLocation;

public class MapInfo {
  public final int width;
  public final int height;

  public final int xOffset;
  public final int yOffset;

  public final int minX;
  public final int maxX;
  public final int minY;
  public final int maxY;

  public MapInfo(int width, int height, int xOffset, int yOffset) {
    this.width = width;
    this.height = height;

    this.xOffset = xOffset;
    this.yOffset = yOffset;

    minX = xOffset;
    maxX = xOffset + width - 1;
    minY = yOffset;
    maxY = yOffset + height - 1;
  }

  public int encodeLocation(MapLocation location) {
    return (location.y - yOffset) * 64 + (location.x - xOffset);
  }

  public MapLocation decodeLocation(int value) {
    int x = value % 64;
    int y = value / 64;

    return new MapLocation(x + xOffset, y + yOffset);
  }

  public static MapInfo parseBoundaries(int minX, int maxX, int minY, int maxY) {
    if (minX == -1 || maxX == -1 || minY == -1 || maxY == -1) {
      return null;
    }

    int width = maxX - minX + 1;
    int height = maxY - minY + 1;

    return new MapInfo(width, height, minX, minY);
  }
}
