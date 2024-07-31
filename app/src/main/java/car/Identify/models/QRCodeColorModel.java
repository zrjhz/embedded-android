package car.Identify.models;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

public class QRCodeColorModel {
    public static String[] class_names = {
            "black", "blue", "green", "red", "yellow"
    };

    //    public static LicensePlateColorModel licensePlateColorModel = new LicensePlateColorModel();
    public native boolean init(AssetManager mgr);

    public native int Detect(Bitmap bitmap);

    public String detect(Bitmap bitmap) {
        int detect = Detect(bitmap);
        if (detect == -1) {
            return "";
        }
        return class_names[detect];
    }

    static {
        System.loadLibrary("yolov5ncnn");
    }
}
