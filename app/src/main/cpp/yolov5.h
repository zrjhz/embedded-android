//
// Created by 无敌の星仔 on 2023/5/15.
//

#ifndef CAR2019_2_YOLOV5_H
#define CAR2019_2_YOLOV5_H

#include <android/asset_manager_jni.h>
#include <android/bitmap.h>
#include <android/log.h>

#include <string>
#include <vector>

// ncnn
#include "layer.h"
#include "net.h"
#include "benchmark.h"
#include <jni.h>
#include "benchmark.h"
//#include "opencv_m/sdk/native/jni/include/opencv2/core/core.hpp"
//#include "opencv_m/sdk/native/jni/include/opencv2/highgui/highgui.hpp"
//#include "opencv_m/sdk/native/jni/include/opencv2/imgproc.hpp"
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc.hpp>

using namespace std;
using namespace cv;


class YoloV5Focus : public ncnn::Layer {
public:
    YoloV5Focus() {
        one_blob_only = true;
    }

    virtual int
    forward(const ncnn::Mat &bottom_blob, ncnn::Mat &top_blob, const ncnn::Option &opt) const {
        int w = bottom_blob.w;
        int h = bottom_blob.h;
        int channels = bottom_blob.c;

        int outw = w / 2;
        int outh = h / 2;
        int outc = channels * 4;

        top_blob.create(outw, outh, outc, 4u, 1, opt.blob_allocator);
        if (top_blob.empty())
            return -100;

#pragma omp parallel for num_threads(opt.num_threads)
        for (int p = 0; p < outc; p++) {
            const float *ptr = bottom_blob.channel(p % channels).row((p / channels) % 2) +
                               ((p / channels) / 2);
            float *outptr = top_blob.channel(p);

            for (int i = 0; i < outh; i++) {
                for (int j = 0; j < outw; j++) {
                    *outptr = *ptr;

                    outptr += 1;
                    ptr += 2;
                }

                ptr += w;
            }
        }

        return 0;
    }
};

DEFINE_LAYER_CREATOR(YoloV5Focus)

struct Object {
    float x;
    float y;
    float w;
    float h;
    int label;
    float prob;
};
struct Object2 {
    float x;
    float y;
    float w;
    float h;
    string label;
    string color;
    float p1x;
    float p1y;
    float p2x;
    float p2y;
    float p3x;
    float p3y;
    float p4x;
    float p4y;
    float prob;
};

static inline float intersection_area(const Object &a, const Object &b) {
    if (a.x > b.x + b.w || a.x + a.w < b.x || a.y > b.y + b.h || a.y + a.h < b.y) {
        // no intersection
        return 0.f;
    }

    float inter_width = std::min(a.x + a.w, b.x + b.w) - std::max(a.x, b.x);
    float inter_height = std::min(a.y + a.h, b.y + b.h) - std::max(a.y, b.y);

    return inter_width * inter_height;
}

static inline float intersection_area2(const Object2 &a, const Object2 &b) {
    if (a.x > b.x + b.w || a.x + a.w < b.x || a.y > b.y + b.h || a.y + a.h < b.y) {
        // no intersection
        return 0.f;
    }

    float inter_width = std::min(a.x + a.w, b.x + b.w) - std::max(a.x, b.x);
    float inter_height = std::min(a.y + a.h, b.y + b.h) - std::max(a.y, b.y);

    return inter_width * inter_height;
}


static void qsort_descent_inplace(std::vector<Object> &faceobjects, int left, int right) {
    int i = left;
    int j = right;
    float p = faceobjects[(left + right) / 2].prob;

    while (i <= j) {
        while (faceobjects[i].prob > p)
            i++;

        while (faceobjects[j].prob < p)
            j--;

        if (i <= j) {
            // swap
            std::swap(faceobjects[i], faceobjects[j]);

            i++;
            j--;
        }
    }

#pragma omp parallel sections
    {
#pragma omp section
        {
            if (left < j) qsort_descent_inplace(faceobjects, left, j);
        }
#pragma omp section
        {
            if (i < right) qsort_descent_inplace(faceobjects, i, right);
        }
    }
}

