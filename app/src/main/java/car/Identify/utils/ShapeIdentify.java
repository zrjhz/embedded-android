package car.Identify.utils;

import static car.car2024.Utils.Image.RectUtils.deepCopyBitmap;
import static car.car2024.Utils.Socket.SendDataUtils.packageOneByte;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import car.Identify.models.ShapeModel;
import car.car2024.ActivityView.FirstActivity;
import car.car2024.ActivityView.XcApplication;
import car.car2024.FragmentView.LeftFragment;
import car.car2024.FragmentView.RightAIFragment;

public class ShapeIdentify {
    public static ShapeModel shapeModel = new ShapeModel();
    public static List<ShapeModel.Obj> shape;
    public static int Bcount;
    public static int ColorMax = 0;
    public static int ShapeMax = 0;
    public static int ShapeColorMax = 0;
    public static Map<String, Integer> shapeColorMap = new HashMap<>();
    public static Map<String, Integer> shapeCountMap = new HashMap<>();
    public static Map<String, Integer> shapeColorAndCountMap = new HashMap<>();
    public static String[] shapeName = {
            "rectangle",//矩形
            "circle",//圆形
            "triangle",//三角形
            "diamond",//菱形
            "pentagon",//五角星
            "trapezoid",//梯形
            "square",//正方形
    };
    public static String[] colorName = {
            "red", "green", "blue",
            "yellow", "magenta", "cyan",
            "black", "white"
    };
    public static String[] colorAndShapeName = {
            "black_circle", "black_diamond", "black_pentagon",
            "black_rectangle", "black_square", "black_trapezoid",
            "black_triangle", "blue_circle", "blue_diamond",
            "blue_pentagon", "blue_rectangle", "blue_square",
            "blue_trapezoid", "blue_triangle", "cyan_circle",
            "cyan_diamond", "cyan_pentagon", "cyan_rectangle",
            "cyan_square", "cyan_trapezoid", "cyan_triangle",
            "green_circle", "green_diamond", "green_pentagon",
            "green_rectangle", "green_square", "green_trapezoid",
            "green_triangle", "magenta_circle", "magenta_diamond",
            "magenta_pentagon", "magenta_rectangle", "magenta_square",
            "magenta_trapezoid", "magenta_triangle", "red_circle",
            "red_diamond", "red_pentagon", "red_rectangle",
            "red_square", "red_trapezoid", "red_triangle",
            "white_circle", "white_diamond", "white_pentagon",
            "white_rectangle", "white_square", "white_trapezoid",
            "white_triangle", "yellow_circle", "yellow_diamond",
            "yellow_pentagon", "yellow_rectangle", "yellow_square",
            "yellow_trapezoid", "yellow_triangle"
    };
    public static double confident = 0.4;

    static {
        for (String s : shapeName) {
            shapeCountMap.put(s, 0);
        }
        for (String s : colorName) {
            shapeColorMap.put(s, 0);
        }
        for (String s : colorAndShapeName) {
            shapeColorAndCountMap.put(s, 0);
        }
    }


    /**
     * 识别图像，并保存个数到内存地址当中
     *
     * @param bitmap 图像
     */
    public static void shapeIdentify(Bitmap bitmap) {
        shapeColorAndCountMap.clear();
        shapeColorMap.clear();
        shapeCountMap.clear();

        XcApplication.executorServicetor.execute(() -> FirstActivity.INSTANCE.runOnUiThread(() -> {
            long startTime = System.currentTimeMillis();
            ShapeModel.Obj[] objs = shapeModel.Detect(bitmap, true);
            Bitmap b = deepCopyBitmap(bitmap);

            List<ShapeModel.Obj> list = new ArrayList<>();
            for (ShapeModel.Obj obj : objs) {
                if (obj.prob > confident) {
                    list.add(obj);
                    String[] t = obj.label.split("_");
                    drawRectangleWithTextOnBitmap(b, obj.x, obj.y, obj.w, obj.h, t[0], t[1]);
                    System.out.println(obj);
                }
            }
            if (!Objects.equals(LeftFragment.show_mode, "static")) {
                RightAIFragment.picrec_iv.setImageBitmap(b);
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            shape = list;
            Bcount = shape.size();
            saveData();
            RightAIFragment.picrectext_tv.setText(shapeColorAndCountMap + "\n用时：" + executionTime + "ms");
        }));
    }

    private static void drawRectangleWithTextOnBitmap(Bitmap bitmap, float x, float y, float width, float height, String color, String text) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bitmap, imageMat);

