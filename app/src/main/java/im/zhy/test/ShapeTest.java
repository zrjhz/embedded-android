//package im.zhy.test;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Environment;
//import com.utils.FileService;
//import im.zhy.shapeDector.ShapeDector;
//import im.zhy.util.ImageDisposeUtils;
//import org.opencv.android.Utils;
//import org.opencv.core.Mat;
//import org.opencv.core.Point;
//import org.opencv.core.Size;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//
///**
// * @author zhy
// * @create_date 2019-05-05 12:54
// */
//public class ShapeTest {
//
//    public static void test(){
//
//
//
//        Bitmap bitmap1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/copy_black.png");
//
//        Bitmap bitmap = bitmap1.copy(Bitmap.Config.ARGB_8888, true);
//
//        for (int x = 0; x < bitmap.getWidth(); x++) {
//            for (int y = 0; y < bitmap.getHeight(); y++) {
//
//                int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//                int f;
//                if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                    f = ImageDisposeUtils.blackOrWhite(false);
//                }else {
//                    f = ImageDisposeUtils.blackOrWhite(true);
//                }
//
//                bitmap.setPixel(x, y, f);
//
//            }
//
//        }
//
//        bitmap = ShapeDector.open(bitmap, 5);
//
//        Bitmap bitmap2 = Bitmap.createBitmap(bitmap);
//
//        for (int x = 0; x < bitmap2.getWidth(); x++) {
//            for (int y = 0; y < bitmap2.getHeight(); y++) {
//
//                int[] rgb = ImageDisposeUtils.getRGB(bitmap2, x, y);
//
//                int f;
//                if (rgb[0] == 0xff && rgb[1] == 0xff && rgb[2] == 0xff){
//                    f = ImageDisposeUtils.blackOrWhite(true);
//                }else {
//                    f = ImageDisposeUtils.blackOrWhite(false);
//                }
//
//                bitmap2.setPixel(x, y, f);
//
//            }
//
//        }
//
//
//        FileService.saveClassPhoto(bitmap2, "test_shape", "Test");
//    }
//
//
//}
