package car.car2024.Utils.Camera;

import android.os.Environment;

import car.car2024.ActivityView.XcApplication;

import car.car2024.ActivityView.FirstActivity;
import car.car2024.FragmentView.LeftFragment;
import im.zhy.param.ColorName;
import im.zhy.util.GetVarName;

import java.io.*;


/**
 * 摄像头工具类
 */
@SuppressWarnings("all")
public class CameraUtils {
    // 亮度
    public static final int BRIGHTNESS = 0;

    // 对比度
    public static final int CONTRAST = 1;

    // 饱和度
    public static final int SATURABILITY = 2;

    // 色度
    public static final int CHROMA = 3;

    private static final int[] param = new int[]{1, 2, 8, 9};

    // 当前参数的预设 是那个 为0就是还没设置
    private static int nowPreinstallParam = 0;

    // 当前预设位 0 就是起始位置
    private static int nowPosition = 0;

    // 当前参数值
    private static int[] nowParam;

    // 参数预设值
    private static int[][] preinstallParam;

    // 图形识别要特殊处理的颜色
    private static String specialColor;

    // 摄像头预设位需要转动的次数
    private static int cameraTime;

    // 摄像头当前已经转动的次数
    public static int nowCameraTime;

    private static Preinstall preinstall;

    static {
        File file = new File(Environment.getExternalStorageDirectory() + "/2021car", "preinstall.txt");
        preinstall = new Preinstall();
        if (file.exists()) {
            if (file.length() > 0) {
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new FileInputStream(file));
                    preinstall = (Preinstall) ois.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        nowPosition = preinstall.nowPosition;
        preinstallParam = preinstall.preinstallParam;
        nowParam = preinstall.nowValue;
        nowPreinstallParam = preinstall.nowPreinstallParam;
        specialColor = preinstall.specialColor;
        cameraTime = preinstall.cameraTime;

        if (nowParam == null) {
            nowParam = new int[]{18, 18, 18, 18};
        }

        if (preinstallParam == null) {
            preinstallParam = new int[3][4];
        }
    }

    public static int getNowPreinstallParam() {
        return nowPreinstallParam;
    }

    public static void setNowPreinstallParam(int nowPreinstallParam) {
        CameraUtils.nowPreinstallParam = nowPreinstallParam;
        preinstall.nowPreinstallParam = nowPreinstallParam;
        inputPreinstall();
    }

    public static int getNowPosition() {
        return nowPosition;
    }

    public static void setNowPosition(int nowPosition) {
        CameraUtils.nowPosition = nowPosition;
        preinstall.nowPosition = nowPosition;
        // 将预设位信息存到本地
        inputPreinstall();
    }

    public static int getParam(int index) {
        return param[index];
    }


    public static int getNowParam(int index) {
        return nowParam[index];
    }

    public static void setNowParam(int index, int value) {
        nowParam[index] = value;
        inputPreinstall();
    }

    public static String getSpecialColor() {
        return specialColor;
    }

    public static void setSpecialColor(int which) {
        String colorName;
        if (which > 7) {
            colorName = "without";
        } else {
            colorName = GetVarName.getColorName(which);
        }
        CameraUtils.specialColor = colorName;
        preinstall.specialColor = colorName;
        inputPreinstall();
    }

    // 摄像头 色温、亮度、饱和度、色度 调节
    public static void setCameraParameter(final int param, final int value) {
        System.out.println(param + ":" + value);

        if (value == getNowParam(param)) {
            return;
        }

        setNowParam(param, value);

        sendPreinstallInfo1(param, value);


    }

    public static int getCameraTime() {
        return cameraTime;
    }

    public static void setCameraTime(int cameraTime) {
        CameraUtils.cameraTime = cameraTime;
        preinstall.cameraTime = cameraTime;
        inputPreinstall();
    }

    public static void setNowCameraTime(int nowCameraTime) {
        CameraUtils.nowCameraTime = nowCameraTime;
        preinstallSetOrCall(1);
    }

    // 转动摄像头到下一个预设位
    public static void cameraPreinstall() {
        System.out.println("摄像头预设位转动到第：" + nowCameraTime);
//        if (nowCameraTime >= cameraTime) {
//            return;
//        }

//        try {
//            Thread.sleep(700);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        preinstallSetOrCall(nowCameraTime * 2 + 1);
//        ++nowCameraTime;
    }

    /**
     * 全部转动完所有的设置好的预设位 用于测试
     */
    public static void allCameraPreinstall() {
        nowCameraTime = 0;

        for (int i = 0; i < cameraTime; i++) {
            cameraPreinstall();

            try {
                Thread.sleep(1600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        nowCameraTime = 0;
    }


    // 参数预设和调用
    public static void preinstallCameraParam(int which) {

        if (which >= 6) {
            initParam();
            return;
        } else {
            int index = which / 2;

            System.out.println("index = " + index);
            if (which % 2 == 0) {
                // 设置
                for (int i = 0; i < 4; i++) {
                    preinstallParam[index][i] = getNowParam(i);
                }
                inputPreinstall();
            } else {
                int n = 0;

                for (int i = 0; i < 4; i++) {
                    if (preinstallParam[index][i] == 0) {
                        n++;
                    }
                }

                if (n >= 4) {
                    return;
                }

                // 调用
                for (int i = 0; i < 4; i++) {
                    setCameraParameter(i, preinstallParam[index][i]);
                }
                setNowPreinstallParam(index + 1);
            }
        }

    }

    // 用于OtherAdapter 设置和调用预设位的
    public static void preinstallSetOrCall(int which) {
        if (which % 2 != 0) {
            setNowPosition((which + 1) / 2);
        }
        sendPreinstallInfo(30 + which);
    }

    public static void movePreinstall_one() {
        preinstallSetOrCall(1);
    }

    public static void movePreinstall_two() {
        preinstallSetOrCall(3);
    }

    public static void movePreinstall_three() {
        preinstallSetOrCall(5);
    }

    public static void movePreinstall_four() {
        preinstallSetOrCall(7);
    }

    public static void movePreinstall_five() {
        preinstallSetOrCall(9);
    }

    // 交通灯识别开始
    private static int brightness;

    public static void trafficLightStart() {
        brightness = getNowParam(CameraUtils.BRIGHTNESS);
//        setCameraParameter(CameraUtils.BRIGHTNESS, 0);
    }

    // 交通灯识别结束
    public static void trafficLightEnd() {
//        setCameraParameter(CameraUtils.BRIGHTNESS, brightness);
    }


    private static void sendPreinstallInfo(final int command) {
        for (int i = 0; i < 5; i++) {
            XcApplication.executorServicetor.execute(new Runnable() {
                public void run() {
                    LeftFragment.cameraCommandUtil.postHttp(FirstActivity.IPCamera, command, 0);
                }
            });
        }
    }

    private static void sendPreinstallInfo1(final int param, final int value) {
        for (int i = 0; i < 5; i++) {
            XcApplication.executorServicetor.execute(new Runnable() {
                public void run() {
                    LeftFragment.cameraCommandUtil.sendHttp(FirstActivity.IPCamera, getParam(param), value * 7);
                }
            });
        }
    }

    public static void initParam() {
        for (int i = 0; i < 4; i++) {
            sendPreinstallInfo1(i, 18);
            setNowParam(i, 18);
        }
    }

    public static void paramPreinstall_one() {
        preinstallCameraParam(1);
    }

    public static void paramPreinstall_two() {
        preinstallCameraParam(3);
    }

    public static void paramPreinstall_three() {
        preinstallCameraParam(5);
    }

    /**
     * 将预设位信息存到本地
     */
    public static void inputPreinstall() {
        File file = new File(Environment.getExternalStorageDirectory() + "/2021car", "preinstall.txt");
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(preinstall);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class Preinstall implements Serializable {
    // 当前参数的预设 是那个 为0就是还没设置
    public int nowPreinstallParam;

    // 当前预设位 0 就是起始位置
    public int nowPosition;

    public int[] nowValue;

    public int[][] preinstallParam;

    // 摄像头预设位需要转动的次数
    public int cameraTime;

    // 图形识别要特殊处理的颜色
    public String specialColor;

    public Preinstall() {
        nowPreinstallParam = 0;
        nowPosition = 0;
        nowValue = new int[]{18, 18, 18, 18};
        preinstallParam = new int[3][4];
        specialColor = ColorName.WHITE;
        cameraTime = 0;
    }
}
