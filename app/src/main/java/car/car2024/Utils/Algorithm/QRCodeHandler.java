package car.car2024.Utils.Algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRCodeHandler {
    public static int getDigitSum(String str) {
        String regex = "\\d";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count += Integer.parseInt(matcher.group());
        }
        return count;
    }

    public static String getMoveBackCountPlaces(String str, int count) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'A' && chars[i] <= 'Z' - count) {
                chars[i] = (char) (chars[i] + count);
            } else if (chars[i] >= 'Z' - count && chars[i] <= 'Z') {
                chars[i] = (char) (chars[i] - 26 + count);
            }
        }
        return new String(chars);
    }

    public static int[] getIntArrayInString(String str) {
        String regex = "\\d";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(str);
        int[] ints = new int[str.length()];
        int i = 0;
        while (matcher.find()) {
            ints[i++] = Integer.parseInt(matcher.group());
        }
        return ints;
    }

    public static byte[] getBytesInStringWithInts(int[] arr, String str) {
        char[] chars = str.toCharArray();
        char[] charsResult = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            charsResult[i] = chars[arr[i]];
        }
        String s = new String(charsResult);
        return s.getBytes();
    }
}
