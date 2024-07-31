package im.zhy.util;

import car.car2024.Utils.Image.ShapeUtils4;

import org.opencv.core.MatOfPoint2f;

import java.util.Comparator;


/**
 * 根据图形在图片上的最小x坐标进行排序
 */
public class MatofPoint2fComparator implements Comparator<MatOfPoint2f> {

    private int width;
    private int height;

    public MatofPoint2fComparator(int width,int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public int compare(MatOfPoint2f mat1, MatOfPoint2f mat2) {
        int mat1_x = ShapeUtils4.shapeScope(mat1.toArray(), width, height).get("min_x")[0];
        int mat2_x = ShapeUtils4.shapeScope(mat2.toArray(), width, height).get("min_x")[0];
        return (mat1_x - mat2_x);
    }
}
