package im.fyhz.utils;

//import com.baidu.ai.edge.core.base.BaseException;

import car.car2024.Utils.Socket.ZigbeeService;
        import car.car2024.Utils.Socket.ThreadUtils;

/**
 * Created by fyhz on 04/05/2021
 */

@SuppressWarnings("all")
public class ShapeAndLicense {

    /**
     * 单个车牌识别
     * @param TFTPage
     */
//    public static void license(int TFTPage){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                long start = System.currentTimeMillis();
//
//                boolean isLicense = false;
//
//                // 图片数量
//                Bitmap[] bitmaps = new Bitmap[TFTPage];
//                for (int j = 0; j < bitmaps.length; j++) {
//
//                    downShow();
//
//                    bitmaps[j] = LeftFragment.bitmap;
//                    FileService.savePhoto(bitmaps[j], "bbbbbb" + j + ".png");
//                }
//
//                String RFid = rfid.getRFID();
//
//
//
//                regular.createLicensePattern(RFid);
//
//                for (int i = 0; i < TFTPage; i++) {
//
////                    Bitmap[] bitmaps = new Bitmap[TFTPage];
//
////                    for (int j = 0; j < bitmaps.length; j++) {
////
////                        downShow();
////                        bitmaps[j] = LeftFragment.bitmap;
////
////                    }
//
//                    // 识别车牌
//                    if (!isLicense){
//                        String str = newShapeAndLicense.licenseSeg(bitmaps[i]);
//                        System.out.println("识别车牌" + str);
//                        if (str != null && str.length() != 0) {
//                            System.out.println("第" + i + "次匹配：" + regular.licenseMatch(str));
//                            if (regular.licenseMatch(str)) {
//                                CarData.license = str;
//                                isLicense = true;
//                                break;
//                            }
//                            if (i == TFTPage - 1) {
//                                CarData.license = str;
//                            }
//                        }
//                    }else {
//                        break;
//                    }
//
//                }
//
//
//                //还未发送
//                if (!CarData.license_shape_complete) {
//                    //发送完成
//                    System.out.println("车牌识别成功-----------");
//                    FirstActivity.Connect_Transport.licenseShape();
//                }
//
//                long end = System.currentTimeMillis();
//
//                System.out.println("车牌识别用时：：：" + (end - start) / 1000);
//
//            }
//        }).start();
//    }


    /**
     * 单个图形识别
     * @param TFTPage
     */
//    public static void shape(int TFTPage){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                long start = System.currentTimeMillis();
//
//                boolean isShape = false;
//
//                // 图片数量
//                Bitmap[] bitmaps = new Bitmap[TFTPage];
//                for (int j = 0; j < bitmaps.length; j++) {
//
//                    downShow();
//
//                    bitmaps[j] = LeftFragment.bitmap;
//                    FileService.savePhoto(bitmaps[j], "cccccc" + j + ".png");
//                }
//
//                for (int i = 0; i < TFTPage; i++) {
//
////                    Bitmap[] bitmaps = new Bitmap[1];
////
////                    for (int j = 0; j < bitmaps.length; j++) {
////
////                        downShow();
////                        bitmaps[j] = LeftFragment.bitmap;
////
////                    }
//
//                    if (!isShape){
//                            ShapeLicenseLight.shapeSeg(bitmaps[i]);
////                        if (str != null && str.length() != 0) {
////                            isShape = true;
////                        }
////                        if (isShape){
////                            break;
////                        }
//                    }
//                }
//
//                for (int i = 0;i < 30;i++){
//                    ThreadUtils.sleep(1000);
//                    if (CarData.shape_complete) {
//                        break;
//                    }
//                }
//                //还未发送
//                if (!CarData.license_shape_complete) {
//                    //发送完成
//                    System.out.println("图形识别成功-----------");
//                    FirstActivity.Connect_Transport.licenseShape();
//                }
//
//                long end = System.currentTimeMillis();
//
//                System.out.println("图形识别用时：：：" + (end - start) / 1000);
//
//            }
//        }).start();
//    }

    /**
     * bitmap里面图片的识别，比如交通标志，车辆，图形，车牌
     */
//    public static void shapeLicense(int TFTPage) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                long start = System.currentTimeMillis();
//
//
//                // 图片数量
//                Bitmap[] bitmaps = new Bitmap[TFTPage];
//                String[] results = new String[TFTPage];
//                for (int j = 0; j < bitmaps.length; j++) {
//
//                    downShow();
//
//                    bitmaps[j] = LeftFragment.bitmap;
//                    FileService.savePhoto(bitmaps[j], "aaaaaa" + j + ".png");
//                }
//                System.gc();
//                // 这是识别图片中一张图片和一张车牌
//                for (int i = 0; i < TFTPage; i++) {
//                    if (bitmaps[i] == null) {
//                        continue;
//                    }
//                    String s = JudgeBitmapAndIdentify.newJudgeBitmap(bitmaps[i]);
//                    results[i] = s;
//                }
//
//
//                for (int i = 0; i < TFTPage; i++) {
//                    try {
//                        JudgeBitmapAndIdentify.Identify(bitmaps[i], results[i]);
//                        //如果tft就两张
////                        if (JudgeBitmapAndIdentify.getVary(Variable.index)[5]==1&&JudgeBitmapAndIdentify.tt==1){
////                            Ocr.OcrRecognition(bitmaps[Math.abs(1-i)]);
////                            JudgeBitmapAndIdentify.tt=0;
////                        }
//                    } catch (BaseException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                System.gc();
//
//                //还未发送
//                if (!CarData.license_shape_complete) {
//                    //发送完成
//                    System.out.println("TFT识别成功-----------");
//                    FirstActivity.Connect_Transport.licenseShape();
//                }
//
//                long end = System.currentTimeMillis();
//
//                System.out.println("TFT识别：：：" + (end - start) / 1000);
//
//            }
//        }).start();
//    }

//    private static boolean shape(Bitmap bitmap) {
//        return newShapeAndLicense.newJudgeShape(bitmap) == 1;
//    }

//    public static boolean license(Bitmap bitmap){
//
//        try {
////            LicenseDector.licenseDector(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("车牌识别出错");
//            return false;
//        }
//
////        return LicenseRectify.judgeLicense();
//    }

    private static void downShow() {
        System.out.println("通知小车将TFT翻到下一页");
        ZigbeeService.TFT_DownShow();

        ThreadUtils.sleep(3600);
    }

}

