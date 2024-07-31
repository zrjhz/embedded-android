package car.Identify.utils;

import static car.car2024.Utils.Image.RectUtils.deepCopyBitmap;
import static car.car2024.Utils.Socket.AcceptCarOrder.setIndex;

import android.graphics.Bitmap;

import car.Identify.models.QRCodeColorModel;
import car.Identify.models.QRCodeModel;

import com.king.wechat.qrcode.WeChatQRCodeDetector;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import car.car2024.ActivityView.FirstActivity;
import car.car2024.ActivityView.XcApplication;
import car.car2024.FragmentView.LeftFragment;
import car.car2024.FragmentView.RightAIFragment;
import car.car2024.Utils.OtherUtils.FileService;
import car.car2024.Utils.Image.Rgb2hsv;

public class QRCodeIdentify {
    private static final int width = 640;
    private static final int height = 360;
    public static Rgb2hsv rgb2hsv = new Rgb2hsv();
    public static QRCodeModel qrCodeModel = new QRCodeModel();
    public static QRCodeColorModel qrCodeColorModel = new QRCodeColorModel();
    public static Map<String, String> qrcodeUsingColorMap = new HashMap<>();
    public static ArrayList<String> qrcodeMap = new ArrayList<>();
    static int[] radius = {-180, 180}; //色相的最小值和最大值
    public static void qrcodeIdentify(Bitmap bitmap) {
        XcApplication.executorServicetor.execute(()-> FirstActivity.INSTANCE.runOnUiThread(()->{
            qrcodeUsingColorMap.clear();
            qrcodeMap.clear();
            long startTime = System.currentTimeMillis();
            Bitmap b = deepCopyBitmap(bitmap);
            QRCodeModel.Obj[] qrcodePosition = qrCodeModel.Detect(bitmap, true);

            for (QRCodeModel.Obj obj : qrcodePosition) {
                int count = 0;
                int max = 3;
                int i = radius[0];
                int index = 0;
                Mat mat = new Mat();
                Mat rgbMat = new Mat();
                Bitmap cutBitmap = cutBitmap(bitmap, obj);
                String color = qrCodeColorModel.detect(cutBitmap);
                String result = "nothing";

                setIndex(1);

                do {
                    if (i > radius[1]) { //如果0~180都没扫出来,就重新获取图片,并初始化i
                        cutBitmap = cutBitmap(bitmap, obj);
                        i = radius[0];
                    }
                    if (count >= max) {
                        break;
                    }
                    count++;
                    if (Objects.equals(color, "yellow")) {
                        Utils.bitmapToMat(cutBitmap, mat); //将bitmap转为mat
                        Imgproc.cvtColor(mat, rgbMat, Imgproc.COLOR_RGBA2BGR); //将rgba转为rgb
                        rgb2hsv.processImage(rgbMat, i++); //调用jni调整色相
                        Utils.matToBitmap(rgbMat, cutBitmap); //将mat转为bitmap
                    }
                    result = QRCodeIdentify.scanMultiple(cutBitmap); //解码二维码
                    System.out.println(color + "QRCode is hueing: " + i++);
                } while (Objects.equals(result, "nothing")); //直达结果不是nothing退出循环
                index = index + 1;
                FileService.saveBitmap(cutBitmap, index + ".png");
                qrcodeMap.add(result);
                qrcodeUsingColorMap.put(color, result);
                drawRectangleWithTextOnBitmap(b, obj.x, obj.y, obj.w, obj.h, color, result);
            }
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            if (!Objects.equals(LeftFragment.show_mode, "static")){
                RightAIFragment.picrec_iv.setImageBitmap(b);
            }
            RightAIFragment.picrectext_tv.setText(qrcodeUsingColorMap + "\n用时：" + executionTime + "ms");
        }));
    }
    public static String scanMultiple(Bitmap bitmap) {
        try {
            List<String> result = WeChatQRCodeDetector.detectAndDecode(bitmap);
            return result.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "nothing";
    }

    public static Bitmap cutBitmap(Bitmap bitmap, QRCodeModel.Obj obj) {
        int x = (int) obj.x;
        int y = (int) obj.y;
        int w = (int) obj.w;
        int h = (int) obj.h;
        int enlargedWidth;
        int enlargedHeight;
        if (x + w * 1.1 < width) {
            enlargedWidth = (int) (w * 1.1);
        } else {
            enlargedWidth = width - x;
        }
        if (y + h * 1.1 < height) {
            enlargedHeight = (int) (h * 1.1);
        } else {
            enlargedHeight = height - y;
        }
        x = x - (enlargedWidth - w) / 2;
        y = y - (enlargedHeight - h) / 2;
        w = enlargedWidth;
        h = enlargedHeight;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }

    private static void drawRectangleWithTextOnBitmap(Bitmap bitmap, float x, float y, float width, float height, String c, String text) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bitmap, imageMat);

        Point startPoint = new Point((int) x - 3, (int) y - 3);
        Point endPoint = new Point((int) (x + width + 3), (int) (y + height + 3));
        Scalar color = new Scalar(0, 0, 0); // 红色
        int thickness = 2;

        Imgproc.rectangle(imageMat, startPoint, endPoint, color, thickness);

        Point textPoint = new Point(startPoint.x, startPoint.y - 10); // 文字的位置，位于矩形上方偏移一些距离
        Scalar textColor = new Scalar(0, 0, 0, 255);
        switch (c) {
            case "red":
                textColor = new Scalar(255, 0, 0, 255);
                break;
            case "black":
                textColor = new Scalar(0, 0, 0, 255);
                break;
            case "yellow":
                textColor = new Scalar(255, 255, 0, 255);
                break;
            case "blue":
                textColor = new Scalar(0, 0, 255, 255);
                break;
            case "green":
                textColor = new Scalar(0, 255, 0, 255);
                break;
        }
        int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
        double fontScale = 0.6;
        int textThickness = 1;

        Imgproc.putText(imageMat, text, textPoint, fontFace, fontScale, textColor, textThickness);

        Utils.matToBitmap(imageMat, bitmap);
        imageMat.release();
    }
}
