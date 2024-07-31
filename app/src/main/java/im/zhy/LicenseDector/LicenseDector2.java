//package im.zhy.LicenseDector;
//
//import android.graphics.Bitmap;
//import car.car2024.ActivityView.FirstActivity;
//import car.car2024.FragmentView.LeftFragment;
//import com.utils.FileService;
//import im.hdy.CarData;
//import im.zhy.util.ImageDisposeUtils;
//import im.zhy.util.ImgPretreatment;
//
///**
// * @author zhy
// * @create_date 2019-05-12 13:46
// */
//public class LicenseDector2 {
//
//    public static void licenseDector(Bitmap bitmap){
//
//        String license = "";
//
//        // 获取最大矩形
//        Bitmap maxSqu = null;
//        try {
//            maxSqu = ImageDisposeUtils.cutoutMaxRectangle(bitmap);
//
//            if (maxSqu == null){
//                return;
//            }
//
//            maxSqu = Bitmap.createBitmap(maxSqu, 30, 30, maxSqu.getWidth() - 60, maxSqu.getHeight() - 60);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        for (int i = 0; i < 6; i++) {
//            FileService.saveClassPhoto(maxSqu, "License7", "License");
//
////            license = FirstAc tivity.plateRecognizer.recognize();
//
//            if (license.length() < 6){
//                continue;
//            }else {
//                license = license.substring(license.length() - 6, license.length());
//            }
//
//            boolean b = LicenseRectify.judgeLicense(license);
//
//            if (b){
//                CarData.license = license;
//                break;
//            }
//
//            try {
//                maxSqu = Bitmap.createBitmap(maxSqu, 10, 8, maxSqu.getWidth() - 15, maxSqu.getHeight() - 16);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return;
//            }
//        }
//
//        System.out.println("license = " + license);
//    }
//
//    public static void licens2eDector2(){
//
////        Bitmap bitmap = LicenseDector.cutoutLicense(LeftFragment.bitmap);
//
//        Bitmap bitmap = ImageDisposeUtils.cutoutMaxRectangle(LeftFragment.bitmap);
//
//        bitmap = ImgPretreatment.converyToGrayImg(bitmap);
//
//        FileService.saveClassPhoto(bitmap, "LicenseTest", "License");
//
//        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        FirstActivity.psmSingle(true);
//        FirstActivity.varWhiteList(0);
//
//        FirstActivity.tessBaseAPI.setImage(bitmap);
//        String result = FirstActivity.tessBaseAPI.getUTF8Text();
//
//        System.out.println("result---------------" + result);
//
//    }
//}
