package car.car2024.Utils.Unused;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import car.car2024.Utils.OtherUtils.FileService;
import im.zhy.param.TrafficLightName;
import im.zhy.util.ImageDisposeUtils;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import car.car2024.FragmentView.LeftFragment;

/**
 * Created by hdy on 07/05/2018.
 * 交通灯颜色识别
 */


@SuppressWarnings("all")
public class TrafficeLight3 {

    /**
     * @return 0 红色 1 黄色 2绿色
     */
    public static int get() {

        Bitmap bitmap = LeftFragment.bitmap;

        Bitmap bitmap1;

        try {

            // 截取 交通灯部分
            bitmap1 = Bitmap.createBitmap(bitmap, 80, 80, bitmap.getWidth() - 200, bitmap.getHeight() - 180);

            FileService.saveBitmap(bitmap1,"picture");

            return processPicture(bitmap1);

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }

    }


    /**
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @param type  颜色 0 红色 1 黄色 2 绿色
     * @return
     */
    public static int colorToRGB(int alpha, int red, int green, int blue, int type) {
        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        double Hue = hsv[0] / 2;
        double Saturation = hsv[1] * 255 / 2;
        double Value = hsv[2] * 255 / 2;
        if (Hue <= 180 && Value <= 46) {
            // 黑色
            return ImageDisposeUtils.blackOrWhite(true);
            // return "黑色";
        }

        if (Hue <= 180 && Saturation <= 43 && Value >= 46 && Value <= 220) {
            //青色
            return ImageDisposeUtils.blackOrWhite(true);
        }

        if (Hue <= 180 && Saturation <= 30 && Value >= 221) {
            // 白色
            return ImageDisposeUtils.blackOrWhite(true);
            // return "白色";
        }

        if (Hue >= 78 && Hue <= 99) {  //源数据  78  99
            // 青色
            return ImageDisposeUtils.blackOrWhite(true);
            // return "青色";
        } else if (Hue >= 100 && Hue <= 124) {
            // 蓝色
            return ImageDisposeUtils.blackOrWhite(true);
            // return "蓝色";
        } else if (Hue >= 125 && Hue <= 155) {
            // 紫色(品)
            return ImageDisposeUtils.blackOrWhite(true);
            // return "品色";
        }
        if ((Hue >= 156 && Hue <= 180)) {
            // 红色
            return ImageDisposeUtils.blackOrWhite(false);
            // return "红色";
        } else if (Hue >= 11 && Hue <= 34) {
            return ImageDisposeUtils.blackOrWhite(false);
        } else if (Hue >= 35 && Hue <= 99) {
            return ImageDisposeUtils.blackOrWhite(false);
        }
        return ImageDisposeUtils.blackOrWhite(true);
    }

