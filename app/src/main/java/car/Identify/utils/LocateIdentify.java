package car.Identify.utils;

import android.graphics.Bitmap;

import car.car2024.ActivityView.XcApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import car.Identify.UsingUtil;
import car.Identify.constant.IdentifyConstant;
import car.Identify.constant.LicenseColorConstant;
import car.Identify.models.LocateModel;
import im.drh.utils.ToastUtil;

public class LocateIdentify {
    public static LocateModel locateModel = new LocateModel();
    public static List<LocateModel.Obj> locate;

    public static void licenseLocateIdentify(Bitmap bitmap) {
        LocateModel.Obj[] objs = locateModel.Detect(bitmap, true);
        System.out.println("licenseLocate: " + objs.length);
        List<LocateModel.Obj> list = new ArrayList<>();
        for (LocateModel.Obj obj : objs) {
            list.add(obj);
            System.out.println(obj);
        }
        locate = list;
    }

    public static void test(Bitmap bitmap) {
        licenseLocateIdentify(bitmap);
        Set<Bitmap> set = new HashSet<>();
        for (LocateModel.Obj obj : locate) {
            set.add(cutLicense(bitmap, obj));
        }
        for (Bitmap bitmap1 : set) {
            String s = LicenseColorIdentify.licenseColorIdentify(bitmap1);
            if (s.equals(LicenseColorConstant.GREEN)) {
                UsingUtil.tftMethodUsing(bitmap1, IdentifyConstant.LICENSE);
                ToastUtil.showToast(XcApplication.getApp(), s);
            }
        }
    }

    public static Bitmap cutLicense(Bitmap bitmap, LocateModel.Obj obj) {
        int x = (int) obj.x;
        int y = (int) obj.y;
        int w = (int) obj.w;
        int h = (int) obj.h;
        int enlargedWidth = (int) (w * 1.1);
        int enlargedHeight = (int) (h * 1.1);
        x = x - (enlargedWidth - w) / 2;
        y = y - (enlargedHeight - h) / 2;
        w = enlargedWidth;
        h = enlargedHeight;
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }
}

