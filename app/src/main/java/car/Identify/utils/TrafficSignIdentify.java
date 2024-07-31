package car.Identify.utils;

import static car.car2024.Utils.Image.RectUtils.deepCopyBitmap;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Objects;

import car.car2024.ActivityView.XcApplication;

import car.Identify.models.TrafficSignModel;
import car.car2024.FragmentView.LeftFragment;
import car.car2024.FragmentView.RightAIFragment;
import im.drh.utils.ToastUtil;

public class TrafficSignIdentify {
    public static TrafficSignModel trafficSignModel = new TrafficSignModel();
    public static int TRAFFIC_SIGN;
    public static int TRAFFIC_COUNT;
    public static String[] trafficSignName = {
            "go_straight", "left", "no_entry", "no_left", "no_right",
            "no_straight", "right", "speed_limit", "turn", "no_turn"
    };

    /**
     * 识别获得置信度最高的label
     *
     * @param bitmap 图像
     */
    public static void trafficSignIdentify(Bitmap bitmap) {
        TRAFFIC_SIGN = 0;
        TRAFFIC_COUNT = 0;

        TrafficSignModel.Obj[] objs = trafficSignModel.Detect(bitmap, true);
        Bitmap b = deepCopyBitmap(bitmap);

        System.out.println("trafficSign:" + objs.length);
        double confident = 0;
        String label = "";
        for (TrafficSignModel.Obj obj : objs) {
            if (obj.prob > confident) {
                confident = obj.prob;
                label = obj.label;
                drawRectangleWithTextOnBitmap(b, obj.x, obj.y, obj.w, obj.h, obj.label);
            }
        }
        if (!Objects.equals(LeftFragment.show_mode, "static")) {
            RightAIFragment.picrec_iv.setImageBitmap(b);
        }
        getLabel(label);
        RightAIFragment.picrectext_tv.setText("识别交通信号为: " + label);
    }

    private static void drawRectangleWithTextOnBitmap(Bitmap bitmap, float x, float y, float width, float height, String text) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bitmap, imageMat);

        Point startPoint = new Point((int) x - 3, (int) y - 3);
        Point endPoint = new Point((int) (x + width + 3), (int) (y + height + 3));
        Scalar c = new Scalar(0, 0, 0); // 红色
        int thickness = 2;

        Imgproc.rectangle(imageMat, startPoint, endPoint, c, thickness);

        Point textPoint = new Point(startPoint.x, startPoint.y - 10); // 文字的位置，位于矩形上方偏移一些距离
        Scalar textColor = new Scalar(0, 0, 0, 255);
        int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
        double fontScale = 0.6;
        int textThickness = 1;

        Imgproc.putText(imageMat, text, textPoint, fontFace, fontScale, textColor, textThickness);

        Utils.matToBitmap(imageMat, bitmap);
        imageMat.release();
    }

    /**
     * 计算协议，与负责主车的人自行商量，可以采用这一套//TODO
     *
     * @param label 输入label确认置信度
     */
    private static void getLabel(String label) {
        if (label == null) {
            TRAFFIC_SIGN = 0x00;
            return;
        }
        switch (label) {
            case "go_straight":
                TRAFFIC_SIGN = 0x01;
                break;
            case "left":
                TRAFFIC_SIGN = 0x02;
                break;
            case "right":
                TRAFFIC_SIGN = 0x03;
                break;
            case "turn":
                TRAFFIC_SIGN = 0x04;
                break;
            case "no_entry":
                TRAFFIC_SIGN = 0x05;
                break;
            case "no_left":
                TRAFFIC_SIGN = 0x06;
                break;
            case "no_right":
                TRAFFIC_SIGN = 0x07;
                break;
            case "no_turn":
                TRAFFIC_SIGN = 0x08;
                break;
            case "speed_limit":
                TRAFFIC_SIGN = 0x09;
                break;
        }
    }
}
