package car.car2024.Utils.Image;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Rgb2hsv {
    static {
        System.loadLibrary("yolov5ncnn");
    }

    // 调整色相值的JNI方法
    public native void adjustHue(long matAddr, int hueAdjustment);

    // 调用这个方法来处理Bitmap
    public void processImage(Mat mat, int hueAdjustment) {
        if (mat.type() != CvType.CV_8UC3) {
            Mat colorMat = new Mat();
            Imgproc.cvtColor(mat, colorMat, Imgproc.COLOR_GRAY2RGB);
            adjustHue(colorMat.getNativeObjAddr(), hueAdjustment);
            mat.release();
            colorMat.copyTo(mat);
        } else {
            adjustHue(mat.getNativeObjAddr(), hueAdjustment);
        }
    }
}
