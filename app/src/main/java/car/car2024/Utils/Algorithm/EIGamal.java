//package im.zhy.data.arithmetic;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * @author zhy
// * @create_date 2019-05-05 10:26
// */
//public class EIGamal {
//
//    public static byte[] realize(String s){
//        String info = StrExtractUtils.inRegexGetGroupStr(s, "“MW”:“.{8}");
//
//        info = info.substring(info.length() - 8, info.length());
//
//        System.out.println(info);
//
//        List<Integer> letter = StrExtractUtils.getLetterBscllDesc(info);
//
//        for (Integer integer : letter) {
//            System.out.println(integer);
//        }
//
//        List<Integer> number = StrExtractUtils.getNumberBscllAsc(info);
//
//
//        System.out.println("--------------------------------");
//        for (Integer integer : number) {
//            System.out.println(integer);
//        }
//
//        String numStr = StrExtractUtils.inRegexGetGroupStr(s, "“SZ”:\\d+");
//
//        numStr = numStr.substring(numStr.lastIndexOf(":") + 1, numStr.length());
//
//        long num = Long.parseLong(numStr);
//
//        System.out.println(num);
//
//        long p = 3;
//        for (long l = num; l > 0; l--) {
//            boolean prime = MathUtils.isPrime(l);
//
//            if (prime){
//                p = l;
//                break;
//            }
//        }
//
//        System.out.println("最大质数：" + p);
//
//
//        int g = 0;
//        i:for (long i = 2; i <= p; i++) {
//            Set<Long> set = new HashSet<>();
//            l:for (long l = 1; l < p; l++) {
//                boolean add = set.add(MathUtils.power(i, l, p));
//                if (!add){
//                    continue i;
//                }
//            }
//            g = (int) i;
//            break i;
//        }
//
//        System.out.println(g);
//
//        List<Integer> list = new ArrayList<>();
//
//        for (int i = 2; i < p - 1; i++) {
//            boolean b = MathUtils.coprimeNumber(i, (int) p - 1);
//            if (b){
//                list.add(i);
//            }
//
//        }
//
//        int k = list.get((list.size() - 1) / 2);
//
//        int a = (int) MathUtils.power(g, k, p);
//
//        System.out.println(a);
//
//        String MStr = "";
//
//        for (Integer integer : letter) {
//            MStr += Integer.toHexString(integer);
//        }
//
//        int m = Integer.parseInt(MStr, 16);
//
//        System.out.println(m);
//
//
//        String xStr = StrExtractUtils.inRegexGetGroupStr(s, "“X”：\\d+");
//        System.out.println(xStr);
//
//        xStr = xStr.substring(xStr.lastIndexOf("：") + 1, xStr.length());
//
//        int x = Integer.parseInt(xStr);
//
//        System.out.println(x);
//
//        int b = 0;
//
//        for (int i = 0; i < 100000000; i++) {
//           if (m == ((x * a) + MathUtils.ride(k, i, p - 1))){
//               b = i;
//               break;
//           }
//        }
//
//        System.out.println(b);
//
//        return null;
//    }
//
//    public static void main(String[] args) {
//        realize("{“MW”:“C23D1A0B”, “SZ”:435，“GR”:“KQW”， “X”：123 }");
//
////        System.out.println(Integer.parseInt("44434241", 16));
//    }
//
//
//}
