//package im.zhy.service;
//
//import android.graphics.Bitmap;
//import car.car2024.ActivityView.FirstActivity;
//import car.car2024.FragmentView.LeftFragment;
//import car.car2024.Utils.ImageIdentify.process.RGBLuminanceSource;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.Result;
//import com.google.zxing.common.HybridBinarizer;
//import com.google.zxing.qrcode.QRCodeReader;
//import com.utils.FileService;
//
//import car.car2024.Utils.Socket.Variable;
//import im.hdy.CarData;
//import im.zhy.LicenseDector.FileUtil;
//import im.zhy.shapeDector.StaticShapeDector;
//import im.zhy.test.QRCode;
//import car.car2024.Utils.Socket.AcceptCarOrder;
//import car.car2024.Utils.Socket.ThreadUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author zhy
// * @create_date 2019-05-18 20:37
// */
//public class StaticLandmark {
//
//    //用于记录 静态标志物 识别是否完成
//    public static boolean static_landmark_complete = false;
//
//    // 返回 为 true 就说明是二维码信息 为 false 就是图形信息
//    public static boolean getIsQRCodeData(int index){
//
//        String qrCode = CarData.QRCodeMap.get(index);
//
//        if (qrCode == null || qrCode.trim().length() == 0){
//
//            int[][] shapeColor = CarData.shapeColorMap.get(100 + index);
//
//            if (shapeColor == null){
//                return true;
//            }else {
//                return false;
//            }
//
//        }else {
//            return true;
//        }
//    }
//
//
//    /**
//     * 一张图中有一个二维码的识别 扫描不出二维码就进行图像识别
//     */
//    public static void qrCodeDect(Bitmap bitmap) {
//
//        System.out.println("单个二维码识别:" + AcceptCarOrder.getIndex());
//
//        static_landmark_complete = false;
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                boolean success = false;
//
//                ThreadUtils.sleep(1700);
//
//                for (int i = 0; i < 5; i++) {
//
////                    Bitmap bitmap = LeftFragment.bitmap;
//
//                    success = qrCodeDispatch(bitmap);
//
//                    if (success){
//                        System.out.println("二维码识别成功， 第 " + (i + 1) + " 轮");
//                        break;
//                    }else {
//                        System.out.println("二维码识别失败， 第 " + (i + 1) + " 轮");
//                    }
//                }
//                FirstActivity.Connect_Transport.erweima();
//
//
//            }
//        }).start();
//
//
//    }
//
//
//    /**
//     * 一张图中有两个二维码的识别  扫描不出二维码就进行图像识别
//     */
//    public static void qrCodeDectMultiple() {
//
//        System.out.println("多个二维码识别");
//
//        ThreadUtils.sleep(1700);
//
//        static_landmark_complete = false;
//
//        double cutoutWidth = 0;
//        boolean success = false;
//
//        Bitmap bitmap = LeftFragment.bitmap;
//
//        double width = bitmap.getWidth();
//
//        double ratio = 0;
//
//        System.out.println("bitmap.getWidth() = " + bitmap.getWidth());
//
//        do {
//
//            Bitmap bitmap1;
//
//            cutoutWidth += 30;
//
////            int x = isLeft ? 0 : 30;
////
////            bitmap = Bitmap.createBitmap(bitmap, x, 0, bitmap.getWidth() - 30, bitmap.getHeight());
//
//            boolean isLeft = true;
//
//            try {
//                // 判断是否识别左边的二维码
//                isLeft = isCodeDectLeft();
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("判别左右失败");
//            }
//
//            try {
//                if (isLeft){
//                    bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, (int) cutoutWidth, bitmap.getHeight());
//                    FileService.savePhoto(bitmap1,"Q_RL.png");
//                }else {
//                    bitmap1 = Bitmap.createBitmap(bitmap, (int) (width - cutoutWidth), 0, (int)cutoutWidth, bitmap.getHeight());
//                    FileService.savePhoto(bitmap1,"Q_RR.png");
//
//                }
//
//                ratio = cutoutWidth / width;
//
//                success = qrCodeDispatch(bitmap1);
//            } catch (Exception e) {
//                System.out.println("多个二维码截取失败");
//                e.printStackTrace();
//                break;
//            }
//
//        }while (!success && ratio < 0.8);
//
//        FirstActivity.Connect_Transport.erweima();
//
////        if (success && ratio < 0.8){
////            System.out.println("二维码识别成功， 第 " + cutoutWidth / 30 + " 轮");
////        }else {
////            System.out.println("二维码识别失败， 第 " + cutoutWidth / 30 + " 轮");
////            StaticShapeDector.shapeDectorThread();
////        }
//
//
//    }
//
//    /**
//     * 一张有两个二维码时判断是不是识别左边的那一个
//     * @return  true 左边  false 右边
//     */
//    private static boolean isCodeDectLeft(){
//        String license = CarData.license;
//        if ("".equals(license)){
//
//            return true;
//        }else {
//            // 判断处理方法
//            // 依据车牌第三位数据判断识别
//            String c = license.charAt(2) + "";
//            System.out.println("isCodeDectLeft" + c);
//            int number = Integer.parseInt(c);
//            if (number % 2 != 0){
//                return true;
//            }else {
//                return false;
//            }
//        }
//    }
//
//
//    private static boolean qrCodeDispatch(Bitmap bitmap) {
//
//        try {
//            Result result = null;
//
//            FileService.savePhoto(bitmap, "QRCode_" + AcceptCarOrder.getIndex() + ".png");
//            Bitmap bitmap1 = Bitmap.createBitmap(bitmap);
//            RGBLuminanceSource rSource = new RGBLuminanceSource(
//                    bitmap1);
//
//            BinaryBitmap binaryBitmap = new BinaryBitmap(
//                    new HybridBinarizer(rSource));
//            Map<DecodeHintType, String> hint = new HashMap<DecodeHintType, String>();
//            hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
//            QRCodeReader reader = new QRCodeReader();
//            result = reader.decode(binaryBitmap, hint);
//            if (result.toString() != null) {
//                String result_qr = result.toString();
//                //获取到识别结果
//
////                            if (index == 1){
////                                CarData.QRCode1 = result_qr;
////                                System.out.println("二维码识别结果：" + CarData.QRCode1);
////                            }else {
////                                CarData.QRCode2 = result_qr;
////                                System.out.println("二维码识别结果：" + CarData.QRCode2);
////                            }
//
//                System.out.println("二维码" + AcceptCarOrder.getIndex() + "识别结果：" + result_qr);
////                Variable.QRMAP.put(AcceptCarOrder.getIndex(),result_qr);
////                CarData.QRCodeMap.put(AcceptCarOrder.getIndex(), result_qr);
////                MessageData.QRCode[MessageData.QRCodeNum++] = result_qr;
//                return true;
//            }else {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//
//    }
//}
