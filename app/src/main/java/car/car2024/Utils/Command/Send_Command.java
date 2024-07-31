package car.car2024.Utils.Command;

public interface Send_Command {
    final byte FromHost_QRCodeRecognition = (byte)  0x00; // 二维码识别
    final byte FromHost_TrafficLight = (byte)  0x01;      // 交通灯
    final byte FromHost_TFTRecognition = (byte)  0x02;    // TFT识别
    final byte FromHost_Start = (byte)  0x03;             // 小车启动命令
    final byte FromHost_Completed = (byte)  0x04;         // 安卓识别任务完成
    final byte FromHost_1 = (byte)  0x20; // 安卓任务一
    final byte FromHost_2 = (byte)  0x21; // 安卓任务二
    final byte FromHost_3 = (byte)  0x22; // 安卓任务三
    final byte FromHost_4 = (byte)  0x23; // 安卓任务四
    final byte FromHost_5 = (byte)  0x24; // 安卓任务五
    final byte FromHost_6 = (byte)  0x25; // 安卓任务六
    final byte FromHost_7 = (byte)  0x26; // 安卓任务七
    final byte FromHost_8 = (byte)  0x27; // 安卓任务八
    final byte FromHost_9 = (byte)  0x28; // 安卓任务九
}
