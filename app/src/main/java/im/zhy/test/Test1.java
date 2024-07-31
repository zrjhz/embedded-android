//package im.zhy.test;
//
//
//import im.hdy.CarData;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @author zhy
// * @create_date 2019-04-26 16:07
// */
//
//@SuppressWarnings("all")
//public class Test1 {
//
////    public static void main(String[] args) {
////        test6();
////    }
//
//
//    private static void test(){
//        int[][] s = new int[3][4];
//        System.out.println(s.length);
//        System.out.println(s[0].length);
//    }
//
//
//    private static void test2(){
//        String s = "G00OE9";
//
////        System.out.println(s.matches(CarData.licenseMatcherStr));
//    }
//
//    private static void test3(){
//        String s1 = "G000E9";
//
//
////        System.out.println(s1.matches(CarData.licenseMatcherStr));
//    }
//
//
//
//    private static void test4(){
//        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
//        byte[] bytes1 = new byte[bytes.length - 4];
//
//        System.arraycopy(bytes, 4, bytes1, 0, bytes1.length);
//
//        for (byte b : bytes1) {
//            System.out.println(b);
//        }
//
//    }
//
//    private static void test5(){
//        String s = "s";
//
//        System.out.println(s.matches("[a-zA-Z0-9]{1}"));
//    }
//
////    private static void test6(){
//////        String s = CarData.licenseMatcherStr;
////
////        s = s.substring(1, s.length() - 1);
////
////        String[] split = s.split("\\}");
////
////        List<Integer> formList = new ArrayList<>(6);
////
////        for (String s1 : split) {
////            System.out.println(s1);
////
////            int number = Integer.parseInt(s1.substring(s1.length() - 1));
////
////            int form = 0;
////
////            if (s1.startsWith("[A-Z0-9]")){
////                form = 0;
////            }else if (s1.startsWith("[A-Z]")){
////                form = 1;
////            }else if (s1.startsWith("[0-9]")){
////                form = 2;
////            }
////
////            for (int i = 0; i < number; i++) {
////                formList.add(form);
////            }
////        }
////
////        for (Integer integer : formList) {
////            System.out.println(integer);
////        }
////
////
////
////    }
////
////
////
//
//
//}
