// Tencent is pleased to support the open source community by making ncnn available.
//
// Copyright (C) 2020 THL A29 Limited, a Tencent company. All rights reserved.
//
// Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
// in compliance with the License. You may obtain a copy of the License at
//
// https://opensource.org/licenses/BSD-3-Clause
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

#include "yolov5.h"


#define xOutput1 "1562"
#define xOutput2 "1574"
#define mOutput1 "1020"
#define mOutput2 "1032"
#define lOutput1 "1291"
#define lOutput2 "1303"

extern "C" {
JNIEXPORT jobjectArray

JNICALL detect(JNIEnv *env, jobject thiz, jobject bitmap,
               jboolean use_gpu, const char *class_names[], int index,
               const char *output1, const char *output2);
// FIXME DeleteGlobalRef is missing for objCls
#define VEHICLE 0
#define MASK 1
#define SHAPE 2
#define LICENSE 3
#define TRAFFIC_SIGN 4
#define LIGHT 5
#define QRCODE 6
#define LICENSE_COLOR 7
#define QRCODE_COLOR 8
#define CAPACITY 9
#define COLOR_LIMIT 8
#define CAPACITY_COLOR 7
static jclass objCls[CAPACITY];
static jmethodID constructorId[CAPACITY];
static jfieldID xId[CAPACITY];
static jfieldID yId[CAPACITY];
static jfieldID wId[CAPACITY];
static jfieldID hId[CAPACITY];
static jfieldID labelId[CAPACITY];
static jfieldID probId[CAPACITY];
static ncnn::UnlockedPoolAllocator g_blob_pool_allocator[CAPACITY];
static ncnn::PoolAllocator g_workspace_pool_allocator[CAPACITY];
static ncnn::Net yolov5[CAPACITY];
static ncnn::Net colorModel[CAPACITY - CAPACITY_COLOR];

#define ASSERT(status, ret)     if (!(status)) { return ret; }
#define ASSERT_FALSE(status)    ASSERT(status, false)
static ncnn::UnlockedPoolAllocator g_blob_pool_allocatorLicense;
static ncnn::PoolAllocator g_workspace_pool_allocatorLicense;
static ncnn::Net yolov5License;
static ncnn::Net crnn;
static ncnn::Net color_net;


static jclass objCls2 = NULL;
static jmethodID constructorId2;
static jfieldID xId2;
static jfieldID yId2;
static jfieldID wId2;
static jfieldID hId2;
static jfieldID p1xId;
static jfieldID p1yId;
static jfieldID p2xId;
static jfieldID p2yId;
static jfieldID p3xId;
static jfieldID p3yId;
static jfieldID p4xId;
static jfieldID p4yId;
static jfieldID labelId2;
static jfieldID probId2;
static jfieldID colorId;

JNIEXPORT jint
JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "JNI_OnLoad");

    ncnn::create_gpu_instance();

    return JNI_VERSION_1_4;
}

JNIEXPORT void
JNI_OnUnload(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "JNI_OnUnload");

    ncnn::destroy_gpu_instance();
}

string plate_chars[78] = {"#", "京", "沪", "津", "渝", "冀", "晋", "蒙", "辽", "吉", "黑",
                          "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤",
                          "桂", "琼", "川", "贵", "云", "藏", "陕", "甘", "青", "宁",
                          "新", "学", "警", "港", "澳", "挂", "使", "领", "民", "航",
                          "危",
                          "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                          "A", "B", "C", "D", "E", "F", "G", "H", "J", "K",
                          "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V",
                          "W", "X", "Y", "Z", "险", "品"};
static vector<string> crnn_rec(const cv::Mat &bgr) {

    cv::Mat img = bgr;
    //获取图片的宽
    int w = img.cols;
    //获取图片的高
    int h = img.rows;

    ncnn::Mat in = ncnn::Mat::from_pixels_resize(img.data, ncnn::Mat::PIXEL_BGR, w, h, 168, 48);
    float mean[3] = {149.94, 149.94, 149.94};
    float norm[3] = {0.020319, 0.020319, 0.020319};
    //对图片进行归一化,将像素归一化到-1~1之间
    in.substract_mean_normalize(mean, norm);

    ncnn::Extractor ex = crnn.create_extractor();
    ex.set_light_mode(true);
    //设置线程个数
    ex.set_num_threads(1);
    //将图片放入到网络中,进行前向推理
    ex.input("images", in);
    ncnn::Mat feat_plate;
    ncnn::Mat feat_color;
//    color = ['黑色', '蓝色', '绿色', '白色', '黄色']
    //获取网络的输出结果
    ex.extract("129", feat_plate);
    ex.extract("output_2", feat_color);

//    int color_index =  max_element(feat_color + 0, feat_color + 78) - feat_color;
//    pretty_print(feat_color);

    ncnn::Mat plate_mat = feat_plate;
    ncnn::Mat color_mat = feat_color;
    vector<string> final_plate_str{};

    string finale_plate;
    for (int q = 0; q < plate_mat.c; q++) {
        float prebs[21];
        for (int x = 0; x < plate_mat.w; x++)  //遍历十八个车牌位置
        {
            const float *ptr = plate_mat.channel(q);
            float preb[78];
            for (int y = 0; y < plate_mat.h; y++)  //遍历68个字符串位置
            {
                preb[y] = ptr[x];  //将18个
                ptr += plate_mat.w;
            }
            int max_num_index = max_element(preb + 0, preb + 78) - preb;
//            cout<<"max_num_index"<<max_num_index<<endl;
            prebs[x] = max_num_index;
        }

        //去重复、去空白a
        vector<int> no_repeat_blank_label{};
        int pre_c = prebs[0];

        if (pre_c != 0) {
            no_repeat_blank_label.push_back(pre_c);
        }
        for (int value: prebs) {
            if (value == 0 or value == pre_c) {
                if (value == 0 or value == pre_c) {
                    pre_c = value;
                }
                continue;
            }
            no_repeat_blank_label.push_back(value);
            pre_c = value;
        }

        // 下面进行车牌lable按照字典进行转化为字符串
        string no_repeat_blank_c = "";
        for (int hh: no_repeat_blank_label) {
            no_repeat_blank_c += plate_chars[hh];
        }


        final_plate_str.push_back(no_repeat_blank_c);
        for (string plate_char: final_plate_str) {

            finale_plate += plate_char;
        }
    }
    string str = finale_plate;


    float color_result[5];
    for (int q = 0; q < color_mat.c; q++) {
        const float *ptr = color_mat.channel(q);
        for (int y = 0; y < color_mat.h; y++) {

            for (int x = 0; x < color_mat.w; x++) {
//                printf("%f ", ptr[x]);
                //cout << "1111:" << ptr[x];
                color_result[x] = ptr[x];
            }
            ptr += color_mat.w;
//            printf("\n");
        }
        printf("------------------------\n");
    }
    int color_code = max_element(color_result, color_result + 5) - color_result;
    string color_names[5] = {
            "黑色", "蓝色", "绿色", "白色", "黄色"
    };
    vector<string> plate_color(2);
    plate_color[0] = str;
    plate_color[1] = color_names[color_code];
    return plate_color;
}

