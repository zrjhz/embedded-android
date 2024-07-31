//package im.zhy.test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class QRCodeTest {
//
//    public static void main(String[] args) {
//        test();
//    }
//
//    public static void test(){
//        String[] s = "<hidethegold|play5fair9example>".toUpperCase().split("\\|");
//
//        String s1 = s[0];
//        String s2 = s[1];
//
//        Pattern pattern = Pattern.compile("[A-Z]{1}");
//        Matcher matcher = pattern.matcher(s1);
//
//        List<String> list = new ArrayList<>();
//
//        while (matcher.find()){
//            list.add(matcher.group());
//        }
//
//        Set<String> set = new LinkedHashSet<>();
//
//        Matcher matcher1 = pattern.matcher(s2);
//        while (matcher1.find()){
//            set.add(matcher1.group());
//        }
//
//        int ascii = 65;
//        while (set.size() < 25){
//           if(ascii != (int)'Q'){
//               System.out.println(((char)ascii+""));
//               set.add(((char)ascii+""));
//
//           }
//            ascii = ascii + 1;
//        }
//
//        for (String s3 : list) {
//            System.out.print(s3+" ");
//        }
//
//        System.out.println();
//
//
//        List<String> list1 = new ArrayList<>();
//        for (String s3 : set) {
//            System.out.print(s3+" ");
//            list1.add(s3);
//        }
//
//        String[][] jz = new String[5][5];
//
//        Map<String,int[]> zb = new HashMap<>();
//
//
//        System.out.println("  ooo"+list1.get(5));
//
//        int index = 0;
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                jz[i][j] = list1.get(index);
//                index++;
//            }
//        }
//
//
//
//        System.out.println();
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                System.out.print(jz[i][j]+" ");
//
//            }
//            System.out.println();
//
//        }
//
//        List<String[]> zfgruep = new ArrayList<>();
//
//        for (int i = 0; i < list.size(); i += 2) {
//            String z1 = list.get(i);
//            String z2;
//            if ((list.size()-1) >= (i+1)){
//                z2 = "X";
//            }else {
//                z2 = list.get(i+1);
//            }
//
//            if (z1 == z2){
//                z2 = "X";
//                i--;
//                continue;
//            }
//
//            zfgruep.add(new String[]{z1,z2});
//
//
//        }
//
//        System.out.println("  "+zfgruep.size());
//        for (String[] strings : zfgruep) {
//            System.out.print(strings[0]+strings[1]+"  ");
//        }
//
//
//
//    }
//}
