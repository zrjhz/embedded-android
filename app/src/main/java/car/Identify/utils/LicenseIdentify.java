package car.Identify.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import car.Identify.models.LicenseModel;

public class LicenseIdentify {
    public static LicenseModel licenseModel = new LicenseModel();
    public static List<LicenseModel.Obj> license = new ArrayList<>();
    public static String licenseString = "";
    public static String[] licenseArrays;

    public static void licenseIdentify(Bitmap bitmap) {
        LicenseModel.Obj[] objs = licenseModel.Detect(bitmap, true);
        System.out.println("license:" + objs.length);
        List<LicenseModel.Obj> list = new ArrayList<>();
        for (LicenseModel.Obj obj : objs) {
            list.add(obj);
            System.out.println(obj);
        }
        license = list;
        license.sort((obj1, obj2) -> Float.compare(obj1.x, obj2.x));
    }

    /**
     * 拼接车牌字符串
     *
     * @return 车牌
     */
    public static String licenseCut() {
        LicenseModel.Obj[] objs = new LicenseModel.Obj[6];
        int count = 1;
        int range = 40 >> 1;
        for (int i = 0; i < license.size(); i++) {
            objs[0] = license.get(i);
            count = 1;
            boolean flag = false;
            if (!license.get(i).label.equals("-")) {
                for (int j = i + 1; j < license.size(); j++) {
                    if (license.get(i).y + range > license.get(j).y
                            && license.get(i).y - range < license.get(j).y
                            && !license.get(j).label.equals("-"))
                        objs[count++] = license.get(j);
                    if (count == 6) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    break;
            }
        }
        try {
            System.out.println(Arrays.toString(objs));
            Arrays.sort(objs, (obj1, obj2) -> Float.compare(obj1.x, obj2.x));
            StringBuffer sb = new StringBuffer();
            for (LicenseModel.Obj obj : objs) {
                sb.append(obj.label);
            }
            licenseString = sb.toString();
            return sb.toString();
        } catch (
                Exception e) {
            System.out.println("长度不足6");
        }
        return "";
    }

    /**
     * 拼接车牌字符串
     *
     * @return 车牌
     */
    public static String licenseCut(Bitmap bitmap) {
        for (int radio = 0; radio >= -90; radio -= 15) {
            if (radio != 0) {
                Matrix matrix = new Matrix();
                matrix.setRotate(radio, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, false);
                license.clear();
                licenseIdentify(bitmap2);
            }
            LicenseModel.Obj[] objs = new LicenseModel.Obj[6];
            int count = 1;
            int range = 40 >> 1;
            for (int i = 0; i < license.size(); i++) {
                objs[0] = license.get(i);
                count = 1;
                boolean flag = false;
                if (!license.get(i).label.equals("-")) {
                    for (int j = i + 1; j < license.size(); j++) {
                        if (license.get(i).y + range > license.get(j).y
                                && license.get(i).y - range < license.get(j).y
                                && !license.get(j).label.equals("-"))
                            objs[count++] = license.get(j);
                        if (count == 6) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag)
                        break;
                }
            }
            try {
                System.out.println(Arrays.toString(objs));
                Arrays.sort(objs, (obj1, obj2) -> Float.compare(obj1.x, obj2.x));
                StringBuffer sb = new StringBuffer();
                for (LicenseModel.Obj obj : objs) {
                    sb.append(obj.label);
                }
                return sb.toString();
            } catch (
                    Exception e) {
                System.out.println("长度不足6");
            }
        }
        return "";
    }
}
