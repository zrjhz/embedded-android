package car.car2024.Utils.Image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import car.car2024.Utils.OtherUtils.FileService;
import im.zhy.util.GetVarName;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hdy on 17/05/2018.
 */

@SuppressWarnings("all")
public class ShapeUtils4 {

    // 用于保存图形个数  主要是用于测试，记录当前图形图像保存到本地的文件名
    private static int[] shape_mun = new int[6];

    public static void clearShapeMun(){

        for (int i = 0; i < shape_mun.length; i++) {
            shape_mun[i] = 0;
        }
    }

    public static void shapeAmountAddOne(String shapeName){
        int index = GetVarName.getShapeInt(shapeName);
        if ("other".equals(shapeName)){
            index = 5;
        }
        shape_mun[index]++;
    }

    public static int getShapeAmout(String shapeName){
        int index = GetVarName.getShapeInt(shapeName);
        if ("other".equals(shapeName)){
            index = 5;
        }
        return shape_mun[index];
    }


    private static BufferedWriter bufferedOWriter;
    public static void cutoutShapeColor(List<ArrayList<MatOfPoint2f>> shapeList,List<Bitmap> bitmapList){

        //初始化流
        try {
            bufferedOWriter = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory()+"/2019car/point.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("shapeList_size:"+shapeList.size());
        System.out.println("bitmapList:"+bitmapList.size());
        int munber = 0;
        for (int i = 0; i < shapeList.size(); i++) {
            Bitmap bitmap = bitmapList.get(i);
            ArrayList<MatOfPoint2f> arrayList = shapeList.get(i);
            for (MatOfPoint2f matOfPoint2f : arrayList) {
                munber += 1;
                String imgName = "shape_"+munber;
                cutoutShape(matOfPoint2f,bitmap,imgName);
                System.out.println("munber:"+imgName+"坐标个数："+matOfPoint2f.toList().size());

                try {
                    printPoints(matOfPoint2f,munber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        try {
            bufferedOWriter.flush();
            bufferedOWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printPoints(MatOfPoint2f matOfPoint2f,int number) throws IOException {
        bufferedOWriter.newLine();
        String lien = "第"+number+"张保存 边"+matOfPoint2f.total()+"   ";
        bufferedOWriter.write(lien,0,lien.length());
        bufferedOWriter.newLine();
        Point[] points = matOfPoint2f.toArray();
        for (int i = 0; i < points.length; i++) {
            lien = "   第"+(i+1)+"坐标：X:"+points[i].x+" Y:"+points[i].y;
            bufferedOWriter.write(lien,0,lien.length());
        }


    }

    public static void cutoutShape(Point[] points, Bitmap bitmap, String colorName, int number){
        Map<String, int[]> map = shapeScope(points, bitmap.getWidth(), bitmap.getHeight());

        int max_x = map.get("max_x")[0];
        int min_x = map.get("min_x")[0];
        int max_y = map.get("max_y")[0];
        int min_y = map.get("min_y")[0];

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, min_x, min_y, max_x, max_y);

        FileService.saveBitmap(newBitmap, colorName + "_" + number);
    }


    //截取图形 并保存截取好的图形
    public static Bitmap cutoutShape(MatOfPoint2f matOfPoint2f,Bitmap bitmap,String shape){

        String imgName;
        if(shape != null && shape.matches("shape_\\d{1,2}")){
            imgName = shape;
        }else if (shape != null && shape.matches("LicenseChar_\\d{1,2}")){
            imgName = shape;
        }else{
            shapeAmountAddOne(shape);
            imgName = shape+getShapeAmout(shape);
        }

        Bitmap newBitmap = cutoutShape(matOfPoint2f, bitmap);

        //保存截好的图形 只适合在测试阶段
        FileService.saveBitmap(newBitmap,imgName);

        return newBitmap;

    }

    public static Bitmap cutoutShape(MatOfPoint2f matOfPoint2f, Bitmap bitmap){
        Point[] points = matOfPoint2f.toArray();

        Map<String, int[]> map = shapeScope(points, bitmap.getWidth(), bitmap.getHeight());

        int max_x = map.get("max_x")[0];
        int min_x = map.get("min_x")[0];
        int max_y = map.get("max_y")[0];
        int min_y = map.get("min_y")[0];

        Bitmap newBitmap = Bitmap.createBitmap(bitmap ,min_x ,min_y ,max_x - min_x ,max_y - min_y);


        return newBitmap;
    }

    //求取最大x，y  最小 x，y
    public static Map<String,int[]> shapeScope(Point[] points,int width,int height){
        int[] max_x = new int[]{(int) points[0].x,0};
        int[] min_x = new int[]{(int) points[0].x,0};
        int[] max_y = new int[]{(int) points[0].y,0};
        int[] min_y = new int[]{(int) points[0].y,0};

        for(int i = 1;i < points.length; i++){
            if(points[i].x > max_x[0]) {
                max_x[0] = (int)points[i].x;
                max_x[1] = (int)points[i].y;
            }
            if(points[i].x < min_x[0]) {
                min_x[0] = (int)points[i].x;
                min_x[1] = (int)points[i].y;
            }

            if(points[i].y > max_y[0]){
                max_y[0] = (int)points[i].y;
                max_y[1] = (int)points[i].x;
            }
            if(points[i].y < min_y[0]) {
                min_y[0] = (int)points[i].y;
                min_y[1] = (int)points[i].x;
            }
        }

        if (max_x[0] + 5 <= width){
            max_x[0] += 5;
        }else{
            max_x[0] = width;
        }

        if (max_y[0] + 5 <= height){
            max_y[0] += 5;
        }else{
            max_y[0] = height;
        }

        if (min_x[0] - 5 >= 0){
            min_x[0] -= 5;
        }else{
            min_x[0] = 0;
        }

        if (min_y[0] - 5 >= 0){
            min_y[0] -= 5;
        }else{
            min_y[0] = 0;
        }

        Map<String,int[]> map = new HashMap<>();
        map.put("max_x",max_x);
        map.put("max_y",max_y);
        map.put("min_x",min_x);
        map.put("min_y",min_y);
        return map;
    }


    public static String getColor(int r, int g, int b) {
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        double Hue = hsv[0] / 2;
        double Saturation = hsv[1] * 255 / 2;
        double Value = hsv[2] * 255 / 2;
//        Log.i("HHHHH", Hue + "");
//        Log.i("SSSSS", hsv[1] * 255 / 2 + "");
//        Log.i("VVVVV", hsv[2] * 255 / 2 + "");
        if (Hue <= 180 && Saturation <= 120 && Value < 46) {
            // 黑色
//            Log.i("颜色", "黑色");
            return "black";
            // return "黑色";
        }

        if (Hue >= 156 && Hue <= 180 && Saturation >= 43 && Value >= 30) {
            // 红色
            return "red";
            // return "红色";
        } else if (Hue >= 11 && Hue < 26 && Saturation >= 43 && Value >= 46) {
            // 橙色
            return "orange";
            // return "橙色";
        } else if (Hue >= 26 && Hue < 41 && Saturation >= 80 && Value >= 46) {
            // 黄色
            return "Yellow";
            // return "黄色";
        } else if (Hue >= 41 && Hue < 76 && Saturation >= 110 && Value >= 46) {
            // 绿
            return "green";
            // return "绿色";
        } else if (Hue >= 80 && Hue < 100 && Saturation >= 43 && Value >= 46) {
            // 青色
            return "cyan";
            // return "青色";
        } else if (Hue >= 100 && Hue < 125 && Saturation >= 90 && Value >= 46) {
            // 蓝色
            return "blue";
            // return "蓝色";
        } else if (Hue >= 125 && Hue <= 155 && Saturation >= 43 && Value >= 46) {
            // 紫色(品)
            return "purple";
            // return "品色";
        }
        if (Hue <= 180 && Saturation <= 50 && Value >= 70) {
            // 白色
            return "white";
            // return "白色";
        }
        return "none";
    }

    public static Moments contourMoments(MatOfPoint contour) {
        Moments m = new Moments();
        int lpt = contour.checkVector(2);
        boolean is_float = true;//(contour.depth() == CvType.CV_32F);
        Point[] ptsi = contour.toArray();
        //PointF[] ptsf = contour.toArray();

        //CV_Assert( contour.depth() == CV_32S || contour.depth() == CV_32F );

        if (lpt == 0)
            return m;

        double a00 = 0, a10 = 0, a01 = 0, a20 = 0, a11 = 0, a02 = 0, a30 = 0, a21 = 0, a12 = 0, a03 = 0;
        double xi, yi, xi2, yi2, xi_1, yi_1, xi_12, yi_12, dxy, xii_1, yii_1;


        {
            xi_1 = ptsi[lpt - 1].x;
            yi_1 = ptsi[lpt - 1].y;
        }

        xi_12 = xi_1 * xi_1;
        yi_12 = yi_1 * yi_1;

        for (int i = 0; i < lpt; i++) {

            {
                xi = ptsi[i].x;
                yi = ptsi[i].y;
            }

            xi2 = xi * xi;
            yi2 = yi * yi;
            dxy = xi_1 * yi - xi * yi_1;
            xii_1 = xi_1 + xi;
            yii_1 = yi_1 + yi;

            a00 += dxy;
            a10 += dxy * xii_1;
            a01 += dxy * yii_1;
            a20 += dxy * (xi_1 * xii_1 + xi2);
            a11 += dxy * (xi_1 * (yii_1 + yi_1) + xi * (yii_1 + yi));
            a02 += dxy * (yi_1 * yii_1 + yi2);
            a30 += dxy * xii_1 * (xi_12 + xi2);
            a03 += dxy * yii_1 * (yi_12 + yi2);
            a21 += dxy * (xi_12 * (3 * yi_1 + yi) + 2 * xi * xi_1 * yii_1 +
                    xi2 * (yi_1 + 3 * yi));
            a12 += dxy * (yi_12 * (3 * xi_1 + xi) + 2 * yi * yi_1 * xii_1 +
                    yi2 * (xi_1 + 3 * xi));
            xi_1 = xi;
            yi_1 = yi;
            xi_12 = xi2;
            yi_12 = yi2;
        }
        float FLT_EPSILON = 1.19209e-07f;
        if (Math.abs(a00) > FLT_EPSILON) {
            double db1_2, db1_6, db1_12, db1_24, db1_20, db1_60;

            if (a00 > 0) {
                db1_2 = 0.5;
                db1_6 = 0.16666666666666666666666666666667;
                db1_12 = 0.083333333333333333333333333333333;
                db1_24 = 0.041666666666666666666666666666667;
                db1_20 = 0.05;
                db1_60 = 0.016666666666666666666666666666667;
            } else {
                db1_2 = -0.5;
                db1_6 = -0.16666666666666666666666666666667;
                db1_12 = -0.083333333333333333333333333333333;
                db1_24 = -0.041666666666666666666666666666667;
                db1_20 = -0.05;
                db1_60 = -0.016666666666666666666666666666667;
            }

            // spatial moments
            m.m00 = a00 * db1_2;
            m.m10 = a10 * db1_6;
            m.m01 = a01 * db1_6;
            m.m20 = a20 * db1_12;
            m.m11 = a11 * db1_24;
            m.m02 = a02 * db1_12;
            m.m30 = a30 * db1_20;
            m.m21 = a21 * db1_60;
            m.m12 = a12 * db1_60;
            m.m03 = a03 * db1_20;

//            m.completeState();
        }
        return m;
    }


    private static Mat colorEnhance(Mat source) {
        int width;
        int height;
        double alpha = 2;
        double beta = 50;

        Mat destination = new Mat(source.rows(), source.cols(), source.type());

        Imgproc.equalizeHist(source, destination);
        return destination;
    }




}
