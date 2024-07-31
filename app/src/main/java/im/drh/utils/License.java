package im.drh.utils;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;


import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import car.car2024.Utils.OtherUtils.FileService;



public class License {



    public static String licenseRect(Bitmap bitmap) {
        //bitmap转mat
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);
        //高斯模糊
        Imgproc.GaussianBlur(image, image, new Size(new Point(3, 3)), 0);
        Mat canny = image.clone();
        //边缘检测
        //需要较少轮廓调大,需要较多轮廓调小
        Imgproc.Canny(image, canny, 400, 300, 3);

        //指定核大小，如果效果不佳，可以试着将核调大
        Mat kernelX = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(29, 4));
        Mat kernelY = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4, 19));
        //多次腐蚀膨胀处理,去噪
        Imgproc.dilate(canny, canny, kernelX, new Point(-1, -1), 3);
        Imgproc.erode(canny, canny, kernelX, new Point(-1, -1), 6);
        Imgproc.dilate(canny, canny, kernelX, new Point(-1, -1), 2);
        Imgproc.erode(canny, canny, kernelY, new Point(-1, -1), 1);
        Imgproc.dilate(canny, canny, kernelY, new Point(-1, -1), 2);

        //再次对图片模糊处理
        Imgproc.medianBlur(canny, canny, 15);
        Imgproc.medianBlur(canny, canny, 15);

        //轮廓检测
        Bitmap bitmap1 = null;
        Mat mat = new Mat();
        Mat hierarchy = canny.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(canny.clone(), contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            Imgproc.rectangle(canny, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0), 2);
            if ((float) rect.width / rect.height >= 2.2 && (float) rect.width / rect.height < 5.0) {
                mat = new Mat(image, rect);
                bitmap1 = Bitmap.createBitmap(bitmap, rect.x, rect.y, rect.width, rect.height);
            }
        }
        FileService.saveBitmap(bitmap1, "fkdo.png");


        Mat ca = mat.clone();
        Imgproc.Canny(mat, ca, 300, 400, 3);
        Mat thresh = mat.clone();
        Imgproc.threshold(ca.clone(), thresh, 127, 255, Imgproc.THRESH_BINARY);


        Mat hier = mat.clone();
        List<MatOfPoint> contour = new ArrayList<>();
        Imgproc.findContours(thresh.clone(), contour, hier, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println(contour.size());
        LinkedList<Integer> index = new LinkedList();
        LinkedList<String> word = new LinkedList();
        for (int i = 0; i < contour.size(); i++) {
            Rect rect = Imgproc.boundingRect(contour.get(i));
            Imgproc.rectangle(thresh, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0), 2);
            System.out.println(rect.x + " " + rect.y + " " + rect.width + " " + rect.height + " " + i);
            if ((float) rect.width / rect.height >= 0.3 && (float) rect.width / rect.height <= 0.7 && rect.height >= 0.6 * thresh.rows()) {
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap1, rect.x, rect.y, rect.width, rect.height);
                FileService.saveBitmap(bitmap2, rect.x + ".png");
                String s = License.licenseOcr(bitmap2);
                System.out.println("第" + i + "次" + s);
                index.add(rect.x);
                word.add(s);
            }
        }
        for (int i = 0; i < index.size(); i++) {
            for (int j = index.size() - 1; j > i; j--) {
                if (index.get(j) < index.get(j - 1)) {
                    String w = word.get(j - 1);
                    index.set(j, index.get(j) + index.get(j - 1));
                    index.set(j - 1, index.get(j) - index.get(j - 1));
                    index.set(j, index.get(j) - index.get(j - 1));
                    word.set(j - 1, word.get(j));
                    word.set(j, w);
                }
            }
        }
        System.out.println("word=========" + word);
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < word.size(); i++) {
            result.append(word.get(i));
        }
        System.out.println("result====" + result);
        return result.toString();
    }

    public static String licenseOcr(Bitmap bitmap) {
        Bitmap bitmap1 = zeroAndOne(bitmap);
        Bitmap open = open(bitmap1);
        FileService.savePhoto(open, UUID.randomUUID() + ".png");
        TessBaseAPI api = new TessBaseAPI();
        api.setDebug(true);
        api.init(Environment.getExternalStorageDirectory() + "/2021car/tesseract/", "eng");
        api.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"); // 识别白名单
        api.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^'、·&*()_丿灬+=、】''\"（）《》？：｜～~·【「」｜--——[]}{;:'\"\\|~`,./<>?`"); // 识别黑名单
        api.setImage(open);
        return api.getUTF8Text();
    }

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
            if ((r > 90 && g > 120 && b > 180) || (r < 40 && g < 40 && b < 40)) {
                gray = 0;
            } else {
                gray = 255;
            }

            newPx[i] = Color.argb(a, gray, gray, gray);//将处理后的透明度（没变），r,g,b分量重新合成颜色值并将其存储在数组中
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

    //开运算
    public static Bitmap open(Bitmap bitmap) {
        Mat sSrc = new Mat();
        Utils.bitmapToMat(bitmap, sSrc);
        Mat sStrElement = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS,
                new Size(3, 3), new Point(-1, -1));
        Mat sDst = sSrc.clone();
        Imgproc.morphologyEx(sSrc, sDst, Imgproc.MORPH_OPEN, sStrElement);
        Utils.matToBitmap(sDst, bitmap);
        return bitmap;
    }

    //闭运算
    public static Bitmap close(Bitmap bitmap) {
        Mat sSrc = new Mat();
        Utils.bitmapToMat(bitmap, sSrc);
        Mat sStrElement = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS,
                new Size(3, 3), new Point(-1, -1));
        Mat sDst = sSrc.clone();
        Imgproc.morphologyEx(sSrc, sDst, Imgproc.MORPH_CLOSE, sStrElement);
        Utils.matToBitmap(sDst, bitmap);
        return bitmap;
    }



//    public static String testOneImage(Bitmap bitmap) {
//        String result = "";
//        if (mEngine == null) {
//            mEngine = new PlatenumberEngine();
//        }
//        try {
//            int bytes = bitmap.getByteCount();
//            ByteBuffer buf = ByteBuffer.allocateDirect(bytes);
//            bitmap.copyPixelsToBuffer(buf);
//            byte[] byteArray = buf.array();
//            ArrayList<PlaterecogResponse> responses = mEngine.processPlaterecog(byteArray,
//                    PlatenumberEngine.PIX_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, true);
////            if (responses == null || responses.size() < 1) {
////                result = "";
////            } else {x
////                for (PlaterecogResponse response : responses) {
////                    if (response.qcScore> 0.7){
////                        result  = response.plateNumber;
////                    }else {
////
////                    }
////                }
////            }
//            System.out.println(responses);
//        } catch (Exception e) {
//            result = " fail 未初始化";
//            Log.e(TAG, "result:" + result);
//            e.printStackTrace();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}
