package car.Identify.utils.socketUtil;

import android.graphics.Bitmap;
import android.util.Log;

import car.Identify.utils.LicenseIdentify;
import car.car2024.ActivityView.XcApplication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import im.drh.utils.ToastUtil;

public class OcrSocketClient implements Runnable {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12546;
    private final Bitmap bitmap;

    public OcrSocketClient(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * 发送图片到socketServer，使用Infer进行识别
     *
     * @param bitmap 图片
     */
    public void sendBitmap(Bitmap bitmap) {
        for (int i = 0; i < 4; i++) {
            try {
                // 建立与服务器的连接
                Socket socket = null;
                try {
                    socket = new Socket(SERVER_IP, SERVER_PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("socket err", String.valueOf(e));
                }
                assert socket != null;
                // 将 Bitmap 转换为字节数组
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                System.out.println("byteArray.length: " + byteArray.length);
                // 发送字节数组到服务器
                OutputStream outputStream = socket.getOutputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                bufferedOutputStream.write(byteArray);
                bufferedOutputStream.flush();
                socket.shutdownOutput();
                System.out.println("bitmap已发送！");
                // 接收服务器返回的 JSON 数据
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputReader);
                String line = reader.readLine();

                Log.d("License", line);
                LicenseIdentify.licenseString = line.trim();
                if (LicenseIdentify.licenseString.contains(" ")) {
                    String[] s = LicenseIdentify.licenseString.split(" ");
                    LicenseIdentify.licenseArrays = s;
                    LicenseIdentify.licenseString = LicenseIdentify.licenseArrays[0];
                    ToastUtil.showToast(XcApplication.getApp(), LicenseIdentify.licenseString);
                }
                socket.close();
                if (!line.equals("")) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        sendBitmap(bitmap);
    }
}
