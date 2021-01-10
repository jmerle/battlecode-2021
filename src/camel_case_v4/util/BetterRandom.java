package camel_case_v4.util;

public class BetterRandom {
  public static int nextInt(int maxExclusive) {
    return (int) Math.floor(Math.random() * maxExclusive);
  }
}
