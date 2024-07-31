package im.hdy;

import im.zhy.param.ColorName;
import im.zhy.param.TrafficLightName;

import java.util.*;

/**
 * Created by hdy on 07/05/2018.
 */

public class CarData {

    // 用于记录同时执行识别图片和车牌成功的指令到底有没有发送，因为要考虑到两个线程的因素,false为还没发送
    public static boolean license_shape_complete = false;

    // 图形识别是否完成
    public static boolean shape_complete = false;

    // 存储 RFID 数据
    public static Map<Integer, String> rfidMap = new HashMap<>();


    //烽火台标志物 开启码 6个
    public static short[] arm_code = new short[]{(short) 0x03, (short) 0x05,
            (short) 0x14, (short) 0x45, (short) 0xDE,
            (short) 0x92};

    //交通灯  1 红  2 绿  3 黄
//    public static int traffic_light = TrafficLightName.YELLOW;

    // 分别存放两个 交通的的信息
    public static volatile Map<Integer, Integer> trafficLightMap = new HashMap<>();


    //车牌信息
    public static String license = "";

    // 分别存放两个 TFT 的车牌信息
//    public static Map<Integer, String> licenseMap = new HashMap<>();


    //二维码
//    public static String QRCode1 = "";
//
//    public static String QRCode2 = "";


//    public static Map<Integer, String> QRCodeMap = new HashMap<>();

//    public static String deputy_QRCode = "";


    // Y的加密参数
    public static String Y = "8";

    // 入库坐标
    public static int N = 1;

    //扇区地址
    public static int section = 3;

    //块地址
    public static int block = 2;

    // 4; 梯形 5：饼图 6：靶图 7：条形图 这个四个并没有什么用只是和官方文档设置成一样
    // i 0：矩形 1：圆形 2：三角形 3：菱形 4; 梯形 5：饼图 6：靶图 7：条形图 8：五角星
    // j 0:红色 1：绿色 2：蓝色  3：黄色  4：紫色  5：青色  6：黑色  7：白色
//    public static int[][] shapeColor = new int[9][8];

    // 分别存放两个 TFT 的形状和颜色信息
//    public static Map<Integer, int[][]> shapeColorMap = new HashMap<>();


    // 预备数据存储
//    public static Map<Integer, byte[]> prepareDataMap = new HashMap<>();


    // 百度 EasyDL
    // 请替换为您的序列号

    // 开发机  4B55-B266-3A60-4CAB
    // 魅族  D6F0-A569-3501-6B45
    // 2933-B67B-43E8-8F25
    // 平板 B16C-59DA-2378-515C
//    public static final String SERIAL_NUM = "3933-4F64-2B25-9E86";
//    public static final String SERIAL_NUM = "DF62-2046-6BF4-2AC3";

    // 图形识别背景色
//    public static String  background_color = ColorName.WHITE;

    // 容易出错的 交通灯
//    public static int easy_error_trafficLight = TrafficLightName.GREEN;

    // 车牌格式正则  L：大写字母 N：数字  C：大写字母或者数字 或直接 XYYYXY X：字母 Y：数字，直接复制题目
//    public static String licenseMatcherStr = createLicenseMatch("C5");

    // 两个静态标志物 0：静态标志物在正前方(在边缘离摄像头稍进的地方)， 1：摄像头需要向右或者左转动90°  2：摄像头需要向右或者左转动45°  3：静态标志物在正前方(在里面离摄像头稍远的地方)
//    public static int[] staticLandmark_site = new int[]{3, 1};



//    private static String createLicenseMatch(String s){
//
//        s = StrExtractUtils.trim(s);
//        String createMatcher = "";
//
//        if (s.startsWith("L") || s.startsWith("N") || s.startsWith("C")){
//            createMatcher = s;
//        }else {
//            List<String> list = StrExtractUtils.inRegexGetGroupStrs(s, "(X{1,6})|(Y{1,6})");
//            for (String s1 : list) {
//                if (s1.startsWith("X")){
//                    createMatcher += "L" + s1.length();
//                }else if (s1.startsWith("Y")){
//                    createMatcher += "N" + s1.length();
//                }
//            }
//            System.out.println("createLicenseMatch" + createMatcher);
//        }
//
//        return StrExtractUtils.createMatcherStr(createMatcher, true);
//    }



    public static void main(String[] args) {


    }



    // 初始化一些测试数据

//    static{
//
//        // 图形信息
//        int[][] shapeColor1 = new int[9][8];
//
//        shapeColor1[0][2] = 1;
//        shapeColor1[1][1] = 1;
//        shapeColor1[2][3] = 1;
//        shapeColor1[2][4] = 3;
//        shapeColor1[3][0] = 1;
//        shapeColor1[8][0] = 1;
//
//        CarData.shapeColorMap.put(1, shapeColor1);
//
//        int[][] shapeColor2 = new int[9][8];
//
//        shapeColor2[0][0] = 2;
//        shapeColor2[0][6] = 1;
//        shapeColor2[1][1] = 1;
//        shapeColor2[1][2] = 1;
//        shapeColor2[1][6] = 1;
//        shapeColor2[2][3] = 1;
//        shapeColor2[3][0] = 1;
//        shapeColor2[8][1] = 1;
//
//        CarData.shapeColorMap.put(2, shapeColor2);
//
//        // 车牌信息
//        licenseMap.put(1, new String("A11111"));
//        licenseMap.put(2, new String("A22222"));
//
//        // 交通灯信息
//        trafficLightMap.put(1, TrafficLightName.RED);
//        trafficLightMap.put(2, TrafficLightName.YELLOW);
//
//
//    }




}
