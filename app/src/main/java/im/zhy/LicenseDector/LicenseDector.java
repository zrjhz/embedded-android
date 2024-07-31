//package im.zhy.LicenseDector;
//
//import android.graphics.Bitmap;
//
//import car.car2024.ActivityView.FirstActivity;
//import com.utils.FileService;
//import com.utils.ShapeUtils4;
//
//import car.car2024.Utils.Socket.Variable;
//import im.fyhz.MessageData;
//import im.hdy.CarData;
//import im.zhy.shapeDector.ShapeDector;
//import car.car2024.Utils.Socket.AcceptCarOrder;
//import im.zhy.util.GetVarName;
//import im.zhy.util.ImageDisposeUtils;
//import org.opencv.core.MatOfPoint2f;
//
//import java.util.*;
//
///**
// * @author zhy
// * @create_date 2019-04-28 10:38
// */
//
//@SuppressWarnings("all")
//public class LicenseDector {
//
//
//    private static Bitmap licenseBitmap;
//
//    private static int getSavePathIndex(){
//        return AcceptCarOrder.getIndex();
//    }
//
//    public static void licenseDector(Bitmap bitmap1) throws Exception{
//
//        FirstActivity.psmSingle(true);
//        FirstActivity.varWhiteList(0);
//
//        createLicenseBitmap(bitmap1);
//
//        if (licenseBitmap == null){
//            System.out.println("licenseDector：不是正确车牌");
//            return;
//        }
//
//        boolean succeed = false;
//
//        for (int i = 3; i > 0; i--) {
//            FirstActivity.tessBaseAPI.setImage(licenseBitmap);
//            String result = FirstActivity.tessBaseAPI.getUTF8Text();
//
//            System.out.println("result---------------" + result);
//            // 存放车牌原处理结果
//            MessageData.rawLicense = result;
//
//            System.out.println("rawLicense------" + MessageData.rawLicense);
//            LicenseRectify.licenseStrDector(result, licenseBitmap);
//
//            // 根据rfid限制车牌格式
////            String card = rfid.getRFID();
////            regular.createPattern(card);
////            if (!regular.licenseMatch(CarData.license)) {
////                break;
////            } else {
////                MessageData.licence = CarData.license;
////                succeed = true;
////                break;
////            }
//
//            if (CarData.license.matches(CarData.licenseMatcherStr)){
//                System.out.println("匹配---------------------" + CarData.license);
//                CarData.license = CarData.license;
//                succeed = true;
//                break;
//            }
//        }
//
//        if (!succeed){
//            licenseCharDector(bitmap1);
//        }
//
//
//    }
//
//
//    public static void licenseCharDector(Bitmap bitmap) throws Exception{
//
//        FirstActivity.psmSingle(false);
//
//        createLicenseBitmap(bitmap);
//
//        if (licenseBitmap == null){
//            System.out.println("不是正确车牌");
//            return;
//        }
//
//        List<MatOfPoint2f> list = licenseExtractChar();
//
//        String license = "";
//
//        for (int i = list.size() - 1; i >= 0; i--) {
//
//            if (license.length() >= 6){
//                break;
//            }
//
//            if (i == list.size() - 1){
//                FirstActivity.varWhiteList(1);
//            }else {
//                FirstActivity.varWhiteList(0);
//            }
//
//            MatOfPoint2f matOfPoint2f = list.get(i);
//
//            Bitmap charBitmap = ShapeUtils4.cutoutShape(matOfPoint2f, licenseBitmap);
//
//            FirstActivity.tessBaseAPI.setImage(charBitmap);
//
//            String result = FirstActivity.tessBaseAPI.getUTF8Text();
//
////            List<Character> list1 = StrExtractUtils.inRegexGetGroup(result, "[A-Z0-9]{1}");
////
////            if (list1 == null || list1.size() < 1){
////                continue;
////            }else {
////                license = list1.get(0) + license;
////
//                FileService.saveClassPhoto(charBitmap, "char_" + (6 - license.length()),"License/" + getSavePathIndex() +"/char");
////            }
//
//            license = result + license;
//
//        }
//
//        LicenseRectify.licenseStrDector(license, licenseBitmap);
//
//    }
//
//
//
//    private static void createLicenseBitmap(Bitmap bitmap1){
//        Bitmap bitmap = Bitmap.createBitmap(bitmap1);
//
////        licenseBitmap = cutoutLicense(bitmap);
//
//        if (licenseBitmap == null){
//            return;
//        }
//
////        licenseBitmap = ImgPretreatment.doPretreatment(licenseBitmap);
//
//        FileService.saveClassPhoto(licenseBitmap, "License_2", "License/" + getSavePathIndex());
//
//        licenseBitmap = Bitmap.createBitmap(licenseBitmap, (int) (licenseBitmap.getWidth() * 0.15), 0, (int) (licenseBitmap.getWidth() * 0.85), licenseBitmap.getHeight());
//
//        licenseBitmap = ShapeDector.open(licenseBitmap, 3);
//
//        wipeBlackSpot();
//
//        cutOutLicenseRangeY();
//
//        cutOutLicenseRangeX();
//
////        licenseBitmap = Bitmap.createBitmap(licenseBitmap, 0, 3, licenseBitmap.getWidth(), licenseBitmap.getHeight() - 6);
//
//        FileService.saveClassPhoto(licenseBitmap, "License_3", "License/" + getSavePathIndex());
//
//        int x = (int) (licenseBitmap.getWidth() * 0.80);
//        int n = 0;
//        for (int i = 6; i < licenseBitmap.getHeight() - 12; i++) {
//            int[] rgb = ImageDisposeUtils.getRGB(licenseBitmap, x, i);
//            if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                n++;
//            }
//        }
//
//        if (n < 10){
//            System.out.println("放的车牌");
//            licenseBitmap = null;
//            return;
//        }
//
//    }
//
//
//    private static void cutOutLicenseRangeY(){
//
//        int height = licenseBitmap.getHeight() - 1;
//        int width = licenseBitmap.getWidth() - 1;
//
//        int[] ranges = new int[4];
//
//        int[] xs = new int[]{0, width, 0, width};
//        int[] ys = new int[]{0, 0, height, height};
//
//        for (int i = 0; i < ranges.length; i++) {
//
//            ranges[i] = lookblackRange(licenseBitmap, xs[i], ys[i], 8, true);
//        }
//
//        for (int range : ranges) {
//            System.out.println("各个角黑边范围Y：：" + range);
//        }
//
//        for (int i = 0; i < ranges.length; i += 2) {
//            int range = ranges[i] > ranges[i + 1] ? ranges[i] : ranges[i + 1];
//
//            if (range > 0){
//
//                int yValue = 0;
//                int yLess = range;
//
//                if (i == 2){
//                    yValue = height - range;
//                    yLess = height;
//                }
//
//                for (int x = 0; x <= width; x++) {
//                    for (int y = yValue; y <= yLess; y++){
//                        licenseBitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(false));
//                    }
//                }
//            }
//        }
//
//
//    }
//
//
//    private static void cutOutLicenseRangeX(){
//
//        int height = licenseBitmap.getHeight() - 1;
//        int width = licenseBitmap.getWidth() - 1;
//
//        int[] ranges = new int[4];
//
//        int[] xs = new int[]{0, 0, width, width};
//        int[] ys = new int[]{10, 44, height / 2 - 5, height / 2 + 5};
//
//        for (int i = 0; i < ranges.length; i++) {
//
//            ranges[i] = lookblackRange(licenseBitmap, xs[i], ys[i], 2, false);
//
//        }
//
//        for (int range : ranges) {
//            System.out.println("各个角黑边范围X：：" + range);
//        }
//
//        for (int i = 0; i < ranges.length; i += 2) {
//            int range = ranges[i] > ranges[i + 1] ? ranges[i] : ranges[i + 1];
//
//            if (range > 0){
//
//                int xValue = 0;
//                int xLess = range;
//
//                if (i == 2){
//                    xValue = width - range;
//                    xLess = width;
//                }
//
//                for (int x = xValue; x <= xLess; x++) {
//                    for (int y = 0; y <= height; y++){
//                        licenseBitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(false));
//                    }
//                }
//            }
//        }
//
//
//    }
//
//
//    /**
//     * 寻找黑色范围
//     * @return
//     */
//    private static int lookblackRange(Bitmap bitmap, int x, int y, int range, boolean isY){
//
//        int add = 0;
//
//        if (isY){
//            add = y < 5 ? 1 : -1;
//        }else {
//            add = x < 5 ? 1 : -1;
//        }
//
//        int n = 0;
//
//        for (int i = 0; i < range; i++) {
//
//            int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//            if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                n++;
//                if (isY){
//                    y += add;
//                }else {
//                    x += add;
//                }
//            }else {
//                break;
//            }
//
//        }
//
//        return n;
//
//    }
//
//
//    /**
//     * 去除指定范围内的黑点
//     */
//    private static void wipeBlackSpot(){
//
//        int width = licenseBitmap.getWidth();
//        int height = licenseBitmap.getHeight();
//
////        boolean b = false;
//        /*for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//
//                boolean isBlackSpot = false;
//
//
//                if (x >= (width * 0.27) && x <= (width * 0.27 + 15) && y >= (height / 2 - 20) && y <= (height / 2 + 20)){
//                    isBlackSpot = true;
//                }
//
//                if (!isBlackSpot){
//                    if ((y >= 0 && y <= (height * 0.06)) || (y >= (height * 0.94) && y <= height)){
//                        isBlackSpot = true;
//                    }
//                }
//
//                if (isBlackSpot){
//
//                    licenseBitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(false));
//
//                }
//
//            }
//
//        }*/
//
//
//        int startX = (int) (width * 0.16);
//        int endX = startX + 13;
//
//        int startY = height / 2 - 20;
//        int endY = height / 2 + 20;
//
//        for (int x = startX; x <= endX; x++){
//            for (int y = startY; y <= endY; y++){
//
//
//                if(x < 0 || y < 0 || y >= licenseBitmap.getWidth() || x >= licenseBitmap.getWidth()){
//                    continue;
//                }
//                licenseBitmap.setPixel(x, y, ImageDisposeUtils.blackOrWhite(false));
//            }
//        }
//    }
//
//
//    private static int maxS = 60;
//
//    private static void licenseExtract(Bitmap bitmap) {
//
//        maxS = getMaxS();
//
//        for (int x = 0; x < bitmap.getWidth(); x++) {
//            for (int y = 0; y < bitmap.getHeight(); y++) {
//
//                int pixel = disColor(bitmap, x, y);
//
//                bitmap.setPixel(x, y, pixel);
//            }
//        }
//    }
//
//
//    private static int disColor(Bitmap bitmap, int x, int y){
//
//        float[] hsv = ImageDisposeUtils.getHSV(bitmap, x, y);
//        float h = hsv[0];
//        float s = hsv[1];
//        int pixel = 0;
//
//        // 196
//        if (h >= 170 && s >= maxS) {
//            pixel = ImageDisposeUtils.blackOrWhite(true);
//        } else {
//            pixel = ImageDisposeUtils.blackOrWhite(false);
//        }
//
//
//        return pixel;
//
//    }
//
//
//    private static Bitmap squMax;
//
//    private static int getMaxS(){
////        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/2019car/License/License.png");
//
//        Bitmap bitmap = squMax;
//
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int s = 0;
//
//        int range = 12;
//
//        float[] hsv = ImageDisposeUtils.getHSV(bitmap, range, range);
//
//        s += hsv[1];
//
//        hsv = ImageDisposeUtils.getHSV(bitmap, width - range, range);
//
//        s += hsv[1];
//
//        hsv = ImageDisposeUtils.getHSV(bitmap, range, height - range);
//
//        s += hsv[1];
//
//        hsv = ImageDisposeUtils.getHSV(bitmap, width - range, height - range);
//
//        s += hsv[1];
//
//        int aveg = s / 4 + 11;
//
//        System.out.println("aveg1 = " + aveg);
//
//        return aveg;
//    }
//
//
//    /**
//     * 截取车牌字符集合，并按照字符从左到右排序
//     * @return
//     */
//    public static List<MatOfPoint2f> licenseExtractChar(){
//        List<MatOfPoint2f> grads = ImageDisposeUtils.getGrads(licenseBitmap, true);
//
//        Collections.sort(grads, new Comparator<MatOfPoint2f>() {
//            @Override
//            public int compare(MatOfPoint2f m1, MatOfPoint2f m2) {
//                Map<String, int[]> map1 = ShapeUtils4.shapeScope(m1.toArray(), licenseBitmap.getWidth(), licenseBitmap.getHeight());
//                int minX1 = map1.get("min_x")[0];
//
//                Map<String, int[]> map2 = ShapeUtils4.shapeScope(m2.toArray(), licenseBitmap.getWidth(), licenseBitmap.getHeight());
//                int minX2 = map2.get("min_x")[0];
//
//                return minX1 > minX2 ? 1 : -1;
//            }
//        });
//
//        return grads;
//
//    }
//
//    public static boolean cutoutLicense(Bitmap bitmap){
//
//        // 获取最大矩形
//        Bitmap maxSqu = ImageDisposeUtils.cutoutMaxRectangle(bitmap);
//        System.out.println(maxSqu == null);
//        if (maxSqu == null){
//            return false;
//        }
//
//        maxSqu = Bitmap.createBitmap(maxSqu, 15, 15, maxSqu.getWidth() - 30, maxSqu.getHeight() - 30);
//
//        squMax = Bitmap.createBitmap(maxSqu);
//
//        FileService.saveClassPhoto(maxSqu, "maxSqu", "License/" + getSavePathIndex());
//
//        // 判断车牌是不是蓝色底
//        boolean licenseBlue = judgeLicenseBlue(squMax);
//        return licenseBlue;
////
////        if (!licenseBlue){
////            System.out.println("不是蓝底------");
////            return null;
////        }
//
////        FileService.saveClassPhoto(maxSqu, "License", "License/" + getSavePathIndex());
////
////        Bitmap licenseBitmap = maxSqu.copy(Bitmap.Config.ARGB_8888, true);
////
////        licenseExtract(licenseBitmap);
////
////
////        licenseBitmap = ShapeDector.open(licenseBitmap,3);
////
////        licenseBitmap = ShapeDector.close(licenseBitmap, 2);
////
////        FileService.saveClassPhoto(licenseBitmap, "License1", "License/" + getSavePathIndex());
////
////        // 梯度求取
////        List<MatOfPoint2f> contours = ImageDisposeUtils.getGrads(licenseBitmap, true);
////
////        System.out.println("License   contours个数：" + contours.size());
////
////        if(contours.size() == 0){
////            //识别出错了。
////            Log.e(TAG, "cutoutLicense: No conters");
////            return null;
////        }
////        Bitmap licenseBitmap1 = ImageDisposeUtils.getMaxBitmap(contours, licenseBitmap);
////
////        licenseBitmap1 = Bitmap.createBitmap(licenseBitmap1, 5, 5, licenseBitmap1.getWidth() - 10, licenseBitmap1.getHeight() - 10);
////
////        for (int x = 0; x < licenseBitmap1.getWidth(); x++) {
////            for (int y = 0; y < licenseBitmap1.getHeight(); y++) {
////                int[] rgb = ImageDisposeUtils.getRGB(licenseBitmap1, x, y);
////
////                boolean isBlack = true;
////                if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
////                    isBlack = false;
////                }
////
////                licenseBitmap1.setPixel(x, y, ImageDisposeUtils.blackOrWhite(isBlack));
////            }
////
////        }
////
////        FileService.saveClassPhoto(licenseBitmap1, "License_1", "License/" + getSavePathIndex());
////        return licenseBitmap1;
//
//    }
//
//    // 判断车牌底色是否为正确车牌
//    public static boolean judgeLicenseBlue(Bitmap bitmap){
////        int width = bitmap.getWidth();
////        int y = bitmap.getHeight() / 2 - 10;
////
////        int n = 0;
////        for (int i = 0; i < 4; i++) {
////            for (int x = 0; x < width; x++) {
////
////                int value = ImageDisposeUtils.colorExtract(bitmap, x, y, GetVarName.getColorInt(ColorName.BLUE));
////
////                if (value == ImageDisposeUtils.blackOrWhite(true)){
////                    n++;
////                }
////
////                if (n > 120){
////                    return true;
////                }
////
////            }
////            y += 12;
////        }
////
////        System.out.println("蓝色点数：" + n);
//
//        int x = bitmap.getWidth() / 2;
//        int height = bitmap.getHeight();
//        int n = 0;
//        for (int y = 0; y < height; y++) {
//            // 可能是修改判定颜色
////            int value = ImageDisposeUtils.colorExtract(bitmap, x, y, GetVarName.getColorInt(Variable.LICENSE));
////            if (value == ImageDisposeUtils.blackOrWhite(true)){
////                n++;
////            }
//
//        }
//
//        System.out.println(n);
//
//        if (n > 9){
//            return true;
//        }
//
//        return false;
//    }
//
//
//}