        Point startPoint = new Point((int) x - 3, (int) y - 3);
        Point endPoint = new Point((int) (x + width + 3), (int) (y + height + 3));
        Scalar c = new Scalar(0, 0, 0); // 红色
        int thickness = 2;

        Imgproc.rectangle(imageMat, startPoint, endPoint, c, thickness);

        Point textPoint = new Point(startPoint.x, startPoint.y - 10); // 文字的位置，位于矩形上方偏移一些距离
        Scalar textColor = new Scalar(0, 0, 0, 255);
        switch (color) {
            case "blue":
                textColor = new Scalar(0, 0, 255, 255);
                break;
            case "magenta":
                textColor = new Scalar(255, 0, 255, 255);
                break;
            case "cyan":
                textColor = new Scalar(0, 255, 255, 255);
                break;
            case "black":
                textColor = new Scalar(255, 255, 255, 255);
                break;
            case "white":
                textColor = new Scalar(0, 0, 0, 255);
                break;
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
     * 储存识别的内容到三个map，一个是图形个数的，一个是颜色个数的，一个包含图形颜色
     */
    private static void saveData() {
        for (String s : shapeName) {
            int count = 0;
            for (ShapeModel.Obj obj : shape) {
                if (obj.label.contains(s)) {
                    count++;
                }
            }
            if (ShapeMax < count) {
                ShapeMax = count;
            }
            shapeCountMap.put(s, count);
        }
        for (String s : colorName) {
            int count = 0;
            for (ShapeModel.Obj obj : shape) {
                if (obj.label.contains(s)) {
                    count++;

                }
            }
            if (ColorMax < count) {
                ColorMax = count;
            }
            shapeColorMap.put(s, count);
        }
        for (String s : colorAndShapeName) {
            int count = 0;
            for (ShapeModel.Obj obj : shape) {
                if (obj.label.contains(s)) {
                    count++;
                }
            }
            if (ShapeColorMax < count) {
                ShapeColorMax = count;
            }
            shapeColorAndCountMap.put(s, count);
        }
        Integer square = shapeCountMap.get("square");
        Integer rectangle = shapeCountMap.get("rectangle");
        if (square != null && rectangle != null) {
            shapeCountMap.put("rectangle", square + rectangle);
        }

        System.out.println("图形个数: " + shapeCountMap);
        System.out.println("图形颜色个数: " + shapeColorMap);
        System.out.println("图形图像颜色个数: " + shapeColorAndCountMap);
    }

    /**
     * 发送图形个数
     *
     * @param shapeInt 图形名字下标
     */
    public static void sendShapeCount(int shapeInt) {
        Integer count = shapeCountMap.get(shapeName[shapeInt]);
        Log.i(shapeName[shapeInt] + " 形状 ", String.valueOf(count));
        System.out.println(shapeName[shapeInt] + " 形状 " + count);
        packageOneByte(count);
    }

    /**
     * 发送颜色个数
     *
     * @param colorInt 颜色名字下标
     */
    public static String sendShapeColor(int colorInt) {
        Integer count = shapeColorMap.get(colorName[colorInt]);
        Log.i(colorName[colorInt] + " 形状 ", String.valueOf(count));
        System.out.println(colorName[colorInt] + " color " + count);
        packageOneByte(count);
        return String.valueOf(count);
    }

    /**
     * 发送图形颜色个数
     *
     * @param shapeInt 图像下标
     * @param colorInt 颜色下标
     */
    public static void sendShapeColorNumber(int shapeInt, int colorInt) {
        String color = colorName[colorInt];
        String shape = shapeName[shapeInt];
        String colorShape = color + "_" + shape;
        Integer count = shapeColorAndCountMap.get(colorShape);
        Log.i("count", String.valueOf(count));
        System.out.println("count" + count);
        packageOneByte(count);
    }

    /**
     * 统计出现形状的种类
     */
    public static int shapeTypeNumber() {
        int count = 0;
        Collection<Integer> values = shapeCountMap.values();
        for (Integer value : values) {
            if (value != 0) count++;
        }
        return count;
    }

    /**
     * 统计出颜色种类
     */
    public static int colorTypeNumber() {
        int count = 0;
        Collection<Integer> values = shapeColorMap.values();
        for (Integer value : values) {
            if (value != 0) count++;
        }
        return count;
    }
}
