package im.drh.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.net.BindException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class JudgeColor {

/**--------------------------二维码--------------------------------------------**/
    //二维码二值化
    public static Bitmap zeroAndOne(Bitmap bm) {
        int width = bm.getWidth();//原图像宽度
        int height = bm.getHeight();//原图像高度
        int color;//用来存储某个像素点的颜色值
        int r, g, b, a;//红，绿，蓝，透明度
        //创建空白图像，宽度等于原图宽度，高度等于原图高度，用ARGB_8888渲染，这个不用了解，这样写就行了
        Bitmap bmp = Bitmap.createBitmap(width, height
                , Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width * height];//用来存储原图每个像素点的颜色信息
        int[] newPx = new int[width * height];//用来处理处理之后的每个像素点的颜色信息
        /**
         * 第一个参数oldPix[]:用来接收（存储）bm这个图像中像素点颜色信息的数组
         * 第二个参数offset:oldPix[]数组中第一个接收颜色信息的下标值
         * 第三个参数width:在行之间跳过像素的条目数，必须大于等于图像每行的像素数
         * 第四个参数x:从图像bm中读取的第一个像素的横坐标
         * 第五个参数y:从图像bm中读取的第一个像素的纵坐标
         * 第六个参数width:每行需要读取的像素个数
         * 第七个参数height:需要读取的行总数
         */
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);//获取原图中的像素信息
        for (int i = 0; i < width * height; i++) {//循环处理图像中每个像素点的颜色值
            color = oldPx[i];//取得某个点的像素值

            r = Color.red(color);//取得此像素点的r(红色)分量
            g = Color.green(color);//取得此像素点的g(绿色)分量
            b = Color.blue(color);//取得此像素点的b(蓝色分量)
            a = Color.alpha(color);//取得此像素点的a通道值

            //此公式将r,g,b运算获得灰度值，经验公式不需要理解
            int gray = 0;

            //下面前两个if用来做溢出处理，防止灰度公式得到到灰度超出范围（0-255）
//            if (gray > 255) {
//                gray = 255;
//            }
//
//            if (gray ==0) {
//                gray = 0;
//            }
//
//            if (gray >=35) {//如果某像素的灰度值不是0(黑色)就将其置为255（白色）
//                gray = 255;
//            }
            if (r>40&& g<50&&b<50){
                gray=255;
            }else {
                gray=0;
            }

            newPx[i] = Color.argb(a, gray,gray,gray);//将处理后的透明度（没变），r,g,b分量重新合成颜色值并将其存储在数组中
        }
        /**
         * 第一个参数newPix[]:需要赋给新图像的颜色数组//The colors to write the bitmap
         * 第二个参数offset:newPix[]数组中第一个需要设置给图像颜色的下标值//The index of the first color to read from pixels[]
         * 第三个参数width:在行之间跳过像素的条目数//The number of colors in pixels[] to skip between rows.
         * Normally this value will be the same as the width of the bitmap,but it can be larger(or negative).
         * 第四个参数x:从图像bm中读取的第一个像素的横坐标//The x coordinate of the first pixels to write to in the bitmap.
         * 第五个参数y:从图像bm中读取的第一个像素的纵坐标//The y coordinate of the first pixels to write to in the bitmap.
         * 第六个参数width:每行需要读取的像素个数The number of colors to copy from pixels[] per row.
         * 第七个参数height:需要读取的行总数//The number of rows to write to the bitmap.
         */
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);//将处理后的像素信息赋给新图
        return bmp;//返回处理后的图像
    }


    /**--------------------------------------------车牌-----------------------------------------------------------------------------**/