    private static int processPicture(Bitmap bitmapColor) throws IOException {
        Bitmap result_bitmap = bitmapColor.copy(Bitmap.Config.ARGB_8888, true);
        Mat input = new Mat();
        Utils.bitmapToMat(result_bitmap, input);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        for (int i = 0; i <= 3; i++) {
            Imgproc.erode(input, input, element, new Point(-1, -1), 1);
        }
        result_bitmap = Bitmap.createBitmap(input.cols(), input.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, result_bitmap);
        int result = -1;
        Bitmap bitmap = result_bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                final int color = bitmap.getPixel(i, j);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int newPixel = colorToRGB(255, r, g, b, 0);
                bitmap.setPixel(i, j, newPixel);
            }
        }
        double[] doubles = getCircle(bitmap);
        Point circle = null;
        int radius = 0;
        if (doubles != null) {
            circle = new Point((int) doubles[0], (int) doubles[1]);
            radius = (int) doubles[2];
        }
        if (circle != null) {


            int color = 0;
            int r = 0;
            int g = 0;
            int b = 0;
            String color_result = "";


            Bitmap bmp = Bitmap.createBitmap(bitmap, (int) circle.x, (int) circle.y, radius, radius, null,
                    false);
            FileService.savePhoto(bmp, "bmp.png");
            //截取右下角的图片进行分析,一旦碰到白色就直接判断当前的颜色
            for (int i = (int) circle.x; i < radius + (int) circle.x; i++) {
                for (int j = (int) circle.y; j < (int) circle.y + radius; j++) {
                    color = bitmap.getPixel(i, j);
                    r = (color >> 16) & 0xff;
                    g = (color >> 8) & 0xff;
                    b = color & 0xff;
                    String isWhite = getColor(r, g, b);
                    if (!"black".equals(isWhite)) {
                        color = result_bitmap.getPixel(i, j);
                        r = (color >> 16) & 0xff;
                        g = (color >> 8) & 0xff;
                        b = color & 0xff;
                        color_result = getColor(r, g, b);
                        if ("red".equals(color_result)) {
                            System.out.println("is red");
                            return TrafficLightName.RED;
                        } else if ("Yellow".equals(color_result)) {
                            System.out.println("is Yellow");
                            return TrafficLightName.YELLOW;
                        } else if ("green".equals(color_result)) {
                            System.out.println("is green");
                            return TrafficLightName.GREEN;
                        } else {
                            System.out.println("错误= =");
                        }
                    } else {
                        continue;
                    }
                }
            }
        } else {
            return -1;
        }
        return result;
    }

//    private static int processPicture() throws IOException {
//        Bitmap result_bitmap = LeftFragment.bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Mat input = new Mat();
//        Utils.bitmapToMat(result_bitmap, input);
//        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
//        for (int i = 0; i <= 3; i++) {
//            Imgproc.erode(input, input, element, new Point(-1, -1), 1);
//        }
//        result_bitmap = Bitmap.createBitmap(input.cols(), input.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(input, result_bitmap);
//        int result = -1;
//        Bitmap bitmap = result_bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        for (int i = 0; i < bitmap.getWidth(); i++) {
//            for (int j = 0; j < bitmap.getHeight(); j++) {
//                final int color = bitmap.getPixel(i, j);
//                final int r = (color >> 16) & 0xff;
//                final int g = (color >> 8) & 0xff;
//                final int b = color & 0xff;
//                int newPixel = colorToRGB(255, r, g, b, 0);
//                bitmap.setPixel(i, j, newPixel);
//            }
//        }
//        double[] doubles = getCircle(bitmap);
//        Point circle = null;
//        int radius = 0;
//        if (doubles != null) {
//            circle = new Point((int) doubles[0], (int) doubles[1]);
//            radius = (int) doubles[2];
//        }
//        if (circle != null) {
//
//
//            int color = 0;
//            int r = 0;
//            int g = 0;
//            int b = 0;
//            String color_result = "";
//
//
//            Bitmap bmp = Bitmap.createBitmap(bitmap, (int) circle.x, (int) circle.y, radius, radius, null,
//                    false);
//            FileService.savePhoto(bmp, "bmp.png");
//            //截取右下角的图片进行分析,一旦碰到白色就直接判断当前的颜色
//            for (int i = (int) circle.x; i < radius + (int) circle.x; i++) {
//                for (int j = (int) circle.y; j < (int) circle.y + radius; j++) {
//                    color = bitmap.getPixel(i, j);
//                    r = (color >> 16) & 0xff;
//                    g = (color >> 8) & 0xff;
//                    b = color & 0xff;
//                    String isWhite = getColor(r, g, b);
//                    if (!"black".equals(isWhite)) {
//                        color = result_bitmap.getPixel(i, j);
//                        r = (color >> 16) & 0xff;
//                        g = (color >> 8) & 0xff;
//                        b = color & 0xff;
//                        color_result = getColor(r, g, b);
//                        if ("red".equals(color_result)) {
//                            System.out.println("is red");
//                            return 0;
//                        } else if ("Yellow".equals(color_result)) {
//                            System.out.println("is Yellow");
//                            return 1;
//                        } else if ("green".equals(color_result)) {
//                            System.out.println("is green");
//                            return 2;
//                        } else {
//                            System.out.println("错误= =");
//                        }
//                    } else {
//                        continue;
//                    }
//                }
//            }
//        } else {
//            return -1;
//        }
//        return result;
//    }

    private static double[] getCircle(Bitmap bitmap) {
        Mat input = new Mat();
        Utils.bitmapToMat(bitmap, input);
        Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2GRAY);
        //膨胀
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        for (int i = 0; i < 1; i++) {
            Imgproc.dilate(input, input, element, new Point(-1, -1), 1);
        }
        Imgproc.GaussianBlur(input, input, new Size(9, 9), 0, 0, Core.BORDER_DEFAULT);
        Mat circles = new Mat();
        Imgproc.blur(input, input, new Size(7, 7), new Point(2, 2));
        // 找圆算法参数
