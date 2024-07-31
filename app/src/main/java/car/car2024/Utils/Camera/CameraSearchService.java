package car.car2024.Utils.Camera;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import car.car2024.ActivityView.MainActivity;
import car.car2024.ActivityView.XcApplication;
import car.car2024.Utils.Socket.ThreadUtils;

public class CameraSearchService extends IntentService {

    public CameraSearchService() {
        super("CameraSearchService");
        //搜索摄像头ip类
        Thread thread = new Thread(() -> {
            int i = 0;
            while (IP == null || IP.equals("0.0.0.0")) {
                i++;
                if (i >= 3) {
                    break;
                }
                //搜索摄像头ip类
                SearchCameraUtil searchCameraUtil = new SearchCameraUtil();
                IP = searchCameraUtil.send();
                ThreadUtils.sleep(1000);
            }
            handler.sendEmptyMessage(10);
        });
        thread.start();
    }

    //摄像头ͷIP
    private String IP = "0.0.0.0";

    private final Handler handler = new Handler(message -> {
        if (message.what == 10) {
            Log.e("ipcamipcamipcam", "  " + IP + ":81");
            Intent intent = new Intent(MainActivity.A_S);
            intent.putExtra("IP", XcApplication.cameraip);
            intent.putExtra("IP", IP + ":81");
            intent.putExtra("pureip", IP);
            sendBroadcast(intent);
            CameraSearchService.this.stopSelf();
        }
        return true; // 返回true表示已处理消息
    });

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    class SearchCameraUtil {
        private String IP = "";
        private final byte[] mbyte = new byte[]{68, 72, 1, 1};
        private DatagramSocket dSocket = null;
        private final byte[] msg = new byte[1024];
        boolean isConn = false;

        public SearchCameraUtil() {
        }

        public String send() {
            InetAddress local;

            String TAG = "UDPClient";
            try {
                local = InetAddress.getByName("255.255.255.255");
                Log.e(TAG, "已找到服务器,连接中...");
            } catch (UnknownHostException var7) {
                Log.e(TAG, "未找到服务器.");
                var7.printStackTrace();
                return null;
            }

            try {
                if (this.dSocket != null) {
                    this.dSocket.close();
                    this.dSocket = null;
                }
                // 第一次连接没有报错，第二次开始报这个错误。字面意思看出是由于端口被占用，未释放导致。
                // 虽然程序貌似已经退出，个人猜测是由于系统还没有及时释放导致的。
//				this.dSocket = new DatagramSocket(3565);
                if (dSocket == null) {
                    dSocket = new DatagramSocket(null);
                    dSocket.setReuseAddress(true);
                    int PORT = 3565;
                    dSocket.bind(new InetSocketAddress(PORT));
                }
                Log.e(TAG, "正在连接服务器...");
            } catch (SocketException var6) {
                var6.printStackTrace();
                Log.e(TAG, "服务器连接失败.");
                return null;
            }

            int SERVER_PORT = 8600;
            DatagramPacket sendPacket = new DatagramPacket(this.mbyte, 4, local, SERVER_PORT);
            DatagramPacket recPacket = new DatagramPacket(this.msg, this.msg.length);

            try {
                this.dSocket.send(sendPacket);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (isConn) {
                            dSocket.close();
                            this.cancel();
                        }
                        isConn = true;
                    }
                }, 0, 500);
                this.dSocket.receive(recPacket);
                timer.cancel();


                byte[] shortArray = recPacket.getData();
                test(shortArray);
                StringBuilder decodedStringBuilder = new StringBuilder();

                for (short encodedValue : shortArray) {
                    char char1 = (char) ((encodedValue >> 8) & 0xFF);  // 大端字节序
                    char char2 = (char) (encodedValue & 0xFF);

                    decodedStringBuilder.append(char1).append(char2);
                }

                String decodedString = decodedStringBuilder.toString();
                System.out.println(decodedString);


                String text = new String(recPacket.getData(), 0, recPacket.getLength(), StandardCharsets.UTF_8);
                try {
                    JSONObject jsonData = new JSONObject(text);
                    // 处理JSON数据
                    Log.e(TAG, "text1 " + text);
                } catch (JSONException e) {
                    // 数据不是JSON格式，可能是其他格式
                    Log.e(TAG, "text0 " + text);
                }
                if (text.startsWith("DH")) {
                    this.getIP(text);
                }

