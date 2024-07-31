package car.car2024.Utils.Algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String getLetter(String strings) {
        String con = "[a-zA-Z]";
        Pattern pattern2 = Pattern.compile(con);
        Matcher matcher2 = pattern2.matcher(strings);
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher2.find()) {
            stringBuilder.append(matcher2.group());
        }
        return stringBuilder.toString();
    }

    public static String getCurrentString2(String strings) {
        String con = "\\w?\\w?$";
        Pattern pattern2 = Pattern.compile(con);
        Matcher matcher2 = pattern2.matcher(strings);
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher2.find()) {
            stringBuilder.append(matcher2.group());
        }
        return stringBuilder.toString();
    }

}
