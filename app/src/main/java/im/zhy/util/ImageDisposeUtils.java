package im.zhy.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import car.car2024.Utils.OtherUtils.FileService;
import car.car2024.Utils.Image.RectUtils;
import car.car2024.Utils.Image.ShapeUtils4;
import im.zhy.param.ColorName;
import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;

/**
 * @author zhy
 * @create_date 2019-05-01 18:28
 */

@SuppressWarnings("all")
public class ImageDisposeUtils  {

    private static final String TAG = "ImageDisposeUtils";

    public static int[] getRGB(Bitmap bitmap, int x, int y){
        if (y >= bitmap.getHeight()){
            return new int[] {0,0,0};
        }
        int color = bitmap.getPixel(x, y);
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;

        int[] rgb = new int[3];

        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;

        return rgb;

    }

    public static float[] getHSV(Bitmap bitmap, int x, int y){
        int[] rgb = getRGB(bitmap, x, y);
        float[] hsv = new float[3];
        Color.RGBToHSV(rgb[0], rgb[1], rgb[2], hsv);
        float h = hsv[0];
        float s = hsv[1] * 100;
        float v = hsv[2] * 100;

        float[] result = new float[3];

        result[0] = h;
        result[1] = s;
        result[2] = v;

        return result;
    }

    public static double[] getLAB(Bitmap bitmap, int x, int y){
        int[] rgb = getRGB(bitmap, x, y);

        double[] rgbd = new double[3];

        for (int i = 0; i < rgb.length; i++) {
            rgbd[i] = rgb[i];
        }

        double[] lab = XYZ2Lab(sRGB2XYZ(rgbd));

        return lab;

    }

    // 静态标志物图像颜色提取
    public static int staticColorExtract(Bitmap bitmap, int x, int y, int colorIndex) {

        float[] hsv = getHSV(bitmap, x, y);
        float h = hsv[0];
        float s = hsv[1];
        float v = hsv[2];

        if (((h > 340 && h <= 360) | (h > 0 && h < 4)) && colorIndex == GetVarName.getColorInt(ColorName.RED)){
            if (s > 40){
                return blackOrWhite(true);
            }
        }

        if (h >= 320 && h <= 340 && colorIndex == GetVarName.getColorInt(ColorName.PURPLE)){
            if (s > 40){
                return blackOrWhite(true);
            }
        }

        if (h >= 210 && h <= 250 && colorIndex == GetVarName.getColorInt(ColorName.BLUE)){
            if (s >= 40){
                return blackOrWhite(true);
            }
        }

        if (colorIndex == GetVarName.getColorInt(ColorName.BLACK)){
           return blackExtractRGB(bitmap, x, y, 5);
        }


        if (h >= 170 && h <= 220 && colorIndex == GetVarName.getColorInt(ColorName.CYAN)){
            if (s >= 40){
                return blackOrWhite(true);
            }
        }

        if (h >= 90 && h <= 120 && colorIndex == GetVarName.getColorInt(ColorName.GREEN)){
            if (s >= 40) {
                return blackOrWhite(true);
            }
        }

        if (h >= 50 && h < 100 && colorIndex == GetVarName.getColorInt(ColorName.YELLOW)){
            if (s > 50){
                return blackOrWhite(true);
            }
        }

        return blackOrWhite(false);

    }


    public static int colorExtract(Bitmap bitmap, int x, int y, int colorIndex){
        int[] rgb = getRGB(bitmap, x, y);
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];

        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        float h = hsv[0];
        float s = hsv[1] * 100;
        float v = hsv[2] * 100;


        if (((h >= 270 && h <= 360) || h < 3) && colorIndex == GetVarName.getColorInt(ColorName.RED)){
            if (s > 40){
                if (b <= 140){
                    return blackOrWhite(true);
                }
            }
        }

        if (h >= 250 && h <= 280 && colorIndex == GetVarName.getColorInt(ColorName.PURPLE)){
            if (b > 140){
                return blackOrWhite(true);
            }
        }

        if (h >= 210 && h <= 250 && colorIndex == GetVarName.getColorInt(ColorName.BLUE)){
            if (s >= 70){
                if (v >= 50){
                    if (g < 70) {
                        return blackOrWhite(true);
                    }
                }
            }
        }

        if (h >= 170 && h < 280 && colorIndex == GetVarName.getColorInt(ColorName.BLACK)){
            return blackExtractLAB(bitmap, x, y);
        }

        if (h >= 170 && h <= 220 && colorIndex == GetVarName.getColorInt(ColorName.WHITE)){
            if (s < 75){
                if (v > 50){
                    if (r > 90){
                        return blackOrWhite(true);
                    }
                }
            }
        }

        if (h >= 170 && h <= 200 && colorIndex == GetVarName.getColorInt(ColorName.CYAN)){
            if (s >= 60){
                if (v >= 60){
                    if (r < 90){
                        return blackOrWhite(true);
                    }
                }
            }
        }

        if (h >= 90 && h < 180 && colorIndex == GetVarName.getColorInt(ColorName.GREEN)){
            if (r < 120){
                if (g > 180){
                    return blackOrWhite(true);
                }
            }
        }