//    //浅蓝0、//黄色1、//品红2、//浅红色3、//蓝色4、//青色5、// 深红色6、//黑色7      车牌蓝底9  车牌绿底10
//    public static double[][] HSV_VALUE_LOW = {
//            {10,163,147},//浅蓝0
//            {77, 163, 147},//黄色1
//            {146, 212, 140},//品红2
//            {126,155, 160},//浅红色3
//            {0, 204, 178},//蓝色4
//            {35, 163, 147},//青色5
//            {110,155,160},// 深红色6
//            {0,0,0},//黑色7
//            {0,0,192},//标准蓝8
//            {0,190,190},//车牌蓝底9      暗的TFT：0,190,190   亮的：0,180,190
//            {120,0,0}//车牌绿底10    暗的TFT H:21 S要调高一点:210  V:211  亮的TFT S值要调底一点：110    10,100,148
//    };
//
//    public static double[][] HSV_VALUE_HIGH = {
//            {47,255,255},//浅蓝0
//            {111, 255,255},//黄色1
//            {241, 255, 255.0},//品红2
//            {150,255, 255},//浅红色3
//            {21, 255, 255},//蓝色4
//            {75, 255.0, 255},//青色5
//            {150,255,255},// 深红色6
//            {180,255,120},//黑色7
//            {45,238,255},//标准蓝8
//            {28,255,255},//车牌蓝底9   亮暗一样
//            {140,0,0}//车牌绿底10   暗H:66     亮H:83
//    };
//
//    public static String plateDetector(Bitmap bitmap){
//
//        String plateStr=null;
//
//        Mat mRgba=Bitmap2Mat(bitmap);
//        /**
//         * ***********************车牌识别**************
//         */
//        //实现步骤1、直接HSV颜色空间裁剪出来车牌，计算长宽比来过滤掉
//        //实现步骤2、阈值分割，边缘检测，检测完之后绘制填充
//        //实现步骤3、填充之后二值化，二值化之后保存下来训练
////        show_bitmap(mRgba);//显示图片到View
//
//        Mat gray=new Mat();
//        Imgproc.cvtColor(mRgba,gray,Imgproc.COLOR_BGR2GRAY);//灰度化
//
//        Mat binary=new Mat();
//        Imgproc.Canny(gray,binary,50,150);//二值化  边缘检测
//
//        Mat kernel=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));//  指定腐蚀膨胀核
//        Imgproc.dilate(binary,binary,kernel);
//        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//        Mat hierarchy=new Mat();
//        Imgproc.findContours(binary, contours, hierarchy,
//                Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);//查找轮廓
//
//        double maxArea = 0;
//        Iterator<MatOfPoint> each = contours.iterator();
//        while (each.hasNext()) {
//            MatOfPoint wrapper = each.next();
//            double area = Imgproc.contourArea(wrapper);
//            if (area > maxArea) {
//                maxArea = area;
//            }
//        }
//
//        Mat result=null;
//        each = contours.iterator();
//        while (each.hasNext()) {
//            MatOfPoint contour = each.next();
//            double area = Imgproc.contourArea(contour);
//            if (area > 0.01 * maxArea) {
//                // 多边形逼近 会使原图放大4倍
//                Core.multiply(contour, new Scalar(4, 4), contour);
//                MatOfPoint2f newcoutour = new MatOfPoint2f(contour.toArray());
//                MatOfPoint2f resultcoutour = new MatOfPoint2f();
//                double length = Imgproc.arcLength(newcoutour, true);
//                Double epsilon = 0.01 * length;
//                Imgproc.approxPolyDP(newcoutour, resultcoutour, epsilon, true);
//                contour = new MatOfPoint(resultcoutour.toArray());
//                // 进行修正，缩小4倍改变联通区域大小
//                MatOfPoint new_contour=new MatOfPoint();
//                new_contour=ChangeSize(contour);
//                double new_area = Imgproc.contourArea(new_contour);//轮廓的面积
//                // 求取中心点
//                Moments mm = Imgproc.moments(contour);
//                int center_x = (int) (mm.get_m10() / (mm.get_m00()));
//                int center_y = (int) (mm.get_m01() / (mm.get_m00()));
//                Point center = new Point(center_x, center_y);
//
//                //最小外接矩形
//                Rect rect = Imgproc.boundingRect(new_contour);
//                double rectarea = rect.area();//最小外接矩形面积
//                if (Math.abs((new_area/rectarea)-1)<0.2){
//                    double wh = rect.size().width / rect.size().height;//宽高比值
//
//                    if (Math.abs(wh - 1.7) < 0.7 && rect.width > 250) {
//                        // 绘图///
//                        Mat imgSource=mRgba.clone();
//                        // 绘制外接矩形
//                        Imgproc.rectangle(imgSource, rect.tl(), rect.br(),
//                                new Scalar(255, 0, 0), 2);
//                        //*****图片裁剪***可以封装成函数*****************
//                        rect.x+=5;
//                        rect.width-=10;
//                        rect.y+=45;
//                        rect.height-=65;
//                        result=new Mat(imgSource,rect);
//                        //*****图片裁剪***可以封装成函数*****************
//                        Imgproc.pyrUp(result,result);//向上采样,放大图片
//                    }
//                }
//            }
//        }
//
//        if (result!=null){
//            Log.e("PlateDetector", "TFT屏幕裁剪成功: ");
//            //******使用HSV阈值分割***************************
//            Mat hsv_img=result.clone();
//
////                save_pic(result,true);
//            Imgproc.cvtColor(hsv_img,hsv_img,Imgproc.COLOR_BGR2HSV);//Hsv颜色空间转换
//
//
//
//
//            //车牌蓝色底9阈值分割
//            Mat plate_blue = new Mat();
//            Core.inRange(hsv_img,new Scalar(HSV_VALUE_LOW[9]),new Scalar(HSV_VALUE_HIGH[9]),plate_blue);
//
//            int blue_pixle_num=0;
//            for (int x = 0; x < plate_blue.width(); x++) {
//                for (int y = 0; y < plate_blue.height(); y++) {
//                    double pixle[] = plate_blue.get(y, x);
//                    if (pixle[0] == 255.0) {// 如果是白色
//                        blue_pixle_num++;
//                    }
//                }
//            }
////            Log.e("PlateDetector", "蓝色车牌像素面积: "+blue_pixle_num );//42873
//            if (blue_pixle_num>40000&&blue_pixle_num<70000){
////                Log.e("PlateDetector", "进入蓝色车牌识别");
////                plateStr=rect(plate_blue,result,1);
//                return "blue";
//            }
//
//            //车牌绿色底10阈值分割
//            Mat plate_green = new Mat();
//            Core.inRange(hsv_img,new Scalar(HSV_VALUE_LOW[10]),new Scalar(HSV_VALUE_HIGH[10]),plate_green);
//            int green_pixle_num=0;
//            for (int x = 0; x < plate_green.width(); x++) {
//                for (int y = 0; y < plate_green.height(); y++) {
//                    double pixle[] = plate_green.get(y, x);
//                    if (pixle[0] == 255.0) {// 如果是白色
//                        green_pixle_num++;
//                    }
//                }
//            }
////            Log.e("PlateDetector", "绿色车牌像素面积: "+green_pixle_num );//42873
//            if (green_pixle_num>50000&&green_pixle_num<90000){
////                Log.e("PlateDetector", "进入绿色车牌识别");
////                plateStr= rect(plate_green,result,2);
//                return "green";
//            }
//        }
//
//        return "yellow";
//
//    }
//
//
//
//    // 转换工具
//    public static Bitmap Mat2Bitmap(Mat cannyMat) {
//        Bitmap bmpCanny = Bitmap.createBitmap(cannyMat.cols(), cannyMat.rows(),
//                Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(cannyMat, bmpCanny);
//        return bmpCanny;
//    }
//
//
//    // 转换工具
//    public static Mat Bitmap2Mat(Bitmap bmp) {
//        Mat mat = new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC4);
//        Utils.bitmapToMat(bmp, mat);
//        return mat;
//    }
//
//    // 把坐标降低到4分之一
//    static MatOfPoint ChangeSize(MatOfPoint contour) {
//        for (int i = 0; i < contour.height(); i++) {
//            double[] p = contour.get(i, 0);
//            p[0] = p[0] / 4;
//            p[1] = p[1] / 4;
//            contour.put(i, 0, p);
//        }
//        return contour;
//    }

    //方案一:根据颜色切割出对应颜色的车牌
    public static Bitmap JudgeLicense(Bitmap bitmap){
        Mat image=new Mat();
        Utils.bitmapToMat(bitmap,image);

        //输出二值图像
        Mat desImage=new Mat();
        Imgproc.cvtColor(image,desImage,Imgproc.COLOR_BGR2HSV);
        /** 阀值设置，比赛如果不准可以手机下载color picker调整**/
        //低阈值
        Scalar lowerScalar=new Scalar(30, 60, 160);//蓝色 100，43，46 黄色 26，43，46 绿色35，43，46
        //高阀值
        Scalar highScalar=new Scalar(5, 255, 255);//蓝色124，255，255 黄色 34，255，255 绿色 77，255，255
        Core.inRange(desImage,lowerScalar,highScalar,desImage);

        //去噪点 小于5*5都忽略
        Mat kernel =  Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10,10));
        Imgproc.morphologyEx(desImage, desImage, Imgproc.MORPH_CLOSE, kernel);

        //识别轮廓
        List<MatOfPoint> pointsVector = new Vector<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(desImage,pointsVector,hierarchy,Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);

        int x =640;
        int y = 320;
        int w = 0;
        int h = 0;
        double area=0;
        //得到最大的矩形
        for(int i=0;i<pointsVector.size();i++) {
            MatOfPoint cntMatOfPoint = pointsVector.get(i);
            Rect rect = Imgproc.boundingRect(cntMatOfPoint);
            if (rect.x<x){
                x=rect.x;
            }
            if (rect.y<y){
                y=rect.y;
            }
            if (rect.x+rect.width>w){
                w=rect.x+rect.width;
            }
            if (rect.y+rect.height>h){
                h=rect.y+rect.height;
            }
        }
        System.out.println(x+"  "+y+" "+w+" "+h);
        return Bitmap.createBitmap(bitmap,x,y,w-x,h-y);
    }

    //方案二:先二值化，获取图片中间的像素,比较1值的个数

    public static Boolean JudgeIsRightLicence(Bitmap bitmap){
        try {


            Mat image = new Mat();
            Utils.bitmapToMat(bitmap, image);
            //输出二值化图片
            Mat desImage = new Mat();
            Imgproc.cvtColor(image,desImage,Imgproc.COLOR_BGR2HSV);
            System.out.println("总像素点" + desImage.rows() * desImage.cols());
            //低阈值
            Scalar lowerScalar = new Scalar(100, 43, 46);//蓝色 100，43，46 黄色 26，43，46 绿色35，43，46
            //高阀值
            Scalar highScalar = new Scalar(124, 255, 255);//蓝色124，255，255 黄色 34，255，255 绿色 77，255，255
            Core.inRange(desImage, lowerScalar, highScalar, desImage);
            //去噪点 小于5*5都忽略
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10));
            Imgproc.morphologyEx(desImage, desImage, Imgproc.MORPH_CLOSE, kernel);

            int point = 0;
            for (int i = 0; i < desImage.rows(); i++) {
                for (int j = 0; j < desImage.cols(); j++) {
                    double[] doubles = desImage.get(i, j);
                    if (doubles[0] > 0) {
                        point++;
                    }
                }
            }
            Log.i("车牌像素", String.valueOf(point));
            System.out.println("计算结果"+point);
            return point>12000;
        }catch (Exception e){ e.printStackTrace();}
        return false;
    }

        /**------------------------文字切割------------------------**/

    /**
     * 下面两种方法为判断文字识别文字区域底色并且截取
     * @param bitmap
     * @return
     */
    //二值化截取
     public static Bitmap cut(Bitmap bitmap){
         try {


             Mat image = new Mat();
             Utils.bitmapToMat(bitmap, image);
             //转灰度图，帮助二值化
             Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
             //THRESH_BINARY 正向二值化，    THRESH_BINARY_INV反向二值化
             Imgproc.threshold(image, image, 127, 255, Imgproc.THRESH_BINARY);//thresh为二值化阀值，根据具体颜色修改，可以根据具体情况修改
             //腐蚀算法，去除背景影响
             Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
             Imgproc.dilate(image, image, structuringElement, new Point(-1, -1), 2);
             //膨胀算法，还原腐蚀对文字的影响
             Imgproc.erode(image, image, structuringElement, new Point(-1, -1), 3);

             //边缘检测将检测到对应颜色背景的文字进行查找
             ArrayList<MatOfPoint> contours = new ArrayList();
             Mat hierarchy = new Mat();
             Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
             System.out.println("查找到" + contours.size() + "边缘");
             for (int i = 0; i < contours.size(); i++) {
                 Rect rect = Imgproc.boundingRect(contours.get(i));

                 //计算文字的长宽比例去除文字
                 double scale = (rect.x + rect.width) / (rect.y + rect.height);
                 double HighStandard = 0.7;
                 double LowStandard = 0.3;
                 if (scale < HighStandard && scale > LowStandard) {
                     return Bitmap.createBitmap(bitmap, rect.x, rect.y, rect.width - rect.x, rect.height - rect.y);
                 }
             }
         }catch (Exception e){
             e.printStackTrace();
         }
        return null;
     }

     //通过HSV蒙版实现截取
    public static Bitmap cutByHsv(Bitmap bitmap){
         try {
             Mat image = new Mat();
             Utils.bitmapToMat(bitmap, image);
             Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
             //蓝色 100，43，46 黄色 26，43，46 绿色35，43，46
             //蓝色124，255，255 黄色 34，255，255 绿色 77，255，255
             Core.inRange(image, new Scalar(35, 43, 46), new Scalar(77, 255, 255), image);
             Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
             //白色区域膨胀
             Imgproc.dilate(image, image, structuringElement, new Point(-1, -1), 1);
             //白色区域腐蚀
             Imgproc.erode(image, image, structuringElement, new Point(-1, -1), 1);

             ArrayList<MatOfPoint> contours = new ArrayList<>();
             Mat hierarchy = new Mat();
             Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

             for (int i = 0; i < contours.size(); i++) {
                 Rect rect = Imgproc.boundingRect(contours.get(i));

                 //计算文字的长宽比例去除文字
                 double scale = (rect.x + rect.width) / (rect.y + rect.height);
                 double HighStandard = 0.7;
                 double LowStandard = 0.3;
                 if (scale < HighStandard && scale > LowStandard) {
                     return Bitmap.createBitmap(bitmap, rect.x, rect.y, rect.width - rect.x, rect.height - rect.y);
                 }
             }
         }catch (Exception e){
             e.printStackTrace();
         }
         return null;
    }
}
