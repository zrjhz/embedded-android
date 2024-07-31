package car.Identify.utils;

import android.graphics.Bitmap;

import java.util.List;

import car.Identify.models.VehicleModel;
import car.Identify.utils.socketUtil.OcrSocketClient;
import car.car2024.ActivityView.XcApplication;
import car.car2024.FragmentView.RightAIFragment;
import im.drh.utils.ToastUtil;

public class OcrLisenceIdentify {

    /**
     * 识别在对应车上的车牌
     *
     * @param bitmap      图片
     * @param vehicleName 车辆常量
     */
    public static void licenseOnVehicle(Bitmap bitmap, String vehicleName) {
        VehicleIdentify.vehicleIdentify(bitmap);
        List<VehicleModel.Obj> vehicle = VehicleIdentify.vehicle;
        float confident = 0;
        VehicleModel.Obj v = null;
        for (VehicleModel.Obj obj : vehicle) {
            if (obj.label.contains(vehicleName) && obj.prob > confident) {
                v = obj;
                confident = obj.prob;
            }
        }
        if (v == null) return;
//        ToastUtil.showToast(XcApplication.getApp(),v.label);
        Bitmap bitmap1 = null;

        if (!v.label.contains("car") || !v.label.contains("truck")) {
            bitmap1 = Bitmap.createBitmap(bitmap, (int) v.x, (int) v.y, (int) v.w, (int) v.h);
        } else {
            int x = (int) v.x, y = (int) v.y, w = (int) v.w, h = (int) v.h;
            if (v.x > 20) x = (int) v.x - 20;
            if (v.w < 620) w = (int) v.w + 20;
            bitmap1 = Bitmap.createBitmap(bitmap, x, y, w, h);
        }
        licenseIdentify(bitmap1);
        ToastUtil.showToast(XcApplication.getApp(), LicenseIdentify.licenseString);
    }

    public static void licenseIdentify(Bitmap bitmap) {
        LicenseIdentify.licenseString = "";

        long startTime = System.currentTimeMillis();

        new Thread(new OcrSocketClient(bitmap)).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        RightAIFragment.picrectext_tv.setText(LicenseIdentify.licenseString + "\n用时：" + executionTime + "ms");
    }
}
