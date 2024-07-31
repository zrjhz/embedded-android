package im.zhy.service;

import android.graphics.Bitmap;
import car.car2024.FragmentView.LeftFragment;
import car.car2024.Utils.Camera.CameraUtils;
import im.zhy.util.GetVarName;
import car.car2024.Utils.Socket.ThreadUtils;

/**
 * @author zhy
 * @create_date 2019-04-23 12:39
 */
public class ShapeService {

    /**
     * 因为 黑色、红色、青色 需要调整预设准确率才会达到最高
     * 青色 需要将对比调到正常值，不是青色则将对比度调到相对大的一个值
     * 为了准确率此处需要获取两张不同参数的图像
     */
    public static void shapeDiscernIncludeCyan(){
//        // 将摄像头调整到第一个预设
//        CameraUtils.paramPreinstall_one();
//        try {
//            Thread.sleep(400);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Bitmap bitmap1 = LeftFragment.bitmap;

        // 判断有无特殊颜色
        if (GetVarName.getColorInt(CameraUtils.getSpecialColor()) >= 0){
            // 将摄像头调整到第二个预设
            CameraUtils.paramPreinstall_two();

            ThreadUtils.sleep(1000);

            Bitmap bitmap2 = LeftFragment.bitmap;

            Bitmap[] bitmaps = new Bitmap[]{bitmap1, bitmap2};
//            ShapeDector.newShapeDector(bitmaps, true);
        }else {
//            ShapeDector.newShapeDector(bitmap1, true);
        }

    }
}