static void qsort_descent_inplace2(std::vector<Object2> &faceobjects, int left, int right) {
    int i = left;
    int j = right;
    float p = faceobjects[(left + right) / 2].prob;

    while (i <= j) {
        while (faceobjects[i].prob > p)
            i++;

        while (faceobjects[j].prob < p)
            j--;

        if (i <= j) {
            // swap
            std::swap(faceobjects[i], faceobjects[j]);

            i++;
            j--;
        }
    }

#pragma omp parallel sections
    {
#pragma omp section
        {
            if (left < j) qsort_descent_inplace2(faceobjects, left, j);
        }
#pragma omp section
        {
            if (i < right) qsort_descent_inplace2(faceobjects, i, right);
        }
    }
}


static void qsort_descent_inplace(std::vector<Object> &faceobjects) {
    if (faceobjects.empty())
        return;

    qsort_descent_inplace(faceobjects, 0, faceobjects.size() - 1);
}

static void qsort_descent_inplace2(std::vector<Object2> &faceobjects) {
    if (faceobjects.empty())
        return;

    qsort_descent_inplace2(faceobjects, 0, faceobjects.size() - 1);
}

static void nms_sorted_bboxes(const std::vector<Object> &faceobjects, std::vector<int> &picked,
                              float nms_threshold) {
    picked.clear();

    const int n = faceobjects.size();

    std::vector<float> areas(n);
    for (int i = 0; i < n; i++) {
        areas[i] = faceobjects[i].w * faceobjects[i].h;
    }

    for (int i = 0; i < n; i++) {
        const Object &a = faceobjects[i];

        int keep = 1;
        for (int j = 0; j < (int) picked.size(); j++) {
            const Object &b = faceobjects[picked[j]];

            // intersection over union
            float inter_area = intersection_area(a, b);
            float union_area = areas[i] + areas[picked[j]] - inter_area;
            // float IoU = inter_area / union_area
            if (inter_area / union_area > nms_threshold)
                keep = 0;
        }

        if (keep)
            picked.push_back(i);
    }
}

static void nms_sorted_bboxes2(const std::vector<Object2> &faceobjects, std::vector<int> &picked,
                               float nms_threshold) {
    picked.clear();

    const int n = faceobjects.size();

    std::vector<float> areas(n);
    for (int i = 0; i < n; i++) {
        areas[i] = faceobjects[i].w * faceobjects[i].h;
    }

    for (int i = 0; i < n; i++) {
        const Object2 &a = faceobjects[i];

        int keep = 1;
        for (int j = 0; j < (int) picked.size(); j++) {
            const Object2 &b = faceobjects[picked[j]];

            // intersection over union
            float inter_area = intersection_area2(a, b);
            float union_area = areas[i] + areas[picked[j]] - inter_area;
            // float IoU = inter_area / union_area
            if (inter_area / union_area > nms_threshold)
                keep = 0;
        }

        if (keep)
            picked.push_back(i);
    }
}


static inline float sigmoid(float x) {
    return static_cast<float>(1.f / (1.f + exp(-x)));
}

