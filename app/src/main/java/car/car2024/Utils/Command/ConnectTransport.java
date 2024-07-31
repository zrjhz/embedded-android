package car.car2024.Utils.Command;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import car.car2024.ActivityView.FirstActivity;
import car.car2024.ActivityView.XcApplication;
import im.hdy.CarData;
import car.car2024.Utils.Socket.ThreadUtils;

/**
 * 连接小车的以及发送数据
 */
//@SuppressWarnings("all")
public class ConnectTransport {
    private int port = 60000;
    public static DataInputStream bInputStream = null;
    public static DataOutputStream bOutputStream = null;
    public static Socket socket = null;
    // 存放传来的数据
    private byte[] rbyte = new byte[40];
    private Handler reHandler;
    // 包头为两字节 0x55+type type识别为不同任务(详情看通信协议)
    public short TYPE = 0xAA;
    // 主指令
    public short MAJOR = 0x00;
    // 副指令第一位
    public short FIRST = 0x00;
    // 副指令第二位
    public short SECOND = 0x00;
    // 副指令第三位
    public short THRID = 0x00;
    // 校验位
    public short CHECKSUM = 0x00;
    //private String path = "/dev/ttyUSB0";
    private String path = "/dev/ttyS4";
    private int baudrate = 115200;

    private SerialPort mSerialPort = null;
    private static OutputStream SerialOutputStream;
    private InputStream SerialInputStream;
    private boolean Firstdestroy = false;  ////Firstactivity 是否已销毁了

