package car.car2024.Utils.Command;

public interface Accept_Command {
    /***************************************请求命令 Request_XX**************************************************/
    byte Zigbee_TFT_A = 0x0B;         // 多功能信息显示A (TFT)
    byte Zigbee_TFT_B = 0x08;         // 多功能信息显示B (TFT)
    byte Zigbee_TFT_C = 0x12;         // 多功能信息显示C (TFT)
    byte Zigbee_TrafficLight_A = 0x0E; // 智能交通信号灯A
    byte Zigbee_TrafficLight_B = 0x0F; // 智能交通信号灯B
    byte Zigbee_TrafficLight_C = 0x13; // 智能交通信号灯C
    byte Zigbee_TrafficLight_D = 0x14; // 智能交通信号灯D
    byte Zigbee_Static_A = 0x15;       // 静态标志物A (自定义)
    byte Zigbee_Static_B = 0x09;       // 静态标志物B (自定义)
    byte TFT_Task_Shape = 0x01;       // 识别形状
    byte TFT_Task_License = 0x02;       // 识别车牌
    byte TFT_Task_TrafficSign = 0x03;       // 识别交通灯
    byte TFT_Task_Mask = 0x04;       // 识别口罩

    byte Task_1 = 0x20; //任务一
    byte Task_2 = 0x21; //任务二
    byte Task_3 = 0x22; //任务三
    byte Task_4 = 0x23;
}
