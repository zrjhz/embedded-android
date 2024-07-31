package car.car2024.Utils.Socket;

import static car.Identify.utils.MaskIdentify.getMaskCount;
import static car.Identify.utils.ShapeIdentify.sendShapeColor;
import static car.Identify.utils.ShapeIdentify.sendShapeColorNumber;
import static car.Identify.utils.ShapeIdentify.sendShapeCount;

import java.io.UnsupportedEncodingException;

import car.car2024.ActivityView.FirstActivity;
import car.Identify.utils.LicenseIdentify;
import car.Identify.utils.LightIdentify;
import car.Identify.utils.OcrIdentify;
import car.Identify.utils.ShapeIdentify;
import car.Identify.utils.TrafficSignIdentify;
import car.Identify.utils.VehicleIdentify;
import car.car2024.Utils.Camera.CameraUtils;
import car.car2024.Utils.Socket.Variable;
import im.drh.utils.JudgeBitmapAndIdentify;
import im.zhy.data.dispose.StaticLandmarkOne;
import im.zhy.util.ShapeColorUtils;

public class SendDataUtils {
    //类型
    private static byte type;

    public static int index;
    public static int by;
    public static byte[] result1;
    public static byte[] result2;

    public static void setType(int cmd) {
        type = (byte) cmd;
    }

    /**
     * 根据协议协调要给小车发什么数据
     */
    public static void Control(byte[] bytes) {

        try {
            System.out.println("小车请求数据：" + bytes[0]);
            type = bytes[0];
            if (bytes.length > 1) {
                index = bytes[1];
            }
            switch (type) {
                case 1:
                    //车牌
                    sendLicense();
                    break;
                case 2:
                    //二维码1(烽火台，算法)
                    sendOneStaticLandmarkResult();
                    break;
                case 3:
                    //二维码2(待定)
                    sendTwoStaticLandmarkResult();
                    break;
                case 4:
                    //从车二维码
                    sendDeputyQRCode();
                    break;
                case 5:
                    //交通灯
                    sendTrafficLight();
                    break;
                case 6:
                    //形状个数
                    by = (bytes[2] - 1);
                    sendShapeCount((int) bytes[2] - 1);
                    break;
                case 7:
                    //颜色个数
                    sendShapeColor((int) bytes[2] - 1);
                    break;
                case 8:
                    //图形的颜色个数
                    sendShapeColorNumber((int) bytes[2] - 1, (int) bytes[3] - 1);
                    break;
                case 9:
                    //RFID白卡
                    sendRFIDDisposeResult();
                    break;
                case 10:
                    // TFT 数据显示
                    tftInfoShow();
                    break;
                case 11:
                    // 颜色总和信息
                    colorTypeNumber();
                    break;
                case 12:
                    // 形状总和信息
                    shapeTypeNumber();
                    break;
                case 13:
                    // 预备方案1
                    // 返回有效数据D
                    backD();
                    break;
                case 14:
                    // 预备方案3
                    backOrientation();
                    break;
                case 15:
                    // 设置下一个预设位
                    CameraUtils.cameraPreinstall();
                    break;
                case 16:
                    //发送交通标志
                    sendTrafficSign();
                    break;
                case 17:
                    //发送车辆识别
                    sendVehicle();
                    break;
                case 18:
                    //交通标志个数
                    sendTrafficSignCount();
                    break;
                case 19:
                    //文本识别
                    sendText();
                    break;
                case 20:
                    //车库位置
                    sendLocation();
                    break;
                case 21:
                    //发送汉字个数
                    sendChi();
                    break;
                case 22:
                    getMaskCount(bytes[2] - 1);
                    break;
            }
        } catch (Exception e) {
            System.out.println("数据请求出现异常");
            e.printStackTrace();
        }
    }

    /**
     * 发送汉字个数
     */
    public static void sendChi() {
        packageOneByte(OcrIdentify.content.length());
    }

    /**
     * 发送文本识别结果
     */
    private static void sendText() throws UnsupportedEncodingException {
        packageData(OcrIdentify.content.getBytes("gbk"));
//        packageData(.getBytes("gbk"));
    }

