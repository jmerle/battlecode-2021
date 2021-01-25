package camel_case_v7_sprint_2.util;

public class Color {
  public static final Color RED = new Color(255, 0, 0);
  public static final Color GREEN = new Color(0, 255, 0);
  public static final Color BLUE = new Color(0, 0, 255);
  public static final Color YELLOW = new Color(255, 255, 0);
  public static final Color PURPLE = new Color(255, 0, 255);
  public static final Color WHITE = new Color(255, 255, 255);
  public static final Color BLACK = new Color(0, 0, 0);

  public final int r;
  public final int g;
  public final int b;

  public Color(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }
}
