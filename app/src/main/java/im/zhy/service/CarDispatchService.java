//package im.zhy.service;
//
//import static im.fyhz.utils.newShapeAndLicense.results;
//
//import android.graphics.Bitmap;
//
//import car.car2024.ActivityView.XcApplication;
//import com.utils.FileService;
//
//import java.util.List;
//
//import car.car2024.ActivityView.FirstActivity;
//import car.car2024.ActivityView.LoginActivity;
//import car.car2024.FragmentView.LeftFragment;
//import Identify.utils.LightIdentify;
//import car.car2024.Utils.Socket.Variable;
//import im.fyhz.utils.ShapeAndLicense;
//import im.hdy.CarData;
//
//
///**
// * Created by zhy on 09/03/2019.
// * 对一些流程进行封装，这样其它类可以直接调用
// */
//
//@SuppressWarnings("all")
//public class CarDispatchService {
//
//    /**
//     * 车牌图形识别
//     *
//     * @param i 0：车牌和图形在一个tft上 1：tft上只有车牌  2：tft上只有图形
//     */
////    public static void licenseShape(int i, int page) {
////
////        //将图形和车牌同时执行的方法的，是否发送了成功指令将它设为false的初始状态
////        CarData.license_shape_complete = false;
////        try {
////            if (i == 0) {
////                ShapeAndLicense.shapeLicense(page);
//////            ShapeLicense.shapeLicense(TFTPage);
//////            } else if (i == 1) {
//////                ShapeAndLicense.license();
////////            ShapeLicense.license(TFTPage);
//////            } else {
//////                ShapeAndLicense.shape();
////////            ShapeLicense.shape(TFTPage);
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//////        new Thread(new Runnable() {
//////            @Override
//////            public void run() {
//////                try {
//////
//////                    Thread.sleep(50000);
//////                } catch (InterruptedException e) {
//////                    e.printStackTrace();
//////                }finally {
//////                    //还未发送
//////                    if (!CarData.license_shape_complete) {
//////                        //发送完成
//////                        System.out.println("图像识别超时，发送图像识别完成信息");
//////                        FirstActivity.Connect_Transport.licenseShape();
//////                    }
//////                }
//////
//////            }
//////        }).start();
////
////    }
//}
//
