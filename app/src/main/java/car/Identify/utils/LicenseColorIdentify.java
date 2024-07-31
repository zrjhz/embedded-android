package car.Identify.utils;

import android.graphics.Bitmap;

import car.Identify.models.LicenseColorModel;

public class LicenseColorIdentify {
    public static LicenseColorModel licenseColorModel = new LicenseColorModel();

    public static String licenseColorIdentify(Bitmap bitmap) {
        return licenseColorModel.detect(bitmap);
    }
}