static void generate_proposals(const ncnn::Mat &anchors, int stride, const ncnn::Mat &in_pad,
                               const ncnn::Mat &feat_blob, float prob_threshold,
                               std::vector<Object> &objects) {
    const int num_grid = feat_blob.h;

    int num_grid_x;
    int num_grid_y;
    if (in_pad.w > in_pad.h) {
        num_grid_x = in_pad.w / stride;
        num_grid_y = num_grid / num_grid_x;
    } else {
        num_grid_y = in_pad.h / stride;
        num_grid_x = num_grid / num_grid_y;
    }

    const int num_class = feat_blob.w - 5;

    const int num_anchors = anchors.w / 2;

    for (int q = 0; q < num_anchors; q++) {
        const float anchor_w = anchors[q * 2];
        const float anchor_h = anchors[q * 2 + 1];

        const ncnn::Mat feat = feat_blob.channel(q);

        for (int i = 0; i < num_grid_y; i++) {
            for (int j = 0; j < num_grid_x; j++) {
                const float *featptr = feat.row(i * num_grid_x + j);

                // find class index with max class score
                int class_index = 0;
                float class_score = -FLT_MAX;
                for (int k = 0; k < num_class; k++) {
                    float score = featptr[5 + k];
                    if (score > class_score) {
                        class_index = k;
                        class_score = score;
                    }
                }

                float box_score = featptr[4];

                float confidence = sigmoid(box_score) * sigmoid(class_score);

                if (confidence >= prob_threshold) {
                    // yolov5/models/yolo.py Detect forward
                    // y = x[i].sigmoid()
                    // y[..., 0:2] = (y[..., 0:2] * 2. - 0.5 + self.grid[i].to(x[i].device)) * self.stride[i]  # xy
                    // y[..., 2:4] = (y[..., 2:4] * 2) ** 2 * self.anchor_grid[i]  # wh

                    float dx = sigmoid(featptr[0]);
                    float dy = sigmoid(featptr[1]);
                    float dw = sigmoid(featptr[2]);
                    float dh = sigmoid(featptr[3]);

                    float pb_cx = (dx * 2.f - 0.5f + j) * stride;
                    float pb_cy = (dy * 2.f - 0.5f + i) * stride;

                    float pb_w = pow(dw * 2.f, 2) * anchor_w;
                    float pb_h = pow(dh * 2.f, 2) * anchor_h;

                    float x0 = pb_cx - pb_w * 0.5f;
                    float y0 = pb_cy - pb_h * 0.5f;
                    float x1 = pb_cx + pb_w * 0.5f;
                    float y1 = pb_cy + pb_h * 0.5f;

                    Object obj;
                    obj.x = x0;
                    obj.y = y0;
                    obj.w = x1 - x0;
                    obj.h = y1 - y0;
                    obj.label = class_index;
                    obj.prob = confidence;

                    objects.push_back(obj);
                }
            }
        }
    }
}