bool BitmapToMatrix(JNIEnv *env, jobject obj_bitmap, cv::Mat &matrix) {
    void *bitmapPixels;                                            // Save picture pixel data
    AndroidBitmapInfo bitmapInfo;                                   // Save picture parameters

    ASSERT_FALSE(AndroidBitmap_getInfo(env, obj_bitmap, &bitmapInfo) >=
                 0);        // Get picture parameters
    ASSERT_FALSE(bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888
                 || bitmapInfo.format ==
                    ANDROID_BITMAP_FORMAT_RGB_565);          // Only ARGB? 8888 and RGB? 565 are supported
    ASSERT_FALSE(AndroidBitmap_lockPixels(env, obj_bitmap, &bitmapPixels) >=
                 0);  // Get picture pixels (lock memory block)
    ASSERT_FALSE(bitmapPixels);

    if (bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        cv::Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC4,
                    bitmapPixels);    // Establish temporary mat
        tmp.copyTo(
                matrix);                                                         // Copy to target matrix
    } else {
        cv::Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC2, bitmapPixels);
        cv::cvtColor(tmp, matrix, cv::COLOR_BGR5652RGB);
    }

    //convert RGB to BGR
    cv::cvtColor(matrix, matrix, cv::COLOR_RGB2BGR);

    AndroidBitmap_unlockPixels(env, obj_bitmap);            // Unlock
    return true;
}



//todo 车辆
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_VehicleModel_Init(JNIEnv *env, jobject thiz,
                                           jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator[VEHICLE];
    opt.workspace_allocator = &g_workspace_pool_allocator[VEHICLE];
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5[VEHICLE].opt = opt;

    yolov5[VEHICLE].register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5[VEHICLE].load_param(mgr, "vehicle/vehicle3_best-sim.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5[VEHICLE].load_model(mgr, "vehicle/vehicle3_best-sim.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass("car/Identify/models/VehicleModel$Obj");
    objCls[VEHICLE] = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));

    constructorId[VEHICLE] = env->GetMethodID(objCls[VEHICLE], "<init>",
                                              "(Lcar/Identify/models/VehicleModel;)V");

    xId[VEHICLE] = env->GetFieldID(objCls[VEHICLE], "x", "F");
    yId[VEHICLE] = env->GetFieldID(objCls[VEHICLE], "y", "F");
    wId[VEHICLE] = env->GetFieldID(objCls[VEHICLE], "w", "F");
    hId[VEHICLE] = env->GetFieldID(objCls[VEHICLE], "h", "F");
    labelId[VEHICLE] = env->GetFieldID(objCls[VEHICLE], "label", "Ljava/lang/String;");
    probId[VEHICLE] = env->GetFieldID(objCls[VEHICLE], "prob", "F");

    return JNI_TRUE;
}

// public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);
JNIEXPORT jobjectArray

JNICALL Java_car_Identify_models_VehicleModel_Detect(JNIEnv *env, jobject thiz,
                                                     jobject bitmap,
                                                     jboolean use_gpu) {

    // objects to Obj[]
    const char *class_names[] = {
            "bicycle", "black_car", "black_truck", "blue_car", "blue_truck",
            "green_car", "green_truck", "grey_car", "grey_truck", "motorcycle",
            "orange_car", "orange_truck", "purple_car", "purple_truck",
            "red_car", "red_truck", "white_car", "white_truck", "yellow_car",
            "yellow_truck"
    };

    return detect(env, thiz, bitmap, use_gpu, class_names, VEHICLE, xOutput1, xOutput2);
}
//todo 口罩
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_MaskModel_Init(JNIEnv *env, jobject thiz,
                                        jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator[MASK];
    opt.workspace_allocator = &g_workspace_pool_allocator[MASK];
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5[MASK].opt = opt;

    yolov5[MASK].register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5[MASK].load_param(mgr, "mask/mask_best-sim.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5[MASK].load_model(mgr, "mask/mask_best-sim.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass("car/Identify/models/MaskModel$Obj");
    objCls[MASK] = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));

    constructorId[MASK] = env->GetMethodID(objCls[MASK], "<init>",
                                           "(Lcar/Identify/models/MaskModel;)V");

    xId[MASK] = env->GetFieldID(objCls[MASK], "x", "F");
    yId[MASK] = env->GetFieldID(objCls[MASK], "y", "F");
    wId[MASK] = env->GetFieldID(objCls[MASK], "w", "F");
    hId[MASK] = env->GetFieldID(objCls[MASK], "h", "F");
    labelId[MASK] = env->GetFieldID(objCls[MASK], "label", "Ljava/lang/String;");
    probId[MASK] = env->GetFieldID(objCls[MASK], "prob", "F");

    return JNI_TRUE;
}

// public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);
JNIEXPORT jobjectArray

JNICALL Java_car_Identify_models_MaskModel_Detect(JNIEnv *env, jobject thiz,
                                                  jobject bitmap,
                                                  jboolean use_gpu) {

    // objects to Obj[]
    const char *class_names[] = {
            "no_mask", "mask"
    };

    return detect(env, thiz, bitmap, use_gpu, class_names, MASK, xOutput1, xOutput2);
}

