//package im.hdy.utils;
//
//import android.util.Log;
//
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import im.hdy.CarData;
//
///**
// * Created by hdy on 21/05/2018.
// */
//
//public class CRC16 {
//
//    static final String HEXES = "0123456789ABCDEF";
//    byte uchCRCHi = (byte) 0xFF;
//    byte uchCRCLo = (byte) 0xFF;
//    private static byte[] auchCRCHi = {0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
//            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01,
//            (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
//            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01,
//            (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
//            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
//            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
//            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80,
//            (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01,
//            (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
//            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
//            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80,
//            (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01,
//            (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
//            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
//            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
//            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80,
//            (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
//            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80,
//            (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
//            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81,
//            (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
//            (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00,
//            (byte) 0xC1, (byte) 0x81, (byte) 0x40};
//
//    private static byte[] auchCRCLo = {(byte) 0x00, (byte) 0xC0, (byte) 0xC1,
//            (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2,
//            (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05,
//            (byte) 0xC5, (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C,
//            (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE,
//            (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B,
//            (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8, (byte) 0xD8,
//            (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB,
//            (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF,
//            (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC,
//            (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7,
//            (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12,
//            (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0,
//            (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1,
//            (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36,
//            (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35,
//            (byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC, (byte) 0xFD,
//            (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE,
//            (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39,
//            (byte) 0xF9, (byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8,
//            (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A,
//            (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF,
//            (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C, (byte) 0xE4,
//            (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,
//            (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3,
//            (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0,
//            (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63,
//            (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6,
//            (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64,
//            (byte) 0xA4, (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D,
//            (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA,
//            (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9,
//            (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9,
//            (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA,
//            (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D,
//            (byte) 0xBD, (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74,
//            (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6,
//            (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73,
//            (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0, (byte) 0x50,
//            (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53,
//            (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57,
//            (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54,
//            (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F,
//            (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A,
//            (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58,
//            (byte) 0x98, (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89,
//            (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E,
//            (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,
//            (byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84, (byte) 0x85,
//            (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86,
//            (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41,
//            (byte) 0x81, (byte) 0x80, (byte) 0x40};
//
//    public int value;
//
//    public CRC16() {
//        value = 0;
//
//    }
//
//    private void update(byte[] puchMsg, int usDataLen) {
//
//        int uIndex;
//        // int i = 0;
//        for (int i = 0; i < usDataLen; i++) {
//            uIndex = (uchCRCHi ^ puchMsg[i]) & 0xff;
//
//            uchCRCHi = (byte) (uchCRCLo ^ auchCRCHi[uIndex]);
//            uchCRCLo = auchCRCLo[uIndex];
//        }
//        value = ((((int) uchCRCHi) << 8 | (((int) uchCRCLo) & 0xff))) & 0xffff;
//
//        return;
//    }
//
//    public void reset() {
//        value = 0;
//        uchCRCHi = (byte) 0xff;
//        uchCRCLo = (byte) 0xff;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    private static byte uniteBytes(byte src0, byte src1) {
//        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
//                .byteValue();
//        _b0 = (byte) (_b0 << 4);
//        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
//                .byteValue();
//        byte ret = (byte) (_b0 ^ _b1);
//        return ret;
//    }
//
//    private static byte[] HexString2Buf(String src) {
//        int len = src.length();
//        byte[] ret = new byte[len / 2 + 2];
//        byte[] tmp = src.getBytes();
//        for (int i = 0; i < len; i += 2) {
//            ret[i / 2] = uniteBytes(tmp[i], tmp[i + 1]);
//        }
//        return ret;
//    }
//
//    public static byte[] getSendBuf(String toSend) {
//        byte[] bb = HexString2Buf(toSend);
//        CRC16 crc16 = new CRC16();
//        crc16.update(bb, bb.length - 2);
//        int ri = crc16.getValue();
//        bb[bb.length - 1] = (byte) (0xff & ri);
//        bb[bb.length - 2] = (byte) ((0xff00 & ri) >> 8);
//        return bb;
//    }
//
//    public static boolean checkBuf(byte[] bb) {
//        CRC16 crc16 = new CRC16();
//        crc16.update(bb, bb.length - 2);
//        int ri = crc16.getValue();
//        if (bb[bb.length - 1] == (byte) (ri & 0xff)
//                && bb[bb.length - 2] == (byte) ((0xff00 & ri) >> 8))
//            return true;
//        return false;
//    }
//
//    public static String getBufHexStr(byte[] raw) {
//        if (raw == null) {
//            return null;
//        }
//        final StringBuilder hex = new StringBuilder(2 * raw.length);
//        for (final byte b : raw) {
//            hex.append(HEXES.charAt((b & 0xF0) >> 4))
//                    .append(HEXES.charAt((b & 0x0F)));
//        }
//        return hex.toString();
//    }
//
//    public static void match(String str) {
//        int highest = 16;
//        int lowest = 1;
//
////        str = "< Fg a1 x13 2x16,F x4 g.5t x6 /rs+\1/h A b>";
////        str = "<A x12 2x16,Fg x8 tcs/ B +\1/ Cz y x3 >";
////        str = "<F Aa a1 x15 2x16,F x2  g.5t /rs+\1/h A Bb>";
////        str = "<Fg Aa a1 x12 2x16,F x5  g.5t /rs+\1/h A b>";
//        Matcher matcher = Pattern.compile("(?![Xx])[A-Za-z]").matcher(str);
//
//        // 匹配多项式
//
//        // 提取多项式计算出多项式码
//        int sum = 1;
////        Matcher matcher2 = Pattern.compile("(x)[1-9]\\d*").matcher(str);
//        Matcher matcher2 = Pattern.compile("(x)[1-9]\\d{0,1}").matcher(str);
//        while (matcher2.find()) {
//            System.out.println(matcher2.group());
//            Integer num = Integer.valueOf(matcher2.group().substring(1));
//            if (num < highest && num > 0) {
//                int pow = (int) Math.pow(2, num);
//                sum += pow;
//            }
//        }
//        System.out.println(Integer.toBinaryString(sum));
//        String hexString = Integer.toHexString(toHex(Integer.toBinaryString(sum),false));
//        int parseInt = Integer.parseInt(hexString, 16);
////        Log.i("多项式码(正):", hexString);
////        Log.i("多项式码(反):", Integer.toHexString(toHex(new StringBuilder(Integer.toBinaryString(sum)).reverse().toString(),true)));
//        // 提取多项式计算出多项式码
//
//        // 提取前两个英文字母 最后两个英文字母
//        ArrayList<String> lists = new ArrayList<>();
//        while (matcher.find()) {
//            String group = matcher.group();
//            lists.add(group);
//        }
//        String first = lists.get(0) + lists.get(1) + lists.get(lists.size() - 2) + lists.get(lists.size() - 1);
//        char[] chars = first.toCharArray();
//        // 提取前两个英文字母 最后两个英文字母
//
////        Log.i("获取到的英文:", first);
//        //获取到CRC码值
//        int crc16 = getCrc16(new byte[]{(byte) chars[0], (byte) chars[1], (byte) chars[2], (byte) chars[3]}, parseInt);
//        //进行控制码的提取
//        //crc16的文本
//        String crc16_str = Integer.toHexString(crc16);
//        System.out.println(crc16_str);
//        if (crc16_str.length() < 4) {
//            crc16_str = "0" + crc16_str;
//        }
//        byte first_code = HexUtils.uniteChars(crc16_str.substring(0, 2).toCharArray());
//        byte last_code = HexUtils.uniteChars(crc16_str.substring(2, 4).toCharArray());
//        short[] bytes = new short[]{first_code, (short) chars[0], (short) chars[1], (short) chars[2], (short) chars[3], last_code};
//        for (short b : bytes) {
//            Log.i("字节:", Integer.toHexString(b));
//        }
//        CarData.arm_code = bytes;
//    }
//
//
//    private static int toHex(String s, boolean resever) {
//        if (s.length() < 16) {
//            String temp = "";
//            for (int i = 0; i < 16 - s.length(); i++) {
//                temp += 0;
//            }
//            if (resever) {
//                s = s + temp;
//            } else {
//
//                s = temp + s;
//            }
//        }
//        System.out.println(s);
//        int sum = 0;
//        for (int i = 0; i < s.length(); i++) {
//            String string = s.substring(i, i + 1);
//            if (string.equals("1")) {
//                sum += Math.pow(2, i);
//            }
//        }
//        return sum;
//    }
//
//    private static int getCrc16(byte[] arr_buff, int polynomial) {
//        int len = arr_buff.length;
//
//        //预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
//        int crc = 0xFFFF;
//        int i, j;
//        for (i = 0; i < len; i++) {
//            //把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
//            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
//            for (j = 0; j < 8; j++) {
//                //把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
//                if ((crc & 0x0001) > 0) {
//                    //如果移出位为 1, CRC寄存器与多项式A001进行异或
//                    crc = crc >> 1;
////                    crc = crc ^ 0xA001;
//                    crc = crc ^ polynomial;
//                } else
//                    //如果移出位为 0,再次右移一位
//                    crc = crc >> 1;
//            }
//        }
//        return crc;
//    }
//
//
//
//}
