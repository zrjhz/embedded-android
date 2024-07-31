package car.car2024.Utils.Algorithm;

public class LongestCommonSubstring {

    public static void main(String[] args) {
        String[] qrCodes = {
                "A1B2C3D4E5",
                "A2B2C3D4E4",
                "A3B2C3D4E6",
                "A5B2C3D4E5"
        };

        String longestCommonSubstring = findLongestCommonSubstring(qrCodes);
        System.out.println("最长字符子串: " + longestCommonSubstring);
    }

    public static String getLongestString(String[] strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        String s = "";
        for (int i = 1; i < strings.length; i++) {
            s = longestString(strings[i], strings[i - 1]);
        }
        return s;
    }

    public static String longestString(String str1, String str2) {
        if (str1.equals(str2)) {
            return str1;
        } else {
            String temp = "";
            for (int i = 0; i < str1.length() && i < str2.length(); i++) {
                if (str1.length() < str2.length()) {
                    for (int j = str1.length(); j > i; j--) {
                        if (str1.substring(i, j).equals(str2.substring(i, j))) {
                            if (j - i > temp.length()) {
                                temp = str1.substring(i, j);
                            }
                        }
                    }
                } else {
                    for (int j = str2.length(); j > i; j--) {
                        if (str1.substring(i, j).equals(str2.substring(i, j))) {
                            if (j - i > temp.length()) {
                                temp = str2.substring(i, j);
                            }
                        }
                    }
                }
            }
            return temp;
        }
    }

    public static String findLongestCommonSubstring(String[] qrCodes) {
        if (qrCodes == null || qrCodes.length == 0) {
            return "";
        }

        String longestCommonSubstring = qrCodes[0];

        for (int i = 1; i < qrCodes.length; i++) {
            longestCommonSubstring = findLongestCommonSubstring(longestCommonSubstring, qrCodes[i]);
        }

        return longestCommonSubstring;
    }

    private static String findLongestCommonSubstring(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        int maxLength = 0;
        int endIndex = 0;

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        endIndex = i;
                    }
                }
            }
        }

        return str1.substring(endIndex - maxLength, endIndex);
    }
}