//todo 图形
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_ShapeModel_Init(JNIEnv *env, jobject thiz,
                                         jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator[SHAPE];
    opt.workspace_allocator = &g_workspace_pool_allocator[SHAPE];
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5[SHAPE].opt = opt;

    yolov5[SHAPE].register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5[SHAPE].load_param(mgr, "shape/shape9-sim.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5[SHAPE].load_model(mgr, "shape/shape9-sim.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass("car/Identify/models/ShapeModel$Obj");
    objCls[SHAPE] = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));

    constructorId[SHAPE] = env->GetMethodID(objCls[SHAPE], "<init>",
                                            "(Lcar/Identify/models/ShapeModel;)V");

    xId[SHAPE] = env->GetFieldID(objCls[SHAPE], "x", "F");
    yId[SHAPE] = env->GetFieldID(objCls[SHAPE], "y", "F");
    wId[SHAPE] = env->GetFieldID(objCls[SHAPE], "w", "F");
    hId[SHAPE] = env->GetFieldID(objCls[SHAPE], "h", "F");
    labelId[SHAPE] = env->GetFieldID(objCls[SHAPE], "label", "Ljava/lang/String;");
    probId[SHAPE] = env->GetFieldID(objCls[SHAPE], "prob", "F");

    return JNI_TRUE;
}

// public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);
JNIEXPORT jobjectArray

JNICALL
Java_car_Identify_models_ShapeModel_Detect(JNIEnv *env, jobject thiz,
                                           jobject bitmap,
                                           jboolean use_gpu) {
    // objects to Obj[]
    const char *class_names[] = {
            "black_circle", "black_diamond", "black_pentagon", "black_rectangle",
            "black_square", "black_trapezoid", "black_triangle", "blue_circle",
            "blue_diamond", "blue_pentagon", "blue_rectangle", "blue_square",
            "blue_trapezoid", "blue_triangle", "cyan_circle", "cyan_diamond",
            "cyan_pentagon", "cyan_rectangle", "cyan_square", "cyan_trapezoid",
            "cyan_triangle", "green_circle", "green_diamond", "green_pentagon",
            "green_rectangle", "green_square", "green_trapezoid",
            "green_triangle", "magenta_circle", "magenta_diamond",
            "magenta_pentagon", "magenta_rectangle", "magenta_square",
            "magenta_trapezoid", "magenta_triangle", "red_circle", "red_diamond",
            "red_pentagon", "red_rectangle", "red_square", "red_trapezoid",
            "red_triangle", "white_circle", "white_diamond", "white_pentagon",
            "white_rectangle", "white_square", "white_trapezoid", "white_triangle",
            "yellow_circle", "yellow_diamond", "yellow_pentagon", "yellow_rectangle",
            "yellow_square", "yellow_trapezoid", "yellow_triangle",
    };

    return detect(env, thiz, bitmap, use_gpu, class_names, SHAPE, xOutput1, xOutput2);
}
//todo 车牌
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_LicenseModel_Init(JNIEnv *env, jobject thiz,
                                           jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator[LICENSE];
    opt.workspace_allocator = &g_workspace_pool_allocator[LICENSE];
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5[LICENSE].opt = opt;

    yolov5[LICENSE].register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5[LICENSE].load_param(mgr, "license/license_best2-sim.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5[LICENSE].load_model(mgr, "license/license_best2-sim.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass("car/Identify/models/LicenseModel$Obj");
    objCls[LICENSE] = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));

    constructorId[LICENSE] = env->GetMethodID(objCls[LICENSE], "<init>",
                                              "(Lcar/Identify/models/LicenseModel;)V");

    xId[LICENSE] = env->GetFieldID(objCls[LICENSE], "x", "F");
    yId[LICENSE] = env->GetFieldID(objCls[LICENSE], "y", "F");
    wId[LICENSE] = env->GetFieldID(objCls[LICENSE], "w", "F");
    hId[LICENSE] = env->GetFieldID(objCls[LICENSE], "h", "F");
    labelId[LICENSE] = env->GetFieldID(objCls[LICENSE], "label", "Ljava/lang/String;");
    probId[LICENSE] = env->GetFieldID(objCls[LICENSE], "prob", "F");

    return JNI_TRUE;
}

// public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);
JNIEXPORT jobjectArray

JNICALL
Java_car_Identify_models_LicenseModel_Detect(JNIEnv *env, jobject thiz,
                                             jobject bitmap,
                                             jboolean use_gpu) {

    // objects to Obj[]
    const char *class_names[] = {
            "-", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "J", "K", "L", "M", "N", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"
    };

    return detect(env, thiz, bitmap, use_gpu, class_names, LICENSE, xOutput1, xOutput2);
}
//todo 交通标志
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_TrafficSignModel_Init(JNIEnv *env, jobject thiz,
                                               jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator[TRAFFIC_SIGN];
    opt.workspace_allocator = &g_workspace_pool_allocator[TRAFFIC_SIGN];
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5[TRAFFIC_SIGN].opt = opt;

    yolov5[TRAFFIC_SIGN].register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5[TRAFFIC_SIGN].load_param(mgr, "trafficSign/traffic5-sim.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5[TRAFFIC_SIGN].load_model(mgr, "trafficSign/traffic5-sim.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass(
            "car/Identify/models/TrafficSignModel$Obj");
    objCls[TRAFFIC_SIGN] = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));

    constructorId[TRAFFIC_SIGN] = env->GetMethodID(objCls[TRAFFIC_SIGN], "<init>",
                                                   "(Lcar/Identify/models/TrafficSignModel;)V");

    xId[TRAFFIC_SIGN] = env->GetFieldID(objCls[TRAFFIC_SIGN], "x", "F");
    yId[TRAFFIC_SIGN] = env->GetFieldID(objCls[TRAFFIC_SIGN], "y", "F");
    wId[TRAFFIC_SIGN] = env->GetFieldID(objCls[TRAFFIC_SIGN], "w", "F");
    hId[TRAFFIC_SIGN] = env->GetFieldID(objCls[TRAFFIC_SIGN], "h", "F");
    labelId[TRAFFIC_SIGN] = env->GetFieldID(objCls[TRAFFIC_SIGN], "label", "Ljava/lang/String;");
    probId[TRAFFIC_SIGN] = env->GetFieldID(objCls[TRAFFIC_SIGN], "prob", "F");

    return JNI_TRUE;
}

// public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);
JNIEXPORT jobjectArray