static void generate_proposals2(const ncnn::Mat &anchors, int stride, const ncnn::Mat &in_pad,
                                const ncnn::Mat &feat_blob, float prob_threshold,
                                std::vector<Object2> &objects) {

    /***************************************************************
      *  @brief     函数作用：未知
      *  @param     参数：
        const ncnn::Mat& anchors：即自定义设置的锚框,
        int stride：步长如8、16、32
        const ncnn::Mat& in_pad：输入的mat
        const ncnn::Mat& feat_blob：输出的mat
        float prob_threshold：未知
        std::vector<Object>& objects：处理结果存放的vector
      *  @note      备注
      *  @Sample usage:     函数的使用方法
     **************************************************************/

    const int num_grid = feat_blob.h;  //获取输出mat的h值，此处为3840=48*80,此处考虑的是stride为8的值

    int num_grid_x;
    int num_grid_y;

//    按照stride缩放，由宽和高相对大小对决定对基于w还是h缩放，最后将缩放后的w和h赋值给x和y
//    这也是为什么输入一定会处理为32的倍数的原因
    if (in_pad.w > in_pad.h) {
        num_grid_x = in_pad.w / stride;
        num_grid_y = num_grid / num_grid_x;
    } else {
        num_grid_y = in_pad.h /
                     stride;  // 这里是w更小，输入的w为384，h为640，故num_grid_y=640/8=80，num_grid_x=48*80/80=48
        num_grid_x = num_grid / num_grid_y;
    }
//    cout<<"num_grid_x："<<num_grid_x<<endl;
//    cout<<"num_grid_y："<<num_grid_y<<endl;
    const int num_class = feat_blob.w - 13;  // 这里w等于14，减去前面四个xywh，以及conf还有四个点的8个坐标一共13个，剩下的就是类别数
    const int num_anchors =
            anchors.w / 2;  // anchors的数量等于anchors的w来除以2，这里的anchors的w为6，则num_anchors为3
//  torch.Size([1, 3, 80, 48, 14]),stride为8时的结果，经过conv之后的结果
    for (int q = 0; q < num_anchors; q++)  // 遍历3，即torch.Size([1, 3, 80, 48, 14])中的第2维度
    {
        const float anchor_w = anchors[q * 2];
        const float anchor_h = anchors[q * 2 + 1];

        const ncnn::Mat feat = feat_blob.channel(q);  // 获取某个stride的三个channel之一

        for (int i = 0; i < num_grid_y; i++)  // 遍历80，即torch.Size([1, 3, 80, 48, 14])中的第三维度
        {
            for (int j = 0; j < num_grid_x; j++) // 遍历48，即torch.Size([1, 3, 80, 48, 14])中的第四维度
            {
                const float *featptr = feat.row(i * num_grid_x +
                                                j); // 对torch.Size([1, 3, 80, 48, 14])中的第四维度中的48遍历获取其值，其值应该是一个数组，包含14个数
                float box_confidence = sigmoid(
                        featptr[4]);  // 将这个数组中的第四也就是实际第五个的conf进行sigmoid，变成0-1，赋值给锚框的置信度
                if (box_confidence >= prob_threshold)  // 判断置信度是否大于预设值，只有大于的才会进入到结果中
                {
                    // find class index with max class score
                    int class_index = 0;
                    float class_score = -FLT_MAX;
                    for (int k = 0; k < num_class; k++) {
                        float score = featptr[5 + 8 + k];
                        if (score > class_score) {
                            class_index = k;
                            class_score = score;
                        }
                    }
                    float confidence =
                            box_confidence * sigmoid(class_score);  // 整体置信度阈值，也就是锚框置信度*类别置信度
                    if (confidence >= prob_threshold) {

                        // 这里是只对xywh做了sigmoid，其中类别conf在上面已经做过了，即：float confidence = box_confidence * sigmoid(class_score);
                        float dx = sigmoid(featptr[0]);
                        float dy = sigmoid(featptr[1]);
                        float dw = sigmoid(featptr[2]);
                        float dh = sigmoid(featptr[3]);

                        float p1x = featptr[5];
                        float p1y = featptr[6];
                        float p2x = featptr[7];
                        float p2y = featptr[8];
                        float p3x = featptr[9];
                        float p3y = featptr[10];
                        float p4x = featptr[11];
                        float p4y = featptr[12];

                        float pb_cx = (dx * 2.f - 0.5f + j) * stride;
                        float pb_cy = (dy * 2.f - 0.5f + i) * stride;


                        float pb_w = pow(dw * 2.f, 2) * anchor_w;
                        float pb_h = pow(dh * 2.f, 2) * anchor_h;
                        // # landmark的进一步处理
                        p1x = p1x * anchor_w + j * stride;
                        p1y = p1y * anchor_h + i * stride;
                        p2x = p2x * anchor_w + j * stride;
                        p2y = p2y * anchor_h + i * stride;
                        p3x = p3x * anchor_w + j * stride;
                        p3y = p3y * anchor_h + i * stride;
                        p4x = p4x * anchor_w + j * stride;
                        p4y = p4y * anchor_h + i * stride;

                        float x0 = pb_cx - pb_w * 0.5f;
                        float y0 = pb_cy - pb_h * 0.5f;
                        float x1 = pb_cx + pb_w * 0.5f;
                        float y1 = pb_cy + pb_h * 0.5f;

                        Object2 obj;
                        obj.x = x0;
                        obj.y = y0;
                        obj.w = x1 - x0;
                        obj.h = y1 - y0;
                        obj.label = "";
                        obj.color = "";
                        obj.prob = confidence;
                        obj.p1x = p1x;
                        obj.p1y = p1y;
                        obj.p2x = p2x;
                        obj.p2y = p2y;
                        obj.p3x = p3x;
                        obj.p3y = p3y;
                        obj.p4x = p4x;
                        obj.p4y = p4y;

                        objects.push_back(obj);
                    }
                }
            }
        }
    }
}

#endif //CAR2019_2_YOLOV5_H
