package im.drh.utils;


import android.graphics.Bitmap;

//import com.baidu.ai.edge.core.segment.SegmentationResultModel;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 交通标志识别类
 */
public class TrafficSign {
    //交通标志对应的通信码
    public static int TRAFFICSIGN = 0x01;
    //有效交通标志的个数
    public static int TRAFFICCOUNT = 0;

    //下标0为直行，下标1为左转，下标2为右转，下标3为掉头，下标4为禁止直行，下标5为禁止通行
    public static int[] TRAFFICARRY = new int[6];

    //单个交通标志识别
//    public static void getOneTraffic(Bitmap bitmap) {
//        System.out.println("进入交通标志识别=========================");
//        //如果无法识别，默认返回直行
//        if (newShapeAndLicense.results == null) {
//            TRAFFICSIGN = 0x05;
//        }
//        //存储当前最大的置信度
//        float MaxConf = 0;
//        for (SegmentationResultModel result : newShapeAndLicense.results) {
//            System.out.println(result.getLabel() + " " + result.getConfidence());
//        }
//        String label = "";
//        //选出值你读最大的那个交通标志
//        for (SegmentationResultModel result : newShapeAndLicense.results) {
//            if (result.getConfidence() > MaxConf && result.getConfidence() > Variable.TRAFFICSIGN && isTraffic(result.getLabel())) {
//                MaxConf = result.getConfidence();
//                label = result.getLabel();
//            }
//        }
//        if (JudgeBitmapAndIdentify.maxConf < MaxConf) {
//            JudgeBitmapAndIdentify.maxConf = MaxConf;
//            JudgeBitmapAndIdentify.label = label;
//            switch (label) {
//                case "直行":
//                    JudgeBitmapAndIdentify.trafficSign = 0;
//                    break;
//                case "左转":
//                    JudgeBitmapAndIdentify.trafficSign = 1;
//                    break;
//                case "右转":
//                    JudgeBitmapAndIdentify.trafficSign = 2;
//                    break;
//                case "掉头":
//                    JudgeBitmapAndIdentify.trafficSign = 3;
//                    break;
//                case "禁止直行":
//                    JudgeBitmapAndIdentify.trafficSign = 4;
//                    break;
//                case "禁止通行":
//                    JudgeBitmapAndIdentify.trafficSign = 5;
//                    break;
//            }
//        }
//
//
//        //根据结果算出协议值
//        getLabel(label);
////        if (TRAFFICSIGN==0x03||TRAFFICSIGN==0x02){
////            LeftOrRight(bitmap);
////        }
//        Log.i("交通标志识别结果", String.valueOf(TRAFFICSIGN));
//        System.out.println("交通标志识别结果---------" + TRAFFICSIGN);
//    }

    /**
     * 获取计算对应交通标志的数量
     */
//    public static void getMultiTraffic() {
//        for (SegmentationResultModel result : newShapeAndLicense.results) {
//            if (result.getConfidence() > Variable.TRAFFICSIGN && isTraffic(result.getLabel())) {
//                TRAFFICCOUNT += 1;
//                getLabel(result.getLabel());
//                TRAFFICARRY[TRAFFICSIGN - 1]++;
//            }
//        }
//        for (int i = 0; i < TRAFFICARRY.length; i++) {
//            System.out.println(i + "   " + TRAFFICARRY[i]);
//        }
//    }

    private static Boolean isTraffic(String label) {
        Pattern p2 = Pattern.compile(".*(直行|左转|右转|掉头|禁止通行|禁止直行)+.*");
        Matcher m2 = p2.matcher(label);
        return m2.matches();
    }

    /**
     * 计算协议，与负责主车的人自行商量，可以采用这一套
     *
     * @param label
     */
    private static void getLabel(String label) {
        switch (label) {
            case "直行":
                TRAFFICSIGN = 0x01;
                break;
            case "左转":
                TRAFFICSIGN = 0x02;
                break;
            case "右转":
                TRAFFICSIGN = 0x03;
                break;
            case "掉头":
                TRAFFICSIGN = 0x04;
                break;
            case "禁止直行":
                TRAFFICSIGN = 0x05;
                break;
            case "禁止通行":
                TRAFFICSIGN = 0x06;
        }
    }

    /**
     * 由于左右转箭头分辨不清晰，通过机器视觉单独判断
     *
     * @param bitmap
     */
    public static void LeftOrRight(Bitmap bitmap) {
        Mat img = new Mat();
        Utils.bitmapToMat(bitmap, img);
        //转成灰度图
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        //Shi-Tomasi角点检测
        MatOfPoint marris = new MatOfPoint();
        //Shi-Tomasi角点检测, 由于箭头只有11个拐点，只用确定11个点就行了不然后续判断可能会出错
        Imgproc.goodFeaturesToTrack(img, marris, 11, 0.01, 10, new Mat(), 3, false, 0.04);

        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 30, 0.01);
        Imgproc.cornerSubPix(img, marris, new Size(5, 3), new Size(-1, -1), criteria);
        List<Point> points = marris.toList();
        /**
         * 由于箭头只有一个点的x轴是独立的，其他的x轴都在一条y轴上，但是考虑到图片在TFT上的拉伸X需要给几个像素点的误差，这边给的是4个像素点的误差
         * 如果找到独立的这个点直接就可以跳出循环
         * 由于独立的点都是在最左侧或者最右侧，只要找到比它大或者比它小的一个点就可以确定是左转还是右转
         */
        double max = 0;
        for (int i = 0; i < points.size(); i++) {
            max = points.get(i).x;
            for (int j = 0; j < points.size(); j++) {
                //图片会别拉伸所以要计算x轴的误差
                if (Math.abs(points.get(j).x - max) < 4 && j != i) max = 0;
            }
            if (max == points.get(i).x) break;
        }
        for (Point point : points) {
            if (point.x > max) {
                TRAFFICSIGN = 0x02;
                break;
            }
            if (point.x < max) {
                TRAFFICSIGN = 0x03;
                break;
            }

        }


    }
}
