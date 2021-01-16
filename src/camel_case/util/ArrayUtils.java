package camel_case.util;

public class ArrayUtils {
  public static <T> T[] shuffle(T[] array) {
    for (int i = array.length - 1; i > 0; i--) {
      int index = BetterRandom.nextInt(i + 1);

      T temp = array[index];
      array[index] = array[i];
      array[i] = temp;
    }

    return array;
  }

  public static <T> T[] reverse(T[] array) {
    int left = 0;
    int right = array.length - 1;

    while (left < right) {
      T temp = array[left];
      array[left] = array[right];
      array[right] = temp;

      left++;
      right--;
    }

    return array;
  }
}
