package im.drh.utils;

import static org.opencv.imgproc.Imgproc.MORPH_RECT;
//import static im.fyhz.utils.newShapeAndLicense.results;

import android.graphics.Bitmap;

//import com.baidu.ai.edge.core.base.BaseException;
//import com.baidu.ai.edge.core.classify.ClassificationResultModel;
//import com.baidu.ai.edge.core.detect.DetectionResultModel;
//import com.baidu.ai.edge.core.infer.InferConfig;
//import com.baidu.ai.edge.core.infer.InferManager;
//import com.baidu.ai.edge.core.segment.SegmentationResultModel;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import car.car2024.Utils.Socket.Variable;

/**
 * 判断加识别
 */
public class JudgeBitmapAndIdentify {

    public static String label = "";
    public static float maxConf = 0;
    public static int trafficSign = 1;
    public static int tt = 0;

    //判断类别
//    public static String newJudgeBitmap(Bitmap bitmap) {
//        InferManager manager = null;
//        int[] vary = getVary(Variable.index);
//        List<DetectionResultModel> detect = null;
//        try {
//            manager = new InferManager(XcApplication.getApp(), LoginActivity.detectConfig, Variable.SERIAL_NUM);
//            detect = manager.detect(bitmap);
//            manager.destroy();
//        } catch (BaseException e) {
//            e.printStackTrace();
//        }
//        String result = "";
//        float max = 0;
//        if (detect != null) {
//            for (DetectionResultModel classificationResultModel : detect) {
//                if (classificationResultModel.getConfidence() > max) {
//                    max = classificationResultModel.getConfidence();
//                    result = classificationResultModel.getLabel();
//                }
//            }
//        }
//        if (max < 0.6f) result = "";
//        System.out.println("================" + max);
//        Log.i("Type", result);
//        System.out.println("判断结果==========" + result);
//        return result;
//    }

//    public static void Identify(Bitmap bitmap, String type) throws BaseException {
//        //判断当前识别对象
//        int[] Efficient = getVary(Variable.index);
//
//        //图形识别
//        if (type.equals("shape") && Efficient[0] == 1) {
//            InferManager manager = null;
//            Log.i("进入图形识别========", "");
//            System.out.println("进入图形识别========");
//            try {
//                manager = new InferManager(XcApplication.getApp(), LoginActivity.shapeConfig, Variable.SERIAL_NUM);
//                results = manager.segment(bitmap, 0.6f);
//                Shape.shapeSeg();
//                manager.destroy();
//            } catch (BaseException e) {
//                if (manager != null) {
//                    manager.destroy();
//                }
//                e.printStackTrace();
//            }
//            return;
//        }
//
//
//        //车牌识别
//        if (type.equals("license") && Efficient[1] == 1) {
//            InferManager manager = null;
//            Log.i("进入车牌识别========", "");
//
//            System.out.println("进入车牌识别==========");
//            //车牌颜色判断
////            if (JudgeColor.JudgeIsRightLicence(bitmap)) {
//            Log.i("车牌颜色判断成功", "");
//            System.out.println("车牌颜色判断成功============");
//            try {
//                manager = new InferManager(XcApplication.getApp(), LoginActivity.licenseConfig, Variable.SERIAL_NUM);
//                results = manager.segment(bitmap, 0.6f);
//                newShapeAndLicense.licenseSeg();
//                manager.destroy();
//            } catch (BaseException e) {
//                if (manager != null) {
//                    manager.destroy();
//                }
//                e.printStackTrace();
//            }
////            }
//            return;
//        }
//
//
//        //识别交通标志
//        if (type.equals("trafficSign") && Efficient[3] == 1) {
//            InferManager manager = null;
//            Log.i("进入交通标志识别", "");
//            System.out.println("进入交通标志识别===========");
//            try {
//                manager = new InferManager(XcApplication.getApp(), LoginActivity.trafficSignConfig, Variable.SERIAL_NUM);
//                results = manager.segment(bitmap, 0.6f);
//                TrafficSign.getOneTraffic(bitmap);
//                TrafficSign.getMultiTraffic();
//                manager.destroy();
//            } catch (BaseException e) {
//                if (manager != null) {
//                    manager.destroy();
//                }
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        //车辆识别
//        if (type.equals("vehicle") && Efficient[2] == 1) {
//            InferManager manager = null;
//            Log.i("车辆识别", "");
//            try {
//                manager = new InferManager(XcApplication.getApp(), LoginActivity.vehicleConfig, Variable.SERIAL_NUM);
//                results = manager.segment(bitmap, 0.6f);
//                Vehicle.getMax();
//                Vehicle.getMul();
//                manager.destroy();
//            } catch (BaseException e) {
//                if (manager != null) {
//                    manager.destroy();
//                }
//                e.printStackTrace();
//            }
//            return;
//        }
//        //识别二维码
//        if (type.equals("QRcode") && Efficient[4] == 1) {
//            QRcode.IdentifyQRCode(bitmap);
//        }
////            if (type.equals("")&&Efficient[5]==1||tt==1){
////                tt=0;
////            Ocr.OcrRecognition(bitmap);
////        }
//    }