JNICALL
Java_car_Identify_models_TrafficSignModel_Detect(JNIEnv *env, jobject thiz,
                                                 jobject bitmap,
                                                 jboolean use_gpu) {
    const char *class_names[] = {
            "go_straight", "left", "no_entry", "no_left", "no_right",
            "no_straight", "right", "speed_limit", "turn", "no_turn"
    };
    const char className[] = "car/Identify/models/TrafficSignModel$Obj";
    const char constructor[] = "(Lcar/Identify/models/TrafficSignModel;)V";
    return detect(env, thiz, bitmap, use_gpu, class_names, TRAFFIC_SIGN, xOutput1, xOutput2);
}
//todo 交通灯
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_LightModel_Init(JNIEnv *env, jobject thiz,
                                         jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator[LIGHT];
    opt.workspace_allocator = &g_workspace_pool_allocator[LIGHT];
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5[LIGHT].opt = opt;

    yolov5[LIGHT].register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5[LIGHT].load_param(mgr, "light/light2_l-sim.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5[LIGHT].load_model(mgr, "light/light2_l-sim.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass("car/Identify/models/LightModel$Obj");
    objCls[LIGHT] = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));

    constructorId[LIGHT] = env->GetMethodID(objCls[LIGHT], "<init>",
                                            "(Lcar/Identify/models/LightModel;)V");

    xId[LIGHT] = env->GetFieldID(objCls[LIGHT], "x", "F");
    yId[LIGHT] = env->GetFieldID(objCls[LIGHT], "y", "F");
    wId[LIGHT] = env->GetFieldID(objCls[LIGHT], "w", "F");
    hId[LIGHT] = env->GetFieldID(objCls[LIGHT], "h", "F");
    labelId[LIGHT] = env->GetFieldID(objCls[LIGHT], "label", "Ljava/lang/String;");
    probId[LIGHT] = env->GetFieldID(objCls[LIGHT], "prob", "F");

    return JNI_TRUE;
}

// public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);
JNIEXPORT jobjectArray

JNICALL
Java_car_Identify_models_LightModel_Detect(JNIEnv *env, jobject thiz,
                                           jobject bitmap,
                                           jboolean use_gpu) {
    const char *class_names[] = {
            "green", "red", "yellow"
    };

    return detect(env, thiz, bitmap, use_gpu, class_names, LIGHT, lOutput1, lOutput2);
}

//todo 二维码检测
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_QRCodeModel_Init(JNIEnv *env, jobject thiz,
                                          jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocator[QRCODE];
    opt.workspace_allocator = &g_workspace_pool_allocator[QRCODE];
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5[QRCODE].opt = opt;

    yolov5[QRCODE].register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5[QRCODE].load_param(mgr, "qrcode/qrcode_best-sim.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5[QRCODE].load_model(mgr, "qrcode/qrcode_best-sim.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass("car/Identify/models/QRCodeModel$Obj");
    objCls[QRCODE] = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));


    constructorId[QRCODE] = env->GetMethodID(objCls[QRCODE], "<init>",
                                             "(Lcar/Identify/models/QRCodeModel;)V");

    xId[QRCODE] = env->GetFieldID(objCls[QRCODE], "x", "F");
    yId[QRCODE] = env->GetFieldID(objCls[QRCODE], "y", "F");
    wId[QRCODE] = env->GetFieldID(objCls[QRCODE], "w", "F");
    hId[QRCODE] = env->GetFieldID(objCls[QRCODE], "h", "F");
    labelId[QRCODE] = env->GetFieldID(objCls[QRCODE], "label", "Ljava/lang/String;");
    probId[QRCODE] = env->GetFieldID(objCls[QRCODE], "prob", "F");

    return JNI_TRUE;
}

// public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);
JNIEXPORT jobjectArray

JNICALL
Java_car_Identify_models_QRCodeModel_Detect(JNIEnv *env, jobject thiz,
                                            jobject bitmap,
                                            jboolean use_gpu) {
    const char *class_names[] = {
            "qrcode"
    };

    return detect(env, thiz, bitmap, use_gpu, class_names, QRCODE, mOutput1, mOutput2);
}

