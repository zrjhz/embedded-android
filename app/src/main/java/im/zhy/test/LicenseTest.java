//package im.zhy.test;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.os.Environment;
//import car.car2024.ActivityView.FirstActivity;
//import com.googlecode.tesseract.android.TessBaseAPI;
//import com.utils.FileService;
//import im.zhy.data.arithmetic.StrExtractUtils;
//import im.zhy.util.ImageDisposeUtils;
//import org.opencv.core.MatOfPoint2f;
//import org.opencv.core.Point;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author zhy
// * @create_date 2019-05-04 15:06
// */
//public class LicenseTest {
//
//    public static void test(){
//        Bitmap bitmapQ = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/Test_4.png");
//        Bitmap bitmapO = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/Test_2.png");
//
//
//        bitmapO = Bitmap.createBitmap(bitmapO, bitmapO.getWidth() / 2, bitmapO.getHeight() / 2, bitmapO.getWidth() / 2, bitmapO.getHeight() / 2);
//        bitmapQ = Bitmap.createBitmap(bitmapQ, bitmapQ.getWidth() / 2, bitmapQ.getHeight() / 2, bitmapQ.getWidth() / 2, bitmapQ.getHeight() / 2);
//
//        List<MatOfPoint2f> gradsQ = ImageDisposeUtils.getGrads(bitmapQ, true);
//
//        List<MatOfPoint2f> gradsO = ImageDisposeUtils.getGrads(bitmapO, true);
//
//
//        System.out.println("gradsQ--------------------");
//        for (MatOfPoint2f matOfPoint2f : gradsQ) {
//            System.out.println("point个数：" + matOfPoint2f.toArray().length + "total个数：" + matOfPoint2f.total());
//        }
//
//
//        System.out.println("gradsO--------------------");
//        for (MatOfPoint2f matOfPoint2f : gradsO) {
//            System.out.println("point个数：" + matOfPoint2f.toArray().length + "total个数：" + matOfPoint2f.total());
//        }
//
//
//
//    }
//
//
//    public static void test1(){
//        Bitmap bitmap1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/License_3.png");
//
//        List<Bitmap> list = new ArrayList<>();
//
//        int width = bitmap1.getWidth();
//
//        for (int i = 0; i < 6; i++) {
//            Bitmap bitmap;
//            if (i < 2){
//                int x = (int) (width * i * 0.2);
//                bitmap = Bitmap.createBitmap(bitmap1, x, 0, (int) (width * 0.2), bitmap1.getHeight());
//                list.add(bitmap);
//            }else {
//                int x = (int) ((width * 0.4) + (width * (i - 2) * 0.15));
//                bitmap = Bitmap.createBitmap(bitmap1, x, 0, (int) (width * 0.15), bitmap1.getHeight());
//                list.add(bitmap);
//            }
//        }
//
//        System.out.println("list size:::" + list.size());
//
//        for (int i = 0; i < list.size(); i++) {
//            FileService.saveClassPhoto(list.get(i), "test_" + i, "License/test");
//        }
//
//        String license = "";
//        for (Bitmap bitmap : list) {
//            FirstActivity.tessBaseAPI.setImage(bitmap);
//            String utf8Text = FirstActivity.tessBaseAPI.getUTF8Text();
//
//            System.out.println("utf8Text = " + utf8Text);
//
//            List<Character> list1 = StrExtractUtils.inRegexGetGroup(utf8Text, "[A-Z0-9]{1}");
//
//            if (list1 == null || list1.size() < 1){
//                continue;
//            }
//
//            license += list1.get(0);
//        }
//
//        System.out.println("license = " + license);
//    }
//
//
//    public static void test2(){
//
//        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/test/test_2.png");
//
//        bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2, 0, bitmap.getWidth() / 2, bitmap.getHeight());
//
//        FirstActivity.tessBaseAPI.setImage(bitmap);
//
//        FirstActivity.psmSingle(false);
//
//        FirstActivity.varWhiteList(0);
//
//        System.out.println("FirstActivity.tessBaseAPI.getUTF8Text() = " + FirstActivity.tessBaseAPI.getUTF8Text());
//
//        Bitmap bitmap1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/Test_4.png");
//
//        bitmap1 = Bitmap.createBitmap(bitmap1, bitmap.getWidth() / 2, 0, bitmap.getWidth() / 2, bitmap1.getHeight());
//
//        FirstActivity.tessBaseAPI.setImage(bitmap1);
//
//        System.out.println("FirstActivity.tessBaseAPI.getUTF8Text() = " + FirstActivity.tessBaseAPI.getUTF8Text());
//    }
//
//
//    public static void test3(){
//        FirstActivity.tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);//设置识别模式
//
//        FirstActivity.tessBaseAPI.setImage(new File(Environment.getExternalStorageDirectory() + "/2019car/License/License7(1).png"));
//
//        System.out.println("FirstActivity.tessBaseAPI.getUTF8Text() = " + FirstActivity.tessBaseAPI.getUTF8Text());
//    }
//
//
//    public static void test4(){
//        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/char/char_1.png");
//
//        Bitmap bitmap1 = resizeImage(bitmap, 1, 1);
//
//        FileService.saveClassPhoto(bitmap1, "char_1_test", "License/char");
//    }
//
//    public static void test5(){
//
//        for (int i = 1; i < 3; i++) {
//
//            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/test/test_" + (i * 2) + ".png");
//
//            int amount = 0;
//
//            for (int x = 0; x < bitmap.getWidth(); x++) {
//                for (int y = 0; y < bitmap.getHeight(); y++) {
//                    int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//                    if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                        amount++;
//                    }
//
//                }
//
//            }
//
//            System.out.println(i + "的黑色个数：：" + amount);
//        }
//    }
//
//
//    public static void test6(){
//        Bitmap bitmapO = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/Test_2.png");
//
//        Bitmap bitmapQ = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/Test_4.png");
//
//        List<MatOfPoint2f> listO = ImageDisposeUtils.getGrads(bitmapO, true);
//
//        MatOfPoint2f matO = listO.get(0);
//
//        System.out.println("O 的 point ---------------------------------");
//
//        for (Point point : matO.toArray()) {
//            System.out.println("X::" + point.x + "   Y::" + point.y);
//        }
//
//        System.out.println("O 的 point ---------------------------------");
//
//        List<MatOfPoint2f> listQ = ImageDisposeUtils.getGrads(bitmapQ, true);
//
//        MatOfPoint2f matQ = listQ.get(0);
//
//        System.out.println("Q 的 point-----------------------------------");
//
//        for (Point point : matQ.toArray()) {
//            System.out.println("X::" + point.x + "   Y::" + point.y);
//        }
//
//        System.out.println("Q 的 point-----------------------------------");
//    }
//
//
//    public static void test7(){
//        Bitmap bitmapQ = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/2/test/test_4.png");
//        Bitmap bitmapO = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/2/test/test_2.png");
//
//        boolean[] bs = new boolean[]{false, false};
//        for (int i = 0; i < 2; i++) {
//
//            Bitmap bitmap = bitmapO;
//            if (i == 1){
//                bitmap = bitmapQ;
//            }
//
//            int x = bitmap.getWidth() / 2 + 5;
//            int y = (int) (bitmap.getHeight() * 0.69 + 1);
//
//            int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//            if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                bs[i] = true;
//            }
//        }
//
//
//        for (boolean b : bs) {
//            System.out.println("B::::::::::::" + b);
//        }
//
//
//    }
//
//
//    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
//        Bitmap BitmapOrg = bitmap;
//        int width = BitmapOrg.getWidth();
//        int height = BitmapOrg.getHeight();
//        int newWidth = w;
//        int newHeight = h;
//
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        // if you want to rotate the Bitmap
//        // matrix.postRotate(45);
//        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
//                height, matrix, true);
//        return resizedBitmap;
//    }
//
//
//    public static void main(String[] args) {
//        String license = "602Y4";
//
//        String[] s = license.split(" ");
//
//        for (String s1 : s) {
//            System.out.println("".equals(s1.trim()));
//        }
//    }
//
//
//}
