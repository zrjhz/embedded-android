//package im.zhy.shapeDector;
//
//import android.graphics.Bitmap;
//import car.car2024.ActivityView.FirstActivity;
//import car.car2024.FragmentView.LeftFragment;
//import com.utils.FileService;
//import im.hdy.CarData;
//import im.zhy.param.ColorName;
//import car.car2024.Utils.Socket.AcceptCarOrder;
//import im.zhy.util.GetVarName;
//import im.zhy.util.ImageDisposeUtils;
//import org.opencv.core.MatOfPoint2f;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author zhy
// * @create_date 2019-05-17 14:43
// */
//
//@SuppressWarnings("all")
//public class StaticShapeDector {
//
//    public static void shapeDectorThread(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    long start = System.currentTimeMillis();
//
//                    shapeDector(LeftFragment.bitmap);
//
//                    System.out.println("静态图形识别时长：：" + (System.currentTimeMillis() - start) / 1000);
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                    System.out.println("静态标志物图形识别出错！！！");
//
//                } finally {
//
//                    if (!StaticLandmark.static_landmark_complete){
//                        System.out.println("发送静态标志物图形识别成功");
//                        FirstActivity.Connect_Transport.erweima();
//                    }
//
//                }
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(23000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }finally {
//                    if (!StaticLandmark.static_landmark_complete){
//                        System.out.println("静态标志物图形识别超时");
//                        FirstActivity.Connect_Transport.erweima();
//                    }
//                }
//            }
//        }).start();
//
//    }
//
//    public static void shapeDector(Bitmap bitmap){
//
//        System.out.println("识别静态标志物图形");
//
//        try {
//            FileService.saveClassPhoto(bitmap, "bitmap", "static_shape");
//
//            int select = CarData.staticLandmark_site[AcceptCarOrder.getIndex() - 1];
//
//            if(select == 0){
//                bitmap = Bitmap.createBitmap(bitmap, (int) (bitmap.getWidth() * 0.16), (int) (bitmap.getHeight() * 0.10), (int) (bitmap.getWidth() * 0.60), (int) (bitmap.getHeight() * 0.90));
//            }else if (select == 1){
//                bitmap = Bitmap.createBitmap(bitmap, (int) (bitmap.getWidth() * 0.35), (int) (bitmap.getHeight() * 0.30), (int) (bitmap.getWidth() * 0.42), (int) (bitmap.getHeight() * 0.68));
//            }else if (select == 2){
//                bitmap = Bitmap.createBitmap(bitmap, (int) (bitmap.getWidth() * 0.34), (int) (bitmap.getHeight() * 0.30), (int) (bitmap.getWidth() * 0.30), (int) (bitmap.getHeight() * 0.52));
//            }else if (select == 3){
//                bitmap = Bitmap.createBitmap(bitmap, (int) (bitmap.getWidth() * 0.29), (int) (bitmap.getHeight() * 0.23), (int) (bitmap.getWidth() * 0.50), (int) (bitmap.getHeight() * 0.77));
//            }
//
//            FileService.saveClassPhoto(bitmap, "bitmap1", "static_shape");
//
////            bitmap = getShapeArea(bitmap);
//
////            FileService.saveClassPhoto(bitmap, "bitmap2", "static_shape");
//
//            Map<String, List> map = getMatOfPoint(bitmap);
//
//            ShapeDector.setStatic();
//
//            ShapeDector.newShapeDectorRealizeSyn(map);
//
//        } catch (Exception e) {
//            System.out.println("识别静态标志物图形出现异常！！！");
//            e.printStackTrace();
//        }finally {
//            FirstActivity.Connect_Transport.erweima();
//        }
//    }
//    /**
//     *
//     * @param bitmap1
//     * @return
//     */
//    private static Map<String, List> getMatOfPoint(Bitmap bitmap1){
//
//        Map<String, List> map = new HashMap<>();
//
//        List colorList = new ArrayList();
//
//        for (int i = 0; i < 7; i++) {
//
//            Bitmap colorBitmap = bitmap1.copy(Bitmap.Config.ARGB_8888, true);
//
//            for (int x = 0; x < colorBitmap.getWidth(); x++) {
//                for (int y = 0; y < colorBitmap.getHeight(); y++) {
//                    // 静态标志物图像颜色提取
//                    int pixel = ImageDisposeUtils.staticColorExtract(colorBitmap, x, y, i);
//
//                    colorBitmap.setPixel(x, y, pixel);
//
//                }
//
//            }
//
//            ShapeDector.open(colorBitmap, 3);
//
//            if (GetVarName.getColorInt(ColorName.RED) == i || GetVarName.getColorInt(ColorName.BLACK) == i) {
//                colorBitmap = ShapeDector.close(colorBitmap, 3);
//
//            }
//
//            FileService.saveClassPhoto(colorBitmap, "color_" + GetVarName.getColorName(i), "static_shape/color");
//
//            System.out.println("color_" + GetVarName.getColorName(i));
//
//            colorList.add(colorBitmap);
//
//            Bitmap image = Bitmap.createBitmap(colorBitmap);
//
//            // 梯度求取
//            List<MatOfPoint2f> list = ImageDisposeUtils.getGrads(image, false);
//
//            for (MatOfPoint2f matOfPoint2f : list) {
//
//                Bitmap bitmap = Bitmap.createBitmap(colorBitmap);
//
//            }
//
//            map.put(GetVarName.getColorName(i), list);
//
//            System.out.println(GetVarName.getColorName(i) + ":" + list.size());
//
//        }
//
//        map.put("colorList", colorList);
//
//        return map;
//
//    }
//
//
//    private static Bitmap getShapeArea(Bitmap bitmap1){
//
//        Bitmap bitmap = bitmap1.copy(Bitmap.Config.ARGB_8888, true);
//
//        int value = 177;
//
//        for (int x = 0; x < bitmap.getWidth(); x++) {
//            for (int y = 0; y < bitmap.getHeight(); y++) {
//
//                int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//                int r = rgb[0];
//                int g = rgb[1];
//                int b = rgb[2];
//
//                if (r > value - 10 && r < value + 10){
//                    if (g > value - 10 && g < value + 10){
//                        if (b > value - 10 && b < value + 10){
//
//                            int avg = (r + g + b) / 3;
//
//                            if (r > avg - 3 && r < avg + 3){
//                                if (g > avg - 3 && g < avg + 3){
//                                    if (b > avg - 3 && b < avg + 3){
//                                        bitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(true));
//                                    }
//                                }
//                            }
//
//
//                        }
//                    }
//                }
//
//                bitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(false));
//
//            }
//        }
//
//        FileService.saveClassPhoto(bitmap, "shapeArea", "static_shape");
//
//        List<MatOfPoint2f> grads = ImageDisposeUtils.getGrads(bitmap, false);
//
//        Bitmap landmark = ImageDisposeUtils.getMaxBitmap(grads, bitmap1);
//
//        FileService.saveClassPhoto(landmark, "landmark", "static_shape");
//
//        return landmark;
//    }
//
//
//    // 保存静态标志物识别结果
//    public static void saveData(){
//        CarData.shapeColorMap.put(100 + AcceptCarOrder.getIndex(), CarData.shapeColor);
//    }
//
//    // 根据标签获取指定数据
//    public static int[][] getData(int index){
//        return CarData.shapeColorMap.get(100 + index);
//    }
//
//
//
//}
