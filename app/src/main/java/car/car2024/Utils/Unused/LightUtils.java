package car.car2024.Utils.Unused;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class LightUtils {
    public static Rect roi;
    public static Double width;
    private static ArrayList<Double> area;


    public LightUtils() {
        super();
    }

    public LightUtils(Double width) {
        super();
        this.width = width;
    }

    public static Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public static void light(Bitmap bitmap) {

        // TODO Auto-generated method stub
        MatOfPoint2f approxCurve = null;

        Mat img = new Mat();
        Utils.bitmapToMat(bitmap, img);

        // 灰度
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

        // 高斯平滑,5×5 内核的高斯平滑
        Mat Blur = new Mat();
        Imgproc.GaussianBlur(gray, Blur, new Size(5, 5), 0);

        // 阈值化
        Mat thresh = new Mat();
        Imgproc.threshold(gray, thresh, 190, 255, Imgproc.THRESH_BINARY);


        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat cannyEdges = new Mat();
        Imgproc.Canny(thresh, cannyEdges, 50, 100, 3, true);

        Utils.matToBitmap(cannyEdges, bitmap);

        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyEdges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Rect> rect = new ArrayList<Rect>(contours.size());

        RotatedRect rotatedRect = new RotatedRect();
        Point[] point = new Point[4];

        area = new ArrayList<Double>();
        for (MatOfPoint matOfPoint : contours) {

            MatOfPoint2f points = new MatOfPoint2f(matOfPoint.toArray());
            rotatedRect = Imgproc.minAreaRect(points);

            // MatOfPoint2f temp = new
            // MatOfPoint2f(matOfPoint.toArray());
            // Imgproc.approxPolyDP(temp, approxCurve,
            // Imgproc.arcLength(temp, true) * 0.02, true);

            rect.add(Imgproc.boundingRect(matOfPoint));
            rotatedRect.points(point);
            roi = Imgproc.boundingRect(matOfPoint);

            // Imgproc.line(cannyEdges, point[i], point[(i + 1) %
            // 4], new
            // Scalar(255, 0, 0), 20);

            Imgproc.rectangle(cannyEdges, new Point(roi.x, roi.y),
                    new Point(roi.x + roi.width, roi.y + roi.height), new Scalar(255, 0, 0), 2, 8, 0);

            area.add((double) roi.width);
            Log.i("area", "a:"+roi.width);
        }
        Utils.matToBitmap(cannyEdges, bitmap);

        for (int i = 0; i < area.size(); i++) {
            if(area.get(i)>150){
                area.remove(i);
                i--;
                Log.i("area", "cg"+roi.width);
            }
        }
        width =Collections.max(area);
    }

}
