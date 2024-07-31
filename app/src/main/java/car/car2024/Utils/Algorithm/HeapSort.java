package car.car2024.Utils.Algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeapSort {
    private static void swap(byte[] a, int i, int j) {
        byte t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
    private static void shiftUp(byte[] a, int k) {
        while (k > 0 && a[(k - 1) >> 1] < a[k]) {
            swap(a, k, (k - 1) >> 1);
            k = (k - 1) >> 1;
        }
    }
    private static void shiftDown(byte[] a, int k) {
        while (((k + 1) << 1) < a.length) {
            int j = ((k + 1) << 1) - 1;
            if (j + 1 < a.length && a[j + 1] > a[j])
                j++;

            if (a[k] >= a[j]) break;
            swap(a, k, j);
            k = j;
        }
    }

    public static byte[] sort(byte[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = arr.length - 1; j > 0; j--) {
                shiftUp(arr, j);
            }
            if (i != 1)
                swap(arr, 0, arr.length - 1);
        }
        byte[] a = new byte[6];
        for (int i = a.length - 2, j = arr.length - 1; i >= 0; i--, j--) {
            a[i] = arr[j];
        }
        a[5] = (byte) 0xE3;
        return a;
    }

    public static String getCurrentString(String strings) {
        String con = "[A-F1-9a-f]";
        Pattern pattern2 = Pattern.compile(con);
        Matcher matcher2 = pattern2.matcher(strings);
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher2.find()) {
            stringBuilder.append(matcher2.group());
        }
        return stringBuilder.toString();
    }
}
