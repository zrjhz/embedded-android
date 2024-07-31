//package im.zhy.shapeDector;
//
//import android.graphics.Bitmap;
//
//import car.car2024.ActivityView.LoginActivity;
//
//import com.baidu.ai.edge.core.base.BaseException;
//import com.baidu.ai.edge.core.detect.DetectionResultModel;
//import com.utils.FileService;
//
//import car.car2024.Utils.Socket.ZigbeeService;
//import car.car2024.Utils.Camera.CameraUtils;
//import im.zhy.param.ColorName;
//import im.zhy.util.*;
//import org.opencv.android.Utils;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfPoint2f;
//import org.opencv.core.Point;
//import org.opencv.core.Size;
//import org.opencv.imgproc.Imgproc;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import car.car2024.FragmentView.LeftFragment;
//import im.hdy.CarData;
//
//
//@SuppressWarnings("all")
//// 新的图形识别类
//public class ShapeDector {
//
//    // 识别的是不是 静态标识图像
//    private static boolean isStatic = false;
//
//    // 设置识别的是 静态标识图像
//    public static void setStatic() {
//        isStatic = true;
//    }
//
//    private static String getSavePath(){
//        return "shape/" + AcceptCarOrder.getIndex();
//    }
//
//    public static void shapeDector(Bitmap bitmap, boolean isAsyn) {
//        Bitmap[] bitmaps = new Bitmap[]{bitmap};
//        shapeDectorRealizeAsyn(bitmaps, isAsyn);
//
//    }
//
//    public static void shapeDector(boolean isAsyn) {
//        Bitmap[] bitmaps = new Bitmap[]{LeftFragment.bitmap};
//        shapeDectorRealizeAsyn(bitmaps, isAsyn);
//    }
//
//    public static void shapeDector(Bitmap[] bitmaps, boolean isAsyn) {
//        shapeDectorRealizeAsyn(bitmaps, isAsyn);
//    }
//
//    // 图形识别实现
//    private static void shapeDectorRealizeAsyn(final Bitmap[] bitmaps, boolean isAsyn) {
//
//        if (isAsyn) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        shapeDectorRealizeSyn(bitmaps);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("图形识别出现异常");
//                    }
//                }
//            });
//
//            thread.start();
//        } else {
//
//            try {
//                shapeDectorRealizeSyn(bitmaps);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("图形识别出现异常");
//            }
//        }
//
//    }
//
//    // 图形识别实现
//    private static void shapeDectorRealizeSyn(Bitmap[] bitmaps) {
//
//        Map<String, List> map = getMatOfPoint(bitmaps);
//
//        shapeDectorRealizeSyn(map);
//    }
//
//    public static void shapeDectorRealizeSyn(Map<String, List> map) {
//        // println test
//        System.out.println("shapeDectorRealizeSyn"+map);
//
//        initShapeData();
//
//        List<Bitmap> colorList = map.get("colorList");
//        // println test
//        System.out.println("shapeDectorRealizeSyn" + colorList);
//        for (int i = 0; i < 8; i++) {
//            List<MatOfPoint2f> list = map.get(GetVarName.getColorName(i));
//
//            if (list == null || list.size() <= 0) {
//                continue;
//            }
//
//            Bitmap colorBitmap = colorList.get(i);
//            if (colorBitmap == null) {
//                continue;
//            }
//
//            for (MatOfPoint2f matOfPoint2f : list) {
//
////                int judge = ShapeJudgeUtils.shapeJudge(matOfPoint2f);
//
//                int judge = ShapeJudgeUtils.newShapeJudge(matOfPoint2f, colorBitmap);
//
//                if (judge < 0) {
//                    System.out.println("Judge:contiune");
//                    continue;
//                }
//
////                CarData.shapeColor[judge][i]++;
//
//            }
//
//        }
//
//        if (!isStatic){
//            System.out.println("保存的是TFT " + AcceptCarOrder.getIndex() + " 识别的数据");
////            CarData.shapeColorMap.put(AcceptCarOrder.getIndex(), CarData.shapeColor);
//        }else {
//            // 用于保存静态标志物的数据
//            System.out.println("保存的是静态标志物识别的数据");
////            StaticShapeDector.saveData();
//
//            isStatic = false;
//        }
//
////        testPrintShapeColor(CarData.shapeColor);
//
//
//    }
//
//    public static void newShapeDector(Bitmap bitmap, boolean isAsyn) {
//        Bitmap[] bitmaps = new Bitmap[]{bitmap};
//        newShapeDectorRealizeAsyn(bitmaps, isAsyn);
//    }
//
//    public static void newShapeDector(Bitmap[] bitmaps, boolean isAsyn) {
//        newShapeDectorRealizeAsyn(bitmaps, isAsyn);
//    }
//
//    private static void newShapeDectorRealizeAsyn(final Bitmap[] bitmaps, boolean isAsyn) {
//
//        if (isAsyn) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        newShapeDectorRealizeSyn(bitmaps);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("图形识别出现异常");
//                    }
//                }
//            });
//
//            thread.start();
//        } else {
//            try {
//                newShapeDectorRealizeSyn(bitmaps);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("图形识别出现异常");
//
//            }
//        }
//
//    }
//
//    private static void newShapeDectorRealizeSyn(Bitmap[] bitmaps) {
//
//        Map<String, List> map = getMatOfPoint(bitmaps);
//
//        newShapeDectorRealizeSyn(map);
//    }
//
//    // 同步
//    public static void newShapeDectorRealizeSyn(Map<String, List> map) {
//
//        System.out.println("newShapeDectorRealizeSyn-----------");
//
//        initShapeData();
//
//        List<Bitmap> colorList = map.get("colorList");
//
//        for (int i = 0; i < 8; i++) {
//            List<MatOfPoint2f> list = map.get(GetVarName.getColorName(i));
//
//            if (list == null || list.size() <= 0) {
//                continue;
//            }
//
//            Bitmap colorBitmap = colorList.get(i);
//
//            if (colorBitmap == null) {
//                continue;
//            }
//
////            ShapeJudgeUtils.shapeJudgeEasyDLDetect(colorBitmap, list.size(), i);
//
//            // 图形检测方法
//            ShapeJudgeUtils.newShapeJudgeEasyDLDetect(colorBitmap, list, i);
//        }
//
//        if (!isStatic){
//            System.out.println("保存的是TFT " + AcceptCarOrder.getIndex() + " 识别的数据");
////            CarData.shapeColorMap.put(AcceptCarOrder.getIndex(), CarData.shapeColor);
//        }else {
//            // 用于保存静态标志物的数据
//            System.out.println("保存的是静态标志物识别的数据");
////            StaticShapeDector.saveData();
//
//            isStatic = false;
//        }
//
//        // 打印输出结果
////        testPrintShapeColor(CarData.shapeColor);
//
//    }
//
//
//    private static Map<String, List> getMatOfPoint(Bitmap[] bitmaps) {
//
//        Map<String, List> map = new HashMap<>();
//
//        // 截取最大矩形
//        Bitmap[] cutoutMaxRectangle = new Bitmap[bitmaps.length];
//
//        for (int i = 0; i < cutoutMaxRectangle.length; i++) {
//            cutoutMaxRectangle[i] = ImageDisposeUtils.cutoutMaxRectangle(bitmaps[i]);
//        }
//
////        List bitmapList = new ArrayList();
//        Bitmap colorBitmap = cutoutMaxRectangle[0].copy(Bitmap.Config.ARGB_8888, true);
//
//        FileService.saveClassPhoto(colorBitmap, "color1", getSavePath());
////        bitmapList.add(colorBitmap);
//
//        List colorList = new ArrayList();
//        for (int i = 0; i < 8; i++) {
//
//            //去除 背景色
////            if (GetVarName.getColorInt(CarData.background_color) == i) {
////                colorList.add(null);
////                continue;
////            }
//
//            if (bitmaps.length > 1 && i == GetVarName.getColorInt(CameraUtils.getSpecialColor())) {
//                colorBitmap = cutoutMaxRectangle[1].copy(Bitmap.Config.ARGB_8888, true);
////                bitmapList.add(colorBitmap);
//                FileService.saveClassPhoto(colorBitmap, "color2", getSavePath());
//            } else {
//                colorBitmap = cutoutMaxRectangle[0].copy(Bitmap.Config.ARGB_8888, true);
//            }
//
//            if (i == GetVarName.getColorInt(ColorName.BLACK)) {
//                colorBitmap = Bitmap.createBitmap(colorBitmap, 13, 13, colorBitmap.getWidth() - 26, colorBitmap.getHeight() - 26);
//
//                FileService.saveClassPhoto(colorBitmap, "color_black", getSavePath());
//
//            }
//
//            for (int x = 0; x < colorBitmap.getWidth(); x++) {
//                for (int y = 0; y < colorBitmap.getHeight(); y++) {
//
//                    int newPixel = ImageDisposeUtils.colorExtract(colorBitmap, x, y, i);
//                    colorBitmap.setPixel(x, y, newPixel);
//
//                }
//            }
//
//            colorBitmap = open(colorBitmap, 3);
//
////            if (!(GetVarName.getColorInt(ColorName.BLACK) == i)){
////                colorBitmap = open(colorBitmap, 3);
////            }
//
//            if (GetVarName.getColorInt(ColorName.BLACK) == i) {
//                colorBitmap = close(colorBitmap, 3);
//
//            }
//
//            colorList.add(colorBitmap);
//
//            FileService.saveClassPhoto(colorBitmap, "copy_" + GetVarName.getColorName(i) + ".png", getSavePath());
//
//            Bitmap image = Bitmap.createBitmap(colorBitmap);
//
////            Mat src = new Mat();
////            Utils.bitmapToMat(image, src);
////
////            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
////
////            Mat dst = new Mat();
////
////            double threshold = Imgproc.threshold(src, dst, 0, 255, Imgproc.THRESH_BINARY_INV);
////
////            List<MatOfPoint> contours = new ArrayList<>();
////            Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//
//            // 梯度求取
//            List<MatOfPoint2f> list = ImageDisposeUtils.getGrads(image, false);
//
//            for (MatOfPoint2f matOfPoint2f : list) {
//
//                Bitmap bitmap = Bitmap.createBitmap(colorBitmap);
//
//            }
//
//            map.put(GetVarName.getColorName(i), list);
//
//            System.out.println(GetVarName.getColorName(i) + ":" + list.size());
//
//        }
//
////        map.put("bitmapList", bitmapList);
//        map.put("colorList", colorList);
//
//        return map;
//    }
//
//    /**
//     * 去除指定范围内的黑点
//     */
//    private static boolean wipeBlackSpot(Bitmap bitmap, int x, int y) {
//
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        if (x >= 0 && x <= 80 && y >= 0 && y <= 10) {
//            return true;
//        }
//
//        if (x >= width - 80 && x <= width && y >= 0 && y <= 10) {
//            return true;
//        }
//
//        if (x >= 0 && x <= 80 && y >= height - 10 && y <= height) {
//            return true;
//        }
//
//        if (x >= width - 80 && x <= width && y >= height - 10 && y <= height) {
//            return true;
//        }
//
//        return false;
//
//    }
//
//
//    public static Map<String, List> judgeShape(Bitmap bitmap){
//
//        try {
//            Bitmap[] bitmaps = new Bitmap[]{bitmap};
//
//            Map<String, List> map = getMatOfPoint(bitmaps);
//
//            int amount = 0;
//            for (int i = 0; i < 8; i++) {
//                List list = map.get(GetVarName.getColorName(i));
//
//                if (list != null && list.size() > 0){
//                    amount++;
//                }
//
//                if (amount >= 5){
//                    return map;
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//
//    }
//
//
//    public  void testShapeEasyDL(){
//        Bitmap bitmap = LeftFragment.bitmap;
//
//        List<DetectionResultModel> detect = null;
////        try {
//////            detect = LoginActivity.manager.detect(bitmap, 0);
////        } catch (BaseException e) {
////            e.printStackTrace();
////        }
//        System.out.println("---------------------" + detect.size());
//
//        System.out.println("-----------------------------" + ShapeJudgeUtils.fillDetectionResultModel(detect).size());
//
//
//    }
//
//
//
//    static int number = 1;
//
//    // 收集图形图像
//    public static void gatherShapeImages(){
//
//        number = 0;
//
//        for (int i = 1; i < 6; i++) {
//            System.out.println("当前轮数:" + i);
//            // 向下翻一张
//            ZigbeeService.TFT_DownShow();
//
//            ThreadUtils.sleep(3000);
//
//            saveShapeImages(i);
//            System.out.println("当前轮数:" + i + "结束");
//        }
//        System.out.println("收集图片结束————————————————————————————————————");
//    }
//
//
//    // 搜集测试的图像
//    public static void gatherTestImage(){
//        number++;
//        FileService.saveClassPhoto(LeftFragment.bitmap, "test_" + number, "test");
//        ZigbeeService.TFT_DownShow();
//    }
//
//
//    // 保存图形图像
//    public static void saveShapeImages(int time){
//        Bitmap bitmap1 = LeftFragment.bitmap;
//        // 截取最大矩形
//        Bitmap cutoutMaxRectangle = ImageDisposeUtils.cutoutMaxRectangle(bitmap1);
//
//        for (int i = 0; i < 7; i++) {
//            Bitmap bitmap = cutoutMaxRectangle.copy(Bitmap.Config.ARGB_8888, true);
//
//            if (i == GetVarName.getColorInt(ColorName.BLACK)) {
//                bitmap = Bitmap.createBitmap(bitmap, 10, 13, bitmap.getWidth() - 20, bitmap.getHeight() - 26);
//
//            }
//
//            for (int x = 0; x < bitmap.getWidth(); x++) {
//                for (int y = 0; y < bitmap.getHeight(); y++) {
//                    int newPixel = ImageDisposeUtils.colorExtract(bitmap, x, y, i);
//                    bitmap.setPixel(x, y, newPixel);
//                }
//            }
//            Mat image = new Mat();
//            Utils.bitmapToMat(bitmap, image);
//            // 腐蚀处理
//            bitmap = open(bitmap, 3);
//            // 保存测试图形图像
////            ShapeJudgeUtils.testSavePhoto(bitmap, ShapeUtils4.getColorName(i) + time);
//            FileService.saveClassPhoto(bitmap, number + "", "color9");
//            number++;
//        }
//
//
//    }
//
//    // 初始化图像数据
//    public static void initShapeData(){
//
//        CarData.shape_complete = false;
//
////        CarData.shapeColor = new int[9][8];
//
//    }
//
//    // 开操作
//    public static Bitmap open(Bitmap bitmap, int size){
//        Mat input = new Mat();
//        Utils.bitmapToMat(bitmap, input);
//        Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2GRAY);
//        //膨胀
//        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_OPEN, new Size(size, size));
//        for (int i = 0; i < 1; i++) {
//            Imgproc.dilate(input, input, element, new Point(-1, -1), 1);
//        }
//
//        Utils.matToBitmap(input, bitmap);
//
//
//        return bitmap;
//
//    }
//
//    // 闭操作
//    public static Bitmap close(Bitmap bitmap, int size){
//
//        for (int x = 0; x < bitmap.getWidth(); x++) {
//            for (int y = 0; y < bitmap.getHeight(); y++) {
//
//                int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//                int f;
//                if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                    f = ImageDisposeUtils.blackOrWhite(false);
//                }else {
//                    f = ImageDisposeUtils.blackOrWhite(true);
//                }
//
//                bitmap.setPixel(x, y, f);
//
//            }
//
//        }
//
//        bitmap = ShapeDector.open(bitmap, size);
//
//
//        for (int x = 0; x < bitmap.getWidth(); x++) {
//            for (int y = 0; y < bitmap.getHeight(); y++) {
//
//                int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//                int f;
//                if (rgb[0] == 0xff && rgb[1] == 0xff && rgb[2] == 0xff){
//                    f = ImageDisposeUtils.blackOrWhite(true);
//                }else {
//                    f = ImageDisposeUtils.blackOrWhite(false);
//                }
//
//                bitmap.setPixel(x, y, f);
//
//            }
//
//        }
//
//        return bitmap;
//
//    }
//
//    // shape color 测试打印
//    private static void testPrintShapeColor(int[][] results){
//
//        CarData.shape_complete = true;
//
//        String name = "";
//        String color = "";
//        shape:for (int i = 0; i < results.length; i++) {
//            switch (i) {
//                //矩形 圆形 三角形 菱形 五角星
//                case 0:
//                    name = "矩形";
//                    break;
//                case 1:
//                    name = "圆形";
//                    break;
//                case 2:
//                    name = "三角形";
//                    break;
//                case 3:
//                    name = "菱形";
//                    break;
//                /*case 4:
//                    name = "梯形";
//                    break;
//                case 5:
//                    name = "饼图";
//                    break;
//                case 6:
//                    name = "靶形";
//                    break;
//                case 7:
//                    name = "条形图";
//                    break;*/
//                case 8:
//                    name = "五角星";
//                    break;
//                default:
//                    continue shape;
//
//            }
//            System.out.println("******" + name + "开始*******");
//
//            for (int j = 0; j < results[i].length; j++) {
//                switch (j) {
//                    case 0:
//                        color = "红";
//                        break;
//                    case 1:
//                        color = "绿";
//                        break;
//                    case 2:
//                        color = "蓝";
//                        break;
//                    case 3:
//                        color = "黄";
//                        break;
//                    case 4:
//                        color = "品";
//                        break;
//                    case 5:
//                        color = "青";
//                        break;
//                    case 6:
//                        color = "黑";
//                        break;
//                    case 7:
//                        color = "白";
//                        break;
//                }
//                System.out.println(color + ":" + results[i][j]);
//            }
//            System.out.println("******" + name + "结束*******");
//        }
//    }
//
//}
