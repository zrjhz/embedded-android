//package im.drh.utils;
//
//import static im.fyhz.utils.newShapeAndLicense.results;
//import static car.car2024.Utils.Socket.SendDataUtils.packageOneByte;
//
//import android.util.Log;
//
//import com.baidu.ai.edge.core.segment.SegmentationResultModel;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//import car.car2024.Utils.Socket.Variable;
//import im.hdy.CarData;
//
///**
// * 图像识别类
// */
//@SuppressWarnings("all")
//public class Shape {
//
//    public static int TRAFFICLIGHT = 1;
//    //只是一个参照没有用
//    public static String[] shape = {"rectangle", "circle", "triangle", "diamond", "梯形"
//            , "饼图", "靶图", "条形图", "star"};
//
//    //保存形状个数
//    public static Map<String, Integer> shapeCount = new HashMap<>(9);
//    //保存颜色个数
//    public static Map<String, Integer> shapeClolor = new HashMap<>(9);
//    //保存图像对应颜色个数
//    public static Map<String, String> shapeAndColor = new HashMap<>();
//    public static String[][] ByteShape = {{"rectangle", "red"}, {"circle", "green"}, {"triangle", "blue"}, {"diamond", "yellow"},
//            {"梯形", "magenta"}, {"饼图", "cyan"}, {"靶图", "black"}, {"条形图", ""}, {"star", ""}};
//
//    /**
//     * 初始化集合，个人感觉写的不是很好，可以修改
//     */
//    private static void iniShape() {
//        shapeAndColor.put("rectangle", "");
//        shapeAndColor.put("circle", "");
//        shapeAndColor.put("triangle", "");
//        shapeAndColor.put("diamond", "");
//        shapeAndColor.put("梯形", "");
//        shapeAndColor.put("饼图", "");
//        shapeAndColor.put("靶图", "");
//        shapeAndColor.put("条形图", "");
//        shapeAndColor.put("star", "");
//        shapeCount.put("rectangle", 0);
//        shapeCount.put("circle", 0);
//        shapeCount.put("triangle", 0);
//        shapeCount.put("diamond", 0);
//        shapeCount.put("梯形", 0);
//        shapeCount.put("饼图", 0);
//        shapeCount.put("靶图", 0);
//        shapeCount.put("条形图", 0);
//        shapeCount.put("star", 0);
//        shapeClolor.put("red", 0);
//        shapeClolor.put("green", 0);
//        shapeClolor.put("blue", 0);
//        shapeClolor.put("yellow", 0);
//        shapeClolor.put("magenta", 0);
//        shapeClolor.put("cyan", 0);
//        shapeClolor.put("black", 0);
//    }
//
//    public static String shapeLabels = "";
//
//    /**
//     * 红路灯对应数值
//     *
//     * @param color
//     * @return
//     */
//    public static int LightNum(String color) {
//        switch (color) {
//            case "green":
//                TRAFFICLIGHT = 2;
//                return 2;
//            case "red":
//                TRAFFICLIGHT = 1;
//                return 1;
//            case "yellow":
//                TRAFFICLIGHT = 3;
//                return 3;
//            default:
//                return 1;
//        }
//    }
//
//    /**
//     * 图像分割
//     *
//     * @param image
//     */
//    public static void shapeSeg() {
//        //初始化集合
//        iniShape();
//        System.out.println("执行shapeseg");
//        //根据置信度，选取采用的图形和颜色
//        for (SegmentationResultModel resultModel : results) {
//            try {
//
//                if (resultModel.getConfidence() > Variable.SHAPECONFIDENCE) {
//                    //统计指定形状
//                    shapeCount.put(resultModel.getLabel().split("_")[1], shapeCount.get(resultModel.getLabel().split("_")[1]) + 1);
//                    //统计指定的颜色
//                    shapeClolor.put(resultModel.getLabel().split("_")[0], shapeClolor.get(resultModel.getLabel().split("_")[0]) + 1);
//                    //统计指定形状的颜色个数
//                    shapeAndColor.put(resultModel.getLabel().split("_")[1], shapeAndColor.get(resultModel.getLabel().split("_")[1]) + resultModel.getLabel().split("_")[0] + "_");
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        Log.i("图形识别个数", String.valueOf(shapeCount));
//        System.out.println("=================" + shapeCount);
//        CarData.license_shape_complete = true;
//    }
//
//    /**
//     * 发送指定形状的个数
//     *
//     * @param shapeInt
//     */
//    public static void sendShapeNum(int shapeInt) {
//        Integer count = shapeCount.get(ByteShape[shapeInt][0]);
//        Log.i(shapeInt + "形状", String.valueOf(count));
//        System.out.println(shapeInt + "形状" + count);
//        packageOneByte(count);
//    }
//
//    /**
//     * 发送指定颜色的个数
//     *
//     * @param colorInt
//     */
//    public static void sendShapeColor(int colorInt) {
//        Integer count = shapeClolor.get(ByteShape[colorInt][1]);
//        Log.i(colorInt + "形状", String.valueOf(count));
//        System.out.println(colorInt + "color" + count);
//        packageOneByte(count);
//    }
//
//    /**
//     * 发送图形的颜色个数
//     */
//    public static void sendShapeColorNumber1(int shapeInt, int colorInt) {
//        int count = shapeAndColor.get(ByteShape[shapeInt][0]).split(ByteShape[colorInt][1]).length;
//        Log.i("count", String.valueOf(count));
//        System.out.println("count" + count);
//        packageOneByte(count);
//    }
//
//
//    /**
//     * 统计出现形状的种类
//     */
//    public static int shapeTypeNumber() {
//        int count = 0;
//        Collection<Integer> values = shapeCount.values();
//        for (Integer value : values) {
//            if (value != 0) count++;
//        }
//        return count;
//    }
//
//    /**
//     * 统计出颜色种类
//     */
//    public static int colorTypeNumber() {
//        int count = 0;
//        Collection<Integer> values = shapeClolor.values();
//        for (Integer value : values) {
//            if (value != 0) count++;
//        }
//        return count;
//    }
//}