        if (h >= 60 && h < 105 && colorIndex == GetVarName.getColorInt(ColorName.YELLOW)){
            if (r > 120){
                return blackOrWhite(true);
            }
        }

        return blackOrWhite(false);
    }


    // 黑色的提取  RGB
    public static int blackExtractRGB(Bitmap bitmap, int x, int y, int range){
        int[] rgb = getRGB(bitmap, x, y);

        boolean b = false;
        int max = 50;

        if (rgb[0] < max && rgb[1] < max && rgb[2] < max){

            int count = rgb[0] + rgb[1] + rgb[2];
            int avg = count / 3;

            for (int i : rgb) {
                if (i > avg - range && i < avg + range){
                    b = true;
                    break;
                }
            }
        }

        return blackOrWhite(b);
    }


    // 黑色的提取  LAB
    public static int blackExtractLAB(Bitmap bitmap, int x, int y){
        double[] lab = getLAB(bitmap, x, y);

        double l = lab[0];
        double a = lab[1];
        double b = lab[2];

        if (l > 0 && l < 30){
            if (a > -10 && a < 20){
                if (b > -30 && b < 10){
                    return blackOrWhite(true);
                }
            }
        }

        return blackOrWhite(false);

    }


    public static Bitmap cutoutMaxRectangle(Bitmap bitmap){
        return newCutoutMaxRectangle(bitmap);
//        return oldCutoutMaxRectangle(bitmap);
    }

    public static Bitmap newCutoutMaxRectangle(Bitmap bitmap1){

        Bitmap bitmap = bitmap1.copy(Bitmap.Config.ARGB_8888, true);

        FileService.saveClassPhoto(bitmap, "squ", "squ");

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
//                int pixel = colorExtract(bitmap, x, y, GetVarName.getColorInt(CarData.background_color));
//                bitmap.setPixel(x, y, pixel);
            }
        }

        FileService.saveClassPhoto(bitmap, "Max_Squ", "squ");

        List<MatOfPoint2f> matOfPoint2fs = getGrads(bitmap, false);

        Bitmap maxBitmap = getMaxBitmap(matOfPoint2fs, bitmap1);

        if (maxBitmap == null){
            return null;
        }

//        maxBitmap = Bitmap.createBitmap(maxBitmap, 10, 10, maxBitmap.getWidth() - 20, maxBitmap.getHeight() - 20);

        FileService.saveClassPhoto(maxBitmap, "Max_Squ1", "squ");

        return maxBitmap;

    }


    // 扣取最大矩形
    public static Bitmap oldCutoutMaxRectangle(Bitmap bitmap){
        boolean flag = true;
        Bitmap bitmapCopy = null;
        Bitmap copy = null;
        int t_num = 0;
        Rect roi = null;
        while (flag) {
            bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            roi = RectUtils.findRectangle(bitmapCopy); // 自动截出最大矩形
            if (t_num > 3) {
                break;
            }
            if (roi == null) {
                // 没找到的处理
                t_num++;
                continue;
            }
            if (roi.width < 200 || roi.height < 120) {
                t_num++;
                continue;
            }else{
                break;
            }


        }

        Mat uncropped = new Mat();
        Utils.bitmapToMat(copy, uncropped);
        Imgproc.cvtColor(uncropped, uncropped, Imgproc.COLOR_BGR2RGB);
//        Imgproc.cvtColor(uncropped, uncropped, Imgproc.COLOR_RGB2Lab);
//        Imgcodecs.imwrite("/sdcard/2019car/Lab.png",uncropped);

        Mat cropped = new Mat(uncropped, roi);// 这里这句话有异常
        Bitmap bitmapIdOriginal = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_BGR2RGB);
