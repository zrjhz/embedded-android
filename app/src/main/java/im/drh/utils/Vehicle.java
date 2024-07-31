package im.drh.utils;

//import com.baidu.ai.edge.core.segment.SegmentationResultModel;


/**
 * 车辆识别类
 */
public class Vehicle {
    //单个车辆识别结果
    public static int VEHICLE;

    //多个车辆数量
    public static int VEHICLECOUNT = 0;
    //每个车辆的数量1为摩托车2为卡车3为汽车4为自行车
    public static int[] TYPEOFVEHICLE = new int[4];

    /**
     * 选取最有可能的车辆
     */
//    public static void getMax() {
//        System.out.println("进入车辆识别====================");
//        //如果无法识别，默认返回car
//        if (newShapeAndLicense.results == null) {
//            VEHICLE = 0x03;
//        }
//        //存储最大置信度
//        float MaxConf = 0;
//        String label = "";
//        //选出置信度最大的车辆
//        if (newShapeAndLicense.results != null) {
//            for (SegmentationResultModel result : newShapeAndLicense.results) {
//                if (result.getConfidence() > MaxConf && result.getConfidence() > Variable.VEHICLECONFIDENCE) {
//                    MaxConf = result.getConfidence();
//                    label = result.getLabel();
//                }
//            }
//        }
//        if (newShapeAndLicense.results != null) {
//            for (SegmentationResultModel result :
//                    newShapeAndLicense.results) {
//                System.out.println(result.getLabel() + "     " + result.getConfidence());
//            }
//        }
//        Log.i("车辆判断", label);
//        System.out.println("车辆判断result---------" + label);
//        getLabel(label);
//    }

    //识别多个车辆数量
//    public static void getMul() {
//        for (SegmentationResultModel result : newShapeAndLicense.results) {
//            if (result.getConfidence() > Variable.VEHICLECONFIDENCE) {
//                System.out.println(result.getLabel());
//                VEHICLECOUNT++;
//                switch (result.getLabel()) {
//                    case "motorcycle":
//                        TYPEOFVEHICLE[0]++;
//                        break;
//                    case "truck":
//                        TYPEOFVEHICLE[1]++;
//                        break;
//                    case "car":
//                        TYPEOFVEHICLE[2]++;
//                        break;
//                    case "bicycle":
//                        TYPEOFVEHICLE[3]++;
//                        break;
//                }
//            }
//        }
//    }


    /**
     * 根据识别结果返回协议，可与主车自行商量，或者采用这一套
     *
     * @param label
     */
    public static void getLabel(String label) {
        switch (label) {
            case "bicycle":
                VEHICLE = 0x01;
                break;
            case "motorcycle":
                VEHICLE = 0x02;
                break;
            case "car":
                VEHICLE = 0x03;
                break;
            case "truck":
                VEHICLE = 0x04;
                break;
        }
    }
}