    /**
     * 获取当前识别的有效图片类型数组
     *
     * @param index
     * @return
     */
    public static int[] getVary(int index) {
        switch (index) {
            case 1:
                return Variable.TFT1Vary;
            case 2:
                return Variable.TFT2Vary;
            case 3:
                return Variable.static1Vary;
            case 4:
                return Variable.static2Vary;
        }
        return Variable.TFT1Vary;
    }

    /**
     * 检测车辆上的车牌
     *
     * @param
     * @return
     * @throws BaseException
     */
//    public static boolean inTheCar(Bitmap bitmap) throws BaseException {
//        InferManager manager = null;
//
//        System.out.println("判断车牌是否在车上");
//        try {
//            //检测车辆
//            manager = new InferManager(XcApplication.getApp(), LoginActivity.vehicleConfig,
//                    Variable.SERIAL_NUM);
//            List<SegmentationResultModel> segment = manager.segment(bitmap);
//            //储存车辆矩形空间
//            Rect box = getRectInVehicle(segment);
//            manager.destroy();
//            //检测车牌
//            Identify(bitmap, "license");
//            Map<String, SegmentationResultModel> license = newShapeAndLicense.license;
//            String[] word = newShapeAndLicense.word;
//            int count = 0;
//            for (int i = 0; i < word.length; i++) {
//                Rect box1 = license.get(word[i]).getBox();
//                if (box.contains(box1)) {
//                    count++;
//                }
//            }
//            return count >= 6;
//        } catch (BaseException e) {
//            if (manager != null) {
//                manager.destroy();
//            }
//            e.printStackTrace();
//        }
//        return false;
//    }

//    private static Rect getRectInVehicle(List<SegmentationResultModel> segment) {
//        float MaxConf = 0;
//        Rect box = null;
//        for (SegmentationResultModel result : segment) {
//            if (result.getConfidence() > MaxConf && result.getConfidence() > Variable.VEHICLECONFIDENCE) {
//                box = result.getBox();
//            }
//        }
//        return box;
//    }

//    public static void judgeLicense() throws Exception {
//        Bitmap bitmap2 = LeftFragment.bitmap;
//        List<Bitmap> bitmaps = cutLicense(bitmap2);
//        String rfid = Variable.RFID.toString();
//        char[] chars = rfid.toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            if (!(chars[i] >= 'A' && chars[i] <= 'Z') || !(chars[i] >= '0' && chars[i] <= '9')) {
//                chars[i] = 0;
//            }
//        }
//        //循环标签
//        boolean loop = true;
//        while (loop) {
//            //结束循环标签
//            boolean flag2 = true;
//            try {
//                for (Bitmap bitmap : bitmaps) {
//                    JudgeBitmapAndIdentify.Identify(bitmap, "license");
//                }
//            } catch (BaseException e) {
//                e.printStackTrace();
//            }
//            String license = CarData.license;
//            char[] chars1 = license.toCharArray();
//            for (int i = 0; i < chars1.length; i++) {
//                //如果是乱码则继续循环
//                if (chars[i] == 0)
//                    continue;
//                //如果识别出来的车牌和字符不相符则直接跳出检测继续循环检测
//                if (chars[i] != chars1[i]) {
//                    flag2 = false;
//                    break;
//                }
//            }
//            //用循环结束标签打断循环
//            if (flag2) {
//                loop = false;
//            }
//            ZigbeeService.TFT_DownShow();
//            Thread.sleep(2000);
//        }
//    }

