package im.fyhz.utils;


//import com.baidu.ai.edge.core.base.BaseException;
//import com.baidu.ai.edge.core.segment.SegmentationResultModel;


/**
 * Created by fyhz on 04/05/2021
 */
public class newShapeAndLicense {

    public static String labels = "";

//    public static String[] shape = {"rectangle", "circle", "triangle", "dimond", "梯形"
//            , "饼图", "靶图", "条形图", "star"};
//
//    public static String[] color = {"red_", "green_", "blue_", "yellow_", "magenta_", "cyan_", "black_"};

//    public static String shapeLabels = "";

//    public static String license;

    public static String[] word = new String[6];

    public static int[] right = new int[6];

//    public static List<SegmentationResultModel> results = null;
//
//    public static Map<String, SegmentationResultModel> license = new HashMap<>();
    /**
     * segment主方法，检测图形的所有的颜色与图形与车牌
     * @param image
     * @param resultConfidence
     * @return
     */

//    public static String shapeSeg(Bitmap image) {
//        List<SegmentationResultModel> results = null;
//        results = segment(image, 0.9f);
//        labels = "";
//        for (SegmentationResultModel resultModel: results) {
//            if (resultModel.getConfidence() > 0.7f) {
//                labels += resultModel.getLabel() + " ";
//            }
//        }
//        System.out.println(labels);
//        // 图形识别
//        Pattern p = Pattern.compile(".*[_]+.*");
//        Matcher m = p.matcher(labels);
//        if (m.matches()) {
//            shapeLabels = labels;
//            System.out.println("shapelabels========================="+shapeLabels);
//            if (shapeTypeNumber(1) == shapeTypeNumber(2)) {
//                // 小车图形识别是否成功
////            if (shapeTypeNumber(SendDataUtils.by)!=0) {
//                CarData.shape_complete = true;
//                return labels;
//            }
////            }
//        }
//        return null;
//    }


    /**
     * 车牌检测
     *
     * @param
     * @return
     */
//    public static String licenseSeg() {
//
//        try {
//            labels = "";
//            for (int i = 0; i < results.size(); i++) {
//                labels += results.get(i).getLabel() + " " + results.get(i).getConfidence() + " ";
//            }
//            System.out.println("车牌检测label" + labels);
//            Log.i("车牌检测label", labels);
//            // 车牌识别
//            Pattern p = Pattern.compile(".*[A-Z]+.*");
//            Matcher m = p.matcher(labels);
//            if (m.matches()) {
//                float max = 0;
//                int index = 0;
//                for (int i = 0; i < 6; i++) {
//
////                if (size<6) {
////                    if (results.get(i).getConfidence()>min) {
////                        word[size] = results.get(i).getLabel();
////                        right[size] = results.get(i).getBox().right;
////                        size++;
////                    }
////                }
//                    for (int j = 0; j < results.size(); j++) {
//                        if (results.get(j).getConfidence() > max) {
//                            max = results.get(j).getConfidence();
//                            index = j;
//                        }
//                    }
//                    word[i] = results.get(index).getLabel();
//                    right[i] = results.get(index).getBox().right;
//                    SegmentationResultModel segmentationResultModel = results.get(index);
//                    license.put(word[i], segmentationResultModel);
//                    segmentationResultModel.setConfidence(0);
//                    results.set(index, segmentationResultModel);
//                    max = 0;
//                    index = i;
//                }
//
//                for (int i = 0; i < right.length - 1; i++) {
//                    int minIndex = i;
//                    for (int j = i + 1; j < right.length; j++) {
//                        if (right[j] < right[minIndex]) {
//                            minIndex = j;
//                        }
//                    }
//
//                    // 如果有更小值，交换字符位置
//                    if (minIndex != i) {
//                        int temp = right[i];
//                        right[i] = right[minIndex];
//                        right[minIndex] = temp;
//
//                        String str = word[i];
//                        word[i] = word[minIndex];
//                        word[minIndex] = str;
//                    }
//                }
//
//                StringBuilder str = new StringBuilder();
//                for (int i = 0; i < word.length; i++) {
//                    if (word[i] == null) {
//                        word[i] = "";
//                    }
//                    str.append(word[i]);
//                }
////            if (word.length >= 6) {
////                for (int i = 0; i < 6; i++) {
////                    str += word[i];
////                }
////            } else {
////                // 识别位数不够，用I补全
////                for (int i = 0; i < word.length; i++) {
////                    str += word[i];
////                }
////                int time = 6 - word.length;
////                for (int i = 0; i < time; i++) {
////                    str += "I";
////                }
////            }
//
//                System.out.println("车牌识别结果为：" + str);
//                Log.i("车牌识别结果为", str.toString());
//                if (str.length() == 6) {
//                    CarData.license = str.toString();
//                }
//                return str.toString();
//            }
//            return null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /**
     * 判断是图形画面，根据模型识别是否有结果判断
     *
     * @param bitmap
     * @return 0：什么都没有 1：图形 2：车牌
     */
//    public static int newJudgeShape(Bitmap bitmap) {
//        labels = "";
//        if (results.size() != 0) {
//            for (int i = 0; i < results.size(); i++) {
//                labels += results.get(i).getLabel() + " ";
//            }
//
//            //两句不可调换位置，国字可能会识别成黑色正方形，换位置可能会直接进入图形识别
//            // 车牌识别
//            Pattern p1 = Pattern.compile(".*[A-Z]+.*");
//            Matcher m1 = p1.matcher(labels);
//            if (m1.matches()) {
//                return 2;
//            }
//
//            // 图形识别
//            Pattern p = Pattern.compile(".*[_]+.*", Pattern.CASE_INSENSITIVE);
//            Matcher m = p.matcher(labels);
//            if (m.matches()) {
//                // 小车图形识别是否成功
//                return 1;
//            } else {
//                return 0;
//            }
//        } else {
//            return 0;
//        }
//    }


