package car.car2024.Utils.Socket;

import static car.Identify.utils.QRCodeIdentify.qrcodeUsingColorMap;
import static car.Identify.utils.ShapeIdentify.colorName;
import static car.Identify.utils.ShapeIdentify.shapeColorMap;
import static car.Identify.utils.ShapeIdentify.shapeCountMap;
import static car.Identify.utils.ShapeIdentify.shapeName;

import java.io.UnsupportedEncodingException;

import car.Identify.constant.IdentifyConstant;
import car.Identify.utils.LicenseIdentify;
import car.Identify.utils.LightIdentify;
import car.Identify.utils.OcrLisenceIdentify;
import car.Identify.utils.ShapeIdentify;
import car.car2024.ActivityView.FirstActivity;
import car.car2024.Utils.Camera.CameraUtils;
import car.car2024.Utils.Command.Accept_Command;
import car.car2024.Utils.Command.Send_Command;
import car.car2024.Utils.DataHandle.DataExtract;

import car.car2024.FragmentView.LeftFragment;
import car.Identify.utils.QRCodeIdentify;

@SuppressWarnings("unused")
// 接受小车命令
public class AcceptCarOrder {
    private static int index = 1;

    public static int getIndex() {
        return Math.min(index, 3);
    }

    public static void setIndex(int index) {
        AcceptCarOrder.index = Math.max(index, 1);
    }

    public static void dispatch(byte[] bytes) throws InterruptedException {
        byte take = bytes[2];
        switch (take) {
            case Accept_Command.Zigbee_Static_A:   // 静态标志物
                LeftFragment.cameraCommandUtil.postHttp(FirstActivity.IPCamera, (30 + 10 * 2 + 1), 0);
                Thread.sleep(3000);

                QRCodeIdentify.qrcodeIdentify(LeftFragment.bitmap);

                String green = qrcodeUsingColorMap.get("green");
                String red = qrcodeUsingColorMap.get("red");

                if (red == null) {
                    red = "<D3+AC6-5B68A>#5";
                }
                if (green == null) {
                    green = "010203345A8C";
                }

                SendDataUtils.result1 = DataExtract.extractSubstring(red).getBytes();
                SendDataUtils.result2 = DataExtract.stringToByte(green);

                FirstActivity.ConnectTransport.IdentifyComplete(Send_Command.FromHost_QRCodeRecognition);
                break;


            case Accept_Command.Zigbee_TrafficLight_A: // 交通灯
                setIndex(1);

                LeftFragment.cameraCommandUtil.postHttp(FirstActivity.IPCamera, (30 + 11 * 2 + 1), 0);
                Thread.sleep(3000);
                LightIdentify.sendLightCode(LeftFragment.bitmap, take);
                break;
            case Accept_Command.Zigbee_TFT_A:  // TFT识别
                setIndex(1);
                LeftFragment.cameraCommandUtil.postHttp(FirstActivity.IPCamera, (30 + 10 * 2 + 1), 0);
                Thread.sleep(3000);

                boolean plate;
                String pattern = "国[A-Z][0-9][0-9][A-Z][0-9][0-9]";
                for (int i = 0; i < 5; i++) {
                    OcrLisenceIdentify.licenseIdentify(LeftFragment.bitmap);
                    plate = LicenseIdentify.licenseString.matches(pattern);
                    if (plate) {
                        break;
                    }
                    ZigbeeService.TFT_DownShow();
                    Thread.sleep(3000);
                }
                FirstActivity.ConnectTransport.IdentifyComplete(Send_Command.FromHost_TFTRecognition);
                System.out.println(LicenseIdentify.licenseString);
                break;
            case Accept_Command.Zigbee_TFT_B:
                setIndex(2);
                LeftFragment.cameraCommandUtil.postHttp(FirstActivity.IPCamera, (30 + 10 * 2 + 1), 0);
                Thread.sleep(3000);

                boolean shape = false;

                do {
                    ShapeIdentify.shapeIdentify(LeftFragment.bitmap);
                    Thread.sleep(1000);
                    shape = ShapeIdentify.Bcount >= 10 && ShapeIdentify.Bcount <= 30;
                    ZigbeeService.TFT_DownShow();
                    Thread.sleep(3000);
                } while (!shape);

                String A = "A" + shapeCountMap.get(shapeName[0]);
                String D = "D" + shapeCountMap.get(shapeName[3]);
                String E2 = "E" + shapeCountMap.get(shapeName[4]);

                for (int i = 0; i < 3; i++) {
                    if (shapeColorMap.get(colorName[0]) > 9) {
                        shapeColorMap.put(colorName[0], 9);
                    }
                }

                String r = "F" + shapeColorMap.get(colorName[0]);
                String g = "F" + shapeColorMap.get(colorName[1]);
                String b = "F" + shapeColorMap.get(colorName[2]);

                FirstActivity.ConnectTransport.TFT_LCD(0x40, Integer.parseInt(A, 16), Integer.parseInt(D, 16), Integer.parseInt(E2, 16), 2);
                Thread.sleep(1000);
                FirstActivity.ConnectTransport.digital(2, Integer.parseInt(r, 16), Integer.parseInt(g, 16), Integer.parseInt(b, 16));
                Thread.sleep(1000);
                FirstActivity.ConnectTransport.task_completed();
                Thread.sleep(1000);
                break;
        }
    }
}
