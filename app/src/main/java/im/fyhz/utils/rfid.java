//package im.fyhz.utils;
//
//import im.fyhz.MessageData;
//
//public class rfid {
//    /**
//     * RFID信息转字符串
//     * @return
//     */
//    public static String getRFID() {
//        if (MessageData.rfid == null || MessageData.rfid.length() == 0) {
//            System.out.println("rfidMap为空");
//            MessageData.rfid = "@@@@@@";
//        } else if (MessageData.rfid.length() < 6) {
//            int length = MessageData.rfid.length();
//            for (int i = 0; i < 6 - length; i++) {
//                MessageData.rfid += "@";
//            }
//        } else if (MessageData.rfid.matches("000")) {
//            MessageData.rfid = "@@@@@@";
//        } else if (MessageData.rfid.matches("0")) {
//            MessageData.rfid = "@@@@@@";
//        }
//        String rfid = "";
//        for (int i = 0; i < MessageData.rfid.length(); i++) {
//            char temp = MessageData.rfid.trim().charAt(i);
//            if (temp == '0') {
//                rfid += "O";
//            } else {
//                rfid += MessageData.rfid.trim().charAt(i);
//            }
//        }
//        System.out.println("rfid---------" + rfid);
//        return rfid;
//    }
//}