//        Imgproc.HoughCircles(input, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 100,
//                65, 60, 15, 0);
        Imgproc.HoughCircles(input, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 100,
                30, 60, 15, 0);
        Log.i("2017car", String.valueOf("size: " + circles.cols()) + ", "
                + String.valueOf(circles.rows()));
        int max_radio = 0;
        double[] target_point = null;
        if (circles.cols() > 0) {
            for (int x = 0; x < Math.min(circles.cols(), 5); x++) {
                double circleVec[] = circles.get(0, x);

                if (circleVec == null) {
                    break;
                }
                Point center = new Point((int) circleVec[0], (int) circleVec[1]);

                int radius = (int) circleVec[2];
                if (max_radio == 0) {
                    max_radio = radius;
                    target_point = circleVec;
                } else {
                    if (radius > max_radio) {
                        max_radio = radius;
                        target_point = circleVec;
                    }
                }
                Log.i("radio", radius + "");
                // 画圆心
                Imgproc.circle(input, center, 3, new Scalar(255, 255, 255), 5);
                // 画圆弧
                Imgproc.circle(input, center, radius,
                        new Scalar(255, 255, 255), 2);
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    input.width(), input.height());
            Utils.matToBitmap(input, createBitmap);
            FileService.savePhoto(createBitmap, "shape.png");
        }
        circles.release();
        input.release();
        return target_point;
    }


    /**
     * 获取指定rgb的颜色
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    private static String getColor(int r, int g, int b) {
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        double Hue = hsv[0] / 2;
        double Saturation = hsv[1] * 255 / 2;
        double Value = hsv[2] * 255 / 2;
//        Log.i("HHHHH", Hue + "");
//        Log.i("SSSSS", hsv[1] * 255 / 2 + "");
//        Log.i("VVVVV", hsv[2] * 255 / 2 + "");
        if (Hue <= 180 && Value <= 55) {
            // 黑色
//            Log.i("颜色", "黑色");
            return "black";
            // return "黑色";
        }

        if (Hue <= 180 && Saturation <= 30 && Value >= 221) {
            // 白色
            Log.i("颜色", "白色");
            return "white";
            // return "白色";
        }
        if (((Hue >= 0 && Hue <= 10)) || (Hue >= 156 && Hue <= 180)) {
            // 红色
            return "red";
            // return "红色";
        } else if (Hue >= 11 && Hue <= 25) {
            // 橙色
            return "orange";
            // return "橙色";
        } else if (Hue >= 26 && Hue <= 34) {
            // 黄色
            return "Yellow";
            // return "黄色";
        } else if (Hue >= 35 && Hue <= 77) {
            // 绿
            return "green";
            // return "绿色";
        } else if (Hue >= 78 && Hue <= 99) {
            // 青色
            return "cyan";
            // return "青色";
        } else if (Hue >= 100 && Hue <= 124) {
            // 蓝色
            return "blue";
            // return "蓝色";
        } else if (Hue >= 125 && Hue <= 155) {
            // 紫色(品)
            return "purple";
            // return "品色";
        } else {
            return "none";
        }
    }
}