    /**
     * 发送交通标志有有效个数
     */
    public static void sendTrafficSignCount() {
        packageOneByte(TrafficSignIdentify.TRAFFIC_COUNT);
    }

    /**
     * 发送车库位置
     */
    public static void sendLocation() {
        packageData("4".getBytes());
    }

    /**
     * 发送车型
     */
    public static void sendVehicle() {
        packageOneByte(VehicleIdentify.VEHICLE);
    }

    /**
     * 发送交通标志
     */
    public static void sendTrafficSign() {
        System.out.println(TrafficSignIdentify.TRAFFIC_SIGN);
        packageOneByte(TrafficSignIdentify.TRAFFIC_SIGN);
    }

    /**
     * 发送车牌
     */
    private static void sendLicense() {
        //处理把有用的放在第一个
        String licenseString = LicenseIdentify.licenseString;
        String license;
        if (licenseString.equals("")) {
            license = "888888";
        } else {
            license = licenseString.substring(1);
        }
        byte[] bytes = license.getBytes();
        packageData(bytes);
    }


    /**
     * 发送形状的总个数
     *
     * @param shapeInt 形状编号
     */
    private static void sendShapeNumber(int shapeInt) {

        System.out.println("shapeInt = " + shapeInt);

        shapeInt = shapeInt < 1 ? 0 : shapeInt - 1;

        int number = ShapeColorUtils.shapeNumber(shapeInt);

        packageOneByte(number);

    }

    /**
     * 发送颜色总个数
     * @param colorInt  颜色编号
     */
//    private static void sendColorNumber(int colorInt){
//
//        System.out.println("colorInt = " + colorInt);
//
//        colorInt = colorInt < 1 ? 0 : colorInt - 1;
//
//        int number = ShapeColorUtils.colorNumber(colorInt);
//
//        packageOneByte(number);
//
//    }

    /**
     * 发送形状的那个颜色数量
     * @param shapeInt  形状编号
     * @param colorInt  颜色编号
     */
//    private static void sendShapeColorNumber(int shapeInt,int colorInt){
//
//        System.out.println("shapeInt = " + shapeInt);
//
//        shapeInt = shapeInt < 1 ? 0 : shapeInt - 1;
//
//        System.out.println("colorInt = " + colorInt);
//
//        colorInt = colorInt < 1 ? 0 : colorInt - 1;
//
//        packageOneByte(CarData.shapeColor[shapeInt][colorInt]);
//    }


    /**
     * 发送交通灯识别结果
     */
    private static void sendTrafficLight() {

//        Integer trafficLight = CarData.trafficLightMap.get(index);

        System.out.println("trafficLight = " + LightIdentify.TRAFFIC_LIGHT);

//        if (trafficLight == null || trafficLight < 1 || trafficLight > 3){
//
//            trafficLight = CarData.trafficLightMap.get(versaIndex());
//
//            if (trafficLight == null || trafficLight < 1 || trafficLight > 3){
//                trafficLight = TrafficLightName.RED;
//            }
//        }

        packageOneByte(LightIdentify.TRAFFIC_LIGHT);
    }

    // 对发送的内容只有一个byte的进行封装
    public static void packageOneByte(int number) {
        byte[] bytes = new byte[]{(byte) number};
        packageData(bytes);
    }


    // index 取反
    private static int versaIndex() {
        if (index == 1) {
            return 2;
        } else {
            return 1;
        }
    }


    /**
     * 发送第一个静态标志物处理后的结果，处理规则根据比赛规则
     */
    private static boolean oneDispose = true;

    private static void sendOneStaticLandmarkResult() {
        packageData(result1);
    }


    /**
     * 发送第二个静态标志物处理后的结果，处理规则根据比赛规则
     */

    private static boolean twoDispose = true;

    public static void sendTwoStaticLandmarkResult() throws UnsupportedEncodingException {
        packageData(result2);
    }

