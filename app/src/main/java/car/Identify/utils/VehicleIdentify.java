package car.Identify.utils;


import android.graphics.Bitmap;

import car.Identify.models.VehicleModel;
import car.car2024.ActivityView.XcApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.drh.utils.ToastUtil;

public class VehicleIdentify {
    public static int VEHICLE;
    public static VehicleModel vehicleModel = new VehicleModel();
    public static List<VehicleModel.Obj> vehicle;
    public static Map<String, Integer> vehicleColorMap = new HashMap<>();
    public static Map<String, Integer> vehicleNumberMap = new HashMap<>();
    public static String[] colorName = {"black", "blue", "green", "grey", "orange", "purple", "red",
            "white", "yellow"};
    public static String[] vehicleName = {"bicycle", "motorcycle", "car", "truck"};

    static {
        for (String s : colorName) {
            vehicleColorMap.put(s, 0);
        }
        for (String s : vehicleName) {
            vehicleNumberMap.put(s, 0);
        }
    }

    /**
     * 从图片中识别车辆
     *
     * @param bitmap 图片
     */
    public static void vehicleIdentify(Bitmap bitmap) {
        vehicleColorMap.clear();
        vehicleNumberMap.clear();
        VehicleModel.Obj[] objs = vehicleModel.Detect(bitmap, true);
        System.out.println("vehicle:" + objs.length);
        List<VehicleModel.Obj> list = new ArrayList<>();
        VehicleModel.Obj v = null;
        float confident = 0;
        for (VehicleModel.Obj obj : objs) {
            list.add(obj);
            System.out.println(obj);
            if (obj.prob > confident) {
                v = obj;
                confident = obj.prob;
            }
        }
        if (v != null) {
            ToastUtil.showToast(XcApplication.getApp(), v.label);
        }
        vehicle = list;
        saveData();
        getLabel(v.label);
    }

    /**
     * 保存数据
     */
    public static void saveData() {
        for (String s : vehicleName) {
            int count = 0;
            for (VehicleModel.Obj obj : vehicle) {
                if (obj.label.contains(s)) {
                    count++;
                }
            }
            vehicleNumberMap.put(s, count);
        }
        for (String s : colorName) {
            int count = 0;
            for (VehicleModel.Obj obj : vehicle) {
                if (obj.label.contains(s)) {
                    count++;
                }
            }
            vehicleColorMap.put(s, count);
        }
        System.out.println("车辆个数: " + vehicleNumberMap);
        System.out.println("车辆颜色个数: " + vehicleColorMap);
    }

    /**
     * 根据识别结果返回协议，可与主车自行商量，或者采用这一套
     *
     * @param label
     */
    public static void getLabel(String label) {
        for (int i = 0; i < 2; i++) {
            switch (label) {
                case "bicycle":
                    VEHICLE = 0x01;
                    break;
                case "motorcycle":
                    VEHICLE = 0x02;
                    break;
                case "car":
                    VEHICLE = 0x03;
                    break;
                case "truck":
                    VEHICLE = 0x04;
                    break;
                default:
                    if (label.contains("car") || label.contains("truck")) {
                        Pattern compile = Pattern.compile("car|truck");
                        Matcher matcher = compile.matcher(label);
                        if (matcher.find()) {
                            label = matcher.group();
                        }
                    }
            }
        }
    }
}
