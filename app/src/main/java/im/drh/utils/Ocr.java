package im.drh.utils;

import static org.opencv.imgproc.Imgproc.CV_SHAPE_RECT;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import car.car2024.Utils.OtherUtils.FileService;
import car.car2024.Utils.Socket.Variable;

/**
 * 文字识别类
 */
public class Ocr {
    // 指定识别的语言英文加中文，其中包含数字识别
    private static String LANGUAGE = "chi_sim";
    public static String RESULT = "4";
    public static String NUMANDTEXT = "A1";
    public static int COUNT = 0;

    /**
     * 主要识别类
     *
     * @param bitmap 图片参数
     * @return
     */
    public static String OcrRecognition(Bitmap bitmap) {
        System.out.println("进入文字识别------------------");
        //二值化处理
//        Bitmap bitmap2 = zeroAndOne(bitmap);
        //二值化后切割图片
//        Bitmap bitmap1 = cutBitmap(bitmap2);
//        Bitmap dilate = dilate(bitmap2);
//        FileService.saveBitmap(dilate,"dila.png");
//        Bitmap corrosion = erode(dilate);
//        FileService.savePhoto(corrosion,"ocr.png");
        Bitmap bitmap1 = toGray(bitmap);
        FileService.saveBitmap(bitmap, "before.png");
        TessBaseAPI api = new TessBaseAPI();
        api.setDebug(true);

        //设置路径，需要将assets包下的chi和eng文件拷贝到手机sd卡目录下，第一个参数为这两个文件的路径，这两个文件在assets下的chi～和eng～自行放到下面的目录中，不然报错
        api.init(Environment.getExternalStorageDirectory() + "/2021car/tesseract/", LANGUAGE);

//        System.out.println(api.meanConfidence());
//        api.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "共创辉煌"); // 识别白名单
        //设置黑名单，匹配到一下字符将自动跳过
        api.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_丿灬+=、】'·`\'\"（）《》？：｜～~·【「」｜--——[]}{;:'\"\\|~`,./<>?`"); // 识别黑名单
        api.setImage(bitmap1);
        //识别方法，虽然不用但是一定需要，不然后面的方法全部不生效
        String utf8Text = api.getUTF8Text();
        ResultIterator resultIterator = api.getResultIterator();
        resultIterator.begin();
        String str = "";
        //进行置信度判断，当置信度高于设置值时，才采用识别到的文字
        do {
            System.out.println(resultIterator.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD));
            System.out.println(resultIterator.confidence(TessBaseAPI.PageIteratorLevel.RIL_WORD));
            if (resultIterator.confidence(TessBaseAPI.PageIteratorLevel.RIL_WORD) > Variable.WORDCONFIDENCE) {
                str += resultIterator.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD);
            }
        } while (resultIterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD));

        //过滤出需要的文字和字母
        String string = getString(str);
        System.out.println("ocr====================" + string);
        //把识别结果转换成协议
        getResult(string);
        getChi(string);
        getNumAndText(string);
        return string;
    }

    /**
     * 取识别结果的数字和字母
     */
    public static void getNumAndText(String str) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        StringBuffer result = new StringBuffer();
        while (m.find()) {
            result.append(m.group());
        }
        NUMANDTEXT = result.toString();
    }

    /**
     * 获取中文个数
     *
     * @param str
     */
    public static void getChi(String str) {
        Pattern compile = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            COUNT++;
        }
    }

    /**
     * 把识别结果转换成协议值
     *
     * @return
     */
    public static void getResult(String string) {

        Pattern p1 = Pattern.compile(".*[富|强]+.*");
        Matcher m1 = p1.matcher(string);
        if (m1.matches()) {
            RESULT = "1";
            return;
        }
        Pattern p2 = Pattern.compile(".*[爱|国]+.*");
        Matcher m2 = p2.matcher(string);
        if (m2.matches()) {
            RESULT = "2";
            return;
        }
        Pattern p3 = Pattern.compile(".*[敬|业]");
        Matcher m3 = p3.matcher(string);
        if (m3.matches()) {
            RESULT = "4";
            return;
        }
        RESULT = "4";
    }


    /**
     * 二值化处理（可用）
     *
     * @param bm
     * @return
     */
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
            int gray = (int) ((float) r * 0.33 + (float) g * 0.33 + (float) b * 0.34);

            //下面前两个if用来做溢出处理，防止灰度公式得到到灰度超出范围（0-255）
            if (gray > 255) {
                gray = 255;
            }

            if (gray == 0) {
                gray = 0;
            }

            if (gray >= 35) {//如果某像素的灰度值不是0(黑色)就将其置为255（白色）
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

    /**
     * 过滤无关符号
     *
     * @param paramValue
     * @return
     */
    public static String getString(String paramValue) {
        System.out.println("原值------" + paramValue);
        paramValue = paramValue.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
        System.out.println("过滤后--------" + paramValue);
        return paramValue;
    }

    /**
     * 腐蚀算法（可用）
     *
     * @param bitmap
     * @return
     */
    public static Bitmap erode(Bitmap bitmap) {
        Mat mSource = new Mat();

        Utils.bitmapToMat(bitmap, mSource);
        Mat grayMat = new Mat();
        Imgproc.cvtColor(mSource, grayMat, Imgproc.COLOR_BGR2GRAY);//转换成灰度图
        //图像腐蚀
        Mat kernel = Imgproc.getStructuringElement(CV_SHAPE_RECT, new Size(3, 3));
        Mat result = new Mat();
        Imgproc.erode(mSource, result, kernel, new Point(-1, -1), 3);
        Utils.matToBitmap(result, bitmap);
        return bitmap;
    }


    /**
     * 膨胀算法（可用）
     *
     * @param bitmap
     * @return
     */
    public static Bitmap dilate(Bitmap bitmap) {
        Mat mSource = new Mat();

        Utils.bitmapToMat(bitmap, mSource);
        Mat grayMat = new Mat();
        Imgproc.cvtColor(mSource, grayMat, Imgproc.COLOR_BGR2GRAY);//转换成灰度图
        //图像膨胀
        Mat kernel = Imgproc.getStructuringElement(CV_SHAPE_RECT, new Size(3, 3));
        Mat result = new Mat();
        Imgproc.dilate(mSource, result, kernel, new Point(-1, -1), 3);
        Utils.matToBitmap(result, bitmap);
        return bitmap;
    }

    /**
     * 切割图片，基本不需要修改，获取图片中间部分。如果循迹有问题可以把这个方法去掉（太愚蠢，已废弃）
     *
     * @param bitmap
     * @return
     */
    public static Bitmap cutBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();//原图像宽度
        int height = bitmap.getHeight();//原图像高度
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, (width / 7), (height / 8), width - 150, height - 100);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,(int) (width / 2), (int)(height / 2), true);
        return bitmap1;
    }

    /**
     * 转灰度图
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toGray(Bitmap bitmap) {
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(image, bitmap);
        return bitmap;
    }
}
