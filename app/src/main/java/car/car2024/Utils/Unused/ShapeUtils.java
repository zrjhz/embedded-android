//package com.utils;
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.util.Log;
//
//import car.car2024.Utils.Socket.ThreadUtils;
//import org.opencv.android.Utils;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.MatOfPoint2f;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.imgproc.Moments;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import car.car2024.FragmentView.LeftFragment;
//
///**
// * 用于形状识别 经过样本测试,该算法识别率基本接近完美了.
// * 除非有新的形状需要识别.基本不需要怎么调整参数了.
// * 如果确实识别有问题.只需要修改下面的一段代码的数字就可以了
// * Imgproc.threshold(Blur, thresh, 150, 255, Imgproc.THRESH_BINARY);
// * 就是上面这段代码.
// * 修改第三个参数的值就可以了
// *
// * @author hdy
// */
//public class ShapeUtils {
//    public static int[][] results = new int[5][8];
//
//    public static void dectShape() {
//        Bitmap bitmapCopy = LeftFragment.bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        ThreadUtils.sleep(500);
//
//        Bitmap copy = LeftFragment.bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Rect roi = RectUtils.findRectangle(bitmapCopy); // 自动截出最大矩形
//        if (roi == null) {
//            // 没找到的处理
//            return;
//        }
//        Mat uncropped = new Mat();
//        Utils.bitmapToMat(copy, uncropped);
//        Imgproc.cvtColor(uncropped, uncropped, Imgproc.COLOR_BGR2RGB);
//        Mat cropped = new Mat(uncropped, roi);// 这里这句话有异常
//        Bitmap bitmapIdOriginal = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888);
//        Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_BGR2RGB);
//        Utils.matToBitmap(cropped, bitmapIdOriginal);
//        ShapeUtils.findShape(bitmapIdOriginal);
//    }
//
//
//    private static void findShape(Bitmap bitmapCopy) {
//        //初始化数据
//        results = new int[5][8];
//        Bitmap copy = bitmapCopy.copy(Bitmap.Config.ARGB_8888, true);
//        FileService.savePhoto(copy, "copy.png");
//
//
//        // 三角形 圆形 矩形 菱形 五角星
//        // 原始图片
//        Mat image = new Mat();
//        Utils.bitmapToMat(bitmapCopy, image);
//        // 灰度化处理
//        Mat gray = new Mat();
//        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
//        // 高斯平滑,5×5 内核的高斯平滑
//        Mat Blur = new Mat();
//        Imgproc.GaussianBlur(gray, Blur, new Size(5, 5), 0);
//        // 阈值化
//        Mat thresh = new Mat();
////        Imgproc.threshold(Blur, thresh, 140, 255, Imgproc.THRESH_BINARY);
////        Imgproc.adaptiveThreshold(Blur, thresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -1);
//        Imgproc.Canny(Blur, thresh, 5, 15, 3, true); // true ?
//        // 保存图片
////        Utils.matToBitmap(thresh, bitmap);
////        FileService.savePhoto(bitmap, "demo.png");
//        Imgcodecs.imwrite("/sdcard/2019car/demo3.png", thresh);
//        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//        Imgproc.findContours(thresh, contours, new Mat(), Imgproc.RETR_CCOMP,
//                Imgproc.CHAIN_APPROX_SIMPLE);
//        Log.i("获得的contours", contours.size() + "");
//
//        int last_cx = 0;
//        int last_cy = 0;
//        for (MatOfPoint matOfPoint : contours) {
//            // 判断矩形是否由两个三角形组成变量
//            boolean isTwoTri = false;
//            // 保存矩形里的两个三角形颜色;
//            String shape1_color = "";
//            String shape2_color = "";
//            // 获取面积
//            double area = Imgproc.contourArea(matOfPoint);
//            // Log.i("area", area + "");
//            if (area >= 30000 || area <= 700) {
//                continue;
//            }
//
//
//            // 形状识别开始
//            String shape = "unknown";
//            MatOfPoint2f temp = new MatOfPoint2f(matOfPoint.toArray());
//            double arcLength = Imgproc.arcLength(temp, true);
//            MatOfPoint2f approxCurve = new MatOfPoint2f();
//            // 轮廓逼近
//            Imgproc.approxPolyDP(temp, approxCurve, 0.04 * arcLength, true);
//            // 判断是不是边缘杂点
//            if (approxCurve.get(0, 0)[0] < 8
//                    || approxCurve.get(0, 0)[0] > (copy.getWidth() - 8)) {
//                continue;
//            }
//            // 获取圆心坐标
////            Moments moments = Imgproc.moments(matOfPoint);
//            Moments moments = contourMoments(matOfPoint);
//            int cx = (int) (moments.m10 / moments.m00);
//            int cy = (int) (moments.m01 / moments.m00);
//            System.out.println("cx:" + cx);
//            System.out.println("cy:" + cy);
//            if (last_cx == 0 && last_cy == 0) {
//                last_cx = cx;
//                last_cy = cy;
//            } else {
//                if (Math.abs(last_cx - cx) <= 5 && Math.abs(last_cy - cy) <= 5) {
//                    continue;
//                } else {
//                    last_cx = cx;
//                    last_cy = cy;
//                }
////                if (last_cx == cx && last_cy == cy) {
////                    //去掉重复的曲线
////                    continue;
////                } else {
////                    last_cx = cx;
////                    last_cy = cy;
////                }
//            }
//            if (approxCurve.total() == 3) {
//                // 三角形
//                shape = "tri";
//            } else if (approxCurve.total() == 4) {
//                int rows = approxCurve.rows();
//                int cols = approxCurve.cols();
//                // 计算夹角:
//                double x1 = approxCurve.get(0, 0)[0];
//                double y1 = approxCurve.get(0, 0)[1];
//
//                double x2 = approxCurve.get(1, 0)[0];
//                double y2 = approxCurve.get(1, 0)[1];
//
//                double x3 = approxCurve.get(2, 0)[0];
//                double y3 = approxCurve.get(2, 0)[1];
//                double k1 = (y2 - y1) / (x2 - x1);
//                double k2 = (y3 - y2) / (x3 - x2);
//                // tanθ=(k2- k1）/(1+ k1k2）
//                // 获得两条直线的夹角
//                double angle = Math.atan((k2 - k1) / (1 + k1 * k2));
//                double degrees = Math.abs(Math.toDegrees(angle));
//                // 判断角度是不是直角
//                Log.i("angle", degrees + "");
//                // 矩形或菱形
//                if (degrees >= 86 && degrees <= 94) {
//                    // 矩形
//                    shape = "squ";
//                    // shape = "矩形";
//                } else {
//                    // 菱形
//                    shape = "squ";
//                    // shape = "菱形";
//                }
//                // 判断是否是两个三角形组成
//                if (cy + 30 > copy.getHeight() || cy - 30 < 0) {
//                    // 说明轮廓有误...
//                    continue;
//                }
//                // 上方颜色
//                Bitmap createBitmap = Bitmap.createBitmap(copy, cx,
//                        cy - 15, 4, 4);
//                int r_a = 0;
//                int g_a = 0;
//                int b_a = 0;
//                for (int i = 0; i < 4; i++) {
//                    for (int j = 0; j < 4; j++) {
//                        int pixel = createBitmap.getPixel(i, j);
//                        int r = Color.red(pixel);
//                        int g = Color.green(pixel);
//                        int b = Color.blue(pixel);
//                        r_a += r;
//                        g_a += g;
//                        b_a += b;
//                    }
//                }
//                r_a = r_a / 4;
//                g_a = g_a / 4;
//                b_a = b_a / 4;
//                String color = getColor(r_a, g_a, b_a);
//                // 下方颜色
//                Bitmap createBitmap2 = Bitmap.createBitmap(copy, cx,
//                        cy + 15, 4, 4);
//                int r_a2 = 0;
//                int g_a2 = 0;
//                int b_a2 = 0;
//                for (int i = 0; i < 4; i++) {
//                    for (int j = 0; j < 4; j++) {
//                        int pixel = createBitmap2.getPixel(i, j);
//                        int r = Color.red(pixel);
//                        int g = Color.green(pixel);
//                        int b = Color.blue(pixel);
//                        r_a2 += r;
//                        g_a2 += g;
//                        b_a2 += b;
//                    }
//                }
//                r_a2 = r_a2 / 4;
//                g_a2 = g_a2 / 4;
//                b_a2 = b_a2 / 4;
//                String color2 = getColor(r_a2, g_a2, b_a2);
//
//                if (color.equals(color2)) {
//                    // 如果返回都是一样的颜色,说明是纯色
//                } else {
//                    // 说明返回的颜色不相同
//                    shape = "squ";
//                    // 颜色不同的肯定是由两个三角形组成的矩形
//                    if (color.equals("none") || color2.equals("none")) {
//                    } else {
//                        isTwoTri = true;
//                        shape1_color = color;
//                        shape2_color = color2;
//                        shape += "(" + color.substring(0, 3)
//                                + color2.substring(0, 3) + ")";
//                    }
//                }
//                // 判断是否是两个三角形结束
//
//            } else if (approxCurve.total() == 5) {
//                // 五角形
//                shape = "pent";
//                // shape = "五边形";
//            } else if (approxCurve.total() == 10) {
//                // 五角星
//                shape = "star";
//                // shape = "五角星";
//            } else {
//                // 圆形
//                shape = "cir";
//                // shape = "圆形";
//            }
//            // 颜色识别开始
//            Bitmap createBitmap = Bitmap.createBitmap(copy, cx - 2,
//                    cy - 2, 4, 4);
//            int r_a = 0;
//            int g_a = 0;
//            int b_a = 0;
//            for (int i = 0; i < 4; i++) {
//                for (int j = 0; j < 4; j++) {
//                    int pixel = createBitmap.getPixel(i, j);
//                    int r = Color.red(pixel);
//                    int g = Color.green(pixel);
//                    int b = Color.blue(pixel);
//                    r_a += r;
//                    g_a += g;
//                    b_a += b;
//                }
//            }
//            r_a = r_a / 4;
//            g_a = g_a / 4;
//            b_a = b_a / 4;
//            // 获取圆心平均颜色得出结果
//            String color = getColor(r_a, g_a, b_a);
//            System.out.println("getClolor:" + color);
//            // 画轮廓
//            Imgproc.drawContours(image, contours, contours.indexOf(matOfPoint),
//                    new Scalar(0, 0, 0), 2);
//            // 添加文字
//            if (isTwoTri) {
//                Imgproc.putText(image, shape, new Point(cx - 30, cy - 20),
//                        Core.FONT_HERSHEY_SIMPLEX, 0.4, new Scalar(255, 255,
//                                255), 1);
//                System.out.println(color + shape);
//                results[0][getColorInt(shape1_color)] = results[0][getColorInt(shape1_color)] + 1;
//                results[0][getColorInt(shape2_color)] = results[0][getColorInt(shape2_color)] + 1;
//            } else {
//                Imgproc.putText(image, color + shape, new Point(cx - 30,
//                        cy - 20), Core.FONT_HERSHEY_SIMPLEX, 0.4, new Scalar(
//                        255, 255, 255), 1);
//                results[getShapeInt(shape)][getColorInt(color)] = results[getShapeInt(shape)][getColorInt(color)] + 1;
//                System.out.println("1:" + color + shape);
//            }
//            // 画圆心
//            Imgproc.circle(image, new Point(cx, cy), 7, new Scalar(0, 0, 0), 2);
//
//        }
//        Utils.matToBitmap(image, copy);
//        FileService.savePhoto(copy, "demo2.png");
//    }
//
//
//    public static String getColor(int r, int g, int b) {
//        float[] hsv = new float[3];
//        Color.RGBToHSV(r, g, b, hsv);
//        double Hue = hsv[0] / 2;
//        double Saturation = hsv[1] * 255 / 2;
//        double Value = hsv[2] * 255 / 2;
////        Log.i("HHHHH", Hue + "");
////        Log.i("SSSSS", hsv[1] * 255 / 2 + "");
////        Log.i("VVVVV", hsv[2] * 255 / 2 + "");
//        if (Hue <= 180 && Value <= 55) {
//            // 黑色
////            Log.i("颜色", "黑色");
//            return "black";
//            // return "黑色";
//        }
//
//        if (Hue <= 180 && Saturation <= 30 && Value >= 221) {
//            // 白色
////            Log.i("颜色", "白色");
//            return "white";
//            // return "白色";
//        }
//        if (((Hue >= 0 && Hue <= 10)) || (Hue >= 156 && Hue <= 180)) {
//            // 红色
//            return "red";
//            // return "红色";
//        } else if (Hue >= 11 && Hue <= 25) {
//            // 橙色
//            return "orange";
//            // return "橙色";
//        } else if (Hue >= 26 && Hue <= 34) {
//            // 黄色
//            return "Yellow";
//            // return "黄色";
//        } else if (Hue >= 35 && Hue <= 77) {
//            // 绿
//            return "green";
//            // return "绿色";
//        } else if (Hue >= 78 && Hue <= 99) {
//            // 青色
//            return "cyan";
//            // return "青色";
//        } else if (Hue >= 100 && Hue <= 124) {
//            // 蓝色
//            return "blue";
//            // return "蓝色";
//        } else if (Hue >= 125 && Hue <= 155) {
//            // 紫色(品)
//            return "purple";
//            // return "品色";
//        } else {
//            return "none";
//        }
//    }
//
//    private static int getColorInt(String color) {
//        switch (color) {
//            case "red":
//                return 0;
//            case "green":
//                return 1;
//            case "blue":
//                return 2;
//            case "Yellow":
//                return 3;
//            case "purple":
//                return 4;
//            case "cyan":
//                return 5;
//            case "black":
//                return 6;
//            case "white":
//                return 7;
//            default:
//                break;
//        }
//        return 0;
//    }
//
//    private static int getShapeInt(String shape) {
//        switch (shape) {
//            case "tri":
//                return 0;
//            case "cir":
//                return 1;
//            case "squ":
//                return 2;
//            case "rhom":
//                return 3;
//            case "star":
//                return 4;
//            default:
//                break;
//        }
//        return 0;
//    }
//
//
//    // 传入英文.获得中文颜色
//    private static String getChineseColorName(String color) {
//        if (color.equals("RED")) {
//            return "红色";
//        } else if (color.equals("BLACK")) {
//            return "黑色";
//        } else if (color.equals("WHITE")) {
//            return "白色";
//        } else if (color.equals("YELLOW")) {
//            return "黄色";
//        } else if (color.equals("BLUE")) {
//            return "蓝色";
//        } else if (color.equals("GREEN")) {
//            return "绿色";
//        } else if (color.equals("CYAN")) {
//            return "青色";
//        } else if (color.equals("PURPLE")) {
//            return "紫色";
//        } else if (color.equals("ORANGE")) {
//            return "橘色";
//        }
//        return "";
//    }
//
//    // 传入英文,获得中文形状
//    private static String getChineseShapeName(String shape) {
//        if (shape.equals("TRI")) {
//            return "三角形";
//        } else if (shape.equals("RHOM")) {
//            return "菱形";
//        } else if (shape.equals("STAR")) {
//            return "五角星";
//        } else if (shape.equals("CIR")) {
//            return "圆形";
//        } else if (shape.equals("SQU")) {
//            return "矩形";
//        }
//        return "";
//    }
//
//    // 用于返回形状个数
//    // 第一种情况 返回某种颜色的形状个数
//    public static int judgeNum1(String color) {
//        int sum = 0;
//        for (int i = 0; i < results.length; i++) {
//            //返回当前颜色的形状个数
//            int num = results[i][getColorInt(color)];
//            sum += num;
//        }
//        return sum;
//    }
//
//    // 第二种情况 所有形状个数
//    public static void judgeNum2() {
//        for (int i = 0; i < results.length; i++) {
//            for (int j = 0; j < results[i].length; j++) {
//                //- -
//            }
//        }
//    }
//
//    // 第三种情况,某个形状各个颜色的个数
//    // String[] shapes = { "TRI", "RHOM", "CIR", "SQU", "STAR" };
//    public static int[] judgeNum3(String shape) {
//        int[] result = results[getShapeInt(shape)];
//        return result;
//    }
//
//
//    // 第四种情况 某个形状的某各颜色
//    public static int judgeNum4(String color, String shape) {
//        int i = results[getShapeInt(shape)][getColorInt(color)];
//        //获取到数量
//        return i;
//    }
//
//
//    // 获取指定某个形状的个数(不区分颜色)
//    public static int judgeNum5(String shape) {
//        int[] result = results[getShapeInt(shape)];
//        int sum = 0;
//        for (int i = 0; i < result.length; i++) {
//            int num = result[i];
//            sum += num;
//        }
//        return sum;
//        //sum为最终的结果总数
//    }
//
//
//    public static Moments contourMoments(MatOfPoint contour) {
//        Moments m = new Moments();
//        int lpt = contour.checkVector(2);
//        boolean is_float = true;//(contour.depth() == CvType.CV_32F);
//        Point[] ptsi = contour.toArray();
//        //PointF[] ptsf = contour.toArray();
//
//        //CV_Assert( contour.depth() == CV_32S || contour.depth() == CV_32F );
//
//        if (lpt == 0)
//            return m;
//
//        double a00 = 0, a10 = 0, a01 = 0, a20 = 0, a11 = 0, a02 = 0, a30 = 0, a21 = 0, a12 = 0, a03 = 0;
//        double xi, yi, xi2, yi2, xi_1, yi_1, xi_12, yi_12, dxy, xii_1, yii_1;
//
//
//        {
//            xi_1 = ptsi[lpt - 1].x;
//            yi_1 = ptsi[lpt - 1].y;
//        }
//
//        xi_12 = xi_1 * xi_1;
//        yi_12 = yi_1 * yi_1;
//
//        for (int i = 0; i < lpt; i++) {
//
//            {
//                xi = ptsi[i].x;
//                yi = ptsi[i].y;
//            }
//
//            xi2 = xi * xi;
//            yi2 = yi * yi;
//            dxy = xi_1 * yi - xi * yi_1;
//            xii_1 = xi_1 + xi;
//            yii_1 = yi_1 + yi;
//
//            a00 += dxy;
//            a10 += dxy * xii_1;
//            a01 += dxy * yii_1;
//            a20 += dxy * (xi_1 * xii_1 + xi2);
//            a11 += dxy * (xi_1 * (yii_1 + yi_1) + xi * (yii_1 + yi));
//            a02 += dxy * (yi_1 * yii_1 + yi2);
//            a30 += dxy * xii_1 * (xi_12 + xi2);
//            a03 += dxy * yii_1 * (yi_12 + yi2);
//            a21 += dxy * (xi_12 * (3 * yi_1 + yi) + 2 * xi * xi_1 * yii_1 +
//                    xi2 * (yi_1 + 3 * yi));
//            a12 += dxy * (yi_12 * (3 * xi_1 + xi) + 2 * yi * yi_1 * xii_1 +
//                    yi2 * (xi_1 + 3 * xi));
//            xi_1 = xi;
//            yi_1 = yi;
//            xi_12 = xi2;
//            yi_12 = yi2;
//        }
//        float FLT_EPSILON = 1.19209e-07f;
//        if (Math.abs(a00) > FLT_EPSILON) {
//            double db1_2, db1_6, db1_12, db1_24, db1_20, db1_60;
//
//            if (a00 > 0) {
//                db1_2 = 0.5;
//                db1_6 = 0.16666666666666666666666666666667;
//                db1_12 = 0.083333333333333333333333333333333;
//                db1_24 = 0.041666666666666666666666666666667;
//                db1_20 = 0.05;
//                db1_60 = 0.016666666666666666666666666666667;
//            } else {
//                db1_2 = -0.5;
//                db1_6 = -0.16666666666666666666666666666667;
//                db1_12 = -0.083333333333333333333333333333333;
//                db1_24 = -0.041666666666666666666666666666667;
//                db1_20 = -0.05;
//                db1_60 = -0.016666666666666666666666666666667;
//            }
//
//            // spatial moments
//            m.m00 = a00 * db1_2;
//            m.m10 = a10 * db1_6;
//            m.m01 = a01 * db1_6;
//            m.m20 = a20 * db1_12;
//            m.m11 = a11 * db1_24;
//            m.m02 = a02 * db1_12;
//            m.m30 = a30 * db1_20;
//            m.m21 = a21 * db1_60;
//            m.m12 = a12 * db1_60;
//            m.m03 = a03 * db1_20;
//
//            m.completeState();
//        }
//        return m;
//    }
//}