JNIEXPORT jobjectArray
detect(JNIEnv *env, jobject thiz, jobject bitmap,
       jboolean use_gpu, const char *class_names[], int index,
       const char *output1, const char *output2) {
    if (use_gpu == JNI_TRUE && ncnn::get_gpu_count() == 0) {
        return NULL;
        //return env->NewStringUTF("no vulkan capable gpu");
    }
    double start_time = ncnn::get_current_time();

    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);
    const int width = info.width;
    const int height = info.height;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
        return NULL;
    // ncnn from bitmap
    const int target_size = 640;

    // letterbox pad to multiple of 32
    int w = width;
    int h = height;
    float scale = 1.f;
    if (w > h) {
        scale = (float) target_size / w;
        w = target_size;
        h = h * scale;
    } else {
        scale = (float) target_size / h;
        h = target_size;
        w = w * scale;
    }

    ncnn::Mat in = ncnn::Mat::from_android_bitmap_resize(env, bitmap, ncnn::Mat::PIXEL_RGB, w, h);
    // pad to target_size rectangle
    // yolov5/utils/datasets.py letterbox
    int wpad = (w + 31) / 32 * 32 - w;
    int hpad = (h + 31) / 32 * 32 - h;
    ncnn::Mat in_pad; //tensor in numpy
    ncnn::copy_make_border(in, in_pad, hpad / 2, hpad - hpad / 2, wpad / 2, wpad - wpad / 2,
                           ncnn::BORDER_CONSTANT, 114.f);

    // yolov5
    std::vector<Object> objects;
    {
        const float prob_threshold = 0.25f;
        const float nms_threshold = 0.45f;

        const float norm_vals[3] = {1 / 255.f, 1 / 255.f, 1 / 255.f};
        in_pad.substract_mean_normalize(0, norm_vals);//均值减法、归一化

        ncnn::Extractor ex = yolov5[index].create_extractor();

        ex.set_vulkan_compute(use_gpu);

        ex.input("images", in_pad);

        std::vector<Object> proposals;

        // anchor setting from yolov5/models/yolov5s.yaml

        // stride 8
        {
            ncnn::Mat out;
            ex.extract("output", out);
            ncnn::Mat anchors(6);
            anchors[0] = 10.f;
            anchors[1] = 13.f;
            anchors[2] = 16.f;
            anchors[3] = 30.f;
            anchors[4] = 33.f;
            anchors[5] = 23.f;

            std::vector<Object> objects8;
            generate_proposals(anchors, 8, in_pad, out, prob_threshold, objects8);
            //模型       步长      图像             预测 概率阈值             输出
            proposals.insert(proposals.end(), objects8.begin(), objects8.end());
        }

        // stride 16
        {
            ncnn::Mat out;
            ex.extract(output1, out);

            ncnn::Mat anchors(6);
            anchors[0] = 30.f;
            anchors[1] = 61.f;
            anchors[2] = 62.f;
            anchors[3] = 45.f;
            anchors[4] = 59.f;
            anchors[5] = 119.f;

            std::vector<Object> objects16;
            generate_proposals(anchors, 16, in_pad, out, prob_threshold, objects16);
            proposals.insert(proposals.end(), objects16.begin(), objects16.end());
        }

        // stride 32
        {
            ncnn::Mat out;
            ex.extract(output2, out);

            ncnn::Mat anchors(6);
            anchors[0] = 116.f;
            anchors[1] = 90.f;
            anchors[2] = 156.f;
            anchors[3] = 198.f;
            anchors[4] = 373.f;
            anchors[5] = 326.f;

            std::vector<Object> objects32;
            generate_proposals(anchors, 32, in_pad, out, prob_threshold, objects32);

            proposals.insert(proposals.end(), objects32.begin(), objects32.end());
        }

        // sort all proposals by score from highest to lowest
        qsort_descent_inplace(proposals);

        // apply nms with nms_threshold
        std::vector<int> picked;
        nms_sorted_bboxes(proposals, picked, nms_threshold);

        int count = picked.size();

        objects.resize(count);
        for (int i = 0; i < count; i++) {
            objects[i] = proposals[picked[i]];

            // adjust offset to original unpadded
            float x0 = (objects[i].x - (wpad / 2)) / scale;
            float y0 = (objects[i].y - (hpad / 2)) / scale;
            float x1 = (objects[i].x + objects[i].w - (wpad / 2)) / scale;
            float y1 = (objects[i].y + objects[i].h - (hpad / 2)) / scale;

            // clip
            x0 = std::max(std::min(x0, (float) (width - 1)), 0.f);
            y0 = std::max(std::min(y0, (float) (height - 1)), 0.f);
            x1 = std::max(std::min(x1, (float) (width - 1)), 0.f);
            y1 = std::max(std::min(y1, (float) (height - 1)), 0.f);

            objects[i].x = x0;
            objects[i].y = y0;
            objects[i].w = x1 - x0;
            objects[i].h = y1 - y0;
        }
    }
    jobjectArray jObjArray = env->NewObjectArray(objects.size(), objCls[index], NULL);

    for (size_t i = 0; i < objects.size(); i++) {
        jobject jObj = env->NewObject(objCls[index], constructorId[index], thiz);

        env->SetFloatField(jObj, xId[index], objects[i].x);
        env->SetFloatField(jObj, yId[index], objects[i].y);
        env->SetFloatField(jObj, wId[index], objects[i].w);
        env->SetFloatField(jObj, hId[index], objects[i].h);
        env->SetObjectField(jObj, labelId[index], env->NewStringUTF(class_names[objects[i].label]));
        env->SetFloatField(jObj, probId[index], objects[i].prob);

        env->SetObjectArrayElement(jObjArray, i, jObj);
    }

    double elasped = ncnn::get_current_time() - start_time;
    __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "%.2fms   detect", elasped);

    return jObjArray;
}

// todo 车牌定位
JNIEXPORT jboolean

JNICALL
Java_car_Identify_models_LocateModel_Init(JNIEnv *env, jobject thiz,
                                          jobject assetManager) {
    ncnn::Option opt;
    opt.lightmode = true;
    opt.num_threads = 4;
    opt.blob_allocator = &g_blob_pool_allocatorLicense;
    opt.workspace_allocator = &g_workspace_pool_allocatorLicense;
    opt.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt.use_vulkan_compute = true;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    yolov5License.opt = opt;

    yolov5License.register_custom_layer("YoloV5Focus", YoloV5Focus_layer_creator);

    // init param
    {
        int ret = yolov5License.load_param(mgr, "locate/best.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = yolov5License.load_model(mgr, "locate/best.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "load_model failed");
            return JNI_FALSE;
        }
    }

    // init jni glue
    jclass localObjCls = env->FindClass("car/Identify/models/LocateModel$Obj");
    objCls2 = reinterpret_cast<jclass>(env->NewGlobalRef(localObjCls));

    constructorId2 = env->GetMethodID(objCls2, "<init>",
                                      "(Lcar/Identify/models/LocateModel;)V");

    xId2 = env->GetFieldID(objCls2, "x", "F");
    yId2 = env->GetFieldID(objCls2, "y", "F");
    wId2 = env->GetFieldID(objCls2, "w", "F");
    hId2 = env->GetFieldID(objCls2, "h", "F");
    p1xId = env->GetFieldID(objCls2, "p1x", "F");
    p1yId = env->GetFieldID(objCls2, "p1y", "F");
    p2xId = env->GetFieldID(objCls2, "p2x", "F");
    p2yId = env->GetFieldID(objCls2, "p2y", "F");
    p3xId = env->GetFieldID(objCls2, "p3x", "F");
    p3yId = env->GetFieldID(objCls2, "p3y", "F");
    p4xId = env->GetFieldID(objCls2, "p4x", "F");
    p4yId = env->GetFieldID(objCls2, "p4y", "F");

    labelId2 = env->GetFieldID(objCls2, "label", "Ljava/lang/String;");
    probId2 = env->GetFieldID(objCls2, "prob", "F");
    colorId = env->GetFieldID(objCls2, "color", "Ljava/lang/String;");

    // TODO: implement Init()
    ncnn::Option opt_crnn;
    opt_crnn.lightmode = true;
    opt_crnn.num_threads = 4;
    opt_crnn.blob_allocator = &g_blob_pool_allocatorLicense;
    opt_crnn.workspace_allocator = &g_workspace_pool_allocatorLicense;
    opt_crnn.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt_crnn.use_vulkan_compute = true;

    crnn.opt = opt_crnn;

    // init param
    {
        int ret = crnn.load_param(mgr, "locate/plate_rec_color.param");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "plate_rec_color", "load_param failed");
            return JNI_FALSE;
        }
    }

    // init bin
    {
        int ret = crnn.load_model(mgr, "locate/plate_rec_color.bin");
        if (ret != 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "plate_rec_color", "load_model failed");
            return JNI_FALSE;
        }
    }

    // TODO: implement Init()
    ncnn::Option opt_color;
    opt_color.lightmode = true;
    opt_color.num_threads = 4;
    opt_color.blob_allocator = &g_blob_pool_allocatorLicense;
    opt_color.workspace_allocator = &g_workspace_pool_allocatorLicense;
    opt_color.use_packing_layout = true;

    // use vulkan compute
    if (ncnn::get_gpu_count() != 0)
        opt_color.use_vulkan_compute = true;

    color_net.opt = opt_color;

    return JNI_TRUE;
}

