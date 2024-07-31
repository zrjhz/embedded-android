package im.zhy.util;

import im.hdy.CarData;

public class ShapeColorUtils {

    private static int[][] shapeColor =  new int[9][8];

    public static void setShapeColor(int[][] shapeColor) {
        ShapeColorUtils.shapeColor = shapeColor;
    }

    /**
     * @TODO
     * 要获取什么数据？
     * @param index
     */
//    public static void selectData(int index){
//
//        if (index == 1 || index == 2){
//
//            System.out.println("index::" + index);
//
//            int[][] ints = CarData.shapeColorMap.get(index);
//
//            if (ints == null){
//
//                System.out.println("要获取的数据为空");
//
//                index = index == 1 ? 2 : 1;
//
//                ints = CarData.shapeColorMap.get(index);
//
//                if (ints != null){
//                    shapeColor = ints;
//                }else {
//                    shapeColor = CarData.shapeColor;
//                }
//
//            }else {
//                shapeColor = ints;
//            }
//
//        }else if (index == 3){
//
//            try {
//
//                shapeColor = new int[9][8];
//
//                int[][] ints1 = CarData.shapeColorMap.get(1);
//                int[][] ints2 = CarData.shapeColorMap.get(2);
//
//                if (ints1 != null && ints2 != null){
//
//                    for (int i = 0; i < shapeColor.length; i++) {
//                        for (int j = 0; j < shapeColor[i].length; j++) {
//
//                            shapeColor[i][j] = ints1[i][j] + ints2[i][j];
//
//                        }
//                    }
//
//                }else if (ints1 != null){
//                    shapeColor = ints1;
//                }else if (ints2 != null){
//                    shapeColor = ints2;
//                }else {
//                    shapeColor = CarData.shapeColor;
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }


    /**
     * 根据颜色编号计算这个颜色的总个数
     * @param colorInt
     * @return
     */
    public static int colorNumber(int colorInt){
        int number = 0;

        for (int i = 0; i < shapeColor.length; i++) {
            number += shapeColor[i][colorInt];
        }

        return number;
    }

    /**
     * 根据图形名称返回对应的图形个数
     * @param shapeName  图形名称
     * @return
     */
    public static int shapeNumberByName(String shapeName){
        int id = GetVarName.getShapeInt(shapeName);
        return shapeNumber(id);
    }

    /**
     * 根据形状编号计算这个形状的总个数
     * @param shapeInt
     * @return
     */
    public static int shapeNumber(int shapeInt){
        int number = 0;

        for (int i = 0; i < shapeColor[shapeInt].length; i++) {
            number += shapeColor[shapeInt][i];
        }

        byte[] bytes = new byte[]{(byte) number};

        return number;
    }

    /**
     * 计算一共有几种类型的图形
     * @return
     */
    public static int shapeTypeNumber(){
        int number = 0;
        for (int i = 0; i < shapeColor.length; i++) {
            int gs = shapeNumber(i);
            if (gs > 0){
                number++;
            }
        }
        return number;
    }

    /**
     * 计算一共有几种类型的颜色
     * @return
     */
    public static int colorTypeNumber(){
        int number = 0;
        for (int i = 0; i < shapeColor[0].length; i++) {
            int gs = colorNumber(i);
            if(gs > 0){
                number++;
            }
        }
        return number;
    }

//    public static void main(String[] args) {
//        int[][] shapeColor1 = new int[9][8];
//
//        shapeColor1[0][2] = 1;
//        shapeColor1[1][1] = 1;
//        shapeColor1[2][3] = 1;
//        shapeColor1[2][4] = 3;
//        shapeColor1[3][0] = 1;
//        shapeColor1[8][0] = 1;
//
//        CarData.shapeColorMap.put(1, shapeColor1);
//
//        int[][] shapeColor2 = new int[9][8];
//
//        shapeColor2[0][0] = 2;
//        shapeColor2[0][6] = 1;
//        shapeColor2[1][1] = 1;
//        shapeColor2[1][2] = 1;
//        shapeColor2[1][6] = 1;
//        shapeColor2[2][3] = 1;
//        shapeColor2[3][0] = 1;
//        shapeColor2[8][1] = 1;
//
//        CarData.shapeColorMap.put(2, shapeColor2);
//
//        selectData(3);
//
//        System.out.println("两个TFT 三角形的总和：" + shapeNumber(2));
//
//        System.out.println("两个TFT 矩形的总和：" + shapeNumber(0));
//
//        System.out.println("两个TFT 红色总和：" + colorNumber(0));
//
//        System.out.println("第一个TFT的 颜色类型总数：" + colorTypeNumber());
//
//        System.out.println("第一个TFT的 图形类型总数：" + shapeTypeNumber());
//
//
//        System.out.println();
//
//        selectData(1);
//
//        System.out.println("第一个TFT 矩形的个数：" + shapeNumber(0));
//
//        System.out.println("第一个TFT 红色的个数：" + colorNumber(0));
//
//        System.out.println("第一个TFT的 颜色类型总数：" + colorTypeNumber());
//
//        System.out.println("第一个TFT的 图形类型总数：" + shapeTypeNumber());
//
//        System.out.println();
//
//        setShapeColor(new int[9][8]);
//
//        selectData(2);
//
//        System.out.println("第二个TFT 矩形的个数：" + shapeNumber(0));
//
//        System.out.println("第二个TFT 红色的个数：" + colorNumber(0));
//
//        System.out.println("第二个TFT的 颜色类型总数：" + colorTypeNumber());
//
//        System.out.println("第二个TFT的 图形类型总数：" + shapeTypeNumber());
//
//    }
}