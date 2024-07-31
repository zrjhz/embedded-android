package im.drh.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import car.car2024.Utils.Image.RGBLuminanceSource;
import car.car2024.Utils.Socket.Variable;
import car.car2024.Utils.Socket.AcceptCarOrder;


public class QRcode {


//    public static void QRCodeByOpencv(Bitmap bitmap){
//        try {
//
//            Mat mat = new Mat();
//            Utils.bitmapToMat(bitmap, mat);
//            QRCodeDetector qrCodeDetector = new QRCodeDetector();
//            mat.copyTo(mat);
//            String decode = qrCodeDetector.detectAndDecode(mat, new Mat());
//            System.out.println("----------------" + decode);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public static void QRCodeDecodeMul(Bitmap bitmap){
//        Mat mat = new Mat();
//        Utils.bitmapToMat(bitmap, mat);
//        List<String> result=new ArrayList<>();
//        QRCodeDetector qrCodeDetector = new QRCodeDetector();
//        boolean b = qrCodeDetector.detectAndDecodeMulti(mat, result, new Mat());
//        if (b){
//            for (String s:result){
//                System.out.println(s);
//            }
//            Variable.QRMAP.put(AcceptCarOrder.getIndex(),result);
//        }
//    }


    public static Bitmap cutQRcode(Bitmap bitmap, Bitmap beforeBitmap) {
        try {
            Mat image = new Mat();
            Utils.bitmapToMat(bitmap, image);
            Mat gray = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
            //彩色图像灰度化
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_RGB2GRAY);
            //高斯模糊
            Mat gauss = gray.clone();
            Imgproc.GaussianBlur(gray, gauss, new Size(new Point(5, 5)), 0);
            //闭运算
            Mat mat = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            Imgproc.morphologyEx(gauss, gauss, Imgproc.MORPH_OPEN, mat);
            // 函数检测边缘
            Mat canny = gauss.clone();
            Imgproc.Canny(gauss, canny, 120, 200);

            //找到轮廓
            Mat hierarchy = canny.clone();
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            List<Integer> ints = new ArrayList<>();
            List<MatOfPoint> points = new ArrayList<>();
            //从轮廓的拓扑结构信息中得到有5层以上嵌套的轮廓
            for (int i = 0; i < contours.size(); i++) {
                int k = i;
                int c = 0;
                while (hierarchy.get(0, k)[2] != -1) {
                    k = (int) hierarchy.get(0, k)[2];
                    c = c + 1;
                    if (c >= 5) {
                        ints.add(i);
                        points.add(contours.get(i));
                    }
                }
            }
            System.out.println("找到" + ints.size() + "个标志轮廓!");

            Point[] point = convertPoints(points);

            //轮廓转换成最小矩形包围盒
            RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(point));

            Mat before = new Mat();
            Utils.bitmapToMat(beforeBitmap, before);
            //截取出二维码
            Rect qrRect = rotatedRect.boundingRect();
            Mat qrCodeImg = new Mat(before, qrRect);

            Bitmap bitmap1 = Bitmap.createBitmap(qrCodeImg.cols(), qrCodeImg.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(qrCodeImg, bitmap1, true);
            return bitmap1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private static Point[] convertPoints(List<MatOfPoint> points) throws Exception {
        Point[] points1 = points.get(0).toArray();
        Point[] points2 = points.get(1).toArray();
        Point[] points3 = points.get(2).toArray();

        Point[] point = new Point[points1.length + points2.length + points3.length];
        System.arraycopy(points1, 0, point, 0, points1.length);
        System.arraycopy(points2, 0, point, points1.length, points2.length);
        System.arraycopy(points3, 0, point, points1.length + points2.length, points3.length);
        return point;
    }


}