    public static void qrCodeDisposeInit() {
        oneDispose = true;
        twoDispose = true;
    }

    /**
     * 发送从车二维码处理后的结果，处理规则根据比赛规则
     */
    private static void sendDeputyQRCode() {
//        byte[] result = QRCodeDeputy.dispose();
//        System.out.println("从车二维码：" + new String(result));
        packageData(StaticLandmarkOne.useOne());
    }

    /**
     * 发送RFID白卡处理后的结果，处理规则根据比赛规则
     */
    private static void sendRFIDDisposeResult() {

//        String data = CarData.rfidMap.get(index);
//
//        if (data == null || data.length() == 0){
//            packageOneByte(0);
//            return;
//        }
//
//        byte[] result = RFID.dispose(data, index);
//
//        packageData(result);


        packageData(Variable.RFIDResult.getBytes());
    }

    /**
     * tft 数据显示
     */
    private static void tftInfoShow() {

        String s = "A代表矩形，a为矩形的数量（0~9）；B代表圆形，b为圆形的数量（0~9）；C代表三角形，c为三角形的数量（0~9）;D代表菱形，d为菱形的数量（0~9）;E代表五角星，e为五角星数量（0~9）";
//        String form = StrExtractUtils.autoCreateShapeShowForm(s, "ADE");

//        System.out.println("form = " + form);

//        packageData(tftHEX(form));

    }

    private static byte[] tftHEX(String s) {
//        List<String> list = StrExtractUtils.inRegexGetGroupStrs(s, "[A-Z]{1}[0-9]{1}");

        byte[] bytes = new byte[3];

        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = (byte) Integer.parseInt(list.get(i), 16);
        }

        return bytes;
    }

    /**
     * 发送颜色总和信息
     */
    private static void colorTypeNumber() {
        packageOneByte(ShapeIdentify.colorTypeNumber());
    }


    /**
     * 发送形状总和信息
     */
    private static void shapeTypeNumber() {
        packageOneByte(ShapeIdentify.shapeTypeNumber());
    }

    /**
     * 预备方案根据比赛规则自由更改，双方协议已经商量好，不需要过多的更改
     */

    private static void backD() {
//        System.out.println(CarData.prepareDataMap.get(1));
        String abc = "3";
        packageData(abc.getBytes());

    }


    private static void backOrientation() {
        byte[] bytes = new byte[2];
//        bytes[0] = Byte.parseByte(MessageData.carOrientations);
//        bytes[1] = Byte.parseByte(MessageData.carOrientations);
        packageData(bytes);

    }


//    private static void prepare_3(){
//        packageData(CarData.prepareDataMap.get(3));
//    }


    /**
     * 发送数据
     *
     * @param bytes 存储数据的bytes数组
     */
    public static void packageData(byte[] bytes) {
        // 包头
        byte top = 0x56;
        // 和小车进行数据交互的自定义协议
        byte pdu = 0x66;

        if (bytes == null || bytes.length < 1) {
            return;
        }

        byte[] sendDataByte = new byte[bytes.length + 3];

        System.arraycopy(bytes, 0, sendDataByte, 3, bytes.length);

        //封装包头
        sendDataByte[0] = top;
        sendDataByte[1] = pdu;
        sendDataByte[2] = type;

        System.out.println("类型：" + type + " 数据为：");
        System.out.println();

        for (byte aByte : bytes) {
            System.out.print(aByte + ",");
        }

        System.out.println();
        System.out.println("类型：" + type + " 结束------------");


        //发送数据
        FirstActivity.ConnectTransport.sendData(sendDataByte);
    }

    public static void main(String[] args) {
        int[][] shapeColor1 = new int[9][8];

        shapeColor1[0][2] = 1;
        shapeColor1[1][1] = 1;
        shapeColor1[2][3] = 1;
        shapeColor1[2][4] = 3;
        shapeColor1[3][0] = 1;
        shapeColor1[8][0] = 1;

//        CarData.shapeColorMap.put(1, shapeColor1);

//        ShapeColorUtils.selectData(1);


        tftInfoShow();
    }

}
