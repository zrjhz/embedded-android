package car.car2024.Utils.Algorithm;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KMP {
    public static byte[] gerCorrect(String str1, String str2) {
        String regex = "->.{0,9}<-";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(str2);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group();
            stringBuffer.append(group);
            stringBuffer.append(" ");
        }
        System.out.println(stringBuffer);
        String s = stringBuffer.toString();
        String[] strings = s.split(" ");
        String regex2 = "\\w+";
        Pattern compile1 = Pattern.compile(regex2);
        for (int i = 0; i < strings.length; i++) {
            Matcher matcher1 = compile1.matcher(strings[i]);
            if (matcher1.find()) {
                String group = matcher1.group();
                strings[i] = group;
            }
        }
        System.out.println(Arrays.toString(strings));
        byte[] bytes = new byte[6];
        for (int i = 0; i < strings.length; i++) {
            Pattern pattern = Pattern.compile(strings[i]);
            Matcher matcher1 = pattern.matcher(str1);
            while (matcher1.find()) {
                bytes[i] = (byte) matcher1.start();
            }
        }
        System.out.println(Arrays.toString(bytes));
        return bytes;
    }
}
