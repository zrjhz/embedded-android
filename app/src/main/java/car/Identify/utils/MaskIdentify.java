package car.Identify.utils;

import static car.car2024.Utils.Socket.SendDataUtils.packageOneByte;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import car.Identify.models.MaskModel;

public class MaskIdentify {
    public static MaskModel maskModel = new MaskModel();
    public static List<MaskModel.Obj> mask;
    public static Map<String, Integer> maskMap = new HashMap<>();
    public static int Bcount;
    public static String[] maskName = {"no_mask", "mask"};

    static {
        for (String s : maskName) {
            maskMap.put(s, 0);
        }
    }

    public static void maskIdentify(Bitmap bitmap) {
        MaskModel.Obj[] objs = maskModel.Detect(bitmap, true);
        System.out.println("mask:" + objs.length);
        List<MaskModel.Obj> list = new ArrayList<>();
        for (MaskModel.Obj obj : objs) {
            list.add(obj);
            System.out.println(obj);
        }
        mask = list;
        Bcount = mask.size();
        saveData();
    }

    public static void saveData() {
        for (String s : maskName) {
            int count = 0;
            for (MaskModel.Obj obj : mask) {
                if (obj.label.contains(s)) {
                    count++;
                }
            }
            maskMap.put(s, count);
        }
    }

    public static void getMaskCount(int index) {
        Integer count = maskMap.get(maskName[index]);
        Log.i(maskName[index] + " 口罩 ", String.valueOf(count));
        System.out.println(maskName[index] + " 口罩 " + count);
        packageOneByte(count);
    }
}
