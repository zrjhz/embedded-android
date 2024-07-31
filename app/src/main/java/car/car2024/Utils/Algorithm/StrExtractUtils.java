//package im.zhy.data.arithmetic;
//
//import android.util.Log;
//
//import im.zhy.util.GetVarName;
//import im.zhy.util.ShapeColorUtils;
//
//import java.time.temporal.ValueRange;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * @author zhy
// * @create_date 2019-05-05 10:29
// */
//
//@SuppressWarnings("all")
//public class StrExtractUtils {
//
//    private static final String TAG = "StrExtractUtils";
//
//    // 提取字符串中的字母的bscll码
//    public static List<Integer> getLetterBscll(String s){
//
//        return inRegexGetGroupBscll(s, "[a-zA-Z]{1}");
//    }
//
//    // 提取字符串中的字母的bscll码  降序
//    public static List<Integer> getLetterBscllDesc(String s){
//
//        return inRegexGetGroupBscllDescOrAsc(s, "[a-zA-Z]{1}", -1);
//    }
//
//
//    // 提取字符串中的字母的bscll码  升序
//    public static List<Integer> getLetterBscllAsc(String s){
//
//        return inRegexGetGroupBscllDescOrAsc(s, "[a-zA-Z]{1}", 1);
//    }
//
//
//    // 获取字符串中的字母
//    public static List<Character> getLetterChar(String s){
//
//        return inRegexGetGroup(s, "[a-zA-Z]{1}");
//
//    }
//
//    // 获取字符串中的字符不重复的
//    public static List<Character> getCharNoRepeatChar(String s, String regex){
//
//        List<Character> letterList = inRegexGetGroup(s, regex);
//
//        Set<Character> letterSet = new LinkedHashSet<>();
//
//        for (Character c : letterList) {
//            letterSet.add(c);
//        }
//
//        return new ArrayList<>(letterSet);
//    }
//
//    // 获取字符串中的字母不重复的
//    public static List<Character> getLetterNoRepeatChar(String s){
//
//       return getCharNoRepeatChar(s, "[a-zA-Z]{1}");
//    }
//
//    // 获取字符中的数字
//    public static List<Integer> getNumber(String s){
//        List<Integer> integerList = new ArrayList<>();
//        List<Character> list = inRegexGetGroup(s, "[0-9]{1}");
//
//        for (Character character : list) {
//            integerList.add(Integer.valueOf(character.toString()));
//        }
//
//        return integerList;
//    }
//
//
//    // 获取字符中的数字的bscll码
//    public static List<Integer> getNumberBscll(String s){
//
//        return inRegexGetGroupBscll(s, "[0-9]{1}");
//
//    }
//
//    // 获取字符中的数字的bscll码 降序
//    public static List<Integer> getNumberBscllDesc(String s){
//
//        return inRegexGetGroupBscllDescOrAsc(s, "[0-9]{1}", -1);
//
//    }
//
//
//    // 获取字符中的数字的bscll码 升序
//    public static List<Integer> getNumberBscllAsc(String s){
//
//        return inRegexGetGroupBscllDescOrAsc(s, "[0-9]{1}", 1);
//
//    }
//
//    // 根据正则提取字符
//    public static List<Character> inRegexGetGroup(String s, String regex){
//        List<Character> list = new ArrayList<>();
//
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(s);
//
//        while (m.find()){
//            list.add(m.group().charAt(0));
//        }
//
//        return list;
//    }
//
//    // 根据正则提取字符串
//    public static String inRegexGetGroupStr(String s, String regex){
//
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(s);
//
//        m.find();
//
//        String re = m.group();
//
//        return re;
//
//    }
//
//    // 根据正则提取字符串列表
//    public static List<String> inRegexGetGroupStrs(String s, String regex){
//
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(s);
//
//        List<String> list = new ArrayList<>();
//
//        while (m.find()){
//            list.add(m.group());
//        }
//
//        return list;
//
//    }
//
//    // 根据正则提取字符的Bscll码
//    public static List<Integer> inRegexGetGroupBscll(String s, String regex){
//        List<Character> list = inRegexGetGroup(s, regex);
//
//        List<Integer> integerList = new ArrayList<>();
//
//        for (char character : list) {
//            int i = (int) character;
//            integerList.add(i);
//        }
//
//        return integerList;
//
//    }
//
//    public static List<Integer> inRegexGetGroupBscllDescOrAsc(String s, String regex,final int x){
//        List<Integer> integerList = inRegexGetGroupBscll(s, regex);
//
//        Collections.sort(integerList, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer i1, Integer i2) {
//                return i1 > i2 ? 1 * x : -1 * x;
//            }
//        });
//
//        return integerList;
//
//    }
//
//
//    /**
//     * 根据指定格式生成相应的正则表达式
//     * @param s  格式  L：大写字母  N：数字  C：大写字母加数字  l：小写字母 c：小写字母加数字 Y：大小字母 P：大小字母加数字
//     *           比如 L1N2 就表示 "[A-Z]{1}[0-9]{2}"
//     * @param isGlobalMatch  是否全局匹配
//     * @return
//     */
//    public static String createMatcherStr(String s, boolean isGlobalMatch){
//
//        String matcher = "";
//        Log.e(TAG, "createMatcherStr: " + s);
//        List<String> groupList = StrExtractUtils.inRegexGetGroupStrs(s, "[A-Za-z]{1}[0-9]{1}");
//        Log.e(TAG, "createMatcherStr: " + groupList);
//
//        for (String group: groupList) {
//
//            char select = group.charAt(0);
//            int number = Integer.parseInt(group.substring(1,2));
//
//            String str = "";
//
//            switch (select){
//                case 'L':
//                    str = "[A-Z]{";
//                    break;
//                case 'N':
//                    str = "[0-9]{";
//                    break;
//                case 'C':
//                    str = "[A-Z0-9]{";
//                    break;
//                case 'l':
//                    str = "[a-z]{";
//                    break;
//                case 'c':
//                    str = "[a-z0-9]{";
//                    break;
//                case 'Y':
//                    str = "[A-Za-z]{";
//                    break;
//                case 'P':
//                    str = "[A-Za-z0-9]{";
//                    break;
//            }
//
//            matcher += str + number + "}";
//
//        }
//
//        if (isGlobalMatch){
//            matcher = "^" + matcher + "$";
//        }
//
//        System.out.println("matcher = " + matcher);
//
//        return matcher;
//
//    }
//
//
//    public static String trim(String s){
//        s = s.replaceAll("\\s", "");
//        return s;
//    }
//
//
//    public static String autoCreateShapeShowForm(String s, String form){
//        form = trim(form);
//        s = trim(s);
//
//        List<String> list = inRegexGetGroupStrs(s, "[A-Z]{1}[\\u4E00-\\u9FA5]{4,5}");
//
//        Map<Integer, String> map = new HashMap<>();
//
//        for (String s1 : list) {
//            int key = getLetterBscll(s1).get(0);
//            String value = inRegexGetGroupStr(s1, "(矩形)|(圆形)|(三角形)|(菱形)|(五角星)");
//            map.put(key, value);
//        }
//
//        String resultForm = "";
//
//        List<String> keyList = inRegexGetGroupStrs(form, "[A-Z]{1}");
//
//        for (String s1 : keyList) {
//            int key = s1.charAt(0);
//            int shapeInt = GetVarName.getShapeIntByChinese(map.get(key));
//            int shapeNumber = ShapeColorUtils.shapeNumber(shapeInt);
//            System.out.println("shapeInt：" + shapeInt + "  shapeNumber：" + shapeNumber);
//            resultForm += s1 + shapeNumber;
//        }
//
//
//        return resultForm;
//    }
//
//
//    public static String autoCreateColorShowForm(String s, String form){
//        form = trim(form);
//        s = trim(s);
//
//        List<String> list = inRegexGetGroupStrs(s, "[A-Z]{1}[\\u4E00-\\u9FA5]{4,5}");
//
//        Map<Integer, String> map = new HashMap<>();
//
//        for (String s1 : list) {
//            int key = getLetterBscll(s1).get(0);
//            String value = inRegexGetGroupStr(s1, "(红色)|(绿色)|(蓝色)|(黄色)|(品红色)|(品色)|(青色)|(黑色)|(白色)");
//            map.put(key, value);
//        }
//
//        String resultForm = "";
//
//        List<String> keyList = inRegexGetGroupStrs(form, "[A-Z]{1}");
//
//        for (String s1 : keyList) {
//            int key = s1.charAt(0);
//            int colorInt = GetVarName.getColorIntByChinese(map.get(key));
//            int colorNumber = ShapeColorUtils.colorNumber(colorInt);
//            System.out.println("colorInt：" + colorInt + "  colorNumber：" + colorNumber);
//            resultForm += s1 + colorNumber;
//        }
//
//
//        return resultForm;
//    }
//
//
//    public static String autoCreateCoord(String s1, int index){
//        s1 = trim(s1);
//        List<String> list = inRegexGetGroupStrs(s1, "[0-9][\\u4E00-\\u9FA5]*：{1}[A-Z]{1}[0-9]{1}");
//
//        for (String s : list) {
//            int key = getNumber(s.split("：")[0]).get(0);
//            String value = inRegexGetGroupStr(s.split("：")[1], "[A-Z]{1}[0-9]{1}");
//            System.out.println("key：" + key + "  value：" + value);
//            if (key == index){
//                return value;
//            }
//        }
//
//
//        return "";
//    }
//
//
//
//    public static void main(String[] args) {
//        testAutoCreateCoord();
//    }
//
//
//    private static void testAutoCreateColorShowForm(){
//        int[][] shapeColor1 = new int[9][8];
//
//        shapeColor1[0][6] = 1;
//        shapeColor1[1][1] = 1;
//        shapeColor1[2][3] = 1;
//        shapeColor1[2][4] = 3;
//        shapeColor1[3][0] = 1;
//        shapeColor1[8][0] = 1;
//        ShapeColorUtils.setShapeColor(shapeColor1);
//
//
//        String s = "F代表红色图形，f为红色图形数量（0~9）；G代表黄色图形， g为黄色图形的数量（0~9）；H代表蓝色图形，h为蓝色图形的数量（0~9）;I代表青色图形， i为青色图形的数量（0~9）；J代表品红色图形， j为品红色图形的数量（0~9）。K代表绿色图形， k为绿色图形的数量（0~9）。L代表黑色图形， l为黑色图形的数量（0~9）。";
//        String form = autoCreateColorShowForm(s, "IJL");
//        System.out.println(form);
//    }
//
//    private static void testAutoCreateShapeShowForm(){
//        int[][] shapeColor1 = new int[9][8];
//
//        shapeColor1[0][2] = 3;
//        shapeColor1[1][1] = 1;
//        shapeColor1[2][3] = 2;
//        shapeColor1[2][4] = 3;
//        shapeColor1[3][5] = 3;
//        shapeColor1[8][0] = 2;
//        ShapeColorUtils.setShapeColor(shapeColor1);
//
//        String s = "A代表矩形，a为矩形的数量（0~9）；B代表圆形，b为圆形的数量（0~9）；C代表三角形，c为三角形的数量（0~9）;D代表菱形，d为菱形的数量（0~9）;E代表五角星，e为五角星数量（0~9）";
//        String form = autoCreateShapeShowForm(s, "ADE");
//        System.out.println(form);
//    }
//
//
//    private static void testAutoCreateCoord(){
//        String s = "N=1    从车入库坐标：B1      N=2     从车入库坐标：D1      \n" +
//                "N=3   从车入库坐标：F1      N=4    从车入库坐标：G2 \n";
//
//        String s1 = autoCreateCoord(s, 4);
//        System.out.println(s1);
//    }
//}
