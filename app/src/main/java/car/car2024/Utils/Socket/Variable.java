package car.car2024.Utils.Socket;


import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;


/**
 * DigestUtils库包含md5,sha
 */

//TODO: 更改配置
public class Variable {
    //根据需求修改车牌底色判断
    //drh.utils包下的JudgeBitmapAndIdentify里面选择开启或者关闭车牌底色判断
//    public static String LICENSE=ColorName.GREEN;
    //tft1页数
    public static int TFTpage1 = 2;
    //tft2
    public static int TFTpage2 = 2;

    //1为有这种类型，0为没有这种类型
    //TFT中图片存在的类型下标0表示图形图像，下标1表示车牌，下标2表示车辆识别，下标3表示交通标志，下标4表示二维码识别，下标5表示文字识别
    public static int[] TFT1Vary = {1, 0, 0, 1, 0, 0};
    //TFT中图片存在的类型下标0表示图形图像，下标1表示车牌，下标2表示车辆识别，下标3表示交通标志，下标4表示二维码识别，下标5表示文字识别
    public static int[] TFT2Vary = {0, 1, 0, 0, 0, 0};
    //静态标志物中图片存在的类型下标0表示图形图像，下标1表示车牌，下标Ocr2表示车辆识别，下标3表示交通标志，下标4表示二维码识别，下标5表示文字识别
    public static int[] static1Vary = {0, 0, 0, 0, 1, 0};
    //静态标志物中图片存在的类型下标0表示图形图像，下标1表示车牌，下标2表示车辆识别，下标3表示交通标志，下标4表示二维码识别，下标5表示文字识别
    public static int[] static2Vary = {0, 0, 0, 0, 0, 0};
    //当前识别的下标，如果自己测试根据TFT修改，比赛中自动赋值不需要改
    public static int index = 1;
    //整体识别率,只有识别到的物体置信度大于它才可以进行后续判断，不建议修改，如果后期模型更完善可以改的更高
    public static float CONFIDENCE = 0.4f;
    //图形识别置信度，只有图形的置信度大于它才可以将其算入图形，下同
    public static float SHAPECONFIDENCE = 0.6f;
    //车辆识别置信度
    public static float VEHICLECONFIDENCE = 0.4f;
    //交通标志置信度
    public static float TRAFFICSIGN = 0.4f;
    //文字识别置信度
    public static int WORDCONFIDENCE = 60;

    //图像分割序列号,每次都要重新请求，有效期3个月，到百度EASYDL里面的图形分割下申请序列号，具体自己看百度文档
    public static final String SERIAL_NUM = "A74F-0223-9FBB-687D";
//    public static final String SERIAL_NUM1 = "2D75-9B57-3C64-1526";


    //存储二维码信息
    public static Map<Integer, Result[]> QRMAP = new HashMap<>();
    //存储RFID
    public static StringBuffer RFID = new StringBuffer();
    public static String RFIDResult = "";

    public static String deputy_QRCode = "";
}
