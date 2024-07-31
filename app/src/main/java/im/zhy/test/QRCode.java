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
//public class QRCode {
//
//    public static void main(String[] args) {
//        //<lovegreen|6techonlogy6>   <agoodidea|Goodmoing168>
//        byte[] bytes = M05("<agoodidea|Goodmoing168>");
//        System.out.println();
//        System.out.println();
//        for (byte aByte : bytes) {
//            System.out.print(aByte+",");
//        }
//
//        System.out.println();
//
//        for (byte aByte : bytes) {
//            System.out.print(Integer.toHexString(aByte) + ",");
//        }
//
//
//        //S2d3s3je5fPg3J5w6h6h4wE8NgN4kyt
////
////        byte[] bytes = M01("S2d3s3je5fPg3J5w6h6h4wE8NgN4kyt");
////        System.out.println();
////        System.out.println();
////        System.out.println();
////        System.out.println();
////        for (byte aByte : bytes) {
////            System.out.println(aByte);
////        }
//
////        System.out.println("奇数和："+((number % 3)+4) );
//    }
//
//    public static byte[] M05(String ss) {
//        String[] s = ss.split("\\|");
//
//        s[0] = s[0].toUpperCase();
//        s[1] = s[1].toUpperCase();
//        System.out.println(s[0]);
//        System.out.println(s[1]);
//
//
//        Pattern pattern = Pattern.compile("[A-Z]{1}");
//        Set<String> mm = new LinkedHashSet();
//        Matcher matcher = pattern.matcher(s[1]);
//        while (matcher.find()) {
//            mm.add(matcher.group());
//        }
//
//        List<String> my = new ArrayList();
//        for (String s1 : mm) {
//            System.out.print(s1 + " ");
//            my.add(s1);
//        }
//        System.out.println();
//
//        List<String> mw = new ArrayList();
//        Matcher matcher1 = pattern.matcher(s[0]);
//
//        while (matcher1.find()) {
//            mw.add(matcher1.group());
//        }
//
//        for (String s1 : mw) {
//            System.out.print(s1 + " ");
//        }
//
//
//        //将密钥加入 5*5 矩阵
//
//        //用数组模拟矩阵
//        String[][] jz = new String[5][5];
//        //用map记录矩阵中每一个 字母的坐标
//        Map<String, int[]> zmzbMap = new HashMap();
//        int myCountNum = my.size() - 1;
//        int myNum = 0;
//        int zfAscll = 65;
//        for (int i = 0; i < jz.length; i++) {
//            for (int j = 0; j < jz[i].length; j++) {
//                String zf = "";
//                if (myNum <= myCountNum) {
//                    zf = my.get(myNum);
//                    myNum++;
//                } else {
//                    wh:
//                    while (true) {
//                        if (zfAscll == 81) {
//                            zfAscll++;
//                            continue wh;
//                        }
//                        zf = (char) zfAscll + "";
//                        if (s[1].matches(".*" + zf + ".*")) {
//                            zfAscll++;
//                            continue wh;
//                        } else {
//                            zf = (char) zfAscll + "";
//                            zfAscll++;
//                            break wh;
//                        }
//                    }
//                }
//
//                jz[i][j] = zf;
//                zmzbMap.put(zf, new int[]{i, j});
//            }
//        }
//
//        System.out.println();
//        for (int i = 0; i < jz.length; i++) {
//            for (int j = 0; j < jz[i].length; j++) {
//                System.out.print(jz[i][j] + " ");
//            }
//            System.out.println();
//        }
//
//
//        List<String[]> mwGroup = new ArrayList();
//        int mwGroupMax = mw.size();
//
//
//        boolean b = false;
//        for (int i = 0; i < mw.size(); i += 2) {
//            if (b) {
//                mwGroup.add(new String[]{mw.get(i), "X"});
//                b = false;
//                i--;
//                continue;
//            }
//            String d1 = mw.get(i);
//            String d2 = "";
//            if ((i + 1) >= mwGroupMax) {
//                d2 = "X";
//
//            } else {
//                d2 = mw.get(i + 1);
//            }
//            if (d1.equals(d2)) {
//                d2 = "X";
//                i = i - 1;
//                b = true;
//            }
//
//
//            System.out.println("d1" + d1 + "   d2" + d2);
//            mwGroup.add(new String[]{d1, d2});
//
//        }
//
//        System.out.println();
//        for (String[] strings : mwGroup) {
//            System.out.print(strings[0] + "" + strings[1]);
//            System.out.print("    ");
//        }
//
//        List<String[]> myGroup = new ArrayList<>();
//
//        for (String[] strings : mwGroup) {
//            int d1_x = zmzbMap.get(strings[0])[0];
//            int d1_y = zmzbMap.get(strings[0])[1];
//
//            int d2_x = zmzbMap.get(strings[1])[0];
//            int d2_y = zmzbMap.get(strings[1])[1];
//
//            String s1;
//            String s2;
//            //同一行 x相等
//            if (d1_x == d2_x) {
//
//
//                if (d1_y == 4) {
//                    s1 = jz[d1_x][0];
//                } else {
//                    s1 = jz[d1_x][d1_y + 1];
//                }
//                if (d2_y == 4) {
//                    s2 = jz[d2_x][0];
//                } else {
//                    s2 = jz[d2_x][d2_y + 1];
//                }
//
//                myGroup.add(new String[]{s1, s2});
//                continue;
//
//            }
//
//            if (d1_y == d2_y) {
//                if (d1_x == 4) {
//                    s1 = jz[0][d1_y];
//                } else {
//                    s1 = jz[d1_x + 1][d1_y];
//                }
//                if (d2_x == 4) {
//                    s2 = jz[0][d2_y];
//                } else {
//                    s2 = jz[d2_x + 1][d2_y];
//                }
//
//                myGroup.add(new String[]{s1, s2});
//                continue;
//            }
//
//            s1 = jz[d1_x][d2_y];
//            s2 = jz[d2_x][d1_y];
//
//            myGroup.add(new String[]{s1, s2});
//            continue;
//
//        }
//
//        List<String> list = new ArrayList<>();
//        System.out.println();
//        for (String[] strings : myGroup) {
//            System.out.print(strings[0] + "" + strings[1]);
//            System.out.print("    ");
//            list.add(strings[0]);
//            list.add(strings[1]);
//        }
//
//        String[] strings = new String[]{list.get(0), list.get(1), list.get(2), list.get(list.size() - 3), list.get(list.size() - 2), list.get(list.size() - 1)};
//
//        byte[] bytes = new byte[strings.length];
//        System.out.println();
//        for (int i = 0; i < strings.length; i++) {
//            char c = strings[i].charAt(0);
//            int i1 = (int) c;
//
//            System.out.println(Integer.toHexString(i1));
//
//            bytes[i] = (byte) i1;
//
//
//        }
//
//        System.out.println();
//
//        for (byte aByte : bytes) {
//            System.out.println(aByte);
//        }
//
//        return bytes;
//
//    }
//
//
//    public static int number = 0;
//    public static byte[] M01(String s){
//        Pattern pattern = Pattern.compile("[0-9]{1}");
//        Matcher matcher  = pattern.matcher(s);
//
//        List<String> list = new ArrayList<>();
//        while (matcher.find()){
//            String s1 = matcher.group().trim();
//            System.out.println(s1);
//            list.add(s1);
//        }
//
//        System.out.println(list.size());
//
//        int count = 0;
//        for (String s1 : list) {
//            int a = Integer.parseInt(s1);
//            if(a % 2 == 0){
//                count += a;
//            }else{
//                //奇数和
//                number += a;
//            }
//        }
//
//        System.out.println(count);
//
//        int[] jy = new int[]{3,5,7,9,11,13};
//
//        byte[] bytes = new byte[jy.length];
//
//        for (int i = 0; i < jy.length; i++) {
//            bytes[i] = (byte) (count % jy[i]);
//        }
//
//        for (byte aByte : bytes) {
//            System.out.println(aByte);
//        }
//
//        return bytes;
//
//
//
//
//    }
//}
