package car.Identify.models;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;

public class QRCodeModel {
    public native boolean Init(AssetManager mgr);

    public class Obj {
        public float x;
        public float y;
        public float w;
        public float h;
        public String label;
        public float prob;

        @NonNull
        @Override
        public String toString() {
            return "Obj{" +
                    "x=" + x +
                    ", y=" + y +
                    ", w=" + w +
                    ", h=" + h +
                    ", label='" + label + '\'' +
                    ", prob=" + prob +
                    '}';
        }
    }

    public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);

    static {
        System.loadLibrary("yolov5ncnn");
    }
}