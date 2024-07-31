//package im.zhy.LicenseDector;
//
//import android.graphics.Bitmap;
//import android.util.Log;
//
//import car.car2024.ActivityView.FirstActivity;
//import com.utils.FileService;
//import com.utils.ShapeUtils4;
//
//import im.fyhz.MessageData;
//import im.hdy.CarData;
//import car.car2024.Utils.Socket.AcceptCarOrder;
//import im.zhy.util.ImageDisposeUtils;
//import org.opencv.core.MatOfPoint2f;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author zhy
// * @create_date 2019-05-12 10:24
// */
//
//@SuppressWarnings("all")
//public class LicenseRectify {
//
//
//    private static final String TAG = "LicenseRectify";
//    private static Bitmap licenseBitmap;
//
//    private static int getSavePathIndex(){
//        return AcceptCarOrder.getIndex();
//    }
//
//    /**
//     * 对车牌结果进行校正
//     * @param license  车牌识别信息
//     */
//    public static void licenseStrDector(String license, Bitmap licenseBitmap1){
//
//        licenseBitmap = licenseBitmap1;
//
//        CarData.license = "";
//
////        license = license.trim().replaceAll(" ", "");
////        System.out.println("license = " + license);
////
////        if (license == null || license.length() <= 6){
////            return;
////        }else if (license.length() > 6){
////            license = license.substring(1, 7);
////        }
//
//        //        license = license.substring(license.length() - 6, license.length());
//
//        String[] licenses = license.split(" ");
//
//        for (String licens : licenses) {
//            System.out.println("licens = " + licens);
//        }
//
//        if (licenses.length == 2){
//            String front = licenses[0].trim();
//            String back = licenses[1].trim();
//
//            System.out.println("front = " + front);
//            System.out.println("back = " + back);
//
//            if (front.length() < 1){
//                return;
//            }else {
//                front = front.substring(front.length() - 1, front.length());
//            }
//
//            if (back.length() < 5){
//                return;
//            }else {
//                back = back.substring(0, 5);
//            }
//
//            license = front + back;
//        }else if (licenses.length == 1){
//            if (license.length() < 6){
//                return;
//            }else if (license.length() > 6){
//                license = license.substring(1, 7);
//            }
//        }else {
//            return;
//        }
//
//        System.out.println("license1 = " + license);
//
//        if (license.length() != 6){
//            return;
//        }
//
//        String result = "";
//
//        List<Integer> formList = getFormList();
//
//        for (int i = 0; i < license.length(); i++) {
//            Character c = license.charAt(i);
//
//            c = formLetterNumber(i, formList.get(i), c);
//
//            result += c;
//        }
//
//        System.out.println("license  result = " + result);
//
//        CarData.license = result;
//        MessageData.licence = result;
//    }
//
//
//    private static Character formLetterNumber(int i, int form, Character character){
//
//        Character c = character;
//
//        if (form == 0){ // 字母数字
//            switch (c){
//                case 'Q':
//                    c = distinctionQ0UD(i);
//                    break;
//                case 'O':
//                    c = distinctionQ0UD(i);
//                    break;
//                case 'U':
//                    c = distinctionQ0UD(i);
//                    break;
//                case 'D':
//                    c = distinctionQ0UD(i);
//                    break;
//                case '0':
//                    c = distinctionQ0UD(i);
//                    break;
//                case 'Z':
//                    c = distinctionChar(i, "Z2");
//                    break;
//                case '2':
//                    c = distinctionChar(i, "Z2");
//                    break;
//                case 'B':
//                    c = distinctionB8R(i);
//                    break;
//                case '8':
//                    c = distinctionB8R(i);
//                    break;
//                case 'R':
//                    c = distinctionB8R(i);
//                    break;
//                case 'G':
//                    c = distinctionG6(i);
//                    break;
//                case '6':
//                    c = distinctionG6(i);
//                    break;
//                case 'I':
//                    c = distinctionI1(i);
//                    break;
//                case '1':
//                    c = distinctionI1(i);
//                    break;
//                case 'A':
//                    c = distinctionA4(i);
//                    break;
//                case '4':
//                    c = distinctionA4(i);
//                    break;
//                case 'Y':
//                    c = distinctionChar(i, "Y7");
//                    break;
//                case '7':
//                    c = distinctionChar(i, "Y7");
//                    break;
//                case 'S':
//                    c = distinctionChar(i, "S3");
//                    break;
//                case '3':
//                    c = distinctionChar(i, "S3");
//                    break;
//
//            }
//        }else if (form == 1){ // 字母
//            switch (c){
//                case '1':
//                    c = 'I';
//                    break;
//                case '0':
//                    c = distinctionQ0UD(i);
//                    c = c == '0' ? 'O' : c;
//                    break;
//                case 'Q':
//                    c = distinctionQ0UD(i);
//                    c = c == '0' ? 'O' : c;
//                    break;
//                case 'O':
//                    c = distinctionQ0UD(i);
//                    c = c == '0' ? 'O' : c;
//                    break;
//                case 'U':
//                    c = distinctionQ0UD(i);
//                    c = c == '0' ? 'O' : c;
//                    break;
//                case 'D':
//                    c = distinctionQ0UD(i);
//                    c = c == '0' ? 'O' : c;
//                    break;
//                case '2':
//                    c = 'Z';
//                    break;
//                case '8':
//                    c = distinctionB8R(i);
//                    c = c == '8' ? 'B' : c;
//                    break;
//                case 'B':
//                    c = distinctionB8R(i);
//                    c = c == '8' ? 'B' : c;
//                    break;
//                case 'R':
//                    c = distinctionB8R(i);
//                    c = c == '8' ? 'B' : c;
//                    break;
//                case '6':
//                    c = 'G';
//                    break;
//                case '4':
//                    c = 'A';
//                    break;
//                case '7':
//                    c = 'Y';
//                    break;
//                case '3':
//                    c = 'S';
//                    break;
//                case '5':
//                    c = 'S';
//                    break;
//            }
//        }else if (form == 2){ // 数字
//            switch (c){
//                case 'I':
//                    c = '1';
//                    break;
//                case 'Q':
//                    c = '0';
//                    break;
//                case 'O':
//                    c = '0';
//                    break;
//                case 'U':
//                    c = '0';
//                    break;
//                case 'D':
//                    c = '0';
//                    break;
//                case 'Z':
//                    c = '2';
//                    break;
//                case 'B':
//                    c = '8';
//                    break;
//                case 'R':
//                    c = '8';
//                    break;
//                case 'G':
//                    c = '6';
//                    break;
//                case 'A':
//                    c = '4';
//                    break;
//                case 'Y':
//                    c = '7';
//                    break;
//                case 'S':
//                    c = '3';
//                    break;
//            }
//        }
//
//        return c;
//    }
//
//
//    private static Character distinctionA4(int index){
//        Bitmap bitmap = getCharBitmap(index);
//
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int x = (int) (width * 0.82);
//        int y = (int) (height * 0.17);
//
//        boolean b = judgeBlack(bitmap, x, y);
//
//        if (b){
//            return '4';
//        }else {
//            return 'A';
//        }
//
//
//    }
//
//    private static Character distinctionG6(int index){
//        Bitmap bitmap = getCharBitmap(index);
//
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int x = (int) (width * 0.23);
//        int y = (int) (height * 0.20);
//
//        boolean b = judgeBlack(bitmap, x, y);
//
//        if (b){
//            return 'G';
//        }else {
//            return '6';
//        }
//
//    }
//
//    private static Character distinctionI1(int index){
//
//        Bitmap bitmap = getCharBitmap(index);
//
//        int width = bitmap.getWidth();
//
//        if (width < 20){
//            return '1';
//        }else {
//            return 'I';
//        }
//
//    }
//
//
//    private static Character distinctionB8R(int index){
//
//        Bitmap bitmap = getCharBitmap(index);
//
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int x = width / 2;
//        int y = (int) (height * 0.90);
//
//        boolean b = judgeBlack(bitmap, x, y);
//
//        if (!b){
//            return 'R';
//        }
//
//        x = (int) (width * 0.18);
//        y = (int) (height * 0.47);
//
//        b = judgeBlack(bitmap, x, y);
//
//        if (b){
//            return 'B';
//        }else {
//            return '8';
//        }
//
//
////        return distinctionChar(index, "B8R");
//    }
//
//
//    private static Character distinctionQ0UD(int index){
//
//        Bitmap bitmap = getCharBitmap(index);
//
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int x = width / 2 + 4;
//        int y = (int) (height * 0.77 + 1);
//
//        boolean b = judgeBlack(bitmap, x, y);
//
//        if (b){
//            return 'Q';
//        }
//
//
//        x = (int) (width * 0.22 - 1);
//        y = (int) (height * 0.13 - 1);
//
//        b = judgeBlack(bitmap, x, y);
//
//        if (b){
//
//            x = width / 2;
//            y = (int) (height * 0.12);
//
//            b = judgeBlack(bitmap, x, y);
//
//            if (!b){
//                return 'U';
//            }else {
//                return 'D';
//            }
//
//        }else {
//            return '0';
//        }
//    }
//
//
//    private static Character distinctionQ0(int index){
//        System.out.println("调用  distinctionOQ----当前index是：：" + index);
////        return distinctonQ01(index);
//
////        return distinctionChar(index, "Q0");
//
//        return distinctonQ03(index);
//    }
//
//    private static Character distinctonQ01(int index){
//
//        MatOfPoint2f matOfPoint2f = distinctionMat(index);
//
//        System.out.println("matOfPoint2f.toArray().length = " + matOfPoint2f.toArray().length);
//        System.out.println("matOfPoint2f.total() = " + matOfPoint2f.total());
//
//        Bitmap bitmap = ShapeUtils4.cutoutShape(matOfPoint2f, licenseBitmap);
//
//        FileService.saveClassPhoto(bitmap, "test_" + index, "License/" + getSavePathIndex() + "/test");
//
//        if (matOfPoint2f.toArray().length < 6 && matOfPoint2f.total() < 6){
//            return 'Q';
//        }else {
//            return '0';
//        }
//
////        Bitmap bitmap = ShapeUtils4.cutoutShape(matOfPoint2f, licenseBitmap);
////
////        FileService.saveClassPhoto(bitmap, "test_" + index, "License/test");
////
////        FirstActivity.psmSingle(false);
////
////        FirstActivity.varWhiteList(0);
////
////        bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2, 0, bitmap.getWidth() / 2, bitmap.getHeight());
////
////        FirstActivity.tessBaseAPI.setImage(bitmap);
////
////        String text = FirstActivity.tessBaseAPI.getUTF8Text().trim();
////
////        System.out.println("text = " + text);
////
////        return text.length() > 0 ? 'Q' : '0';
//    }
//
//    private static Character distinctonQ02(int index){
//
//        Bitmap bitmap = getCharBitmap(index);
//
//        int amount = 0;
//
//        for (int x = 0; x < bitmap.getWidth(); x++) {
//            for (int y = 0; y < bitmap.getHeight(); y++) {
//                int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//                if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                    amount++;
//                }
//
//            }
//
//        }
//
//        System.out.println("黑色数量：：" + amount);
//
//        return amount >= 700 ? 'Q' : '0';
//
//    }
//
//    private static Character distinctonQ03(int index){
//
//        Bitmap bitmap = getCharBitmap(index);
//
//        int x = bitmap.getWidth() / 2 + 5;
//        int y = (int) (bitmap.getHeight() * 0.77 + 1);
//
//        boolean b = judgeBlack(bitmap, x, y);
//
//        if (b){
//            return 'Q';
//        }else {
//            return '0';
//        }
//
//    }
//
//
//
//
//
//    private static Bitmap getCharBitmap(int index){
//
//        MatOfPoint2f matOfPoint2f = distinctionMat(index);
//
//        Bitmap bitmap = ShapeUtils4.cutoutShape(matOfPoint2f, licenseBitmap);
//
//        FileService.saveClassPhoto(bitmap, "test_" + index, "License/" + getSavePathIndex() + "/test");
//
//        return bitmap;
//    }
//
//
//    private static boolean judgeBlack(Bitmap bitmap, int x, int y){
//
//        for (int i = 0; i < 5; i++) {
//
//            switch (i){
//                case 1:
//                    x--;
//                    break;
//                case 2:
//                    x += 2;
//                    break;
//                case 3:
//                    x--;
//                    y--;
//                    break;
//                case 4:
//                    y += 2;
//                    break;
//            }
//
//            int[] rgb = ImageDisposeUtils.getRGB(bitmap, x, y);
//
//            if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                return true;
//            }
//
//        }
//
//        return false;
//
//    }
//
//
//
//    private static Character distinctionChar(int index, String whiteList){
//
//        System.out.println("调用  distinctionChar-------当前index是：：" + index);
//
//        FirstActivity.psmSingle(false);
//
//        FirstActivity.varWhiteList(whiteList);
//
//        MatOfPoint2f matOfPoint2f = distinctionMat(index);
//
//        Bitmap bitmap = ShapeUtils4.cutoutShape(matOfPoint2f, licenseBitmap);
//
//        FileService.saveClassPhoto(bitmap, "test_" + index, "License/" + getSavePathIndex() + "/test");
//
//        System.out.println("point个数：" + matOfPoint2f.toArray().length + "total个数：" + matOfPoint2f.total());
//
//        FirstActivity.tessBaseAPI.setImage(bitmap);
//        String result = FirstActivity.tessBaseAPI.getUTF8Text();
//
//        System.out.println("result = " + result);
//
//        if (result == null || result.length() < 1){
//            return ' ';
//        }
//
//        return result.charAt(0);
//    }
//
//    private static boolean distinction(int index, int range){
//
//        MatOfPoint2f matOfPoint2f = distinctionMat(index);
//
//        Bitmap bitmap = ShapeUtils4.cutoutShape(matOfPoint2f, licenseBitmap);
//
//        FileService.saveClassPhoto(bitmap, "Test_" + index, "License/" + getSavePathIndex());
//
//        System.out.println("point个数：" + matOfPoint2f.toArray().length + "total个数：" + matOfPoint2f.total());
//
////        return new int[]{matOfPoint2f.toArray().length, (int) matOfPoint2f.total()};
//
//        if (matOfPoint2f.toArray().length < range && matOfPoint2f.total() < range){
//            return true;
//        }else {
//            return false;
//        }
//
//    }
//
//    /**
//     * 通过 下标 获取单个字符的 MatOfPoint2f
//     * @param index  下标
//     * @return
//     */
//    private static MatOfPoint2f distinctionMat(int index) {
//
//        List<MatOfPoint2f> grads = LicenseDector.licenseExtractChar();
//
//        if (grads.size() > 6) {
//            index += grads.size() - 6;
//        }
//
//        MatOfPoint2f matOfPoint2f = grads.get(index);
//
//        return matOfPoint2f;
//
//    }
//
//
//    /**
//     * 根据 车牌正则表达式 获取格式列表
//     * @return
//     */
//    public static List<Integer> getFormList(){
//        String s = CarData.licenseMatcherStr;
//        Log.e(TAG, "getFormList: " + s);
//        s = s.substring(1, s.length() - 1);
//
//        String[] split = s.split("\\}");
//
//        List<Integer> formList = new ArrayList<>(6);
//
//        for (String s1 : split) {
//            Log.e(TAG, "getFormList: " + s1);
//
//            int number = Integer.parseInt(s1.substring(s1.length() - 1));
//
//            int form = 0;
//
//            if (s1.startsWith("[A-Z0-9]")){
//                form = 0;
//            }else if (s1.startsWith("[A-Z]")){
//                form = 1;
//            }else if (s1.startsWith("[0-9]")){
//                form = 2;
//            }
//
//            for (int i = 0; i < number; i++) {
//                formList.add(form);
//            }
//        }
//
//        for (int i = formList.size(); i < 6; i++){
//            formList.add(0);
//        }
//
//        for (Integer integer : formList) {
//            System.out.println(integer);
//        }
//
//        return formList;
//    }
//
//
//    public static boolean judgeLicense(){
//        return judgeLicense(CarData.license);
//    }
//
//
//    public static boolean judgeLicense(String license){
//
//        System.out.println("CarData.license::" + license);
//
//        if ("".equals(license) || !license.matches(CarData.licenseMatcherStr)){
//
//            CarData.license = "";
//
//            return false;
//        }else {
//
//            CarData.licenseMap.put(AcceptCarOrder.getIndex(), CarData.license);
//
//            return true;
//        }
//    }
//
//
//
//}
