//package im.zhy.data.arithmetic;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * @author zhy
// * @create_date 2019-05-29 16:11
// */
//public class CRC {
//
//    public static byte[] getCrc(String s){
//        List<Integer> list = StrExtractUtils.inRegexGetGroupBscll(s, "[A-Za-z&&[^Xx]]{1}");
//
//        int[] datas = new int[]{list.get(0), list.get(1), list.get(list.size() - 2), list.get(list.size() - 1)};
//
//
//        // 求多项式
//        int g = 0;
//
//        String regex = "(x[1-9]{1,2})|(\\\\1)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(s);
//        List<Integer> list1 = new ArrayList<>();
//        while (matcher.find()) {
//            String x = matcher.group();
//            x = x.substring(1, x.length());
//            list1.add(Integer.valueOf(x));
//        }
//
//        for (Integer integer : list1) {
//            if(integer<=16){
//                if(integer==1){
//                    g++;
//                }else{
//                    g += Math.pow(2,integer);
//                }
//
//            }
//        }
//
//        g &= 0xffff;
//
//        System.out.println(Integer.toBinaryString(g));
//
//        String b = Integer.toBinaryString(g);
//        String b1 = "";
//
//        for (int i = 0; i < b.length(); i++) {
//            b1 = b.charAt(i) + b1;
//        }
//
//        g = Integer.parseInt(b1, 2);
//
//        System.out.println(Integer.toBinaryString(g));
//
//        char crc = 0xFFFF;
//
//        for (int data : datas) {
//            crc ^= data & 0xFF;
//            for (int i = 0; i < 8; i++) {
//                if (crc % 2 != 0){
//                    crc >>= 1;
//                    crc ^= g;
//                }else {
//                    crc >>= 1;
//                }
//            }
//        }
//
//        System.out.println(Integer.toHexString(crc));
//
//        byte[] bytes = new byte[6];
//
//        bytes[0] = (byte) (crc >> 8);
//        bytes[5] = (byte) (crc & 0xFF);
//
//        for (int i = 0; i < datas.length; i++) {
//            bytes[i + 1] = (byte) datas[i];
//        }
//
//        for (byte aByte : bytes) {
//            System.out.println(Integer.toHexString(aByte & 0xff));
//        }
//
//        return bytes;
//    }
//
//    public static void main(String[] args) {
//        getCrc("<Aa12x16,Fg.5tx15/x2+\\1/hgBb>");
//
//
////        byte[] bytes = new byte[]{0x41, 0x61, 0x42, 0x62};
////
////        String s = "";
////        for (byte aByte : bytes) {
////            s += Integer.toBinaryString(aByte);
////        }
////
////        int[] ints = new int[]{14, 13, 0};
////
////        System.out.println(s);
////        int g = 0;
////        for (int i : ints) {
////            String c = s.charAt(i) + "";
////            g += Math.pow(2, i + 1);
////        }
////
////
////        System.out.println(Integer.toHexString(g));
//    }
//}
