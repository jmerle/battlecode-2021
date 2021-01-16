package camel_case.util;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Painter {
  private final RobotController rc;
  private final MapInfo mapInfo;

  private final double[][] colors = {
    {255, 36, 0},
    {232, 29, 29},
    {232, 183, 29},
    {227, 232, 29},
    {29, 232, 64},
    {29, 221, 232},
    {43, 29, 232},
    {221, 0, 243}
  };

  private int r = 0;
  private int g = 0;
  private int b = 0;

  private int currentColorIndex = 0;
  private double ratio = 0.0;

  public Painter(RobotController rc, MapInfo mapInfo) {
    this.rc = rc;
    this.mapInfo = mapInfo;
  }

  public void paintText(String[] lines) {
    ratio += 0.15;

    if (ratio >= 1.0) {
      ratio = 0.0;
      currentColorIndex = (currentColorIndex + 1) % colors.length;
    }

    double[] current = colors[currentColorIndex];
    double[] next = colors[(currentColorIndex + 1) % colors.length];

    r = (int) Math.round((1 - ratio) * current[0] + ratio * next[0]);
    g = (int) Math.round((1 - ratio) * current[1] + ratio * next[1]);
    b = (int) Math.round((1 - ratio) * current[2] + ratio * next[2]);

    int height = lines.length * 5;
    int baseY = mapInfo.maxY - (mapInfo.height - height) / 2 - 4;

    for (String line : lines) {
      paintLine(line, baseY);
      baseY -= 5;
    }
  }

  private void paintLine(String line, int baseY) {
    int width = line.length() * 3;
    int baseX = mapInfo.minX + (mapInfo.width - width) / 2;

    char[] chars = line.toLowerCase().toCharArray();
    for (char ch : chars) {
      paintCharacter(ch, baseX, baseY);
      baseX += 3;
    }
  }

  private void paintCharacter(char ch, int baseX, int baseY) {
    switch (ch) {
      case 'a':
        paintA(baseX, baseY);
        break;
      case 'b':
        paintB(baseX, baseY);
        break;
      case 'c':
        paintC(baseX, baseY);
        break;
      case 'd':
        paintD(baseX, baseY);
        break;
      case 'e':
        paintE(baseX, baseY);
        break;
      case 'f':
        paintF(baseX, baseY);
        break;
      case 'g':
        paintG(baseX, baseY);
        break;
      case 'h':
        paintH(baseX, baseY);
        break;
      case 'i':
        paintI(baseX, baseY);
        break;
      case 'j':
        paintJ(baseX, baseY);
        break;
      case 'k':
        paintK(baseX, baseY);
        break;
      case 'l':
        paintL(baseX, baseY);
        break;
      case 'm':
        paintM(baseX, baseY);
        break;
      case 'n':
        paintN(baseX, baseY);
        break;
      case 'o':
        paintO(baseX, baseY);
        break;
      case 'p':
        paintP(baseX, baseY);
        break;
      case 'q':
        paintQ(baseX, baseY);
        break;
      case 'r':
        paintR(baseX, baseY);
        break;
      case 's':
        paintS(baseX, baseY);
        break;
      case 't':
        paintT(baseX, baseY);
        break;
      case 'u':
        paintU(baseX, baseY);
        break;
      case 'v':
        paintV(baseX, baseY);
        break;
      case 'w':
        paintW(baseX, baseY);
        break;
      case 'x':
        paintX(baseX, baseY);
        break;
      case 'y':
        paintY(baseX, baseY);
        break;
      case 'z':
        paintZ(baseX, baseY);
        break;
      case '0':
        paint0(baseX, baseY);
        break;
      case '1':
        paint1(baseX, baseY);
        break;
      case '2':
        paint2(baseX, baseY);
        break;
      case '3':
        paint3(baseX, baseY);
        break;
      case '4':
        paint4(baseX, baseY);
        break;
      case '5':
        paint5(baseX, baseY);
        break;
      case '6':
        paint6(baseX, baseY);
        break;
      case '7':
        paint7(baseX, baseY);
        break;
      case '8':
        paint8(baseX, baseY);
        break;
      case '9':
        paint9(baseX, baseY);
        break;
      case '_':
        paint_(baseX, baseY);
        break;
    }
  }

  private void paintA(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
  }

  private void paintB(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 1, baseY + 2);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 1);
    paintLine(baseX + 2, baseY + 4, baseX + 2, baseY + 3);
    paintLine(baseX + 2, baseY + 3, baseX + 1, baseY + 2);
    paintLine(baseX + 2, baseY + 1, baseX + 1, baseY + 2);
  }

  private void paintC(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paintD(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 1, baseY);
    paintLine(baseX, baseY + 4, baseX + 1, baseY + 4);
    paintLine(baseX + 2, baseY + 1, baseX + 2, baseY + 3);
    paintLine(baseX + 1, baseY, baseX + 2, baseY + 1);
    paintLine(baseX + 1, baseY + 4, baseX + 2, baseY + 3);
  }

  private void paintE(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
  }

  private void paintF(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 1, baseY + 2);
  }

  private void paintG(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 2);
    paintLine(baseX + 1, baseY + 2, baseX + 2, baseY + 2);
  }

  private void paintH(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
  }

  private void paintI(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX + 1, baseY, baseX + 1, baseY + 4);
  }

  private void paintJ(int baseX, int baseY) {
    paintLine(baseX + 1, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX, baseY, baseX, baseY + 1);
  }

  private void paintK(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 4);
  }

  private void paintL(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paintM(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 1, baseY + 2);
    paintLine(baseX + 2, baseY + 4, baseX + 1, baseY + 2);
  }

  private void paintN(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY);
  }

  private void paintO(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paintP(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX + 2, baseY + 2, baseX + 2, baseY + 4);
  }

  private void paintQ(int baseX, int baseY) {
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX, baseY + 2, baseX, baseY + 4);
  }

  private void paintR(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX + 2, baseY + 2, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY);
  }

  private void paintS(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX, baseY + 2, baseX, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 2);
  }

  private void paintT(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX + 1, baseY, baseX + 1, baseY + 4);
  }

  private void paintU(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
  }

  private void paintV(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 1, baseY);
    paintLine(baseX + 1, baseY, baseX + 2, baseY + 4);
  }

  private void paintW(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 1, baseY + 2);
    paintLine(baseX + 1, baseY + 2, baseX + 2, baseY);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
  }

  private void paintX(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 2, baseY);
    paintLine(baseX, baseY, baseX + 2, baseY + 4);
  }

  private void paintY(int baseX, int baseY) {
    paintLine(baseX + 1, baseY, baseX + 1, baseY + 2);
    paintLine(baseX + 1, baseY + 2, baseX, baseY + 4);
    paintLine(baseX + 1, baseY + 2, baseX + 2, baseY + 4);
  }

  private void paintZ(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paint0(int baseX, int baseY) {
    paintO(baseX, baseY);
    paintLine(baseX, baseY, baseX + 2, baseY + 4);
  }

  private void paint1(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 1, baseY + 4);
    paintLine(baseX + 1, baseY, baseX + 1, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paint2(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX + 2, baseY + 4, baseX + 2, baseY + 3);
    paintLine(baseX + 2, baseY + 3, baseX, baseY + 1);
    paintLine(baseX, baseY + 1, baseX, baseY);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paint3(int baseX, int baseY) {
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paint4(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX, baseY + 2);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
  }

  private void paint5(int baseX, int baseY) {
    paintS(baseX, baseY);
  }

  private void paint6(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 2);
  }

  private void paint7(int baseX, int baseY) {
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
  }

  private void paint8(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX, baseY + 4);
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY, baseX + 2, baseY);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
  }

  private void paint9(int baseX, int baseY) {
    paintLine(baseX + 2, baseY, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX, baseY + 4);
    paintLine(baseX, baseY + 4, baseX + 2, baseY + 4);
    paintLine(baseX, baseY + 2, baseX + 2, baseY + 2);
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paint_(int baseX, int baseY) {
    paintLine(baseX, baseY, baseX + 2, baseY);
  }

  private void paintLine(int fromX, int fromY, int toX, int toY) {
    MapLocation from = new MapLocation(fromX, fromY);
    MapLocation to = new MapLocation(toX, toY);
    rc.setIndicatorLine(from, to, r, g, b);
  }

  private void paintDot(int x, int y) {
    rc.setIndicatorDot(new MapLocation(x, y), r, g, b);
  }
}
