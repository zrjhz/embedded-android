//package im.hdy.utils;
//
//import car.car2024.ActivityView.FirstActivity;
//import im.hdy.CarData;
//
///**
// * Created by hdy on 05/05/2018.
// */
//
//public class LEDUtils {
//
//    /**
//     * @param distance 距离
//     *                 <p>
//     *                 注意单位是cm
//     */
//    public static void sendDistance(int distance) {
//        FirstActivity.Connect_Transport.digital_dic(distance);
//    }
//
//    /**
//     * 向LED显示发送数据
//     *
//     * @param main  第几行 1 2
//     * @param bytes 1 1 2
//     */
//    public static void sendHex(int main, byte[] bytes) {
//
//        int one = bytes[0] / 10 * 16
//                + bytes[0] % 10;
//
//        int two = bytes[1] / 10 * 16
//                + bytes[1] % 10;
//
//        int three = bytes[2] / 10 * 16
//                + bytes[2] % 10;
//
//        FirstActivity.Connect_Transport.digital(main, one, two, three);
//    }
//}
