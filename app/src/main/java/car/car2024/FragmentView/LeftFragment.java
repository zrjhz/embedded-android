package car.car2024.FragmentView;

import static car.car2024.ActivityView.FirstActivity.IPCamera;
import static car.car2024.ActivityView.FirstActivity.IPCar;
import static car.car2024.ActivityView.FirstActivity.vSimple;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import car.car2024.ActivityView.R;
import car.car2024.ActivityView.XcApplication;

import car.car2024.ActivityView.FirstActivity;
import car.car2024.Utils.Camera.CameraCommandUtils;
import im.drh.utils.ToastUtil;

@SuppressLint("StaticFieldLeak")
public class LeftFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "LeftFragment";
    private float x1 = 0;
    private float y1 = 0;
    private static ImageView image_show;
    private static TextureView camera_show;
    public static CameraCommandUtils cameraCommandUtil;
    private static TextView showip = null;
    private static TextView switchover_photo;
    boolean phone_cameraState = false; // 相机启用状态
    public static String show_mode = "normal"; // 显示状态
    public static Bitmap staticBitmap = null;// 静态图片
    public static boolean useCarCamera = true;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_fragment, container, false);
        initView(view);
        if (Objects.equals(IPCar, "0.0.0.0")) {
            ToastUtil.showToast(FirstActivity.getContext(), "平台未连接,使用相机模式");
            useCarCamera = false;
            openCamera(camera_show);
        }
        return view;
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void initView(View view) {
        //图片
        image_show = view.findViewById(R.id.img);
        image_show.setOnTouchListener(new ontouchlistener1());
        image_show.setOnClickListener(this);
        camera_show = new TextureView(requireContext());
        camera_show = view.findViewById(R.id.camera_view);
        camera_show.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                texture_surface = new Surface(camera_show.getSurfaceTexture());
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
            }
        });
        //文字
        showip = view.findViewById(R.id.showip);
        //按钮
        TextView save_photo = view.findViewById(R.id.save_photo_btn);
        switchover_photo = view.findViewById(R.id.switchover_photo_btn);
        TextView refersh_image = view.findViewById(R.id.refresh_btn);
        save_photo.setOnClickListener(this);
        switchover_photo.setOnClickListener(this);
        refersh_image.setOnClickListener(this);

        cameraCommandUtil = new CameraCommandUtils();

        if (XcApplication.isserial == XcApplication.Mode.SOCKET && !IPCamera.equals("null:81")) {
            showip.setText("WiFi-IP：" + FirstActivity.IPCar + "\n" + "Camera-IP:" + FirstActivity.purecameraip);
        } else if (XcApplication.isserial == XcApplication.Mode.SOCKET) {
            showip.setText("WiFi-IP：" + FirstActivity.IPCar + "\n" + "摄像头未连接！");
        }

        XcApplication.executorServicetor.execute(() -> {
            while (true) {
                getBitmap();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public static Handler showidHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 22) {
                showip.setText(msg.obj + "\n" + "CameraIP:" + FirstActivity.purecameraip);
            }
        }
    };
    // 图片
    public static Bitmap bitmap;

    // 得到当前摄像头的图片信息
    public static void getBitmap() {
        if (Objects.equals(show_mode, "camera")) {
            try {
                bitmap = getCamera();
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            bitmap = cameraCommandUtil.httpForImage(IPCamera);
        }
        phHandler.sendEmptyMessage(10);
    }


    // 显示图片
    @SuppressLint("HandlerLeak")
    private static Handler phHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                switch (show_mode) {
                    case "normal":
                        image_show.setImageBitmap(bitmap);
                        break;
                    case "static":
                        image_show.setImageBitmap(staticBitmap);
                        break;
                }
            }
        }
    };

    private class ontouchlistener1 implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 图片区域滑屏监听点击和弹起坐标位置
            int MINLEN = 30;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 点击位置坐标
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    break;
                // 弹起坐标
                case MotionEvent.ACTION_UP:
                    float x2 = event.getX();
                    float y2 = event.getY();
                    float xx = x1 > x2 ? x1 - x2 : x2 - x1;
                    float yy = y1 > y2 ? y1 - y2 : y2 - y1;
                    // 判断滑屏趋势
                    if (xx > yy) {
                        if ((x1 > x2) && (xx > MINLEN)) {        // left
                            Toast.makeText(getActivity(), "左边", Toast.LENGTH_SHORT).show();
                            XcApplication.executorServicetor.execute(() -> {
                                cameraCommandUtil.postHttp(IPCamera, 4, 1);  //左
                            });
                        } else if ((x1 < x2) && (xx > MINLEN)) { // right
                            Toast.makeText(getActivity(), "右边", Toast.LENGTH_SHORT).show();
                            XcApplication.executorServicetor.execute(() -> {
                                cameraCommandUtil.postHttp(IPCamera, 6, 1);  //右
                            });
                        }
                    } else {
                        if ((y1 > y2) && (yy > MINLEN)) {// up
                            Toast.makeText(getActivity(), "上边", Toast.LENGTH_SHORT).show();
                            XcApplication.executorServicetor.execute(() -> {
                                cameraCommandUtil.postHttp(IPCamera, 0, 1);  //上
                            });
                        } else if ((y1 < y2) && (yy > MINLEN)) { // down
                            Toast.makeText(getActivity(), "下边", Toast.LENGTH_SHORT).show();

                            XcApplication.executorServicetor.execute(() -> {
                                cameraCommandUtil.postHttp(IPCamera, 2, 1);  //下
                            });
                        }
                    }
                    x1 = 0;
                    y1 = 0;
                    break;
            }
            return true;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_photo_btn:
                vSimple(requireContext(), 30); // 控制手机震动进行反馈
                XcApplication.executorServicetor.execute(() -> {
                    switch (show_mode) {
                        case "normal":
                            saveBitmapAsJpg(getSDPath(), bitmap);
                            break;
                        case "camera":
                            try {
                                saveBitmapAsJpg(getSDPath(), Objects.requireNonNull(getCamera()));
                            } catch (CameraAccessException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "static":
                            saveBitmapAsJpg(getSDPath(), staticBitmap);
                            break;
                    }
                    FirstActivity.INSTANCE.runOnUiThread(() -> ToastUtil.showToast(getContext(), "图片成功保存在 " + getSDPath() + " 路径下"));
                });
                break;
            case R.id.refresh_btn:
                vSimple(requireContext(), 30); // 控制手机震动进行反馈
                ToastUtil.showToast(FirstActivity.getContext(), "刷新成功");
                if (useCarCamera) {
                    show_mode = "normal";
                } else {
                    openCamera(camera_show);
                }
                break;
            case R.id.switchover_photo_btn:
                if (phone_cameraState) {
                    phone_cameraState = closeCamera();
                } else {
                    if (checkPermission()) {
                        phone_cameraState = openCamera(camera_show);
                    }
                }
                break;
        }
    }

    public static final String PIC_DIR_NAME = "car2024";

    public void saveBitmapAsJpg(String path, Bitmap bitmap) {
        // 生成图片名称
        String imageName = generateImageName();
        // 指定保存路径和文件名
        File file = new File(path, imageName);
        try {
            // 创建文件输出流
            FileOutputStream fos = new FileOutputStream(file);
            // 将 Bitmap 压缩为 JPG 格式并写入文件输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // 关闭文件输出流
            fos.close();
            // 保存成功
            Log.d("Save Image", "Image saved successfully. Name: " + imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateImageName() {
        // 获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = dateFormat.format(new Date());
        // 生成6位随机编号
        Random random = new Random();
        int randomNum = random.nextInt(900000) + 100000; // 生成100000到999999之间的随机数
        // 组合图片名称
        return timeStamp + "_" + randomNum + ".jpg";
    }

    public static String getSDPath() {
        File mPicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), PIC_DIR_NAME);
        Log.d(TAG, "sdDir:" + mPicDir.getAbsolutePath());
        if (!mPicDir.exists()) {
            if (mPicDir.mkdirs()) {
                Log.d(TAG, "文件夹创建成功！！");
            } else Log.d(TAG, "sdDir: 文件夹创建失败！！");
        }
        return mPicDir.getAbsolutePath();
    }


    static CameraManager cameraManager;
    static CameraDevice.StateCallback cam_stateCallback;
    static CameraDevice opened_camera;
    static CameraCaptureSession.StateCallback cam_capture_session_stateCallback;
    static CameraCaptureSession cameraCaptureSession;
    static CaptureRequest.Builder requestBuilder;
    static CaptureRequest request;
    static Surface texture_surface;

    public boolean openCamera(TextureView cameraTextureView) {
        show_mode = "camera";
        switchover_photo.setText(R.string.cut_car_camera);
        // 将显示区域设置为可见，避免残影与RTSP冲突显示
        cameraTextureView.setVisibility(View.VISIBLE);
        // 锁定屏幕方向，防止旋转后异常旋转，关闭相机后恢复正常
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        // 1 创建相机管理器，调用系统相机
        cameraManager = (CameraManager) requireActivity().getSystemService(Context.CAMERA_SERVICE);
        // 2 准备 相机状态回调对象为后面用
        cam_stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                // 2.1 保存已开启的相机对象
                opened_camera = camera;
                try {
                    // 2.2 构建请求对象（设置预览参数，和输出对象）
                    requestBuilder = opened_camera.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG); // 设置参数：预览
                    requestBuilder.addTarget(texture_surface); // 设置参数：目标容器
                    request = requestBuilder.build();
                    //2.3 创建会话的回调函数，后面用
                    cam_capture_session_stateCallback = new CameraCaptureSession.StateCallback() {
                        @Override  //2.3.1  会话准备好了，在里面创建 预览或拍照请求
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            cameraCaptureSession = session;
                            try {
                                // 2.3.2 预览请求
                                session.setRepeatingRequest(request, null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                        }
                    };
                    // 设置矩阵变换，将相机获取到的图片进行旋转
                    cameraTextureView.setTransform(changeSize(cameraTextureView));
                    // 2.3 创建会话
                    opened_camera.createCaptureSession(Collections.singletonList(texture_surface), cam_capture_session_stateCallback, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {

            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {

            }
        };
        // 4 检查相机权限并开启相机（传入：要开启的相机ID，ID的第一个一般为后置主摄，状态回调对象）
        if (checkPermission()) {
            try {
                cameraManager.openCamera(cameraManager.getCameraIdList()[0], cam_stateCallback, null);
                return true;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 关闭移动设备相机
     */
    public static boolean closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
        }
        // 再关闭相机
        if (null != opened_camera) {
            show_mode = "normal";
            opened_camera.close();
            clearSurface(camera_show.getSurfaceTexture());
        }
        camera_show.setVisibility(View.GONE);
        switchover_photo.setText(R.string.cut_camera);
        // 设置屏幕横向可旋转
        return false;
    }

    /**
     * 关闭camera的时候，清空TextureView最后一帧，设置颜色为黑色（再次打开相机可正常显示出画面）
     */
    private static void clearSurface(SurfaceTexture surface) {
        EGLDisplay display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        EGL14.eglInitialize(display, version, 0, version, 1);
        int[] attribList = {
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_NONE, 0,
                EGL14.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        EGL14.eglChooseConfig(display, attribList, 0, configs, 0, configs.length, numConfigs, 0);

        EGLConfig config = configs[0];
        EGLContext context = EGL14.eglCreateContext(display, config, EGL14.EGL_NO_CONTEXT, new int[]{
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        }, 0);
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(display, config, surface,
                new int[]{
                        EGL14.EGL_NONE
                }, 0);
        EGL14.eglMakeCurrent(display, eglSurface, eglSurface, context);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        EGL14.eglSwapBuffers(display, eglSurface);
        EGL14.eglDestroySurface(display, eglSurface);
        EGL14.eglMakeCurrent(display, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(display, context);
        EGL14.eglTerminate(display);
    }

    /**
     * 设置相机预览页面旋转
     */
    private static Matrix changeSize(TextureView textureView) throws CameraAccessException {
        Matrix matrix_camera = new Matrix();
        // 设置旋转，合适角度为270°
        matrix_camera.postRotate(270, textureView.getWidth() / 2f, textureView.getHeight() / 2f);
        // 获取原图的宽高
        String cameraId = cameraManager.getCameraIdList()[0]; // 获取第一个相机的ID，这里假设只有一个相机
        CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        assert map != null;
        Size previewSize = map.getOutputSizes(SurfaceTexture.class)[0]; // 获取预览尺寸
        // 计算宽高比例
        float originalAspectRatio = (float) textureView.getWidth() / previewSize.getHeight();
        float textureViewAspectRatio = (float) textureView.getHeight() / textureView.getHeight();

        float scaleX = textureViewAspectRatio / originalAspectRatio / 0.75f;
        float scaleY = 1f;

        // 根据缩放比例和旋转进行变换
        matrix_camera.postScale(scaleX, scaleY, textureView.getWidth() / 2f, textureView.getHeight() / 2f);
        return matrix_camera;
    }

    public static Bitmap getCamera() throws CameraAccessException {
        Bitmap bitmap = camera_show.getBitmap();
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap bitmap_2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, changeSize(camera_show), true);

        int x = 200 * 2;
        int y = 100 * 2;
        int w = 640 * 2;
        int h = 360 * 2;

        Bitmap bitmap_3 = Bitmap.createBitmap(bitmap_2, x, y, w, h);

        return resizeBitmap(bitmap_3, 640, 360);
    }

    public static Bitmap resizeBitmap(Bitmap originalBitmap, int targetWidth, int targetHeight) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        float scaleWidth = ((float) targetWidth) / originalWidth;
        float scaleHeight = ((float) targetHeight) / originalHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, false);
    }

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private boolean checkPermission() {
        if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return false;
        } else {
            // 已经有相机权限，可以执行打开相机的操作
            return true;
        }
    }
}
