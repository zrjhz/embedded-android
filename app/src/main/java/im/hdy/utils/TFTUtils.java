package im.hdy.utils;

import car.car2024.ActivityView.FirstActivity;

/**
 * Created by hdy on 05/05/2018.
 */

public class TFTUtils {

    /**
     * @param which 1 上翻
     *              <p>
     *              2 下翻
     *              <p>
     *              3 自动翻页
     */
    public static void up_down_auto(int which) {
        switch (which) {
            case 1:
                FirstActivity.ConnectTransport.TFT_LCD(0x10, 0x01, 0x00, 0x00);
                break;
            case 2:
                FirstActivity.ConnectTransport.TFT_LCD(0x10, 0x02, 0x00, 0x00);
                break;
            case 3:
                FirstActivity.ConnectTransport.TFT_LCD(0x10, 0x03, 0x00, 0x00);
                break;
        }
    }


    /**
     * 指定图片显示
     *
     * @param which 0x01 第一页.后面依次类推
     */
    public static void appoint_pic_display(byte which) {
        FirstActivity.ConnectTransport.TFT_LCD(0x10, which, 0x00, 0x00);
    }

    /**
     * TFT车牌显示
     *
     * @param license 获取到的车牌
     */
    public static void TFT_plate_number(String license) {
        char[] chars = license.toCharArray();
        FirstActivity.ConnectTransport.TFT_LCD(0x20, chars[0], chars[1], chars[2]);
        FirstActivity.ConnectTransport.yanchi(1000);
        FirstActivity.ConnectTransport.TFT_LCD(0x21, chars[3], chars[4], chars[5]);
    }


    /**
     * TFT 计时
     * @param which
     *
     * 0 开始
     *
     * 1 关闭
     *
     * 2 停止
     */
    public static void tft_timer(int which){
        switch(which)
        {
            case 0:
                FirstActivity.ConnectTransport.TFT_LCD(0x30, 0x01, 0x00, 0x00);
                break;
            case 1:
                FirstActivity.ConnectTransport.TFT_LCD(0x30, 0x02, 0x00, 0x00);
                break;
            case 2:
                FirstActivity.ConnectTransport.TFT_LCD(0x30, 0x00, 0x00, 0x00);
                break;
        }
    }

    /**
     *
     * @param distance 距离
     *
     *  110mm
     *  220mm
     *
     *                 还没写完
     */
    public static void tft_distance(int distance){
        FirstActivity.ConnectTransport.TFT_LCD(0x50, 0x00, 0x01, 0x00);
    }

    /**
     *
     * @param bytes 数据
     *
     * 三个字节
     */
    public static void hex(byte[] bytes){
        FirstActivity.ConnectTransport.TFT_LCD(0x40, bytes[0], bytes[1], bytes[2]);
    }
}