//        Imgproc.GaussianBlur(cropped, cropped, new Size(5, 5), 0);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));

        for (int i = 0; i <= 3; i++) {
            Imgproc.dilate(cropped, cropped, element, new Point(-1, -1), 1);
            Imgproc.erode(cropped, cropped, element, new Point(-1, -1), 1);
        }

        Utils.matToBitmap(cropped, bitmapIdOriginal);

        bitmapIdOriginal = Bitmap.createBitmap(bitmapIdOriginal, 10, 10, bitmapIdOriginal.getWidth() - 26, bitmapIdOriginal.getHeight() - 26);

        return bitmapIdOriginal;
    }


    public static Bitmap getMaxBitmap(List<MatOfPoint2f> list, Bitmap bitmap){
        MatOfPoint2f matOfPoint2f = getMaxMat(list);
        return ShapeUtils4.cutoutShape(matOfPoint2f, bitmap);
    }

    public static MatOfPoint2f getMaxMat(List<MatOfPoint2f> list){

        Collections.sort(list, new Comparator<MatOfPoint2f>() {
            @Override
            public int compare(MatOfPoint2f m1, MatOfPoint2f m2) {
                return Imgproc.contourArea(m1) > Imgproc.contourArea(m2) ? -1 : 1;
            }
        });

        if (list == null || list.size() < 1){
            return null;
        }

        MatOfPoint2f matOfPoint2f = list.get(0);
        return matOfPoint2f;
    }

    // 图形梯度求取
    public static List<MatOfPoint2f> getGrads(Bitmap image, boolean isLicense){
        Mat src = new Mat();
        Utils.bitmapToMat(image, src);

        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);

        Mat dst = new Mat();

        double threshold = Imgproc.threshold(src, dst, 0, 255, Imgproc.THRESH_BINARY_INV);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_L1);

        List<MatOfPoint2f> list = new ArrayList<>();
        Log.e(TAG,"contours_size "+contours.size());

        for (MatOfPoint matOfPoint : contours) {
            // 获取面积
            double area = Imgproc.contourArea(matOfPoint);
//            Log.e(TAG,"area："+area);
            if (isLicense){
                if (area >= 300000 || area <= 80) {
                    continue;
                }
            }else {
                if (area >= 300000 || area <= 25) {
                    continue;
                }
            }

            Map<String, int[]> scope = ShapeUtils4.shapeScope(matOfPoint.toArray(), 30000, 30000);

            int max_x = scope .get("max_x")[0];
            int min_x = scope .get("min_x")[0];
            int max_y = scope .get("max_y")[0];
            int min_y = scope .get("min_y")[0];

            if (isLicense){
                if (max_y - min_y < 17){
                    continue;
                }
            }

            MatOfPoint2f temp = new MatOfPoint2f(matOfPoint.toArray());
            double arcLength = Imgproc.arcLength(temp, true);
            MatOfPoint2f approxCurve = new MatOfPoint2f();

            // 轮廓逼近
            Imgproc.approxPolyDP(temp, approxCurve, 0.04 * arcLength, true);

            list.add(approxCurve);

        }

        return list;
    }


    // 静态标志物背景色提取
    public static int landmarkBackground(Bitmap bitmap, int x, int y){

        int[] rgb = getRGB(bitmap, x, y);
        float h = getHSV(bitmap, x, y)[0];

        int rgbMin = 130;

        if (rgb[0] < rgbMin || rgb[1] < rgbMin || rgb[2] < rgbMin){
            return blackOrWhite(false);
        }

        int avg = (rgb[0] + rgb[1] + rgb[2]) / 3;

        for (int i : rgb) {
            if (i > avg + 5 || i < avg - 5){
                if (h > 190){
                    return blackOrWhite(false);
                }
            }
        }

        return blackOrWhite(true);
    }


    /**
     *
     * @param b true 返回 黑色 false 放回 白色
     * @return
     */
    public static int blackOrWhite(boolean b){
        if (b){
            return 0xff000000;
        }else {
            return 0xffffffff;
        }
    }



    private static double[] XYZ2Lab(double[] XYZ) {
        double[] Lab = new double[3];
        double X, Y, Z;
        X = XYZ[0];
        Y = XYZ[1];
        Z = XYZ[2];
        double Xn, Yn, Zn;
        Xn = 95.04;
        Yn = 100;
        Zn = 108.89;
        double XXn, YYn, ZZn;
        XXn = X / Xn;
        YYn = Y / Yn;
        ZZn = Z / Zn;

        double fx, fy, fz;

        if (XXn > 0.008856) {
            fx = Math.pow(XXn, 0.333333);
        } else {
            fx = 7.787 * XXn + 0.137931;
        }

        if (YYn > 0.008856) {
            fy = Math.pow(YYn, 0.333333);
        } else {
            fy = 7.787 * YYn + 0.137931;
        }

        if (ZZn > 0.008856) {
            fz = Math.pow(ZZn, 0.333333);
        } else {
            fz = 7.787 * ZZn + 0.137931;
        }

        Lab[0] = 116 * fy - 16;
        Lab[1] = 500 * (fx - fy);
        Lab[2] = 200 * (fy - fz);

        return Lab;
    }


    private static double[] sRGB2XYZ(double[] sRGB) {
        double[] XYZ = new double[3];
        double sR, sG, sB;
        sR = sRGB[0];
        sG = sRGB[1];
        sB = sRGB[2];
        sR /= 255;
        sG /= 255;
        sB /= 255;

        if (sR <= 0.04045) {
            sR = sR / 12.92;
        } else {
            sR = Math.pow(((sR + 0.055) / 1.055), 2.4);
        }

        if (sG <= 0.04045) {
            sG = sG / 12.92;
        } else {
            sG = Math.pow(((sG + 0.055) / 1.055), 2.4);
        }

        if (sB <= 0.04045) {
            sB = sB / 12.92;
        } else {
            sB = Math.pow(((sB + 0.055) / 1.055), 2.4);
        }

        XYZ[0] = 41.24 * sR + 35.76 * sG + 18.05 * sB;
        XYZ[1] = 21.26 * sR + 71.52 * sG + 7.2 * sB;
        XYZ[2] = 1.93 * sR + 11.92 * sG + 95.05 * sB;

        return XYZ;
    }


    public static void main(String[] args) {
        double[] doubles = sRGB2XYZ(new double[]{32, 37, 57});

        double[] doubles1 = XYZ2Lab(doubles);

        for (double v : doubles1) {
            System.out.println(v);
        }
    }



}
