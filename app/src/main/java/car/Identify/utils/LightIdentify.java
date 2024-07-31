package car.Identify.utils;

import static car.car2024.Utils.Image.RectUtils.deepCopyBitmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import car.Identify.constant.LightConstant;
import car.Identify.models.LightModel;
import car.car2024.ActivityView.XcApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import car.car2024.ActivityView.FirstActivity;
import car.car2024.FragmentView.LeftFragment;
import car.car2024.FragmentView.RightAIFragment;
import im.drh.utils.ToastUtil;
import car.car2024.Utils.Socket.ZigbeeService;

/**
 * 交通灯识别
 */
public class LightIdentify {
    public static LightModel lightModel = new LightModel();

    public static List<LightModel.Obj> light;
    public static int TRAFFIC_LIGHT = 1;
    public static void lightIdentify(Bitmap bitmap) {
        TRAFFIC_LIGHT = 1;

        LightModel.Obj[] objs = lightModel.Detect(bitmap, true);
        Bitmap b = deepCopyBitmap(bitmap);

        System.out.println("light:" + objs.length);
        List<LightModel.Obj> list = new ArrayList<>();
        for (LightModel.Obj obj : objs) {
            list.add(obj);
            drawRectangleWithTextOnBitmap(b, obj.x, obj.y, obj.w, obj.h, obj.label);
            System.out.println(obj);
        }
        if (!Objects.equals(LeftFragment.show_mode, "static")){
            RightAIFragment.picrec_iv.setImageBitmap(b);
        }
        light = list;
    }

    /**
     * 接收小车识别交通灯请求时调用这个方法，发送交通灯代码，自带识别
     *
     * @param bitmap 图片
     */
    @SuppressLint("SetTextI18n")
    public static void sendLightCode(Bitmap bitmap, byte type) {
        lightIdentify(bitmap);

        String label = LightConstant.YELLOW;
        float confident = 0;
        for (LightModel.Obj obj : light) {
            if (obj.prob > confident) {
                confident = obj.prob;
                label = obj.label;
            }
        }
        RightAIFragment.picrectext_tv.setText("识别交通灯为: " + label);
        int lightNum = lightNum(label);
        ZigbeeService.sendTrafficResult(lightNum, type);
        FirstActivity.ConnectTransport.light_traffice(lightNum);
    }

    private static void drawRectangleWithTextOnBitmap(Bitmap bitmap, float x, float y, float width, float height, String text) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bitmap, imageMat);

        Point startPoint = new Point((int) x - 3, (int) y - 3);
        Point endPoint = new Point((int) (x + width + 3), (int) (y + height + 3));
        Scalar color = new Scalar(0, 0, 0); // 红色
        int thickness = 2;

        Imgproc.rectangle(imageMat, startPoint, endPoint, color, thickness);

        Point textPoint = new Point(startPoint.x, startPoint.y - 10); // 文字的位置，位于矩形上方偏移一些距离
        Scalar textColor = new Scalar(0, 0, 0, 255);
        switch (text) {
            case "red":
                textColor = new Scalar(255, 0, 0, 255);
                break;
            case "yellow":
                textColor = new Scalar(255, 255, 0, 255);
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

    /**
     * 红路灯对应数值
     *
     * @param color
     * @return
     */
    public static int lightNum(String color) {
        switch (color) {
            case LightConstant.GREEN:
                TRAFFIC_LIGHT = 0x02;
                return 0x02;
            case LightConstant.RED:
                TRAFFIC_LIGHT = 0x01;
                return 0x01;
            case LightConstant.YELLOW:
                TRAFFIC_LIGHT = 0x03;
                return 0x03;
            default:
                return 0x01;
        }
    }
}
