package car.Identify.utils;


import android.graphics.Bitmap;
import car.Identify.utils.socketUtil.OcrSocketClient2;
import car.car2024.ActivityView.XcApplication;
import im.drh.utils.ToastUtil;

public class OcrIdentify {
    public static String content = "";

    /**
     * ocr识别，使用转发
     *
     * @param bitmap 图片
     */
    public static void ocrIdentify(Bitmap bitmap) {
        new Thread(new OcrSocketClient2(bitmap)).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ToastUtil.showToast(XcApplication.getApp(), OcrIdentify.content);
        System.out.println("OCR识别结果为: " + OcrIdentify.content);
    }
}
