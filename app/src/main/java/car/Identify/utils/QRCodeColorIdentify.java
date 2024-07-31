package car.Identify.utils;

import android.graphics.Bitmap;

import car.Identify.models.QRCodeColorModel;

public class QRCodeColorIdentify {
    public static QRCodeColorModel qrCodeColorModel = new QRCodeColorModel();

    public static String qrCodeColorIdentify(Bitmap bitmap) {
        return qrCodeColorModel.detect(bitmap);
    }
}
