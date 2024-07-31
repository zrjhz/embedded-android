//package im.fyhz;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//public class MessageData {
////    public static String carLicense;
////    public static int car_license_3th;
//
//    // 原始车牌
////    public static String rawLicense = "";
//
////    public static String QRCode[] = new String[5];
//    // 二维码数量
////    public static int QRCodeNum = 0;
//    // 车头朝向carOrientations与固定数据D
////    public static String carOrientations = "";
////    public static String D = "11";
//
////    public static HashMap hashMap = new HashMap();
//
//    // 校验过后的车牌
////    public static String licence = "";
////    public static int phindex = 100;
//    public static String rfid = "";
//
//    /**
//     * 静态标志物 1 数据处理类
//     * 对QRid进行处理，获取车头朝向carOrientations与固定数据D
//     */
////    public static void QRCodeDispose() {
////        if (QRCode == "") {
////            System.out.println("QRCode为空");
////            return;
////        }
////        String disposedStr = "";
////        for(int i= 0;i < QRCode.length();i++) {
////            if (QRCode.charAt(i) == '$') {
////                i++;
////                while (QRCode.charAt(i) != '$') {
////                    if (check(QRCode.substring(i,i+1))) {
////                        disposedStr+=QRCode.substring(i,i+1);
////                        while (!isNumberic(QRCode.substring(i,i+1)) || QRCode.charAt(i) == '$') {
////                            i++;
////                        }
////                        if (isNumberic(QRCode.substring(i,i+1))) {
////                            disposedStr+=QRCode.substring(i,i+1);
////                        } else if (QRCode.charAt(i) == '$') {
////                            break;
////                        }
////                    }
////                    i++;
////                }
////            }
////            if (QRCode.charAt(i) == '&') {
////                i++;
////                while (QRCode.charAt(i) != '&'){
////                    D += QRCode.substring(i,i + 1);
////                    i++;
////                }
////                break;
////            }
////        }
////        carOrientations = disposedStr.substring(disposedStr.length() - 2);
////
////    }
//
//    /**
//     * 静态标志物 2 数据处理类
//     * 对QRid进行处理，获取车头朝向carOrientations与固定数据D7或H7
//     */
//    public static String regularSearch(String raw) {
//        String pattern1 = ".*D7.*";
//        String pattern2 = ".*F7.*";
//        if (Pattern.matches(pattern1, raw)) {
//            System.out.println("D7D7D7D7D7");
//            return "D7";
//        } else if (Pattern.matches(pattern2, raw)) {
//            System.out.println("F7F7F7F7F7");
//            return "F7";
//        } else {
//            System.out.println("fakeD7");
//            return "D7";
//        }
//    }
//
//
//    /**
//     * if is number
//     * @param str
//     * @return
//     */
//    public static boolean isNumberic(String str) {
//        Pattern pattern = Pattern.compile("[0-9]*");
//        return pattern.matcher(str).matches();
//    }
//
//    /**
//     * if is letter
//     * @param str
//     * @return
//     */
//    public static boolean check(String str) {
//        char c = str.charAt(0);
//        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