JNIEXPORT jobjectArray

JNICALL
Java_car_Identify_models_LocateModel_Detect(JNIEnv *env, jobject thiz,
                                            jobject bitmap,
                                            jboolean use_gpu) {
    if (use_gpu == JNI_TRUE && ncnn::get_gpu_count() == 0) {
        return NULL;
    }

    double start_time = ncnn::get_current_time();

    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);
    const int width = info.width;
    const int height = info.height;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
        return NULL;

    // ncnn from bitmap
    const int target_size = 640;

    // letterbox pad to multiple of 32
    int w = width;
    int h = height;
    float scale = 1.f;
    if (w > h) {
        scale = (float) target_size / w;
        w = target_size;
        h = h * scale;
    } else {
        scale = (float) target_size / h;
        h = target_size;
        w = w * scale;
    }

    ncnn::Mat in = ncnn::Mat::from_android_bitmap_resize(env, bitmap, ncnn::Mat::PIXEL_RGB, w, h);

    int wpad = (w + 31) / 32 * 32 - w;
    int hpad = (h + 31) / 32 * 32 - h;
    ncnn::Mat in_pad;
    ncnn::copy_make_border(in, in_pad, hpad / 2, hpad - hpad / 2, wpad / 2, wpad - wpad / 2,
                           ncnn::BORDER_CONSTANT, 114.f);

    std::vector<Object2> objects;
    {
        const float prob_threshold = 0.25f;
        const float nms_threshold = 0.45f;

        const float norm_vals[3] = {1 / 255.f, 1 / 255.f, 1 / 255.f};
        in_pad.substract_mean_normalize(0, norm_vals);

        ncnn::Extractor ex = yolov5License.create_extractor();

        ex.set_vulkan_compute(use_gpu);

//        ex.input("images", in_pad);
        ex.input("data", in_pad);

        std::vector<Object2> proposals;

        // stride 8
        {
            ncnn::Mat out;
            ex.extract("stride_8", out);
            ncnn::Mat anchors(6);
            anchors[0] = 4.f;
            anchors[1] = 5.f;
            anchors[2] = 8.f;
            anchors[3] = 10.f;
            anchors[4] = 13.f;
            anchors[5] = 16.f;

            std::vector<Object2> objects8;
            generate_proposals2(anchors, 8, in_pad, out, prob_threshold, objects8);
            proposals.insert(proposals.end(), objects8.begin(), objects8.end());

        }
        //
        // stride 16
        {
            ncnn::Mat out;
            //ex.extract("781", out);
            ex.extract("stride_16", out);

            ncnn::Mat anchors(6);
            anchors[0] = 23.f;
            anchors[1] = 29.f;
            anchors[2] = 43.f;
            anchors[3] = 55.f;
            anchors[4] = 73.f;
            anchors[5] = 105.f;

            std::vector<Object2> objects16;
            generate_proposals2(anchors, 16, in_pad, out, prob_threshold, objects16);

            proposals.insert(proposals.end(), objects16.begin(), objects16.end());
        }

        // stride 32
        {
            ncnn::Mat out;
            //ex.extract("801", out);
            ex.extract("stride_32", out);
            ncnn::Mat anchors(6);
            anchors[0] = 146.f;
            anchors[1] = 217.f;
            anchors[2] = 231.f;
            anchors[3] = 300.f;
            anchors[4] = 335.f;
            anchors[5] = 433.f;
            std::vector<Object2> objects32;
            generate_proposals2(anchors, 32, in_pad, out, prob_threshold, objects32);
            proposals.insert(proposals.end(), objects32.begin(), objects32.end());
        }

        // sort all proposals by score from highest to lowest
        qsort_descent_inplace2(proposals);

        // apply nms with nms_threshold
        std::vector<int> picked;
        nms_sorted_bboxes2(proposals, picked, nms_threshold);

        int count = picked.size();

        objects.resize(count);
        for (int i = 0; i < count; i++) {
            objects[i] = proposals[picked[i]];

            // adjust offset to original unpadded
            float x0 = (objects[i].x - (wpad / 2)) / scale;
            float y0 = (objects[i].y - (hpad / 2)) / scale;

            float p1x = (objects[i].p1x - (wpad / 2)) / scale;
            float p1y = (objects[i].p1y - (hpad / 2)) / scale;
            float p2x = (objects[i].p2x - (wpad / 2)) / scale;
            float p2y = (objects[i].p2y - (hpad / 2)) / scale;
            float p3x = (objects[i].p3x - (wpad / 2)) / scale;
            float p3y = (objects[i].p3y - (hpad / 2)) / scale;
            float p4x = (objects[i].p4x - (wpad / 2)) / scale;
            float p4y = (objects[i].p4y - (hpad / 2)) / scale;

            float x1 = (objects[i].x + objects[i].w - (wpad / 2)) / scale;
            float y1 = (objects[i].y + objects[i].h - (hpad / 2)) / scale;

            // clip
            x0 = std::max(std::min(x0, (float) (width - 1)), 0.f);
            y0 = std::max(std::min(y0, (float) (height - 1)), 0.f);
            x1 = std::max(std::min(x1, (float) (width - 1)), 0.f);
            y1 = std::max(std::min(y1, (float) (height - 1)), 0.f);

            p1x = std::max(std::min(p1x, (float) (width - 1)), 0.f);
            p1y = std::max(std::min(p1y, (float) (height - 1)), 0.f);
            p2x = std::max(std::min(p2x, (float) (width - 1)), 0.f);
            p2y = std::max(std::min(p2y, (float) (height - 1)), 0.f);
            p3x = std::max(std::min(p3x, (float) (width - 1)), 0.f);
            p3y = std::max(std::min(p3y, (float) (height - 1)), 0.f);
            p4x = std::max(std::min(p4x, (float) (width - 1)), 0.f);
            p4y = std::max(std::min(p4y, (float) (height - 1)), 0.f);

            objects[i].x = x0;
            objects[i].y = y0;

            objects[i].w = x1 - x0;
            objects[i].h = y1 - y0;

            objects[i].p1x = p1x;
            objects[i].p1y = p1y;
            objects[i].p2x = p2x;
            objects[i].p2y = p2y;
            objects[i].p3x = p3x;
            objects[i].p3y = p3y;
            objects[i].p4x = p4x;
            objects[i].p4y = p4y;
        }
    }

    jobjectArray jObjArray = env->NewObjectArray(objects.size(), objCls2, NULL);

    for (size_t i = 0; i < objects.size(); i++) {
        // letterbox pad to multiple of 32
        cv::Mat image;
        BitmapToMatrix(env, bitmap, image);
        const Object2 &obj = objects[i];

        float new_x1 = objects[i].p3x - objects[i].x;
        float new_y1 = objects[i].p3y - objects[i].y;
        float new_x2 = objects[i].p4x - objects[i].x;
        float new_y2 = objects[i].p4y - objects[i].y;
        float new_x3 = objects[i].p2x - objects[i].x;
        float new_y3 = objects[i].p2y - objects[i].y;
        float new_x4 = objects[i].p1x - objects[i].x;
        float new_y4 = objects[i].p1y - objects[i].y;

        cv::Point2f src_points[4];
        cv::Point2f dst_points[4];
        //通过Image Watch查看的二维码四个角点坐标
        src_points[0] = cv::Point2f(new_x1, new_y1);
        src_points[1] = cv::Point2f(new_x2, new_y2);
        src_points[2] = cv::Point2f(new_x3, new_y3);
        src_points[3] = cv::Point2f(new_x4, new_y4);
        //期望透视变换后二维码四个角点的坐标
        dst_points[0] = cv::Point2f(0.0, 0.0);
        dst_points[1] = cv::Point2f(168.0, 0.0);
        dst_points[2] = cv::Point2f(0.0, 48.0);
        dst_points[3] = cv::Point2f(168.0, 48.0);

        cv::Mat rotation, img_warp;
        cv::Rect_<float> rect;
        rect.x = objects[i].x;
        rect.y = objects[i].y;
        rect.height = objects[i].h;
        rect.width = objects[i].w;
        cv::Mat ROI = image(rect);
        rotation = getPerspectiveTransform(src_points, dst_points);
//        cout<<"image.size():"<<image.size()<<endl;
        warpPerspective(ROI, ROI, rotation, cv::Size(168, 48));
        vector<string> plate_color_result(2);
        plate_color_result = crnn_rec(ROI);
        string plate_str = plate_color_result[0];
//        string color_names[3] = {
//                "blue", "green","yellow"
//                "蓝", "绿", "黄"
//        };
//        int color_code = color_rec_1(ROI);
//        string color_name = color_names[color_code];
        string color_name = plate_color_result[1];

        char *p = (char *) plate_str.data();
        char *p_color = (char *) color_name.data();
        jobject jObj = env->NewObject(objCls2, constructorId2, thiz);

        env->SetFloatField(jObj, xId2, objects[i].x);
        env->SetFloatField(jObj, yId2, objects[i].y);
        env->SetFloatField(jObj, wId2, objects[i].w);
        env->SetFloatField(jObj, hId2, objects[i].h);
        env->SetFloatField(jObj, p1xId, objects[i].p1x);
        env->SetFloatField(jObj, p1yId, objects[i].p1y);
        env->SetFloatField(jObj, p2xId, objects[i].p2x);
        env->SetFloatField(jObj, p2yId, objects[i].p2y);
        env->SetFloatField(jObj, p3xId, objects[i].p3x);
        env->SetFloatField(jObj, p3yId, objects[i].p3y);
        env->SetFloatField(jObj, p4xId, objects[i].p4x);
        env->SetFloatField(jObj, p4yId, objects[i].p4y);

//        env->SetObjectField(jObj, labelId, env->NewStringUTF(class_names[objects[i].label]));
        env->SetObjectField(jObj, labelId2, env->NewStringUTF(p));
        env->SetObjectField(jObj, colorId, env->NewStringUTF(p_color));
        env->SetFloatField(jObj, probId2, objects[i].prob);

        env->SetObjectArrayElement(jObjArray, i, jObj);
    }

    double elasped = ncnn::get_current_time() - start_time;
    __android_log_print(ANDROID_LOG_DEBUG, "YoloV5Ncnn", "%.2fms   detect", elasped);

    return jObjArray;
}

