package im.zhy.data.dispose;

/**
 * @author zhy
 * @create_date 2019-04-27 10:49
 */

import im.fyhz.utils.May8.May8Base64;
import im.hdy.CarData;

import im.zhy.param.ShapeName;
import im.zhy.util.ShapeColorUtils;

/**
 * 静态标志物 2 数据处理类
 * 应先调用 dispose() 将要用的数据存放在成员变量中，useOne() 和 useTwo() 对数据进行二次处理最后返回
 */
@SuppressWarnings("all")
public class StaticLandmarkTwo {

    static byte[] bytes = new byte[6];

    private static final int INDEX = 2;

//    public static void dispose(){
//        boolean isQRCodeData = StaticLandmark.getIsQRCodeData(INDEX);
//
//        if (isQRCodeData){
//            String s = CarData.QRCodeMap.get(INDEX);
//
//            if (s == null || s.trim().length() == 0){
//                return;
//            }else {
//                qrCodeDispose(s);
//            }
//
//        }else {
//            shapeColorDispose();
//        }
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
//
//        }
//    }



    // 二维码数据处理
    private static void qrCodeDispose(String s){

//        s = StrExtractUtils.trim(s);

        System.out.println("静态标志物：" + INDEX + " 二维码：" + s);

    }



    // 图形数据处理
    private static void shapeColorDispose(int[][] shapeColor) {
        System.out.println("静态标志物：" + INDEX + " 图形");

        ShapeColorUtils.setShapeColor(shapeColor);

        int i = ShapeColorUtils.shapeNumberByName(ShapeName.STAR);

        System.out.println("star::" + i);

    }

    // 用途 1
//    public static byte[] useOne(){
//        // 数据校验，用于测试
//        System.out.println("MessageData.QRCode[0]:"+MessageData.QRCode[0]);
////        System.out.println(CarData.QRCodeMap.get(1));
//        // 数据获取
//        String raw = MessageData.QRCode[0];
//        // 处理并返回结果
////        return algorithm.HillCipher(MessageData.QRCode[0]).getBytes();
//        System.out.println(May8Base64.Base64(raw));//.getBytes());
//        return May8Base64.Base64(raw).getBytes();
////        return bytes;
//    }
//
//    public static String[] bu = "123456789QWERTYUIOPASDFGHJKLZXCVBNM".split("");
//    // 用途 2
//    public static byte[] useTwo(){
//        // 数据校验，用于测试
//        System.out.println("MessageData.QRCode[1]:"+MessageData.QRCode[1]);
//        System.out.println(CarData.QRCodeMap.get(2));
//        // 数据获取
//        String back = "";
//        if (MessageData.QRCode != null) {
//            back = MessageData.QRCode[1].trim();
//        }
//        if (back.length() < 6) {
//            System.out.println("长度不足，随机进行补位");
//            int length = back.length();
//            for (int i = 0; i < 6-length; i++) {
//                back += bu[(int) (Math.random()*36)];
//            }
//        }
//        return back.getBytes();
////        return algorithm.getCenter(MessageData.QRCode[1]).getBytes();
//    }
//
//    public static void main(String[] args) {
//        CarData.QRCodeMap.put(2, "A1b2C3D4f1B2C3D4");
//
//        int[][] shapeColor = new int[9][8];
//        shapeColor[8][1] = 2;
//        shapeColor[8][2] = 1;
//        CarData.shapeColorMap.put(101, shapeColor);
//
//        dispose();
//    }

}
