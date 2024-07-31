package car.car2024.FragmentView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import car.Identify.utils.LightIdentify;
import car.Identify.utils.MaskIdentify;
import car.Identify.utils.OcrIdentify;
import car.Identify.utils.OcrLisenceIdentify;
import car.Identify.utils.QRCodeIdentify;
import car.Identify.utils.ShapeIdentify;
import car.Identify.utils.TrafficSignIdentify;
import car.Identify.utils.VehicleIdentify;
import car.car2024.ActivityView.FirstActivity;
import car.car2024.ActivityView.MainActivity;
import car.car2024.ActivityView.R;
import car.car2024.ActivityView.XcApplication;
import car.car2024.Utils.Socket.AcceptCarData;
import car.car2024.Utils.Socket.AcceptCarOrder;
import car.car2024.Utils.Socket.SendDataUtils;

@SuppressLint("StaticFieldLeak")
public class RightAIFragment extends Fragment implements View.OnClickListener {
    private byte[] mByte = new byte[11];
    Context minstance = null;
    public static TextView picrectext_tv;
    public static ImageView picrec_iv;

    public static RightAIFragment getInstance() {
        return RightAIHolder.mInstance;
    }

    private static class RightAIHolder {
        @SuppressLint("StaticFieldLeak")
        private static final RightAIFragment mInstance = new RightAIFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        minstance = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picinformation_fragment, container, false);
        FirstActivity.recvhandler = rehHandler;
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mask_all_btn:
                MaskIdentify.maskIdentify(LeftFragment.bitmap);//口罩
                break;
            case R.id.qr_all_btn:
                QRCodeIdentify.qrcodeIdentify(LeftFragment.bitmap);//二维码
                break;
            case R.id.carplate_all_btn:
                OcrLisenceIdentify.licenseIdentify(LeftFragment.bitmap);//车牌
                break;
            case R.id.ocrrec_all_btn:
                OcrIdentify.ocrIdentify(LeftFragment.bitmap);//文字
                break;
            case R.id.tracfficrec_btn:
                LightIdentify.sendLightCode(LeftFragment.bitmap, (byte) 0x01);//交通灯识别
                break;
            case R.id.cartype_all_btn:
                VehicleIdentify.vehicleIdentify(LeftFragment.bitmap);//车辆
                break;
            case R.id.tracfficsign_all_btn:
                TrafficSignIdentify.trafficSignIdentify(LeftFragment.bitmap);//交通信号
                break;
            case R.id.graphic_color_btn:
                ShapeIdentify.shapeIdentify(LeftFragment.bitmap);//图形
                break;
            case R.id.refresh_btn:
                picrectext_tv.setText("");
                picrec_iv.setImageBitmap(null);
                break;
            default:
                break;
        }
    }

    private void initView(View view) {
        Button mask_all_btn = view.findViewById(R.id.mask_all_btn);
        mask_all_btn.setOnClickListener(this);
        Button qr_all_btn = view.findViewById(R.id.qr_all_btn);
        qr_all_btn.setOnClickListener(this);
        Button carplate_all_btn = view.findViewById(R.id.carplate_all_btn);
        carplate_all_btn.setOnClickListener(this);
        Button ocrrec_all_btn = view.findViewById(R.id.ocrrec_all_btn);
        ocrrec_all_btn.setOnClickListener(this);
        Button tracfficrec_btn = view.findViewById(R.id.tracfficrec_btn);
        tracfficrec_btn.setOnClickListener(this);
        Button cartype_all_btn = view.findViewById(R.id.cartype_all_btn);
        cartype_all_btn.setOnClickListener(this);
        Button tracfficsign_all_btn = view.findViewById(R.id.tracfficsign_all_btn);
        tracfficsign_all_btn.setOnClickListener(this);
        Button graphic_color_btn = view.findViewById(R.id.graphic_color_btn);
        graphic_color_btn.setOnClickListener(this);
        Button refresh_btn = view.findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(this);
        picrectext_tv = view.findViewById(R.id.picrectext_tv);
        picrec_iv = view.findViewById(R.id.picrec_iv);

        if (XcApplication.isserial == XcApplication.Mode.SOCKET) {
            XcApplication.executorServicetor.execute(() -> {
                FirstActivity.ConnectTransport.connect(rehHandler, FirstActivity.IPCar);
            });
        } else if (XcApplication.isserial == XcApplication.Mode.SERIAL) {
            XcApplication.executorServicetor.execute(() -> {
                FirstActivity.ConnectTransport.serial_connect(rehHandler);
            });
        }
    }

    /**
     * 接受显示小车发送的数据
     */
    @SuppressLint("HandlerLeak")
    private final Handler rehHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mByte = (byte[]) msg.obj;

                XcApplication.executorServicetor.execute(() -> {
                    FirstActivity.INSTANCE.runOnUiThread(() -> {
                        try {
                            if (mByte[0] == 0x55) {
                                if (mByte[1] == 0x03) { //请求任务
                                    AcceptCarOrder.dispatch(mByte);
                                }
                            } else if (mByte[0] == 0x56) { // 请求数据
                                if (mByte[1] == 0x66) {
                                    byte[] bytes = new byte[mByte.length - 2];
                                    System.arraycopy(mByte, 2, bytes, 0, bytes.length);
                                    SendDataUtils.Control(bytes);
                                } else if (mByte[1] == 0x76) { // 接收数据
                                    AcceptCarData.dispatch(mByte);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        }
    };
}
