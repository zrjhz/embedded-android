package car.car2024.Utils.Command;

/**
 * @author zhy
 * @create_date 2019-05-07 11:02
 */

/**
 * 用于存放接受小车上传的数据 相应的id
 */
public interface Accept_Data {
    byte AcceptData_RFID1 = 1;
    byte AcceptData_RFID2 = 2;
    byte AcceptData_RFID3 = 3;
    byte AcceptData_DeputyQRCode = 0;
    byte AcceptData_Calculator = 4;
    byte AcceptData_Var1 = 5;
    byte AcceptData_Var2 = 6;
    byte AcceptData_Var3 = 7;
    byte AcceptData_Var4 = 8;
}
