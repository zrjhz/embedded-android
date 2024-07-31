package car.Identify;

import android.graphics.Bitmap;

import car.Identify.constant.IdentifyConstant;
import car.Identify.utils.MaskIdentify;
import car.Identify.utils.OcrLisenceIdentify;
import car.Identify.utils.TrafficSignIdentify;

import car.Identify.utils.OcrIdentify;
import car.Identify.utils.QRCodeIdentify;
import car.Identify.utils.ShapeIdentify;
import car.Identify.utils.VehicleIdentify;


public class UsingUtil {
    /**
     * 使用该方法对tft进行识别
     *
     * @param bitmap 图片
     * @param str    tft常量
     */
    public static void tftMethodUsing(Bitmap bitmap, String... str) {
        if (bitmap == null) {
            return;
        }
        switch (str[0]) {
            case IdentifyConstant.OCR://文字
                OcrIdentify.ocrIdentify(bitmap);
                break;
            case IdentifyConstant.MASK://口罩
                MaskIdentify.maskIdentify(bitmap);
                break;
            case IdentifyConstant.SHAPE://图形
                ShapeIdentify.shapeIdentify(bitmap);
                break;
            case IdentifyConstant.QRCODE://二维码
                QRCodeIdentify.qrcodeIdentify(bitmap);
                break;
            case IdentifyConstant.VEHICLE://车辆
                VehicleIdentify.vehicleIdentify(bitmap);
                break;
            case IdentifyConstant.TRAFFIC_SIGN://交通标识
                TrafficSignIdentify.trafficSignIdentify(bitmap);
                break;
            case IdentifyConstant.LICENSE_ON_VEHICLE://车辆上的车牌
                if (str.length > 1) OcrLisenceIdentify.licenseOnVehicle(bitmap, str[1]);
                break;
            case IdentifyConstant.LICENSE://车牌
                OcrLisenceIdentify.licenseIdentify(bitmap);
                break;
        }
//        ZigbeeService.TFT_DownShow();
    }
}