//todo 车牌颜色

JNIEXPORT jboolean
JNICALL
Java_car_Identify_models_LicenseColorModel_init(JNIEnv *env, jobject thiz,
                                                jobject assetManager) {
    __android_log_print(ANDROID_LOG_DEBUG, "vehicle_color", "JNI_OnLoad");
    //实例化ncnn
    ncnn::create_gpu_instance();
    ncnn::Option option;
    option.lightmode = true;
    option.num_threads = 1;
    option.blob_allocator = &g_blob_pool_allocator[LICENSE_COLOR];
    option.workspace_allocator = &g_workspace_pool_allocator[LICENSE_COLOR];

    //use vulkan
    if (ncnn::get_gpu_count() > 0) {
        option.use_vulkan_compute = true;
    }
    colorModel[LICENSE_COLOR - COLOR_LIMIT].opt = option;
    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);
    //init param
    int ret = colorModel[LICENSE_COLOR - COLOR_LIMIT].load_param(mgr,
                                                                 "color/license_color2.param");
    if (ret != 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "vehicle_color", "load_param failed");
        return false;
    }

    //init model
    ret = colorModel[LICENSE_COLOR - COLOR_LIMIT].load_model(mgr, "color/license_color2.bin");
    if (ret != 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "vehicle_color", "load_model failed");
        return false;
    }

    return true;
}

