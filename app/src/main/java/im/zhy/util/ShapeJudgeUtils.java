//package im.zhy.util;
//
//import android.graphics.*;
//
////import com.baidu.ai.edge.core.base.BaseException;
////import com.baidu.ai.edge.core.classify.ClassificationResultModel;
////import com.baidu.ai.edge.core.detect.DetectionResultModel;
//import com.utils.FileService;
//import com.utils.ShapeUtils4;
//
//import im.hdy.CarData;
//import im.zhy.param.ColorName;
//import im.zhy.shapeDector.model.ClassifyResultModel;
//import im.zhy.shapeDector.model.DetectResultModel;
//import org.opencv.android.Utils;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfPoint2f;
//import org.opencv.core.Point;
//
//import java.util.*;
//
//import car.car2024.ActivityView.LoginActivity;
//import im.zhy.param.ShapeName;
//
//@SuppressWarnings("all")
//public class ShapeJudgeUtils {
//
//    static int[][] shapeNumber = new int[5][1];
//
//    public static int shapeJudge(MatOfPoint2f matOfPoint2f){
//
//
//        String shapeName;
//        long total = matOfPoint2f.total();
//        Point[] points = matOfPoint2f.toArray();
//
//        if(total == 3 || points.length == 3){
//
//            // 三角形
//            shapeName = ShapeName.TRI;
//
//        }else if(total == 4 || points.length == 4){
//            // 判断是矩形还是菱形
//            shapeName = judgeSquOrRhom(matOfPoint2f);
//        }else {
//            if(total == 5 && points.length == 5){
//                shapeName = ShapeName.STAR;
//            }else{
//                shapeName = judgeCriOrStar(matOfPoint2f);
//            }
//
//        }
//
//        return GetVarName.getShapeInt(shapeName);
//    }
//
//
//    /**
//     * 好像不是EasyDL的方法
//     * @param matOfPoint2f
//     * @param bitmap
//     * @return
//     */
//    public static int newShapeJudge(MatOfPoint2f matOfPoint2f, Bitmap bitmap){
//
//
//        String shapeName;
//        System.out.println(matOfPoint2f);
//        System.out.println("newShapeJudge"+bitmap);
//        long total = matOfPoint2f.total();
//        Point[] points = matOfPoint2f.toArray();
//
//        if(total == 3 || points.length == 3){
//
//            // 三角形
//            shapeName = ShapeName.TRI;
//
//        }else if(total == 4 || points.length == 4){
//            // 判断是矩形还是菱形
//            shapeName = judgeSquOrRhom(matOfPoint2f);
//            if (ShapeName.RHOM.equals(shapeName)){
//                shapeName = judgeSquOrRhomBaiduEasy(matOfPoint2f, bitmap);
//            }
//        }else {
//            // 判断是五角星还是圆形
//            shapeName = judgeCriOrStarBaiduEasy(matOfPoint2f, bitmap);
//        }
//
//        return GetVarName.getShapeInt(shapeName);
//    }
//
//
//    private static String judgeSquOrRhom(MatOfPoint2f matOfPoint2f){
//        Point[] points = matOfPoint2f.toArray();
//
//        Arrays.sort(points, new Comparator<Point>() {
//            @Override
//            public int compare(Point t1, Point t2) {
//                if (t1.x == t2.x){
//                    return (int) (t1.y - t2.y);
//                }else {
//                    return (int) (t1.x - t2.x);
//                }
//            }
//        });
//
//        List<Double> lenghtList = new ArrayList<>();
//        double lenghtCount = 0;
//        // 求两点长度
//        for (int i1 = 0; i1 < points.length; i1++) {
//            int i2 = i1 + 1;
//            if (i1 == points.length - 1){
//                i2 = 0;
//            }
//
//            int x1 = (int) points[i1].x;
//            int y1 = (int) points[i1].y;
//
//            int x2 = (int) points[i2].x;
//            int y2 = (int) points[i2].y;
//
//            double lenght = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
//            lenghtList.add(lenght);
//            lenghtCount += lenght;
//        }
//
//        Collections.sort(lenghtList);
//
//        if (lenghtList.get(lenghtList.size() - 1) - lenghtList.get(0) > 10){
//            if (lenghtList.get(1) - lenghtList.get(0) < 3){
//                if (lenghtList.get(lenghtList.size() - 1) - lenghtList.get(lenghtList.size() - 2) < 3){
//                    System.out.println("矩形");
//                    return ShapeName.SQU;
//                }
//            }
//        }
//
//        double lengthAvg = lenghtCount / lenghtList.size();
//
//        boolean b = true;
//
//        for (int i = 0; i < lenghtList.size(); i++) {
//            System.out.println("边 " + i + " 长度为：" + lenghtList.get(i));
//        }
//
//        for (Double lenght : lenghtList) {
//            if (lenght < lengthAvg - 3 || lenght > lengthAvg + 3){
//                b = false;
//                break;
//            }
//        }
//
//        Point[] points_1 = new Point[3];
//
//        for (int i = 0; i < points.length; i++) {
//            int p = i;
//
//            for (int j = 0; j < 3; j++) {
//
//                if (p == points.length){
//                    p = 0;
//                }
//
//                points_1[j] = points[p];
//
//                p++;
//            }
//
//            double degrees = angle(points_1[0], points_1[1], points_1[2]);
//
//            System.out.println("角度为：：" + degrees);
//
//            int n = 0;
//            if (degrees >= 80 && degrees <= 100 && Double.isNaN(degrees)){
//               n++;
//            }
//
//            if (b){
//                if (n > 1){
//                    System.out.println("矩形");
//                    return ShapeName.SQU;
//                }
//            }else {
//                if (n == 1){
//                    System.out.println("矩形");
//                    return ShapeName.SQU;
//                }
//            }
//
//        }
//
//        System.out.println("菱形");
//        return ShapeName.RHOM;
//
//    }
//
//
//    // 根据三个坐标求角度
//    private static double angle(Point first, Point second, Point cen) {
//        double x1 = first.x;
//        double y1 = first.y;
//
//        double x2 = second.x;
//        double y2 = second.y;
//
//        double x3 = cen.x;
//        double y3 = cen.y;
//        double k1 = (y2 - y1) / (x2 - x1);
//        double k2 = (y3 - y2) / (x3 - x2);
//        // tanθ=(k2- k1）/(1+ k1k2）
//        // 获得两条直线的夹角
//        double angle = Math.atan((k2 - k1) / (1 + k1 * k2));
//        double degrees = Math.abs(Math.toDegrees(angle));
//
//        return degrees;
//    }
//
//
//    private static String judgeSquOrRhomBaiduEasy(MatOfPoint2f matOfPoint2f, Bitmap bitmap){
//        Bitmap shapeBitmap = getMatRangeBitmap(bitmap, matOfPoint2f);
//
//        List<DetectResultModel> resultModels = getResultList(shapeBitmap);
//
//        for (int i = 0; i < resultModels.size(); i++) {
//            DetectResultModel model = resultModels.get(i);
//
//            if (model.getConfidence() > 0.3f){
//                String shapeName = model.getName();
//
//                if (ShapeName.RHOM.equals(shapeName) || ShapeName.SQU.equals(shapeName)){
//                    return shapeName;
//                }else {
//                    continue;
//                }
//
//            }else {
//                continue;
//            }
//        }
//
//        return ShapeName.RHOM;
//    }
//
//
//
//    private static String judgeCriOrStar(MatOfPoint2f matOfPoint2f){
//        Point[] points = matOfPoint2f.toArray();
//        Map<String, int[]> map = ShapeUtils4.shapeScope(points, 30000, 30000);
//        int max_x = map.get("max_x")[0];
//        int min_x = map.get("min_x")[0];
//        int max_y = map.get("max_y")[0];
//        int min_y = map.get("min_y")[0];
//
//        int max_x_y = map.get("max_x")[1];
//        int min_x_y = map.get("min_x")[1];
//        int max_y_x = map.get("max_y")[1];
//        int min_y_x = map.get("min_y")[1];
//
//        int center_x = (max_x + min_x)/2;
//        int center_y = (max_y + min_y)/2;
//
//        int r = ((max_x - center_x)+(max_y - center_y))/2;
//
//        int number = 0;
//        for (int i = 0; i < points.length; i++) {
//            double x = points[i].x;
//            double y = points[i].y;
//            double xx = (x - center_x)*(x - center_x);
//            double yy = (y - center_y)*(y - center_y);
//            int distance = (int) Math.sqrt((xx+yy));
//
//            int distance2 = Math.abs(distance - r);
//
//            if(distance2 <= 6 && distance2 >= 0){
//                number++;
//            }
//        }
//
//        System.out.println(number);
//
//
//
//        if(number == points.length){
//            // 圆形
//            return ShapeName.CIR;
//        }else{
//            // 五角星
//            return ShapeName.STAR;
//        }
//
//
//    }
//
//    private static String judgeCriOrStarBaiduEasy(MatOfPoint2f matOfPoint2f, Bitmap bitmap){
//        Bitmap shapeBitmap = getMatRangeBitmap(bitmap, matOfPoint2f);
//
////        FileService.saveClassPhoto(shapeBitmap, "cri_star", "test");
//
//        List<DetectResultModel> resultModels = getResultList(shapeBitmap);
//
//        for (int i = 0; i < resultModels.size(); i++) {
//            DetectResultModel model = resultModels.get(i);
//
//            if (model.getConfidence() > 0.3f){
//                String shapeName = model.getName();
//
//                if (ShapeName.CIR.equals(shapeName) || ShapeName.STAR.equals(shapeName)){
//                    return shapeName;
//                }else {
//                    continue;
//                }
//
//            }else {
//                continue;
//            }
//        }
//
//        if (matOfPoint2f.toArray().length < 5){
//
//            String shapeName = judgeSquOrRhom(matOfPoint2f);
//            if (ShapeName.RHOM.equals(shapeName)){
//                shapeName = judgeSquOrRhomBaiduEasy(matOfPoint2f, bitmap);
//            }
//            return shapeName;
//
//        }else {
//            return ShapeName.STAR;
//        }
//    }
//
//
////    public static void shapeJudgeEasyDLClassify(Bitmap colorBitmap, List<MatOfPoint2f> matOfPoint2fList, int colorInt){
////
////        System.out.println(GetVarName.getColorName(colorInt) + "开始------------------------");
////        for (int i = 0; i < matOfPoint2fList.size(); i++) {
////            MatOfPoint2f matOfPoint2f = matOfPoint2fList.get(i);
////            Bitmap bitmap = ShapeUtils4.cutoutShape(matOfPoint2f, colorBitmap);
////
////            List<ClassifyResultModel> resultModels = null;
////            try {
////                List<ClassificationResultModel> modelList = LoginActivity.mClassifyDLManager.classify(bitmap);
////                resultModels = fillClassificationResultModel(modelList);
////            } catch (BaseException e) {
////                e.printStackTrace();
////            }
////
////            resultModels.sort(new Comparator<ClassifyResultModel>() {
////                @Override
////                public int compare(ClassifyResultModel c1, ClassifyResultModel c2) {
////                    return c1.getConfidence() > c2.getConfidence() ? 1 : -1;
////                }
////            });
////
////            ClassifyResultModel result = resultModels.get(0);
////            System.out.println(result.getName() + ":::::" + result.getIndex() + "::::::::::::" + result.getConfidence());
////
////        }
////        System.out.println(GetVarName.getColorName(colorInt) + "结束------------------------");
////
////    }
//
//
//    public static void shapeJudgeEasyDLDetect(Bitmap bitmap, int amount, int colorInt){
//
//        List<DetectResultModel> resultModels = getResultList(bitmap);
//
//        List<DetectResultModel> resultModelList = new ArrayList<>();
//        for (DetectResultModel resultModel : resultModels) {
//            if (resultModel.getConfidence() >= 0.05f){
//                resultModelList.add(resultModel);
//            }
//        }
//
//        System.out.println(GetVarName.getColorName(colorInt) + "--------------------");
//        for (DetectResultModel detectResultModel : resultModelList) {
//            System.out.println(detectResultModel.getName() + " : " + detectResultModel.getConfidence());
//        }
//        System.out.println(GetVarName.getColorName(colorInt) + "--------------------");
//
//        List<DetectResultModel> shapeList = null;
//
//        if (resultModelList.size() <= amount){
//            shapeList = resultModelList;
//        }else {
//            shapeList = new ArrayList<>();
//
//            for (int i = 0; i < amount; i++) {
//                shapeList.add(resultModelList.get(i));
//            }
//        }
//
//        for (int i = 0; i < shapeList.size(); i++) {
//            DetectResultModel model = shapeList.get(i);
//            String shapeName = model.getName();
//            int shapeInt = GetVarName.getShapeInt(shapeName);
//
////            CarData.shapeColor[shapeInt][colorInt]++;
//
//            Rect rect = model.getBounds();
//            drawRect(rect, bitmap, shapeName + String.valueOf(model.getConfidence()), colorInt,i);
//        }
//
//    }
//
//    public static void newShapeJudgeEasyDLDetect(Bitmap bitmap, List<MatOfPoint2f> matOfPoint2fList, int colorInt){
//
//        System.out.println(GetVarName.getColorName(colorInt) + "  开始，进行图形检测-------------matOfPoint2fList.size::" + matOfPoint2fList.size());
//
//        int number = 0;
//
//        for (MatOfPoint2f matOfPoint2f : matOfPoint2fList) {
////            Bitmap colorBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565, true);
//            Bitmap colorBitmap = getMatRangeBitmap(bitmap, matOfPoint2f);
//
//            number++;
//
//            List<DetectResultModel> resultModels = getResultList(colorBitmap);
//
//            DetectResultModel model = resultModels.get(0);
//
//            int shapeInt;
//            if (model.getConfidence() > 0.3f){
//
//                String shapeName = model.getName();
//
//                System.out.println("shapeName = --------------------------------------" + shapeName);
//
//                if (ShapeName.TRI.equals(shapeName)){
//
//                    shapeInt = shapeJudge(matOfPoint2f);
//
//                    if (shapeInt != GetVarName.getShapeInt(ShapeName.TRI)){
//                        int time = 1;
//
//                        while (time < 6 && time < resultModels.size()){
//
//                            shapeName = resultModels.get(time).getName();
//
//                            if (!ShapeName.TRI.equals(shapeName)){
//                                break;
//                            }
//
//                            time++;
//
//                        }
//                    }
//
//
//                }
//
//                shapeInt = GetVarName.getShapeInt(shapeName);
//
////                CarData.shapeColor[shapeInt][colorInt]++;
//
//                System.out.println("Name::::::::::::::::::::::::" + shapeName + "  Confidence:::::::::::::::::::::::::::" + model.getConfidence());
//
//                drawRect(model.getBounds(), colorBitmap, shapeName + "_" + model.getConfidence(), colorInt, 0);
//            }else {
//                shapeInt = newShapeJudge(matOfPoint2f, bitmap);
//
////                CarData.shapeColor[shapeInt][colorInt]++;
//            }
//
//        }
//
//        System.out.println(GetVarName.getColorName(colorInt) + "  开始，进行图形检测结束-------------");
//
//    }
//
//
//
//    private static int number;
//    public static Bitmap getMatRangeBitmap(Bitmap bitmap, MatOfPoint2f matOfPoint2f){
//
//        Bitmap colorBitmap = Bitmap.createBitmap(bitmap);
//        Map<String, int[]> map = ShapeUtils4.shapeScope(matOfPoint2f.toArray(), bitmap.getWidth(), bitmap.getHeight());
//
//        int minX = map.get("min_x")[0];
//        int maxX = map.get("max_x")[0];
//        int minY = map.get("min_y")[0];
//        int maxY = map.get("max_y")[0];
//
//        Mat src = new Mat();
//        Utils.bitmapToMat(colorBitmap, src);
//
//        int width = src.cols();
//        int height = src.rows();
//        int cnum = src.channels();
//
//        byte[] bgra = new byte[cnum];
//
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//
//                if (x > minX && x < maxX){
//                    if (y > minY && y < maxY){
//                        continue;
//                    }
//                }
//
//                src.get(y, x, bgra);
//
//                for (int i = 0; i < cnum; i++) {
//                    bgra[i] = (byte) 0xff;
//                }
//
//                src.put(y, x, bgra);
//
//            }
//
//        }
//
//        Utils.matToBitmap(src, colorBitmap);
//
////        FileService.saveClassPhoto(colorBitmap, number++ + "", "shape/" + AcceptCarOrder.getIndex() + "/cutout_shape");
//
//        return colorBitmap;
//    }
//
////    private static  List<DetectResultModel> getResultList(Bitmap bitmap){
////        List<DetectionResultModel> list = null;
//////        try {
//////            list = LoginActivity.manager.detect(bitmap, 0);
//////        } catch (BaseException e) {
//////            e.printStackTrace();
//////        }
////        List<DetectResultModel> resultModels = fillDetectionResultModel(list);
////
////        Collections.sort(resultModels, new Comparator<DetectResultModel>() {
////            @Override
////            public int compare(DetectResultModel t1, DetectResultModel t2) {
////                return t1.getConfidence() > t2.getConfidence() ? -1 : 1;
////            }
////        });
////
////        return resultModels;
////    }
//
//    public static MatOfPoint2f judgePosition(List<MatOfPoint2f> list, Rect rect){
//        int top = rect.top;
//        int left = rect.left;
//
//        for (MatOfPoint2f matOfPoint2f : list) {
//            Map<String, int[]> map = ShapeUtils4.shapeScope(matOfPoint2f.toArray(), 3000, 3000);
//
//            int minX = map.get("min_x")[0];
//            int minY = map.get("min_y")[0];
//
//            if (top >= minY - 20 && top <= minY + 20){
//                if (left >= minX - 20 && left <= minX + 20){
//                    return matOfPoint2f;
//                }
//            }
//        }
//
//        return null;
//
//    }
//
//    public static void drawRect(Rect rect, Bitmap bitmap, String shapeName, int colorInt, int number){
//
//        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        Canvas canvas = new Canvas(bitmap1);
//
//        //图像上画矩形
//        Paint paint = new Paint();
//        switch (GetVarName.getColorName(colorInt)){
//            case ColorName.RED:
//                paint.setColor(Color.RED);
//                break;
//            case ColorName.GREEN:
//                paint.setColor(Color.GREEN);
//                break;
//            case ColorName.BLUE:
//                paint.setColor(Color.BLUE);
//                break;
//            case ColorName.YELLOW:
//                paint.setColor(Color.YELLOW);
//                break;
//            case ColorName.PURPLE:
//                paint.setColor(Color.MAGENTA);
//                break;
//            case ColorName.CYAN:
//                paint.setColor(Color.CYAN);
//                break;
//            case ColorName.BLACK:
//                paint.setColor(Color.BLACK);
//                break;
//            case ColorName.WHITE:
//                paint.setColor(Color.WHITE);
//                break;
//            default:
//                paint.setColor(Color.RED);
//                break;
//
//        }
//
//        paint.setStyle(Paint.Style.STROKE);//不填充
//        paint.setStrokeWidth(5);  //线的宽度
//        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
//
//
//        FileService.saveClassPhoto(bitmap1, shapeName + "_" + number,"color/" + GetVarName.getColorName(colorInt));
//
//
//
//    }
//
//
//    public static void testSavePhoto(Bitmap bitmap ,String shapeName){
//        System.out.println("正在保存：" + shapeName);
//        ShapeUtils4.shapeAmountAddOne(shapeName);
//        String fileName = String.valueOf(ShapeUtils4.getShapeAmout(shapeName));
//        FileService.saveClassPhoto(bitmap, fileName, shapeName);
//
//    }
//
//
//    public static List<DetectResultModel> fillDetectionResultModel(List<DetectionResultModel> modelList) {
//        List<DetectResultModel> results = new ArrayList<>();
//        for (int i = 0; i < modelList.size(); i++) {
//            DetectionResultModel mDetectionResultModel = modelList.get(i);
//            DetectResultModel mDetectResultModel = new DetectResultModel();
//            mDetectResultModel.setIndex(i + 1);
//            mDetectResultModel.setConfidence(mDetectionResultModel.getConfidence());
//            mDetectResultModel.setName(mDetectionResultModel.getLabel());
//            mDetectResultModel.setBounds(mDetectionResultModel.getBounds());
//            results.add(mDetectResultModel);
//        }
//        return results;
//    }
//
//    private static List<ClassifyResultModel> fillClassificationResultModel(
//            List<ClassificationResultModel> modelList) {
//        List<ClassifyResultModel> results = new ArrayList<>();
//        for (int i = 0; i < modelList.size(); i++) {
//            ClassificationResultModel mClassificationResultModel = modelList.get(i);
//            ClassifyResultModel mClassifyResultModel = new ClassifyResultModel();
//            mClassifyResultModel.setIndex(i + 1);
//            mClassifyResultModel.setConfidence(mClassificationResultModel.getConfidence());
//            mClassifyResultModel.setName(mClassificationResultModel.getLabel());
//            results.add(mClassifyResultModel);
//        }
//        return results;
//    }
//
//
//    public static void main(String[] args) {
//        for (int i = 0; i < 4; i++) {
//            int p = i;
//
//            for (int j = 0; j < 3; j++) {
//
//                if (p == 4){
//                    p = 0;
//                }
//
//                System.out.print(p + ",");
//
//                p++;
//            }
//
//            System.out.println();
//
//        }
//    }
//}
//
//
//
//
