//package im.fyhz.utils;
//
//import java.util.regex.Pattern;
//
//public class regular {
//    public static String pattern = "";
//    public static boolean licenseMatch(String license) {
//
//        return Pattern.matches(pattern, license);
//
//    }
//
//
//    public static String createLicensePattern(String a) {
//
//        for (int i = 0; i < a.length(); i++) {
//            String temp = a.substring(i,i+1);
//            if (isNumber(temp) || isLetter(temp)) {
//                pattern += a.substring(i,i+1);
//            } else {
//                pattern += "[0-9A-Z]";
//            }
//        }
//        return pattern;
//    }
//
////    public static boolean isLetter() {
////
////    }
//
//
//    /**
//     * if is number
//     * @param str
//     * @return
//     */
//    public static boolean isNumber(String str) {
//        Pattern pattern = Pattern.compile("[0-9]*");
//        return pattern.matcher(str).matches();
//    }
//
//    /**
//     * if is letter
//     * @param str
//     * @return
//     */
//    public static boolean isLetter(String str) {
//        char c = str.charAt(0);
//        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
