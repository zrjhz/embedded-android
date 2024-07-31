package im.zhy.data.dispose;


import im.hdy.CarData;
import im.zhy.util.GetVarName;
import im.zhy.util.ShapeColorUtils;

import java.util.List;

/**
 * @author zhy
 * @create_date 2019-04-27 10:54
 */

@SuppressWarnings("all")
public class RFID {

    // 根据比赛规则进行数据处理
    public static byte[] dispose(String s, int index) {

        System.out.println("RFID " + index + " :" + s);

        byte[] data = null;

        // 进行相应的业务处理
        switch (index) {
            case 1:
                data = rfidDispose1(s);
                break;
            case 2:
                data = rfidDispose2(s);
                break;
            case 3:
                data = rfidDispose3(s);
                break;
        }

        // 将处理后的结果返回
        return data;
    }

    public static byte[] dispose(int index) {
        int n = index;
        String rfid_data = "";
        for (int i = index; i < index + 3; i++) {
            if (n > 3) {
                n = 1;
            }
            System.out.println("n = " + n);
            rfid_data = CarData.rfidMap.get(n);
            System.out.println("rfid_data = " + rfid_data);

            if (rfid_data != null && rfid_data.trim().length() > 0) {
                break;
            }

            n++;
        }

        byte[] data = null;

        if ("".equals(rfid_data)) {
            return data;
        }

        // 进行相应的业务处理
        switch (index) {
            case 1:
                data = rfidDispose1(rfid_data);
                break;
            case 2:
                data = rfidDispose2(rfid_data);
                break;
            case 3:
                data = rfidDispose3(rfid_data);
                break;
        }

        return data;

    }


    private static byte[] rfidDispose1(String s) {

//        s = StrExtractUtils.trim(s);

        String[] ss = s.split("\\|");

        byte[] bytes = new byte[4];

        for (String s1 : ss) {
            if (s1 == null || "".equals(s1.trim())){
                continue;
            }
//            List<String> list = StrExtractUtils.inRegexGetGroupStrs(s1, "[0-9]{2}-{1}[0-9]{1}");
            String s3 = s1.split("/")[1];
            String[] list = s3.split("-");
            for (String s2 : list) {
                System.out.println(s2);
            }
            if(s1.startsWith("2")){
                for (int i = 0; i < 2; i++) {
                    bytes[i] = (byte) Integer.parseInt(list[i]);
                }
            }else {
                for (int i = 2; i < 4; i++) {
                    bytes[i] = (byte) Integer.parseInt(list[i - 2]);
                }
            }
        }


        System.out.println();
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }

        return bytes;
    }


    private static int color(int x) {
        String[] colors = new String[]{"红色", "黄色", "蓝色", "青色", "品红色", "绿色", "黑色"};

        int index = GetVarName.getColorIntByChinese(colors[x - 1]);

        return ShapeColorUtils.colorNumber(index);
    }


    private static int shape(int y) {
        String[] shapes = new String[]{"五角星", "菱形", "三角形", "矩形", "圆形"};

        int index = GetVarName.getShapeIntByChinese(shapes[y - 1]);

        return ShapeColorUtils.shapeNumber(index);
    }


    private static byte[] rfidDispose2(String s) {
//        s = StrExtractUtils.trim(s);

//        List<Integer> number = StrExtractUtils.getNumber(s);

        int n = 0;

//        for (Integer integer : number) {
//            if (integer % 2 != 0) {
//                n += integer;
//            }
//        }


        n = n % 4 + 1;

        return new byte[]{(byte) n};


    }

    private static byte[] rfidDispose3(String s) {
        return s.getBytes();
    }


    public static void main(String[] args) {

        CarData.rfidMap.put(1, "|3/05-1||2/03-2|");
        dispose(1);
    }
}