    public static List<Bitmap> cutLicense(Bitmap bitmap) throws Exception {
        //转Mat
        Mat img = new Mat();
        Utils.bitmapToMat(bitmap, img);
        //转灰度
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        //高斯模糊
        Mat blur = new Mat();
        Imgproc.GaussianBlur(gray, blur, new Size(3, 3), 0);
        //边缘检测
        Mat edge = new Mat();
        Imgproc.Canny(blur, edge, 100, 200);
        //膨胀
        Mat kernel = Imgproc.getStructuringElement(MORPH_RECT, new Size(2, 2));
        Point point = new Point(-1, -1);
        Mat binary = new Mat();
        Imgproc.dilate(edge, binary, kernel, point, 1);
        // Find connected components
        Mat labels = new Mat();
        Mat stats = new Mat();
        Mat centroids = new Mat();
        int nLabels = Imgproc.connectedComponentsWithStats(binary, labels, stats, centroids);

        // Filter out small components and get bounding boxes
        List<org.opencv.core.Rect> boundingBoxes = new ArrayList<>();
        int minArea = 1000; // adjust this value to change the minimum area of a component
        for (int i = 1; i < nLabels; i++) {
            int area = (int) stats.get(i, Imgproc.CC_STAT_AREA)[0];
            if (area > minArea) {
                int x = (int) stats.get(i, Imgproc.CC_STAT_LEFT)[0];
                int y = (int) stats.get(i, Imgproc.CC_STAT_TOP)[0];
                int width = (int) stats.get(i, Imgproc.CC_STAT_WIDTH)[0];
                int height = (int) stats.get(i, Imgproc.CC_STAT_HEIGHT)[0];
                boundingBoxes.add(new org.opencv.core.Rect(x, y, width, height));
            }
        }

        // Print bounding boxes
        for (org.opencv.core.Rect bbox : boundingBoxes) {
            System.out.println(bbox.toString());
        }
        boundingBoxes.sort(Comparator.comparingInt(rect -> rect.x));

        // 按照x轴上是否有交集分组
        List<List<org.opencv.core.Rect>> groups = new ArrayList<>();
        for (org.opencv.core.Rect rect : boundingBoxes) {
            boolean isGrouped = false;
            for (List<org.opencv.core.Rect> group : groups) {
                if (isIntersectWithGroup(rect, group)) {
                    group.add(rect);
                    isGrouped = true;
                    break;
                }
            }
            if (!isGrouped) {
                List<org.opencv.core.Rect> newGroup = new ArrayList<>();
                newGroup.add(rect);
                groups.add(newGroup);
            }
        }

        // 在每个分组中找到最大的矩形
        List<org.opencv.core.Rect> maxRects = new ArrayList<>();
        for (List<org.opencv.core.Rect> group : groups) {
            org.opencv.core.Rect maxRect = null;
            int maxArea = 0;
            for (org.opencv.core.Rect rect : group) {
                int area = rect.width * rect.height;
                if (area > maxArea && rect.x != 0) {
                    maxArea = area;
                    maxRect = rect;
                }
            }
            maxRects.add(maxRect);
        }


        System.out.println("去重取值后=======================");
        // 输出结果
        List<Bitmap> list = new ArrayList<>();
        System.out.println("Distinct largest rectangles: ");
        for (org.opencv.core.Rect rect : maxRects) {
            System.out.println(rect.toString());
            Bitmap bitmap1 = Bitmap.createBitmap(rect.width, rect.height, Bitmap.Config.RGB_565);
            Utils.matToBitmap(img.submat(rect), bitmap1);
            list.add(bitmap1);
        }
        return list;
    }

    private static boolean isIntersectWithGroup(org.opencv.core.Rect rect, List<org.opencv.core.Rect> group) {
        for (org.opencv.core.Rect r : group) {
            if (isIntersect(rect, r)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIntersect(org.opencv.core.Rect rect1, org.opencv.core.Rect rect2) {
        return rect1.x < rect2.x + rect2.width &&
                rect2.x < rect1.x + rect1.width &&
                rect1.y < rect2.y + rect2.height &&
                rect2.y < rect1.y + rect1.height;
    }
}

