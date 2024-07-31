package car.car2024.ActivityView;

import static car.Identify.utils.LicenseColorIdentify.licenseColorModel;
import static car.Identify.utils.LightIdentify.lightModel;
import static car.Identify.utils.LocateIdentify.locateModel;
import static car.Identify.utils.MaskIdentify.maskModel;
import static car.Identify.utils.QRCodeIdentify.qrCodeModel;
import static car.Identify.utils.QRCodeColorIdentify.qrCodeColorModel;
import static car.Identify.utils.ShapeIdentify.shapeModel;
import static car.Identify.utils.TrafficSignIdentify.trafficSignModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.king.wechat.qrcode.WeChatQRCodeDetector;

import org.opencv.OpenCV;
import org.opencv.android.OpenCVLoader;

import car.Identify.utils.OcrIdentify;
import car.car2024.Utils.Camera.CameraSearchService;
import car.car2024.Utils.OtherUtils.WiFiStateUtil;
import car.car2024.Utils.Socket.ThreadUtils;
import im.drh.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_connect = null;
    public static Boolean libraryIsLoad = false;
    public static Boolean shapeInit = false;
    public static Boolean trafficSignInit = false;
    public static Boolean maskInit = false;
    public static Boolean lightInit = false;
    public static Boolean qrCodeInit = false;
    public static Boolean qrCodeColorInit = false;
    public static Boolean locateInit = false;
    public static Boolean licenseColorInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        widget_init();
        checkPermission();

        if (!libraryIsLoad) {//TODO
//            shapeInit = shapeModel.Init(getAssets()); //形状
//            trafficSignInit = trafficSignModel.Init(getAssets()); //交通信号
//            maskInit = maskModel.Init(getAssets()); //口罩
            lightInit = lightModel.Init(getAssets()); //交通灯
            qrCodeInit = qrCodeModel.Init(getAssets()); //二维码
            qrCodeColorInit = qrCodeColorModel.init(getAssets()); //二维码颜色
//            locateInit = locateModel.Init(getAssets()); //车牌位置
//            licenseColorInit = licenseColorModel.init(getAssets()); //车牌颜色
            libraryIsLoad = true;
        }

        OpenCV.initAsync(this);
        WeChatQRCodeDetector.init(this);

        verifyStoragePermissions(); //验证文件读写权限
        Camer_Init(); //摄像头初始化

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                search();                        //开启Service 搜索摄像头IP
            }
        }, 1000);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void verifyStoragePermissions() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * OpenCV库静态加载并初始化
     */
    private void staticLoadCVLibraries() {
        if (OpenCVLoader.initDebug()) {
            Log.i("CV", "Open CV Libraries loaded...");
        } else {
            Log.i("CV", "Open CV Libraries loaded Faild...");
        }
    }

    private void widget_init() {
        bt_connect = findViewById(R.id.connect);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch transmitswitch = findViewById(R.id.transmit_way);
        bt_connect.setOnClickListener(this);
        transmitswitch.setOnCheckedChangeListener((Button, isChecked) -> {
            if (isChecked) {
                XcApplication.isserial = XcApplication.Mode.USB_SERIAL;
                Button.setText("使用有线连接");
            } else {
                XcApplication.isserial = XcApplication.Mode.SOCKET;
                Button.setText("使用无线连接");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.equals(bt_connect)) {
            if (XcApplication.isserial == XcApplication.Mode.SOCKET) {
                new WiFiStateUtil(this).wifiInit();
                search();
            }
            if (XcApplication.isserial != XcApplication.Mode.SOCKET) {
                Intent ipintent = new Intent();
                //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
                ipintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
                //设置要传送的数据
                ipintent.putExtra("purecameraip", FirstActivity.purecameraip);
                startService(ipintent);   //摄像头设为静态192.168.16.20时，可以不用发送
            }

            if (FirstActivity.IPCamera != null) {
                startActivity(new Intent(MainActivity.this, FirstActivity.class));
            }
        }
    }

    // 广播名称
    public static final String A_S = "com.a_s";
    private final BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            FirstActivity.IPCamera = arg1.getStringExtra("IP");
            FirstActivity.purecameraip = arg1.getStringExtra("pureip");
            Log.e("camera ip::", "  " + FirstActivity.IPCamera);
            unregisterReceiver(myBroadcastReceiver);
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void Camer_Init() {
        //广播接收器注册
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(A_S);
        registerReceiver(myBroadcastReceiver, intentFilter);
        // 搜索摄像头图片工具
    }

    // 搜索摄像cameraIP进度条
    private void search() {
        Intent intent = new Intent(MainActivity.this, CameraSearchService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;

    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }
        if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            String[] permissions = mPermissionList.toArray(new String[0]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
        }
    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PERMISSION_REQUEST)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