    /**
     * resultModel 中的 labels 合并到一个 String 中
     *
     * @param results
     * @return 合并的String
     */
//    public static String resultModelToString(List<SegmentationResultModel> results) {
////        labels = "";
//
//        for (SegmentationResultModel result : results) {
//            System.out.println(result);
////            labels += result.getLabel() + " ";
//        }
//        return labels;
//    }

    /**
     * 统计出现的指定形状数量
     */
//    public static int shapeTypeNumber(int shapeInt) {
//        String str = shape[shapeInt];
//        Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(shapeLabels);
//        int count = 0;
//        while (m.find()) {
//            count++;
//        }
//        System.out.println(shape[shapeInt] + "形状数量为：" + count);
////        packageOneByte(count);
//        return count;
//    }

    /**
     * 统计出现的形状种类
     */
//    public static int shapeTypeNumber() {
//        int count = 0;
//        for (String shape :
//                shape) {
//            Pattern p = Pattern.compile(shape, Pattern.CASE_INSENSITIVE);
//            Matcher m = p.matcher(shapeLabels);
//            if (m.find()) {
//                count++;
//            }
//        }
//        System.out.println("形状种类为：" + count);
////        packageOneByte(count);
//        return count;
//    }

    /**
     * 统计出现的指定颜色种类
     */
//    public static int colorTypeNumber(int colorInt) {
//        String str = color[colorInt];
//        Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(shapeLabels);
//        int count = 0;
//        while (m.find()) {
//            count++;
//        }
//        System.out.println(color[colorInt] + "形状数量为：" + count);
////        packageOneByte(count);
//        return count;
//    }


    /**
     * 统计出现的颜色种类
     */
//    public static int colorTypeNumber() {
//        int count = 0;
//        for (String color :
//                color) {
//            Pattern p = Pattern.compile(color, Pattern.CASE_INSENSITIVE);
//            Matcher m = p.matcher(shapeLabels);
//            if (m.find()) {
//                count++;
//            }
//        }
//        System.out.println("颜色种类为：" + count);
////        packageOneByte(count);
//        return count;
//    }

    /**
     * 统计字符串中出现的请求颜色形状的次数并发送
     * @param shapeInt
     * @param colorInt
     * @return
     */
//    public static void sendShapeColorNumber(int shapeInt, int colorInt) {
//        String str = color[colorInt] + shape[shapeInt];
//        Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(shapeLabels);
//        int count = 0;
//        while (m.find()) {
//            count++;
//        }
//        System.out.println(str + "数量为：" + count);
//        packageOneByte(count);
//    }

    /**
     * 统计字符串中出现的请求颜色形状
     * @param shapeInt
     * @param colorInt
     */
//    public static int ShapeColorNumber(int shapeInt, int colorInt) {
//        String str = color[colorInt] + shape[shapeInt];
//        Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(shapeLabels);
//        int count = 0;
//        while (m.find()) {
//            count++;
//        }
//        System.out.println(str + "数量为：" + count);
//        return count;
//    }


    /**
     * 统计字符串中出现的请求颜色的次数
     * @param colorInt
     * @return
     */
//    public static void sendColorNumber(int colorInt) {
//        String str = color[colorInt];
//        Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(shapeLabels);
//        int count = 0;
//        while (m.find()) {
//            count++;
//        }
//        System.out.println(str + "数量为：" + count);
//        packageOneByte(count);
//    }

    /**
     * 统计字符串中出现的请求形状的次数
     * @param shapeInt
     * @return
     */
//    public static void sendShapeNumber(int shapeInt) {
//        String str = shape[shapeInt];
//        Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(shapeLabels);
//        int count = 0;
//        while (m.find()) {
//            count++;
//        }
//        System.out.println(str + "数量为：" + count);
//        packageOneByte(count);
//    }

    /**
     * 检测模型测试方法，未用到，根据模型需要切换
     * @param image
     * @param resultConfidence
     * @return
     */
//    public static List<DetectionResultModel> detect(Bitmap image, Float resultConfidence) {
//        List<DetectionResultModel> results = null;
//        try {
//            results = LoginActivity.manager.detect(image, resultConfidence);
//        } catch (BaseException e) {
//            e.printStackTrace();
//        }
//        for (DetectionResultModel result: results) {
////            Log.i(TAG, result.getLabelIndex() + ":" + result.getConfidence()
////                + ":" + result.getBounds());
//            System.out.println(result.getLabel()+"    "+result.getConfidence());
//        }
//        return results;
//    }
}
