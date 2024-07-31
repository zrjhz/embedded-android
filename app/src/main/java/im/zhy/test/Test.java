//package im.zhy.test;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Environment;
//import car.bkrc.com.car2018.BR;
//import com.utils.FileService;
//import car.car2024.Utils.Socket.ZigbeeService;
//import im.zhy.shapeDector.ShapeDector;
//import im.zhy.util.ImageDisposeUtils;
//import im.zhy.util.ImgPretreatment;
//
//
//
///**
// * @author zhy
// * @create_date 2019-05-23 10:19
// */
//public class Test {
//
//    public static void test1(){
//        Bitmap bitmap = getBitmap(1);
//    }
//
//    public static void test2(){
////        Bitmap bitmap = getBitmap(2);
//        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/1/License.png");
//
////        LicenseDector.judgeLicenseBlue(bitmap);
//
//        try {
////            LicenseDector.licenseDector(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void test3(){
//        Bitmap bitmap1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/1/License_2.png");
//
//        Bitmap bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
//
//        long start = System.currentTimeMillis();
//
//        for (int x = 0; x < bitmap.getWidth(); x++) {
//            for (int y = 0; y < bitmap.getHeight(); y++) {
//
//                int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//                if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                    bitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(false));
//                }else {
//                    bitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(true));
//                }
//
//            }
//
//        }
//
//        System.out.println("总用时：：：" + (System.currentTimeMillis() - start));
//
//        FileService.saveClassPhoto(bitmap, "LicenseTest", "test");
//    }
//
//    public static void test4(){
////        Bitmap bitmap = getBitmap(4);
//
//        byte by1 = (byte) (Integer.parseInt("A" + 1, 16));
//        byte by2 = (byte) (Integer.parseInt("B" + 2, 16));
//        byte by3 = (byte) (Integer.parseInt("E" + 3, 16));
//
//        ZigbeeService.TFT_HEX(by1, by2, by3);
//
//    }
//
//    public static void test5(){
//        Bitmap bitmap = getBitmap(5);
//    }
//
//    private static Bitmap getBitmap(int index){
//        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/test/test_" + index + ".png");
//
//        return bitmap;
//    }
//
//}
