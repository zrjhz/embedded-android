//package im.hdy.utils;
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.util.Log;
//
//import com.utils.FileService;
//
//import im.zhy.util.ImageDisposeUtils;
//import org.opencv.android.Utils;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfInt;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.MatOfPoint2f;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.imgproc.Moments;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import car.car2024.FragmentView.LeftFragment;
//
///**
// * Created by hdy on 11/05/2018.
// * 通过摄像头判断灯光亮度
// */
//
//public class LightUtils {
//
//
//    public static void get() {
//        processPicture();
//    }
//
//
//    private static int processPicture() {
//        Bitmap result_bitmap = LeftFragment.bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Mat input = new Mat();
//        Utils.bitmapToMat(result_bitmap, input);
//        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
//        for (int i = 0; i <= 3; i++) {
//            Imgproc.erode(input, input, element, new Point(-1, -1), 1);
//            Imgproc.dilate(input, input, element, new Point(-1, -1), 1);
//        }
//        result_bitmap = Bitmap.createBitmap(input.cols(), input.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(input, result_bitmap);
//        int result = -1;
//        Bitmap bitmap = result_bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        int black_num = 0;
//        int white_num = 0;
//        for (int i = 0; i < bitmap.getWidth(); i++) {
//            for (int j = 0; j < bitmap.getHeight(); j++) {
//                final int color = bitmap.getPixel(i, j);
//                final int r = (color >> 16) & 0xff;
//                final int g = (color >> 8) & 0xff;
//                final int b = color & 0xff;
//                int newPixel = colorToRGB(255, r, g, b);
//                if (newPixel == ImageDisposeUtils.blackOrWhite(true)) {
//                    black_num += 1;
//                } else {
//                    white_num += 1;
//                }
//                bitmap.setPixel(i, j, newPixel);
//            }
//        }
//        System.out.println("black数量:" + black_num);
//        System.out.println("white数量:" + white_num);
//        System.out.println((double) (black_num / white_num));
////        Mat src = new Mat();
////        Utils.bitmapToMat(bitmap, src);
////        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
////        Mat thresh = new Mat();
////        Imgproc.adaptiveThreshold(src, thresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, (bitmap.getWidth() + bitmap.getHeight()) / 200, 2);
////        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
////        Imgproc.findContours(thresh, contours, new Mat(), Imgproc.RETR_TREE,
////                Imgproc.CHAIN_APPROX_SIMPLE);
////
////        int num = 0;
////        System.err.println(bitmap.getWidth());//640
////        System.err.println(bitmap.getHeight());//360
//
//
//        //第一档 35 88 (0.39)
//        //第二档 48 17 83 (0.20)
//        //第三档 70 43 70 (0.6)
//        //第四档 87 26 32 (0.26)
//
//        //第一档 24 19 37 37 26 13 38 39 36(0.39)
//        //第二档 49  23 15 (0.20)
//        //第三档 70 43 70 (0.6)
//        //第四档 87 26 32 (0.26)
//
//        //第一档 36 30 65(0.39)
//        //第二档 36 30 65 15 21 (0.20)
//        //第三档 70 43 70 (0.6)
//        //第四档 87 26 32 (0.26)
////        for (MatOfPoint matOfPoint : contours) {
////            Rect rect = Imgproc.boundingRect(matOfPoint);
////            int x = rect.x;
////            int y = rect.y;
////            int w = rect.width;
////            int h = rect.height;
////
////
////            int pixel = bitmap.getPixel((rect.x + rect.width) / 2, (rect.y + rect.height) / 2);
////            int r = (pixel >> 16) & 0xff;
////            final int g = (pixel >> 8) & 0xff;
////            final int b = pixel & 0xff;
////            float[] hsv = new float[3];
////            Color.RGBToHSV(r, g, b, hsv);
////            double Hue = hsv[0] / 2;
////            double Saturation = hsv[1] * 255 / 2;
////            double Value = hsv[2] * 255 / 2;
////            if (Hue <= 180 && Value <= 46) {
////                Log.i("判断查找的轮廓是不是白色", "是黑色");
//////                continue;
////                // 黑色
////                // return "黑色";
////            } else {
////                //白色
////                continue;
////            }
////
////
////            if (w > 630 && h > 10 && h <= 190) {
////                System.out.println("******开始******");
////                System.out.println(rect.width);
////                System.out.println(rect.height);
////                System.out.println(rect.x);
////                System.out.println(rect.y);
////                System.out.println("******结束******");
////
////            }
////        }
//
//        FileService.savePhoto(bitmap, "light_bitmap.png");
//
//        return -1;
//    }
//
//
//    /**
//     * @param alpha
//     * @param red
//     * @param green
//     * @param blue
//     * @return
//     */
//    private static int colorToRGB(int alpha, int red, int green, int blue) {
//        float[] hsv = new float[3];
//        Color.RGBToHSV(red, green, blue, hsv);
//        double Hue = hsv[0] / 2;
//        double Saturation = hsv[1] * 255 / 2;
//        double Value = hsv[2] * 255 / 2;
//        if (Hue <= 180 && Value <= 46) {
//            // 黑色
//            return ImageDisposeUtils.blackOrWhite(true);
//            // return "黑色";
//        }
//        return ImageDisposeUtils.blackOrWhite(false);
//    }
//
//
//    public static Rect findRectangle(Bitmap image) {
//        try {
//            Mat tempor = new Mat();
//            Mat src = new Mat();
//            Utils.bitmapToMat(image, tempor);
//
//            Imgproc.cvtColor(tempor, src, Imgproc.COLOR_BGR2RGB);
//
//            Mat blurred = src.clone();
//            Imgproc.medianBlur(src, blurred, 9);
//
//            Mat gray0 = new Mat(blurred.size(), CvType.CV_8U), gray = new Mat();
//
//            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//
//            List<Mat> blurredChannel = new ArrayList<Mat>();
//            blurredChannel.add(blurred);
//            List<Mat> gray0Channel = new ArrayList<Mat>();
//            gray0Channel.add(gray0);
//
//            MatOfPoint2f approxCurve;
//
//            double maxArea = 0;
//            int maxId = -1;
//
//            for (int c = 0; c < 3; c++) {
//                int ch[] = {c, 0};
//                Core.mixChannels(blurredChannel, gray0Channel, new MatOfInt(ch));
//                Utils.matToBitmap(gray0, image);
//                int thresholdLevel = 1;
//                for (int t = 0; t < thresholdLevel; t++) {
//                    if (t == 0) {
//                        Imgproc.Canny(gray0, gray, 10, 20, 3, true); // true ?
//                        Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1); // 1
//                        // ?
//                    } else {
//                        Imgproc.adaptiveThreshold(gray0, gray, thresholdLevel,
//                                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
//                                Imgproc.THRESH_BINARY,
//                                (src.width() + src.height()) / 200, t);
//                    }
//
//                    Imgproc.findContours(gray, contours, new Mat(),
//                            Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//
//                    for (MatOfPoint contour : contours) {
//                        MatOfPoint2f temp = new MatOfPoint2f(contour.toArray());
//
//                        double area = Imgproc.contourArea(contour);
//                        approxCurve = new MatOfPoint2f();
//                        Imgproc.approxPolyDP(temp, approxCurve,
//                                Imgproc.arcLength(temp, true) * 0.02, true);
//
//                        if (approxCurve.total() == 4 && area >= maxArea) {
//                            // 获取圆心坐标
//                            Moments moments = Imgproc.moments(contour);
//                            int cx = (int) (moments.m10 / moments.m00);
//                            int cy = (int) (moments.m01 / moments.m00);
//
//                            int pixel = image.getPixel(cx, cy);
//                            int r = (pixel >> 16) & 0xff;
//                            final int g = (pixel >> 8) & 0xff;
//                            final int b = pixel & 0xff;
//                            float[] hsv = new float[3];
//                            Color.RGBToHSV(r, g, b, hsv);
//                            double Hue = hsv[0] / 2;
//                            double Saturation = hsv[1] * 255 / 2;
//                            double Value = hsv[2] * 255 / 2;
//                            if (Hue <= 180 && Value <= 46) {
//                                Log.i("判断查找的轮廓是不是白色", "是黑色");
//                                continue;
//                                // 黑色
//                                // return "黑色";
//                            } else {
//                                //白色
//                            }
//
//
//                            double maxCosine = 0;
//
//                            List<Point> curves = approxCurve.toList();
//                            for (int j = 2; j < 5; j++) {
//
//                                double cosine = Math.abs(angle(curves.get(j % 4),
//                                        curves.get(j - 2), curves.get(j - 1)));
//                                maxCosine = Math.max(maxCosine, cosine);
//                            }
//
//                            if (maxCosine < 0.3) {
//                                maxArea = area;
//                                maxId = contours.indexOf(contour);
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (maxId >= 0) {
//                Rect rect = Imgproc.boundingRect(contours.get(maxId));
//
//                Imgproc.rectangle(src, rect.tl(), rect.br(), new Scalar(255, 0, 0, .8), 4);
//
//                int mDetectedWidth = rect.width;
//                int mDetectedHeight = rect.height;
//
//                Log.d("", "Rectangle width :" + mDetectedWidth + " Rectangle height :" + mDetectedHeight);
//                return rect;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private static double angle(Point p1, Point p2, Point p0) {
//        double dx1 = p1.x - p0.x;
//        double dy1 = p1.y - p0.y;
//        double dx2 = p2.x - p0.x;
//        double dy2 = p2.y - p0.y;
//        return (dx1 * dx2 + dy1 * dy2)
//                / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
//                + 1e-10);
//    }
//
//
//}