JNIEXPORT jint
JNICALL
Java_car_Identify_models_LicenseColorModel_Detect(JNIEnv *env, jobject clazz,
                                                  jobject bitmap) {
    const char *class_names[] = {
            "blue", "green", "white", "yellow"
    };

    // ################## 预处理 ##################
    // 对输入图像进行预处理操作
    ncnn::Mat in = ncnn::Mat::from_android_bitmap_resize(env, bitmap, ncnn::Mat::PIXEL_RGB, 128,
                                                         28);
    in.substract_mean_normalize(
            0,
            new float[]{1 / 255.f, 1 / 255.f, 1 / 255.f}
    );

    // ################## 预处理 ##################

    // ################## 推理 ##################
    ncnn::Extractor extractor = colorModel[LICENSE_COLOR - COLOR_LIMIT].create_extractor();
    extractor.set_vulkan_compute(true);
    extractor.set_num_threads(1);
    extractor.input("input.1", in);
    ncnn::Mat result;
    extractor.extract("202", result);
    result = result.reshape(result.w);
    // ################## 推理 ##################

    // ################## softmax + argmax ##################
    float max = -999999.0;
    for (int i = 0; i < result.w; i++) {
        //np.max(scores)
        float val = result[i];
        if (val > max) {
            max = val;
        }
    }
    float sum_exp = 0;//np.sum(np.exp(scores))
    for (int i = 0; i < result.w; i++) {
        //scores -= np.max(scores)
        //np.exp(scores)
        result[i] = exp(result[i] - max);
        sum_exp += result[i];
    }
    for (int i = 0; i < result.w; i++) {
        //np.exp(scores) / np.sum(np.exp(scores))
        result[i] = result[i] / sum_exp;
        float val = result[i];
//        __android_log_print(ANDROID_LOG_DEBUG, "vehicle_color", "%f",val);
    }
    //找到最大的元素概率
    max = -999999.0;
    int max_index = -1;
    for (int i = 0; i < result.w; i++) {
        //np.exp(scores) / np.sum(np.exp(scores))
        float val = result[i];
        if (val > max) {
            max = val;
            max_index = i;
        }
        __android_log_print(ANDROID_LOG_DEBUG, "vehicle_color", "%f", val);
    }
    if (max_index == -1) {
        return -1;
    }
    // ################## softmax + argmax ##################
    __android_log_print(ANDROID_LOG_DEBUG, "vehicle_color", "预测结果：%s,概率为：%f",
                        class_names[max_index], max);

    return max_index;
}

//todo 二维码颜色
JNIEXPORT jboolean
JNICALL
Java_car_Identify_models_QRCodeColorModel_init(JNIEnv *env, jobject thiz,
                                               jobject assetManager) {
    __android_log_print(ANDROID_LOG_DEBUG, "qrcode_color", "JNI_OnLoad");
    //实例化ncnn
    ncnn::create_gpu_instance();
    ncnn::Option option;
    option.lightmode = true;
    option.num_threads = 1;
    option.blob_allocator = &g_blob_pool_allocator[QRCODE_COLOR];
    option.workspace_allocator = &g_workspace_pool_allocator[QRCODE_COLOR];

    //use vulkan
    if (ncnn::get_gpu_count() > 0) {
        option.use_vulkan_compute = true;
    }
    colorModel[QRCODE_COLOR - COLOR_LIMIT].opt = option;
    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);
    //init param
    int ret = colorModel[QRCODE_COLOR - COLOR_LIMIT].load_param(mgr, "color/qrcode_color2.param");
    if (ret != 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "qrcode_color", "load_param failed");
        return false;
    }

    //init model
    ret = colorModel[QRCODE_COLOR - COLOR_LIMIT].load_model(mgr, "color/qrcode_color2.bin");
    if (ret != 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "qrcode_color", "load_model failed");
        return false;
    }

    return true;
}

JNIEXPORT jint
JNICALL
Java_car_Identify_models_QRCodeColorModel_Detect(JNIEnv *env, jobject clazz,
                                                 jobject bitmap) {
    const char *class_names[] = {
            "black", "blue", "green", "red", "yellow"
    };

    // ################## 预处理 ##################
    // 对输入图像进行预处理操作
    ncnn::Mat in = ncnn::Mat::from_android_bitmap_resize(env, bitmap, ncnn::Mat::PIXEL_RGB, 64,
                                                         64);
    in.substract_mean_normalize(
            0,
            new float[]{1 / 255.f, 1 / 255.f, 1 / 255.f}
    );

    // ################## 预处理 ##################

    // ################## 推理 ##################
    ncnn::Extractor extractor = colorModel[QRCODE_COLOR - COLOR_LIMIT].create_extractor();
    extractor.set_vulkan_compute(true);
    extractor.set_num_threads(1);
    extractor.input("input.1", in);
    ncnn::Mat result;
    extractor.extract("203", result);
    result = result.reshape(result.w);
    // ################## 推理 ##################

    // ################## softmax + argmax ##################
    float max = -999999.0;
    for (int i = 0; i < result.w; i++) {
        //np.max(scores)
        float val = result[i];
        if (val > max) {
            max = val;
        }
    }
    float sum_exp = 0;//np.sum(np.exp(scores))
    for (int i = 0; i < result.w; i++) {
        //scores -= np.max(scores)
        //np.exp(scores)
        result[i] = exp(result[i] - max);
        sum_exp += result[i];
    }
    for (int i = 0; i < result.w; i++) {
        //np.exp(scores) / np.sum(np.exp(scores))
        result[i] = result[i] / sum_exp;
        float val = result[i];
//        __android_log_print(ANDROID_LOG_DEBUG, "vehicle_color", "%f",val);
    }
    //找到最大的元素概率
    max = -999999.0;
    int max_index = -1;
    for (int i = 0; i < result.w; i++) {
        //np.exp(scores) / np.sum(np.exp(scores))
        float val = result[i];
        if (val > max) {
            max = val;
            max_index = i;
        }
        __android_log_print(ANDROID_LOG_DEBUG, "qrcode_color", "%f", val);
    }
    if (max_index == -1) {
        return -1;
    }
    // ################## softmax + argmax ##################
    __android_log_print(ANDROID_LOG_DEBUG, "qrcode_color", "预测结果：%s,概率为：%f",
                        class_names[max_index], max);

    return max_index;
}

}

extern "C"
JNIEXPORT void JNICALL
Java_car_car2024_Utils_Image_Rgb2hsv_adjustHue(JNIEnv *env, jobject thiz,
                                               jlong matAddr, jint hueAdjustment) {
    Mat &img = *(Mat *) matAddr;

    // 转换颜色空间为HSV
    cvtColor(img, img, COLOR_RGB2HSV);

    // 调整色相值
    img.forEach<Vec3b>([&](Vec3b &pixel, const int *position) -> void {
        // 调整色相值
        pixel[0] = (pixel[0] + hueAdjustment) % 180;
    });

    // 将颜色空间转换回RGB
    cvtColor(img, img, COLOR_HSV2RGB);
}