    /**
     * 关闭各种流
     */
    public void destory() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                bInputStream.close();
                bOutputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 链接到socket地址
     *
     * @param reHandler
     * @param IP
     */
    public void connect(Handler reHandler, String IP) {
        try {
            this.reHandler = reHandler;
            Firstdestroy = false;
            //链接小车
            socket = new Socket(IP, port);

            bInputStream = new DataInputStream(socket.getInputStream());
            bOutputStream = new DataOutputStream(socket.getOutputStream());
            reThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 串口通信
     *
     * @param reHandler
     */
    public void serial_connect(Handler reHandler) {
        this.reHandler = reHandler;
        try {
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            SerialOutputStream = mSerialPort.getOutputStream();
            SerialInputStream = mSerialPort.getInputStream();
            //new Thread(new SerialRunnable()).start();
            //reThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        XcApplication.executorServicetor.execute(new SerialRunnable());
        //new Thread(new serialRunnable()).start();
    }

    byte[] serialreadbyte = new byte[20];

    class SerialRunnable implements Runnable {
        @Override
        public void run() {
            while (SerialInputStream != null) {
                try {
                    int num = SerialInputStream.read(serialreadbyte);
                    // String  readserialstr =new String(serialreadbyte);
                    String readserialstr = new String(serialreadbyte, 0, num, "utf-8");
                    Log.e("----serialreadbyte----", "******" + readserialstr);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = serialreadbyte;
                    reHandler.sendMessage(msg);
                    /*
                    for (int i = 0; i < num; i++) {
                        Log.e("----serialreadbyte----", "******" +Integer.toHexString(serialreadbyte[i]));
                      //  Log.e("----serialreadbyte----", "******" + serialreadbyte[i]);
                    }
                    */
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ThreadUtils.sleep(1);
            }
        }
    }

    /**
     *
     */
    private final Thread reThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (socket != null && !socket.isClosed()) {
                if (Firstdestroy)  //Firstactivity 已销毁了
                {
                    break;
                }
                try {
                    bInputStream.read(rbyte);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = rbyte;
                    reHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    /**
     * 发送指令
     */
    private void send() {
        // 为主指令和三位副指令的直接求和并对0XFF取余得到校验值
        // 原256 现改为0xff
        CHECKSUM = (short) ((MAJOR + FIRST + SECOND + THRID) % 0xff);
        // 发送数据字节数组
        final byte[] sbyte = {0x55, (byte) TYPE, (byte) MAJOR, (byte) FIRST, (byte) SECOND, (byte) THRID, (byte) CHECKSUM, (byte) 0xBB};

        sendRedlize(sbyte);
    }

    public void sendData(byte[] bytes) {
        sendRedlize(bytes);
    }

    /**
     * 主发送程序,所有的通过这个发送
     *
     * @param sbyte
     */
    private void sendRedlize(final byte[] sbyte) {
        // 采用Socket
        if (XcApplication.isserial == XcApplication.Mode.SOCKET) {
            XcApplication.executorServicetor.execute(() -> {
                try {
                    System.out.println("Mode.SOCKET");
                    if (socket != null && !socket.isClosed()) {
                        bOutputStream.write(sbyte);
                        bOutputStream.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            // 采用串口
        } else if (XcApplication.isserial == XcApplication.Mode.SERIAL) {

            XcApplication.executorServicetor.execute(() -> {
                try {
                    System.out.println("Mode.SERIAL");
                    SerialOutputStream.write(sbyte, 0, sbyte.length);
                    SerialOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            // 采用USB连接方式
        } else if (XcApplication.isserial == XcApplication.Mode.USB_SERIAL)
            try {
                System.out.println("Mode.USB_SERIAL");
                FirstActivity.sPort.write(sbyte, 5000);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 发送语音播报
     *
     * @param textbyte
     */
    public void send_voice(final byte[] textbyte) {
        if (XcApplication.isserial == XcApplication.Mode.SOCKET) {
            XcApplication.executorServicetor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (socket != null && !socket.isClosed()) {
                            bOutputStream.write(textbyte, 0, textbyte.length);
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (XcApplication.isserial == XcApplication.Mode.SERIAL) {

            XcApplication.executorServicetor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SerialOutputStream.write(textbyte, 0, textbyte.length);
                        SerialOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (XcApplication.isserial == XcApplication.Mode.USB_SERIAL)
            try {
                FirstActivity.sPort.write(textbyte, 5000);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void IdentifyComplete(byte cmd){
        MAJOR = cmd;
        SECOND = 0x00;
        THRID = 0x00;
        CHECKSUM = 0x00;
        send();
    }


    // 前进
    public void go(int sp_n, int en_n) {
        MAJOR = 0x02;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = (byte) (en_n & 0xff);
        THRID = (byte) (en_n >> 8);
        Log.i("已发送", Integer.toHexString(SECOND));
        Log.i("已发送", Integer.toHexString(THRID));
        Log.i("已发送", SECOND + "");
        Log.i("已发送", THRID + "");
        send();
    }

    // 后退
    public void back(int sp_n, int en_n) {
        MAJOR = 0x03;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = (byte) (en_n & 0xff);
        THRID = (byte) (en_n >> 8);
        send();
    }
    // 停车
    public void stop() {
        MAJOR = 0x01;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    // 循迹
    /*
    param sp_n: 速度
     */
    public void line(int sp_n) {  //寻迹
        MAJOR = 0x06;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    //清除码盘值
    public void clear() {
        MAJOR = 0x07;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    public void vice(int i) {//主从车状态转换
        if (i == 1) {//从车状态
            TYPE = 0x02;
            MAJOR = 0x80;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            yanchi(500);

            TYPE = (byte) 0xAA;
            MAJOR = 0x80;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            TYPE = 0x02;
        } else if (i == 2) {// 主车状态
            TYPE = 0x02;
            MAJOR = 0x80;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            yanchi(500);

            TYPE = (byte) 0xAA;
            MAJOR = 0x80;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            TYPE = 0xAA;
        }
    }
    //主车识别任务完成
    public void task_completed() {
        byte type = (byte) TYPE;
        TYPE = 0xAA;
        MAJOR = (byte) 0xB0;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }
    //智能交通灯
    public void traffic_control(int major, int first) {
        byte type = (byte) TYPE;
        TYPE = 0x0E;
        MAJOR = (byte) major;
        FIRST = (byte) first;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    //智能交通灯
    public void traffic_control(int major, int first, byte index) {
        byte type = (byte) TYPE;
        TYPE = index;
        MAJOR = (byte) major;
        FIRST = (byte) first;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    //立体车库控制
    public void garage_control(int major, int first) {
        byte type = (byte) TYPE;
        TYPE = 0x0D;
        MAJOR = (byte) major;
        FIRST = (byte) first;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    //openmv摄像头
    public void opencv_control(int major, int first) {
        byte type = (byte) TYPE;
        TYPE = 0x02;
        MAJOR = (byte) major;
        FIRST = (byte) first;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    // 闸门
    public void gate(int major, int first, int second, int third) {// 闸门
        byte type = (byte) TYPE;
        TYPE = 0x03;
        MAJOR = (byte) major;
        FIRST = (byte) first;
        SECOND = (byte) second;
        THRID = (byte) third;
        send();
        TYPE = type;
    }


    // 红外
    public void infrared(byte one, byte two, byte thrid, byte four, byte five,
                         byte six) {
        MAJOR = 0x10;
        FIRST = one;
        SECOND = two;
        THRID = thrid;
        send();
        yanchi(500);
        MAJOR = 0x11;
        FIRST = four;
        SECOND = five;
        THRID = six;
        send();
        yanchi(500);
        MAJOR = 0x12;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(1000);
    }

    // 双色led灯
    public void lamp(byte command) {
        MAJOR = 0x40;
        FIRST = command;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    // 指示灯
    public void light(int left, int right) {
        if (left == 1 && right == 1) {
            MAJOR = 0x20;
            FIRST = 0x01;
            SECOND = 0x01;
            THRID = 0x00;
            send();
        } else if (left == 1 && right == 0) {
            MAJOR = 0x20;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        } else if (left == 0 && right == 1) {
            MAJOR = 0x20;
            FIRST = 0x00;
            SECOND = 0x01;
            THRID = 0x00;
            send();
        } else if (left == 0 && right == 0) {
            MAJOR = 0x20;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        }
    }

    // 蜂鸣器
    public void buzzer(int i) {
        if (i == 1)
            FIRST = 0x01;
        else if (i == 0)
            FIRST = 0x00;
        MAJOR = 0x30;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    public void picture(int i) {// 图片上翻下翻
        if (i == 1)
            MAJOR = 0x50;
        else
            MAJOR = 0x51;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    public void gear(int i) {// 光照档位加
        if (i == 1)
            MAJOR = 0x61;
        else if (i == 2)
            MAJOR = 0x62;
        else if (i == 3)
            MAJOR = 0x63;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    public void fan() {// 风扇
        MAJOR = 0x70;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    //立体显示
    public void infrared_stereo(short[] data) {
        MAJOR = 0x10;
        FIRST = 0xff;
        SECOND = data[0];
        THRID = data[1];
        send();
        yanchi(500);
        MAJOR = 0x11;
        FIRST = data[2];
        SECOND = data[3];
        THRID = data[4];
        send();
        yanchi(500);
        MAJOR = 0x12;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(500);
    }

    public void gate(int i) {// 闸门
        byte type = (byte) TYPE;
        if (i == 1) {
            TYPE = 0x03;
            MAJOR = 0x01;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        } else if (i == 2) {
            TYPE = 0x03;
            MAJOR = 0x01;
            FIRST = 0x02;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        }
        TYPE = type;
    }

    //LCD 显示标志物进入计时模式
    public void digital_close() {//数码管关闭
        byte type = (byte) TYPE;
        TYPE = 0x04;
        MAJOR = 0x03;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    public void digital_open() {//数码管打开
        byte type = (byte) TYPE;
        TYPE = 0x04;
        MAJOR = 0x03;
        FIRST = 0x01;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    public void digital_clear() {//数码管清零
        byte type = (byte) TYPE;
        TYPE = 0x04;
        MAJOR = 0x03;
        FIRST = 0x02;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    public void digital_dic(int dis) {//LCD显示标志物第二排显示距离

        byte type = (byte) TYPE;
        int a = 0, b = 0, c = 0;
        a = (dis / 100) & (0xF);
        b = (dis % 100 / 10) & (0xF);
        c = (dis % 10) & (0xF);
        b = b << 4;
        b = b | c;
        TYPE = 0x04;
        MAJOR = 0x04;
        FIRST = 0x00;
        SECOND = (short) (a);
        THRID = (short) (b);
        send();
        TYPE = type;
    }

    public void digital(int i, int one, int two, int three) {// 数码管
        byte type = (byte) TYPE;
        TYPE = 0x04;
        if (i == 1) {//数据写入第一排数码管
            MAJOR = 0x01;
            FIRST = (byte) one;
            SECOND = (byte) two;
            THRID = (byte) three;
        } else if (i == 2) {//数据写入第二排数码管
            MAJOR = 0x02;
            FIRST = (byte) one;
            SECOND = (byte) two;
            THRID = (byte) three;
        }
        send();
        TYPE = type;
    }

    public void arm(int MAIN, int KIND, int COMMAD, int DEPUTY) {
        MAJOR = (short) MAIN;
        FIRST = (byte) KIND;
        SECOND = (byte) COMMAD;
        THRID = (byte) DEPUTY;
        send();
    }

    public void TFT_LCD(int MAIN, int KIND, int COMMAD, int DEPUTY)  //tft lcd
    {
        byte type = (byte) TYPE;
        TYPE = (short) 0x0B;
        MAJOR = (short) MAIN;
        FIRST = (byte) KIND;
        SECOND = (byte) COMMAD;
        THRID = (byte) DEPUTY;
        send();
        TYPE = type;
    }

    public void TFT_LCD(int MAIN, int KIND, int COMMAD, int DEPUTY, int index)  //tft lcd
    {
        byte type = (byte) TYPE;
        TYPE = (short) ((short) index == 1 ? 0x0B : 0x08);
        MAJOR = (short) MAIN;
        FIRST = (byte) KIND;
        SECOND = (byte) COMMAD;
        THRID = (byte) DEPUTY;
        send();
        TYPE = type;
    }

    public void magnetic_suspension(int MAIN, int KIND, int COMMAD, int DEPUTY) //磁悬浮
    {
        byte type = (byte) TYPE;
        TYPE = (short) 0x0A;
        MAJOR = (short) MAIN;
        FIRST = (byte) KIND;
        SECOND = (byte) COMMAD;
        THRID = (byte) DEPUTY;
        send();
        TYPE = type;
    }

    // 沉睡 单位毫秒ms  延迟
    public void yanchi(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // *******************TFT相关**************************

    /**
     * 向TFT显示发送车牌 需要有六位 如P779G9
     */
    public void sendTFTLicense(String license) {
//		license = license.substring(1);
//		int length = license.length();
//		if (license.length() < 6) {
//			// 说明车牌不全
//			for (int i = 0; i < (6 - length); i++) {
//				license += "0";
//			}
//		}
        TFT_LCD(0x20, license.charAt(0), license.charAt(1), license.charAt(2));
        yanchi(1000);
        TFT_LCD(0x21, license.charAt(3), license.charAt(4), license.charAt(5));
    }


    /**
     * 向TFT显示发送计时
     * <p>
     * 0 开始
     * <p>
     * 1 关闭
     * <p>
     * 2 停止
     */
    public void sendTFTTimer(int which) {
        yanchi(500);
        switch (which) {
            case 0:
                TFT_LCD(0x30, 0x01, 0x00, 0x00);
                break;
            case 1:
                TFT_LCD(0x30, 0x02, 0x00, 0x00);
                break;
            case 2:
                TFT_LCD(0x30, 0x00, 0x00, 0x00);
                break;

            default:
                break;
        }
    }

    /**
     * 向TFT显示发送距离
     * <p>
     * 0 10cm
     * <p>
     * 1 20cm
     * <p>
     * 2 30cm
     */
    public void sendTFTDistance(int which) {
        yanchi(500);
        switch (which) {
            case 0:
                TFT_LCD(0x50, 0x00, 0x01, 0x00);
                break;
            case 1:
                TFT_LCD(0x50, 0x00, 0x02, 0x00);
                break;
            case 2:
                TFT_LCD(0x50, 0x00, 0x03, 0x00);
                break;
            default:
                break;
        }
    }


    /**
     * 向TFT显示发送十六进制
     * <p>
     * 一共可以发送三个字节
     */
    public void sendTFTHex(int fisrt, int second, int third) {
        // 不知道是不是固定格式
        // 一会试试
        yanchi(500);
        TFT_LCD(0x40, fisrt, second, third);

    }

    /**
     * 向TFT显示发送图片
     * <p>
     * 0上翻
     * <p>
     * 1下翻
     * <p>
     * 2自动翻页
     */
    public void sendTFTPictureChange(int which) {
        yanchi(500);
        switch (which) {
            case 0:
                TFT_LCD(0x10, 0x01, 0x00, 0x00);
                break;
            case 1:
                TFT_LCD(0x10, 0x02, 0x00, 0x00);
                break;
            case 2:
                TFT_LCD(0x10, 0x03, 0x00, 0x00);
                break;
            default:
                break;
        }
    }

    /**
     * 向TFT显示发送指定图片显示
     * <p>
     * which 代表第几张图片.从1开始
     */
    public void sendTFTPictureItem(int which) {
        yanchi(500);
        TFT_LCD(0x00, which, 0x00, 0x00);
    }

    // *******************TFT相关**************************

    // *******************立体显示相关**************************

    /**
     * 向立体显示发送颜色信息
     * <p>
     * "红色0", "绿色1", "蓝色2", "黄色3", "紫色4", "青色5", "黑色6", "白色7"
     */
    public void sendDisplayColor(int which) {
        // 延迟100毫秒
        yanchi(100);
        short[] data = {0x00, 0x00, 0x00, 0x00, 0x00};
        data[0] = 0x13;
        data[1] = (short) (which + 0x01);
        infrared_stereo(data);
        yanchi(1000);
    }

    /**
     * 向立体显示发送形状信息
     * <p>
     * "矩形0", "圆形1", "三角形2", "菱形3", "梯形4", "饼图5", "靶图6","条形图7"
     */
    public void sendDisplayShape(int which) {
        yanchi(100);
        short[] data = {0x00, 0x00, 0x00, 0x00, 0x00};
        data[0] = 0x12;
        data[1] = (short) (which + 0x01);
        infrared_stereo(data);
        yanchi(1000);
    }

    /**
     * 向立体显示发送路况信息
     * <p>
     * 隧道有事故，请绕行", "前方施工，请绕行
     */
    public void sendDisplayRoad(int which) {
        yanchi(100);
        short[] data = {0x00, 0x00, 0x00, 0x00, 0x00};
        data[0] = 0x14;
        data[1] = (short) (which + 0x01);
        infrared_stereo(data);
        yanchi(1000);
    }

    /**
     * 向立体显示发送距离信息
     *
     * @param disNum 距离mm
     */
    public void sendDisplayDis(long disNum) {
        yanchi(100);
        short[] data = {0x00, 0x00, 0x00, 0x00, 0x00};
        data[0] = 0x11;
        data[1] = (short) (disNum / 10 + 0x30);
        data[2] = (short) (disNum % 10 + 0x30);
        infrared_stereo(data);
    }

    /**
     * 向立体显示发送车牌信息 建议八个字节.如果没有八个字节,进行手动修改
     */
    public void sendDisplayLicense(String str) {
        short[] data = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        short[] li = StringToBytes(str);
        // 需要八个字节
        data[0] = (short) (li[0]);
        data[1] = (short) (li[1]);
        data[2] = (short) (li[2]);
        data[3] = (short) (li[3]);
        data[4] = (short) (li[4]);
        data[5] = (short) (li[5]);

        infrared_stereo(data);
    }

    // 从string中得到short数据数组
    private short[] StringToBytes(String licString) {
        Log.i("licString", licString);
        if (licString == null || licString.equals("")) {
            return null;
        }
        licString = licString.toUpperCase();// 字符转换为大写后的字符串。

        // int length = licString.length();
        char[] hexChars = licString.toCharArray();
        int length = hexChars.length;
        short[] d = new short[]{0, 0, 0, 0, 0, 0};
        for (int i = 0; i < length; i++) {

            d[i] = (short) hexChars[i];

            Log.i("data", d[i] + "");
        }

        // Log.i("dddddddd",d[0] +"," +d[1]+"," +d[2]+"," +d[3]+"," +d[4] +","
        // +d[5]);
        return d;
    }

    // *******************立体显示相关**************************


    // *******************自定义函数****************************
    // 交通灯
    public void light_traffice(int state) {
        if (state == 1) {
            FIRST = 0x10;// 红色
        } else if (state == 2) {
            FIRST = 0x20;// 黄
        } else if (state == 3) {
            FIRST = 0x30;// 绿
        } else {
            FIRST = 0x10;// 红色
        }
        MAJOR = 0xA5;
        // 交通灯识别判断
        SECOND = 0x00;
        THRID = 0x00;
        CHECKSUM = 0x00;
        send();

        // 调整摄像头预设位
//        CameraUtils.cameraPreinstall();
    }

    //报警器
    //需要自定义的参数
    public void arm(short[] bytes) {
        MAJOR = 0xA7;
        FIRST = CarData.arm_code[0];
        SECOND = CarData.arm_code[1];
        THRID = CarData.arm_code[2];
        send();
        yanchi(500);
        MAJOR = 0xA8;
        FIRST = CarData.arm_code[3];
        SECOND = CarData.arm_code[4];
        THRID = CarData.arm_code[5];
        send();
    }


    //从车入库位置
    public void deputy_location(byte b) {
        MAJOR = 0xA9;
        FIRST = b;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }


    //立体车库层数
    public void stereoscopic(byte b) {
        MAJOR = 0xB1;
        FIRST = b;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    //立体显示
    public void stereos_display(short[] data) {
        MAJOR = data[0];
        FIRST = data[1];
        SECOND = data[2];
        THRID = data[3];
        send();
    }

    //发送LED标志物
    public void send_led(byte first, byte second, byte third) {
        MAJOR = 0xC1;
        FIRST = first;
        SECOND = second;
        THRID = third;
        send();
    }

    /**
     * 发送从车的车头 出发位置 目的地
     *
     * @param headstock 车头朝向 左3 右4 上2 下1
     * @param start     出发地址 （1，0），你就发0x10
     *                  [
     *                  <p>
     *                  具体看地图
     *                  ]
     * @param put_in    目的地地址 比如（1，0），你就发0x10
     *                  [
     *                  <p>
     *                  具体看地图
     *                  ]
     */
    public void sendDuputyCarLocation(short headstock, short start, short put_in) {
        MAJOR = 0x71;
        FIRST = headstock;
        SECOND = start;
        THRID = put_in;
        send();
    }


    /**
     * @param i 类型
     *          0 到达第一层
     *          1 到达第二层
     *          2 到达第三层
     *          3 到达第四层
     *          4 请求返回车库位于第几层
     *          5 请求返回前后侧红外状态
     */
    public void send_stereo_garage(int i) {
        switch (i) {
            case 0:  //到达第一层
                FirstActivity.ConnectTransport.garage_control(0x01, 0x01);
                break;
            case 1:  //到达第二层
                FirstActivity.ConnectTransport.garage_control(0x01, 0x02);
                break;
            case 2:  //到达第三层
                FirstActivity.ConnectTransport.garage_control(0x01, 0x03);
                break;
            case 3:  //到达第四层
                FirstActivity.ConnectTransport.garage_control(0x01, 0x04);
                break;
            case 4:  //请求返回车库位于第几层
                FirstActivity.ConnectTransport.garage_control(0x02, 0x01);
                break;
            case 5:  //请求返回前后侧红外状态
                FirstActivity.ConnectTransport.garage_control(0x02, 0x02);
                break;
            default:
                break;
        }
    }

    public void start() {
        MAJOR = Send_Command.FromHost_Start;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        System.out.println("全自动");
        send();

        yanchi(1000);

        // 调整摄像头预设位
//        CameraUtils.cameraPreinstall();
    }


    /**
     * 立体车库完成
     */
    public void car_complete() {
        MAJOR = 0xB1;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }
    // *******************自定义函数****************************


    /*
     * 全自动启动指令
     * **/
    private void Start_Up() {
        MAJOR = (short) 0x1A;
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        while (rbyte[2] != 0x1A)            // 等待到TFT位置
        {
            Log.e("tab", "状态" + rbyte[2]);
        }
    }

    //三角形 圆 矩形 五角星 菱形 保留
    public short[] Shape_NumToMastTab = {0x01, 0x02, 0x03, 0x01, 0x01, 0x00};


    //  红色 绿色 蓝色 黄色 紫色 青色 黑色
    public short[] Color_NumToMastTab = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};


    private void Color_NumberSend(short[] Colorvalue) {


        MAJOR = (short) 0x1C;
        FIRST = (short) (((Colorvalue[0] & 0x0F) << 4) + (Colorvalue[1] & 0x0F));
        SECOND = (short) (((Colorvalue[2] & 0x0F) << 4) + (Colorvalue[3] & 0x0F));
        THRID = (short) (((Colorvalue[4] & 0x0F) << 4) + (Colorvalue[5] & 0x0F));
        send();
        yanchi(500);
        MAJOR = (short) 0x1D;
        FIRST = (short) (((Colorvalue[6] & 0x0F) << 4) + (0x00 & 0x0F));
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
    }

    /*
     * 发送车牌识别结果
     * Platevalue	六位车牌号码
     * **/
    private void Plate_Number(String Platevalue) {
        char[] Plate_byte_Tab = CarData.license.toCharArray();
        TFT_LCD(0x20, Plate_byte_Tab[0], Plate_byte_Tab[1], Plate_byte_Tab[2]);
        yanchi(1000);
        TFT_LCD(0x21, Plate_byte_Tab[3], Plate_byte_Tab[4], Plate_byte_Tab[5]);
        yanchi(1000);
        MAJOR = (short) 0x1E;                                // 车牌任务完成
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        while (rbyte[2] != 0x2A)                                         //等待到达交通灯位置
        {
            Log.e("tab", "状态" + rbyte[2]);
        }
    }

    public int mark = 0;


    public byte[] order_data = new byte[6];

    /**
     * algorithm here
     *
     * @param num
     * @param Qrvalue
     */
    public void Sw_algorithm(int num, String Qrvalue) {
        switch (num) {
            case 1:                //RSA
//                new algorithm().RSA_Code(Qrvalue, order_data);
                break;
            case 2:                //CRC
//                new algorithm().CRC_Code(Qrvalue, order_data);
                break;
            case 3:                //仿射码
//                new algorithm().Affine(Qrvalue, order_data);
                break;
        }
    }
}
