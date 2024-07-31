package car.car2024.Utils.Socket;

import android.util.Log;

import car.car2024.FragmentView.LeftFragment;
import car.car2024.Utils.Command.Accept_Data;

import car.car2024.Utils.Socket.Variable;
import im.hdy.CarData;

/**
 * @author zhy
 * @create_date 2019-05-07 10:56
 */
public class AcceptCarData {

    public static void dispatch(byte[] bytes) {
        byte take = bytes[2];

        byte[] data = new byte[bytes[3]];

        try {
            System.arraycopy(bytes, 4, data, 0, bytes[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (take) {
            case Accept_Data.AcceptData_RFID1:
            case Accept_Data.AcceptData_RFID2:
            case Accept_Data.AcceptData_RFID3:
                CarData.rfidMap.put((int) take, new String(data));
                Log.e("RFID", String.valueOf(data));
                System.out.println("接受到的  RFID " + take + "信息为：：");

                for (byte b : data) {
                    Log.e("RFID", String.valueOf((char) b));
                    if (String.valueOf((char) b).equals("_")) break;
                    Variable.RFID.append((char) b);
                }
                System.out.println();
                break;

            case Accept_Data.AcceptData_DeputyQRCode:
//              CarData.deputy_QRCode = new String(data);
                Variable.deputy_QRCode = new String(data);
                System.out.println("接受到的  从车二维码信息为：：" + Variable.deputy_QRCode);
                // 用于信息处理完直接发送
//              QRCodeDeputy.disposeDataToAGV();
                break;

            case Accept_Data.AcceptData_Calculator:

                break;

            case Accept_Data.AcceptData_Var1:

                break;

            case Accept_Data.AcceptData_Var2:

                break;

            case Accept_Data.AcceptData_Var3:

                break;

            case Accept_Data.AcceptData_Var4:

                break;
        }
    }
}
