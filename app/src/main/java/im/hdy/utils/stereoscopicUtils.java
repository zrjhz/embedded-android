//package im.hdy.utils;
//
//import car.car2024.ActivityView.FirstActivity;
//
///**
// * Created by hdy on 05/05/2018.
// */
//
//public class stereoscopicUtils {
//
//    public static void sendLicense(String license) {
//        if (license == null || license.length() != 6) {
//            license = "B543E8";
//        }
//        short[] li = StringToBytes(license);
//
//        for (int i = 0; i < li.length; i++) {
//            System.out.println(Integer.toHexString(li[i]));
//        }
//
//        short[] data = {0x00, 0x00, 0x00, 0x00};
//        data[0] = 0x88;
//        data[1] = (short) (li[0]);
//        data[2] = (short) (li[1]);
//        data[3] = (short) (li[2]);
//        FirstActivity.Connect_Transport.stereos_display(data);
//        FirstActivity.Connect_Transport.yanchi(500);
//        data[0] = 0x99;
//        data[1] = (short) (li[3]);
//        data[2] = (short) (li[4]);
//        data[3] = (short) (li[5]);
//        FirstActivity.Connect_Transport.stereos_display(data);
//    }
//
//    //从string中得到short数据数组
//    private static short[] StringToBytes(String licString) {
//        if (licString == null || licString.equals("")) {
//            return null;
//        }
//        licString = licString.toUpperCase();
//        int length = licString.length();
//        char[] hexChars = licString.toCharArray();
//        short[] d = new short[length];
//        for (int i = 0; i < length; i++) {
//            d[i] = (short) hexChars[i];
//        }
//        return d;
//    }
//}
