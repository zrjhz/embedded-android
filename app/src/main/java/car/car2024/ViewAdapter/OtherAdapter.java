package car.car2024.ViewAdapter;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import car.car2024.ActivityView.FirstActivity;
import car.car2024.ActivityView.R;
import car.car2024.FragmentView.LeftFragment;
import car.car2024.Utils.Camera.CameraUtils;


@SuppressWarnings("unused")
public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder> {
    private final List<Other_Landmark> mOtherLandmarkList;
    static Context context = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View InfrareView;
        ImageView OtherImage;
        TextView OtherName;

        public ViewHolder(View view) {
            super(view);
            InfrareView = view;
            OtherImage = view.findViewById(R.id.landmark_image);
            OtherName = view.findViewById(R.id.landmark_name);
        }
    }

    public OtherAdapter(List<Other_Landmark> InfrareLandmarkList, Context context) {
        mOtherLandmarkList = InfrareLandmarkList;
        OtherAdapter.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.OtherName.setOnClickListener(v -> {
            int position = holder.getBindingAdapterPosition();
            Other_Landmark otherLandmark = mOtherLandmarkList.get(position);
            Other_select(otherLandmark);
        });
        holder.OtherImage.setOnClickListener(v -> {
            int position = holder.getBindingAdapterPosition();
            Other_Landmark otherLandmark = mOtherLandmarkList.get(position);
            Other_select(otherLandmark);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Other_Landmark InfrareLandmark = mOtherLandmarkList.get(position);
        holder.OtherImage.setImageResource(InfrareLandmark.getImageId());
        holder.OtherName.setText(InfrareLandmark.getName());
    }

    @Override
    public int getItemCount() {
        return mOtherLandmarkList.size();
    }


    private void Other_select(Other_Landmark InfrareLandmark) {
        switch (InfrareLandmark.getName()) {
            case "设置预设位":
                setPositionDialog();
                break;
            case "调用预设位":
                callPositionDialog();
                break;
            case "设置预设位转动次数":
                setCameraTime();
                break;
            case "设置亮度":
                cameraSetParameter(CameraUtils.BRIGHTNESS);
                break;
            case "设置对比度":
                cameraSetParameter(CameraUtils.CONTRAST);
                break;
            case "设置饱和度":
                cameraSetParameter(CameraUtils.SATURABILITY);
                break;
            case "设置色度":
                cameraSetParameter(CameraUtils.CHROMA);
                break;
            case "预设":
                cameraParameter();
                break;
        }
    }
    // 调用摄像头预设位
    private void callPositionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("调用摄像头预设位");
        final String[] set_item = {"call1", "call2", "call3", "call4", "call5", "call6", "call7", "call8", "call9", "call10", "原点", "交通灯", "右90°", "左90°", "右45°", "左45°"};
        builder.setSingleChoiceItems(set_item, -1, (dialog, which) -> {
            LeftFragment.cameraCommandUtil.postHttp(FirstActivity.IPCamera, (30 + which * 2 + 1), 0);
            showInfo("预设位：" + set_item[which] + "  调用成功");
            dialog.cancel();
        });
        builder.create().show();
    }

    // 设置摄像头预设位
    private void setPositionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("设置摄像头预设位");
        final String[] set_item = {"set1", "set2", "set3", "set4", "set5", "set6", "set7", "set8", "set9", "set10", "原点", "交通灯", "右90°", "左90°", "右45°", "左45°"};
        builder.setSingleChoiceItems(set_item, -1, (dialog, which) -> {
            LeftFragment.cameraCommandUtil.postHttp(FirstActivity.IPCamera, (30 + which * 2), 0);
            showInfo("预设位：" + set_item[which] + "  设置成功");
            dialog.cancel();
        });
        builder.create().show();
    }

    // 设置预设位转动次数
    private void setCameraTime() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("设置预设位转动次数：" + CameraUtils.getCameraTime());
        final String[] set_item = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "转动全部"};
        builder.setSingleChoiceItems(set_item, -1, (dialog, which) -> {
            if (which < set_item.length - 1) {
                CameraUtils.setCameraTime(which);
                showInfo("预设转动：" + which + " 次设置成功");
            } else {
                new Thread(CameraUtils::allCameraPreinstall).start();
                showInfo("转动全部预设位");
            }

            dialog.cancel();
        });
        builder.create().show();
    }


    // 摄像头 色温、亮度、饱和度、色度 调节
    private void cameraSetParameter(final int param) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final String title = getTitle(param);

        builder.setTitle(title);
        final String[] set_item = new String[35];
        for (int i = 0; i < set_item.length; i++) {
            set_item[i] = "set_" + i;
        }

        builder.setSingleChoiceItems(set_item, -1, (dialog, which) -> {
            CameraUtils.setCameraParameter(param, which);
            showInfo(title + " 设置成：" + which + " 级别成功");
            dialog.cancel();
        });
        builder.create().show();
    }

    // 参数预设及调用
    private void cameraParameter() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("预设：" + (CameraUtils.getNowPreinstallParam() == 0 ? "默认" : CameraUtils.getNowPreinstallParam()));
        final String[] set_item = {"set1", "call1", "set2", "call2", "set3", "call3", "默认值"};
        builder.setSingleChoiceItems(set_item, -1, (dialog, which) -> {
            CameraUtils.preinstallCameraParam(which);

            String title;
            if (which % 2 == 0) {
                title = " 设置";
            } else {
                title = " 调用";
            }

            int rank = which / 2 + 1;

            showInfo("预设参数：" + rank + title + "成功");

            dialog.cancel();

        });
        builder.create().show();
    }

    private String getTitle(int param) {
        String title = "";
        switch (param) {
            case CameraUtils.BRIGHTNESS:
                title = "亮度：" + CameraUtils.getNowParam(CameraUtils.BRIGHTNESS);
                break;
            case CameraUtils.CONTRAST:
                title = "对比度：" + CameraUtils.getNowParam(CameraUtils.CONTRAST);
                break;
            case CameraUtils.SATURABILITY:
                title = "饱和度：" + CameraUtils.getNowParam(CameraUtils.SATURABILITY);
                break;
            case CameraUtils.CHROMA:
                title = "色度：" + CameraUtils.getNowParam(CameraUtils.CHROMA);
                break;
        }
        return title;
    }

    public static void showInfo(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}