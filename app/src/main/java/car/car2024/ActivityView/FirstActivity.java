package car.car2024.ActivityView;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import car.car2024.FragmentView.RightAIFragment;
import car.car2024.UI.Transparent.Transparent;
import car.car2024.Utils.Command.ConnectTransport;
import car.car2024.ViewAdapter.ViewPagerAdapter;
import car.car2024.FragmentView.LeftFragment;
import car.car2024.FragmentView.RightInfraredFragment;
import car.car2024.FragmentView.RightOtherFragment;
import car.car2024.FragmentView.RightZigbeeFragment;
import car.car2024.Utils.Socket.SendDataUtils;


//@SuppressWarnings("all")
public class FirstActivity extends AppCompatActivity {
    private ViewPager viewPager;
    public static TessBaseAPI tessBaseAPI = new TessBaseAPI();
    @SuppressLint("StaticFieldLeak")
    public static FirstActivity INSTANCE;

    public static ConnectTransport ConnectTransport;
    public static String IPCar;
    public static String IPCamera = null;
    public static String purecameraip = null;
    public static Handler recvhandler = null;
    public static androidx.viewpager.widget.ViewPager mLateralViewPager;
    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            vSimple(getApplicationContext(), 30); // 控制手机震动进行反馈
            switch (item.getItemId()) {
                case R.id.home_page_item:
                    mLateralViewPager.setCurrentItem(0);
                    return true;
                case R.id.scene_setting_item:
                    mLateralViewPager.setCurrentItem(1);
                    return true;
                case R.id.personal_center_item:
                    mLateralViewPager.setCurrentItem(2);
                    return true;
                case R.id.ai_item:
                    mLateralViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first1);
        INSTANCE = this;
        initAll();
    }
    private final LeftFragment leftFragment = new LeftFragment();
    private void initAll() {
        context = getApplicationContext();
        tessBaseAPI.setDebug(true);
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"); // 识别白名单
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?"); // 识别黑名单
        if (XcApplication.isserial == XcApplication.Mode.USB_SERIAL) {  //竞赛平台和a72通过usb转串口通信
            mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS); //启动usb的识别和获取
            Transparent.showLoadingMessage(this, "加载中", false);//启动旋转效果的对话框，实现usb的识别和获取
        }

        Button auto_btn = findViewById(R.id.auto_drive_btn);
        auto_btn.setOnClickListener(v -> {//启动按钮
            SendDataUtils.qrCodeDisposeInit();
            ConnectTransport.start();
            ConnectTransport.send_voice("杭州科技职业技术学院".getBytes());
            vSimple(getApplicationContext(), 30); // 控制手机震动进行反馈
        });

        viewPager = findViewById(R.id.viewpager);//使用viewPager实现页面滑动效果
        viewPager.setOffscreenPageLimit(3);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.man);//manbaout
        ImageButton buttonsp = findViewById(R.id.r_logo_iv); //引用按钮触发事件
        buttonsp.setOnClickListener(v -> {
            mp.start();
            vSimple(getApplicationContext(), 30); // 控制手机震动进行反馈
        });

        nativeView();
        addFragment(leftFragment, R.id.left_fragment);

        ConnectTransport = new ConnectTransport();        //实例化连接类
    }
    /**
     * 加载页面的fragment
     */
    private void addFragment(Fragment showFragment, int view) {
        FragmentManager manager = this.getSupportFragmentManager();
        if (!showFragment.isAdded()) {
            manager.beginTransaction().setCustomAnimations(R.anim.across_translate_into, R.anim.across_translate_out)
                    .replace(view, showFragment, "leftFragment").show(showFragment).commit();
        } else {
            manager.beginTransaction().setCustomAnimations(R.anim.across_translate_into, R.anim.across_translate_out).
                    show(showFragment).commit();
        }
    }
    private void nativeView() {
        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        navigation.setItemIconTintList(null);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        mLateralViewPager = findViewById(R.id.viewpager);//获取到ViewPager
        setupViewPager(viewPager);                      //加载fragment
        //ViewPager的监听
        final BottomNavigationView finalNavigation = navigation;
        mLateralViewPager.addOnPageChangeListener(new androidx.viewpager.widget.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                finalNavigation.getMenu().getItem(position).setChecked(true);
                //写滑动页面后做的事，使每一个fragmen与一个page相对应
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(RightAIFragment.getInstance());
        adapter.addFragment(RightZigbeeFragment.getInstance());
        adapter.addFragment(RightInfraredFragment.getInstance());
        adapter.addFragment(RightOtherFragment.getInstance());
        viewPager.setAdapter(adapter);
    }

    //------------------------------------------------------------------------------------------
    //获取和实现usb转串口的通信，实现A72和竞赛平台的串口通信
    public static UsbSerialPort sPort = null;

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.e(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {   //新的数据
                    FirstActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = recvhandler.obtainMessage(1, data);
                            msg.sendToTarget();
                            FirstActivity.this.updateReceivedData(data);
                        }
                    });
                }
            };

    protected void controlusb() {
        Log.e(TAG, "Resumed, port=" + sPort);
        if (sPort == null) {
            Toast.makeText(FirstActivity.this, "No serial device.", Toast.LENGTH_SHORT).show();
        } else {
            openUsbDevice();
            if (connection == null) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);
                Toast.makeText(FirstActivity.this, "Opening device failed", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                sPort.open(connection);
                sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                Toast.makeText(FirstActivity.this, "Error opening device: ", Toast.LENGTH_SHORT).show();
                try {
                    sPort.close();
                } catch (IOException ignored) {
                }
                sPort = null;
                return;
            }
            Toast.makeText(FirstActivity.this, "Serial device: " + sPort.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
        onDeviceStateChange();
        Transparent.dismiss();//关闭加载对话框
    }

    // 在打开usb设备前，弹出选择对话框，尝试获取usb权限
    private void openUsbDevice() {
        tryGetUsbPermission();
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbDeviceConnection connection;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void tryGetUsbPermission() {

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbPermissionActionReceiver, filter);
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);

        //here do emulation to ask all connected usb device for permission
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            //add some conditional check if necessary
            if (mUsbManager.hasPermission(usbDevice)) {
                //if has already got permission, just goto connect it
                //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
                //and also choose option: not ask again
                afterGetUsbPermission(usbDevice);
            } else {
                //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
        }
    }

    private void afterGetUsbPermission(UsbDevice usbDevice) {

        Toast.makeText(FirstActivity.this, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        doYourOpenUsbDevice(usbDevice);
    }

    private void doYourOpenUsbDevice(UsbDevice usbDevice) {
        connection = mUsbManager.openDevice(usbDevice);
    }

    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        if (null != usbDevice) {
                            afterGetUsbPermission(usbDevice);
                        }
                    } else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.e(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            Log.e(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener); //添加监听
            mExecutor.submit(mSerialIoManager); //在新的线程中监听串口的数据变化
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        Log.e("read data is ：：", "   " + message);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (XcApplication.isserial == XcApplication.Mode.USB_SERIAL) {
            unregisterReceiver(mUsbPermissionActionReceiver);
            try {
                sPort.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sPort = null;
        } else if (XcApplication.isserial == XcApplication.Mode.SOCKET) {
            ConnectTransport.destory();
        }

//        plateRecognizer.destroyRes();

    }

    private static final int MESSAGE_REFRESH = 101;
    private static final long REFRESH_TIMEOUT_MILLIS = 5000;
    private UsbManager mUsbManager;
    private final List<UsbSerialPort> mEntries = new ArrayList<>();
    private final String TAG = FirstActivity.class.getSimpleName();

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_REFRESH) {
                refreshDeviceList();
            } else {
                super.handleMessage(msg);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private final Handler usbHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                useUsbtoserial();
            }
        }
    };

    private void useUsbtoserial() {
        final UsbSerialPort port = mEntries.get(0);  //A72上只有一个 usb转串口，用position =0即可
        final UsbSerialDriver driver = port.getDriver();
        final UsbDevice device = driver.getDevice();
        final String usbid = String.format("Vendor %s  ，Product %s",
                HexDump.toHexString((short) device.getVendorId()),
                HexDump.toHexString((short) device.getProductId()));
        Message msg = LeftFragment.showidHandler.obtainMessage(22, usbid);
        msg.sendToTarget();
        FirstActivity.sPort = port;
        if (sPort != null) {
            controlusb();  //使用usb功能
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void refreshDeviceList() {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                Log.e(TAG, "Refreshing device list ...");
                Log.e("mUsbManager is :", "  " + mUsbManager);
                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    Log.e(TAG, String.format("+ %s: %s port%s",
                            driver, ports.size(), ports.size() == 1 ? "" : "s"));
                    result.addAll(ports);
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<UsbSerialPort> result) {
                mEntries.clear();
                mEntries.addAll(result);
                usbHandler.sendEmptyMessage(2);
                Log.e(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
            }
        }.execute((Void) null);
    }
    public static void vSimple(Context context, int millisecond) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(millisecond);
    }

}