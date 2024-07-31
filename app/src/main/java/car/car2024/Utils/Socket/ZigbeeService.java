package car.car2024.Utils.Socket;

import car.car2024.ActivityView.FirstActivity;


/**
 * Created by zhy on 09/03/2019
 */
public class ZigbeeService {


    /**
     * TFT 向上翻一页
     */
    public static void TFT_UpShow() {
        for (int i = 0; i < 3; i++) {
            FirstActivity.ConnectTransport.TFT_LCD(0x10, 0x01, 0x00, 0x00, AcceptCarOrder.getIndex());
            ThreadUtils.sleep(200);
        }
    }


    /**
     * TFT 向下翻一页
     */
    public static void TFT_DownShow() {
        for (int i = 0; i < 3; i++) {
            FirstActivity.ConnectTransport.TFT_LCD(0x10, 0x02, 0x00, 0x00, AcceptCarOrder.getIndex());
            ThreadUtils.sleep(200);
        }
    }

    /**
     * TFT 自动翻页
     */
    public static void TFT_AutoShow() {
        for (int i = 0; i < 5; i++) {
            FirstActivity.ConnectTransport.TFT_LCD(0x10, 0x03, 0x00, 0x00, AcceptCarOrder.getIndex());
            ThreadUtils.sleep(200);
        }
    }


    /**
     * TFT HEX显示信息发送
     */
    public static void TFT_HEX(byte by1, byte by2, byte by3) {
        for (int i = 0; i < 5; i++) {
            FirstActivity.ConnectTransport.TFT_LCD(0x40, by1, by2, by3, AcceptCarOrder.getIndex());
            ThreadUtils.sleep(200);
        }
    }


    /**
     * 发送交通灯识别结果
     */
    public static void sendTrafficResult(int lightnum, byte type) {

        for (int j = 0; j < 4; j++) {

            FirstActivity.ConnectTransport.traffic_control(0x02, lightnum, type);

            ThreadUtils.sleep(200);
        }
    }
}
