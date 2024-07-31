package car.car2024.Utils.DataHandle;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataExtract {
    /************************************************************************************************************
     【函  数】:extractSubstring
     【参  数】:input 输入字符串
     【返  回】:处理后结果
     【简  例】:extractSubstring("/-A*1xB2C|3-<D4mE-5/");  返回A1B2C3D4E5
     【说  明】:取出大写字母和数字
     ************************************************************************************************************/
    public static String extractSubstring(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c) || Character.isDigit(c)) result.append(c);
        }
        return result.toString();
    }

    /************************************************************************************************************
     【函  数】:findLongestCommonSubstring
     【参  数】:qrCodes 字符串数组
     【返  回】:处理后结果
     【简  例】:findLongestCommonSubstring({"A1B2C3D4E5", "A2B2C3D4E4", "A3B2C3D4E6", "A5B2C3D4E5"});  返回2C3D4E
     【说  明】:最长公共子串
     ************************************************************************************************************/
    public static String findLongestCommonSubstring(String[] qrCodes) {
        if (qrCodes == null || qrCodes.length == 0) {
            return "";
        }
        String firstQRCode = qrCodes[0];
        int maxLength = 0;
        int maxStartIndex = 0;
        // 遍历第一个二维码的字符
        for (int startIndex = 0; startIndex < firstQRCode.length(); startIndex++) {
            // 遍历剩余的二维码
            for (int i = 1; i < qrCodes.length; i++) {
                String currentQRCode = qrCodes[i];
                int length = 0;
                int currentIndex = startIndex;
                // 检查当前二维码是否足够长以包含当前位置的字符
                while (currentIndex < firstQRCode.length() && currentIndex < currentQRCode.length()) {
                    // 检查当前二维码的字符是否与第一个二维码对应位置的字符相同
                    if (currentQRCode.charAt(currentIndex) == firstQRCode.charAt(currentIndex)) {
                        length++;
                        currentIndex++;
                    } else {
                        break;
                    }
                }
                // 更新最长公共子串的长度和起始索引
                if (length > maxLength) {
                    maxLength = length;
                    maxStartIndex = startIndex;
                }
            }
        }
        return firstQRCode.substring(maxStartIndex, maxStartIndex + maxLength);
    }

    /************************************************************************************************************
     【函  数】:extractChineseCharacters
     【参  数】:String 输入文字
     【返  回】:处理后结果
     【简  例】:extractChineseCharacters("安卓Character提取汉字") 返回 安卓提取汉字
     【说  明】:提取汉字
     ************************************************************************************************************/
    public static String extractChineseCharacters(String text) {
        String regex = "[\\u4e00-\\u9fa5]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group());
        }
        return result.toString();
    }

    /************************************************************************************************************
     【函  数】:sortNumberLetter
     【参  数】:String 输入文字 mode 先数字还是先字母
     【返  回】:处理后结果
     【简  例】:sortNumberLetter("0E1234AB5678CD9",true) 返回 0123456789ABCDE;
              sortNumberLetter("0E1234AB5678CD9",false) 返回 ABCDE0123456789;
     【说  明】:排序
     ************************************************************************************************************/
    public static String sortNumberLetter(String input, Boolean mode) {
        StringBuilder digits = new StringBuilder();
        StringBuilder letters = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                digits.append(c);
            } else if (Character.isLetter(c)) {
                letters.append(c);
            }
        }

        char[] sortedDigits = digits.toString().toCharArray();
        char[] sortedLetters = letters.toString().toCharArray();

        Arrays.sort(sortedDigits);
        Arrays.sort(sortedLetters);
        if (mode) {
            return new String(sortedDigits) + new String(sortedLetters);
        } else {
            return new String(sortedLetters) + new String(sortedDigits);
        }
    }
    /************************************************************************************************************
     【函  数】:stringToByte
     【参  数】:infix 输入文字
     【返  回】:处理后结果
     【简  例】:stringToByte("010203") 返回[0,1,0,2,0,3]
     【说  明】:分割每一位 返回byte
     ************************************************************************************************************/
    public static byte[] stringToByte(String infix) {
        byte[] rlt = new byte[6];
        for (int i = 0; i < 6; i++) {
            char c = infix.charAt(i);
            int s = Integer.parseInt(String.valueOf(c));
            System.out.println(s);
            rlt[i] = (byte) s;
        }
        return rlt;
    }
}