                Log.e("IP值", this.IP);
                this.dSocket.close();
                Log.e(TAG, "消息发送成功!");
            } catch (IOException var5) {
                var5.printStackTrace();
                Log.e(TAG, "消息发送失败.");
                return null;
            }

            return this.IP;
        }

        private void getIP(String text) {
            byte[] ipbyte = text.getBytes(StandardCharsets.UTF_8);

            for (int i = 4; i < 22 && ipbyte[i] != 0; ++i) {
                if (ipbyte[i] == 46) {
                    this.IP = this.IP + ".";
                } else {
                    this.IP = this.IP + (ipbyte[i] - 48);
                }
            }

        }
    }


    private void test(byte[] data) {
        // 使用ByteBuffer来解析数据
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // 小端字节序

        // 解析STARTCODE
        short startCode = buffer.getShort();
        String startCodeString = new String(new char[]{(char) ((startCode >> 8) & 0xFF), (char) (startCode & 0xFF)});
        System.out.println("STARTCODE: " + startCodeString);

        // 解析CMD
        short cmd = buffer.getShort();
        System.out.println("CMD: " + Integer.toHexString(cmd));

        // 解析STATUS
        int status = buffer.getInt();
        System.out.println("STATUS: " + Integer.toHexString(status));

        // 解析BCASTPARAM结构
        BCASTPARAM bcastParam = new BCASTPARAM();
        buffer.position(buffer.position() - 4);
        buffer.get(bcastParam.szIpAddr, 0, bcastParam.szIpAddr.length);
        buffer.get(bcastParam.szMask, 0, bcastParam.szMask.length);
        buffer.get(bcastParam.szGateway, 0, bcastParam.szGateway.length);
        buffer.get(bcastParam.szDns1, 0, bcastParam.szDns1.length);
        buffer.get(bcastParam.szDns2, 0, bcastParam.szDns2.length);
        buffer.get(bcastParam.szMacAddr, 0, bcastParam.szMacAddr.length);
//        buffer.get(bcastParam.nPort,0,bcastParam.nPort);
        bcastParam.nPort = buffer.getShort();
        buffer.get(bcastParam.dwDeviceID, 0, bcastParam.dwDeviceID.length);
        buffer.get(bcastParam.szDevName, 0, bcastParam.szDevName.length);
        buffer.get(bcastParam.sysver, 0, bcastParam.sysver.length);
        buffer.get(bcastParam.appver, 0, bcastParam.appver.length);
        buffer.get(bcastParam.szUserName, 0, bcastParam.szUserName.length);
        buffer.get(bcastParam.szPassword, 0, bcastParam.szPassword.length);
        bcastParam.sysmode = buffer.get();
        bcastParam.dhcp = buffer.get();
        bcastParam.vertype = buffer.get();
        bcastParam.other = buffer.get();
        buffer.get(bcastParam.other1, 0, bcastParam.other1.length);

        /// 打印解析结果
        System.out.println("IP Address: " + nullTerminatedString(bcastParam.szIpAddr));
        System.out.println("Mask: " + nullTerminatedString(bcastParam.szMask));
        System.out.println("Gateway: " + nullTerminatedString(bcastParam.szGateway));
        System.out.println("dwDeviceID: " + nullTerminatedString(bcastParam.dwDeviceID));
        // 解析端口号
        buffer.position(buffer.position() + 3);
        short port = buffer.getShort();
        System.out.println("Port: " + port);


        // 其他字段类似，根据实际情况打印出需要的信息
    }

    // 辅助方法，处理以null结尾的字节数组转为字符串
    private static String nullTerminatedString(byte[] byteArray) {
        int length = 0;
        while (length < byteArray.length && byteArray[length] != 0) {
            length++;
        }
        return new String(byteArray, 0, length, StandardCharsets.UTF_8);
    }

    // BCASTPARAM结构
    class BCASTPARAM {
        byte[] szIpAddr = new byte[16];
        byte[] szMask = new byte[16];
        byte[] szGateway = new byte[16];
        byte[] szDns1 = new byte[16];
        byte[] szDns2 = new byte[16];
        byte[] szMacAddr = new byte[6];
        short nPort;
        byte[] dwDeviceID = new byte[32];
        byte[] szDevName = new byte[80];
        byte[] sysver = new byte[16];
        byte[] appver = new byte[16];
        byte[] szUserName = new byte[32];
        byte[] szPassword = new byte[32];
        byte sysmode;
        byte dhcp;
        byte vertype;
        byte other;
        byte[] other1 = new byte[20];
    }

}
