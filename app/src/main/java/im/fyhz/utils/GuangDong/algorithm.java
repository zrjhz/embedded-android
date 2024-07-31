//package im.fyhz.utils.GuangDong;
//
//import java.util.ArrayList;
//import java.util.regex.Pattern;
//
//public class algorithm {
//    public static String HillCipher(String raw) {
//        // step 1
//        String data = raw.split("\"")[3].toUpperCase().trim();
//        String key = raw.split("\"")[7].toUpperCase().trim();
//        System.out.println(data+key);
//        // step 2
//        int n = (int) Math.sqrt(key.length());
//        int[][] data2D = new int[data.length()/n][n];
//        for (int i = 0; i < data2D.length; i++) {
//            for (int j = 0; j < data2D[i].length; j++) {
//                data2D[i][j] = (int) data.charAt(i*n+j) - 65;
//                System.out.println(data.charAt(i*n+j) + ":" + data2D[i][j]);
//            }
//        }
//        System.out.println("data2D--------");
//        for (int i = 0; i < data2D.length; i++) {
//            for (int j = 0; j < data2D[i].length; j++) {
//                System.out.print( data2D[i][j] + ",");
//            }
//            System.out.println();
//        }
//        int[][] key2D = new int[n][n];
//        for (int i = 0; i < key2D.length; i++) {
//            for (int j = 0; j < key2D[i].length; j++) {
//                key2D[i][j] =  (int) key.charAt(i*n+j) - 65;
//                System.out.println(key.charAt(i*n+j) + ":" +key2D[i][j]);
//            }
//        }
//        System.out.println("key2D--------");
//        for (int i = 0; i < key2D.length; i++) {
//            for (int j = 0; j < key2D[i].length; j++) {
//                System.out.print( key2D[i][j] + ",");
//            }
//            System.out.println();
//        }
//        // step 3
//        System.out.println();
//        int[] result = new int[data2D[0].length*4];
//        for (int i = 0; i < data2D.length; i++) {
//            for (int j = 0; j < data2D[i].length; j++) {
//                int temp = 0;
//                for (int k = 0; k < n; k++) {
//                    temp+=(data2D[i][k] * key2D[k][j]);
////                    System.out.println(data2D[i][k] +" "+ key2D[j][k]);
//                }
//                result[i*n+j] = temp%26;
//            }
//        }
//        System.out.println("result--------");
//        for (int i = 0; i < result.length; i++) {
//            System.out.print( result[i] + ",");
//        }
//
//        String back = "";
//        for (int i = 0; i < 3; i++) {
//            back +=  (char) (result[i] + 65);
//        }
//        for (int i = result.length-3; i < result.length; i++) {
//            back += (char)(result[i] + 65);
//        }
//        System.out.println(back);
//
//        return back;
//
//    }
//    public static int[][] getChar(int a,int b,String x){
//        int[][] c=new int[a][b];
//        for (int i=0;i<a;i++){
//            for (int j=0;j<b;j++){
//                c[i][j]=(x.charAt(i*3+j)-97);
//            }
//        }
//        return c;
//    }
//    /**
//     * get the middle part of the String
//     * @param raw
//     * @return
//     */
//    public static String getCenter(String raw) {
//        raw = raw.split(">")[1].split("<")[0];
//        raw = getNumAndWord(raw);
//        String end = "";
//        for (int i = 0; i < raw.length()-1; i++) {
//            if (isLetter(raw.substring(i,i+1))) {
//                if (isNumberic(raw.substring(i+1,i+2))) {
//                    end += raw.substring(i,i+1) + raw.substring(i+1,i+2);
//                    i++;
//                }
//            }
//        }
//        return end;
//    }
//
//    /**
//     * get Words in the raw String
//     * @param raw
//     * @return Words
//     */
//    public static String getWords(String raw) {
//        String regex = "[^a-zA-Z]";
//        return raw.replaceAll(regex,"");
//    }
//    /**
//     * get Numbers And Words in the raw String
//     * @param raw
//     * @return Numbers And Words
//     */
//    public static String getNumAndWord(String raw) {
//        String regex = "[^0-9a-zA-Z]";
//        return raw.replaceAll(regex, "");
//    }
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
//    public static boolean isLetter(String str) {
//        char c = str.charAt(0);
//        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
