package im.zhy.data.dispose;

/**
 * @author zhy
 * @create_date 2019-04-27 10:48
 */

import car.Identify.utils.QRCodeIdentify;
import car.car2024.Utils.Algorithm.DataHandle;
import car.car2024.Utils.Socket.Variable;
import im.zhy.param.ShapeName;
import im.zhy.util.ShapeColorUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.zxing.Result;

/**
 * 静态标志物 1 数据处理类
 * 应先调用 dispose() 将要用的数据存放在成员变量中，useOne() 和 useTwo() 对数据进行二次处理最后返回
 */


@SuppressWarnings("all")
public class StaticLandmarkOne {

    public static String my;

    private static byte[] bytes = new byte[6];

    private static final int INDEX = 1;

//    public static void dispose(){
//
//        boolean isQRCodeData = StaticLandmark.getIsQRCodeData(INDEX);
//
//        if (isQRCodeData){
//            String s = CarData.QRCodeMap.get(INDEX);
//
//            if (s == null || s.trim().length() == 0){
//               return;
//            }else {
//                qrCodeDispose(s);
//            }
//
//        }else {
////            shapeColorDispose();
//        }
//
//    }

//    public static void dispose(){
//
//        int index = INDEX;
//        boolean b = true;
//
//        for (int i = 0; i < 4; i++) {
//            if (i > 1 && b){
//                index = index == 1 ? 2 : 1;
//                b = false;
//
//            }
//            if (i % 2 == 0){
//                String s = CarData.QRCodeMap.get(index);
//                System.out.println("获取QRCode index：：" + index);
//                if (s == null || s.trim().length() == 0){
//                    continue;
//                }else {
//                    qrCodeDispose(s);
//                    break;
//                }
//            }else {
//                int[][] shapeColor = StaticShapeDector.getData(index);
//                System.out.println("获取shapeColor index：：" + index);
//                if (shapeColor == null){
//                    continue;
//                }else {
//                    shapeColorDispose(shapeColor);
//                    break;
//                }
//            }
//        }
//    }

    static int number = 0;

    // 二维码数据处理
    private static void qrCodeDispose(String s) {
//        s = StrExtractUtils.trim(s);
        s = s.toUpperCase();
        String[] ss = s.split(">");

        my = ss[1];

        for (String s1 : ss) {
            System.out.println(s1);
        }

        String[] arrays = ss[0].split("\\|");

        for (int i = 0; i < arrays.length; i++) {
//            arrays[i] = StrExtractUtils.inRegexGetGroupStr(arrays[i], "[A-Z0-9]{1,}");

            System.out.println(arrays[i]);
        }

        bytes = new byte[6];

        for (int i = 1; i < bytes.length - 1; i++) {
            bytes[i] = (byte) Integer.parseInt(arrays[i], 16);
        }

        bytes[0] = (byte) Integer.parseInt(arrays[0].substring(0, 2), 16);
        bytes[5] = (byte) Integer.parseInt(arrays[5].substring(0, 2), 16);


        for (byte aByte : bytes) {
            System.out.println(Integer.toHexString(aByte & 0xff));
        }


    }


    // 图形数据处理
    private static void shapeColorDispose(int[][] shapeColor) {
        System.out.println("静态标志物：" + INDEX + " 图形");

        ShapeColorUtils.setShapeColor(shapeColor);


        int colorTypeNumber = ShapeColorUtils.colorTypeNumber();
        int statNumber = ShapeColorUtils.shapeNumberByName(ShapeName.STAR);
        System.out.println("statNumber = " + statNumber);

        bytes = new byte[]{(byte) colorTypeNumber, (byte) statNumber};

    }


    // 用途 1
    public static byte[] useOne() {
        String yellow = QRCodeIdentify.qrcodeUsingColorMap.get("yellow");
        String red = QRCodeIdentify.qrcodeUsingColorMap.get("red");
        String code = "";
        if (red != null) {
            code += red;
        }
        if (yellow != null)
            code += yellow;
        return DataHandle.getResult(code);
//        return playfairCipher(MessageData.QRCode[0]);
    }

    // 用途 2
    public static byte[] useTwo() throws UnsupportedEncodingException {
        // 主车入库坐标
//        int n = ((number*5)%6)+1;
//
//
//        String s = "1    竞赛平台入库坐标：A2\n" +
//                "2    竞赛平台入库坐标：A4\n" +
//                "3    竞赛平台入库坐标：A6\n" +
//                "4    竞赛平台入库坐标：B7\n" +
//                "5    竞赛平台入库坐标：D7\n";
//
//
//        String coord = StrExtractUtils.autoCreateCoord(s, n);
        Result[] s = Variable.QRMAP.get(2);
        Pattern p = Pattern.compile("富|强|民|主");
        Matcher m = p.matcher(s[0].getText());
        String str = "";
        while (m.find()) {
            str += m.group(0);
        }
        Pattern p1 = Pattern.compile("");
        Matcher m1 = p1.matcher(s[0].getText());
        while (m1.find()) {
            str += m1.group(0);
        }
        Pattern p2 = Pattern.compile("谐|和|友|善");
        Matcher m2 = p2.matcher(s[0].getText());
        while (m2.find()) {
            str += m2.group(0);
        }
        str += "\0";
        System.out.println("二维码返回=========" + str);
        return str.getBytes("GBK");

    }


//    public static void main(String[] args) {
//       CarData.QRCodeMap.put(1, "<013|B2|C3|D4|E5|F6C><A1B2C3D4>");
//       dispose();
//        byte[] bytes = useOne();
//        for (byte aByte : bytes) {
//            System.out.println(Integer.toHexString(aByte  & 0xff));
//        }
//        System.out.println(my);
//    }

}
