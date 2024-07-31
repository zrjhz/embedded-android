package im.zhy.util;

import im.zhy.param.ColorName;
import im.zhy.param.ShapeName;
import im.zhy.param.TrafficLightName;

/**
 * @author zhy
 * @create_date 2019-04-27 10:13
 */

@SuppressWarnings("all")
public class GetVarName {

    public static String getColorName(int colorInt){
        switch (colorInt){
            case 0:
                return ColorName.RED;
            case 1:
                return ColorName.GREEN;
            case 2:
                return ColorName.BLUE;
            case 3:
                return ColorName.YELLOW;
            case 4:
                return ColorName.PURPLE;
            case 5:
                return ColorName.CYAN;
            case 6:
                return ColorName.BLACK;
            case 7:
                return ColorName.WHITE;
            default:
                return ColorName.RED;
        }
    }

    public static int getColorInt(String color) {
        switch (color) {
            case ColorName.RED:
                return 0;
            case ColorName.GREEN:
                return 1;
            case ColorName.BLUE:
                return 2;
            case ColorName.YELLOW:
                return 3;
            case ColorName.PURPLE:
                return 4;
            case ColorName.CYAN:
                return 5;
            case ColorName.BLACK:
                return 6;
            case ColorName.WHITE:
                return 7;
            default:
               return 0;
        }
    }

    public static int getShapeInt(String shapeName) {
        switch (shapeName){
            case ShapeName.SQU:
                return 0;
            case ShapeName.CIR:
                return 1;
            case ShapeName.TRI:
                return 2;
            case ShapeName.RHOM:
                return 3;
            case ShapeName.TRAP:
                return 4;
            case ShapeName.PIE:
                return 5;
            case ShapeName.TARGET:
                return 6;
            case ShapeName.BAR:
                return 7;
            case ShapeName.STAR:
                return 8;
            default:
                return 0;
        }
    }


    // 传入英文.获得中文颜色
    public static String getChineseColorName(String color) {
        if (color.equals("RED")) {
            return "红色";
        } else if (color.equals("BLACK")) {
            return "黑色";
        } else if (color.equals("WHITE")) {
            return "白色";
        } else if (color.equals("YELLOW")) {
            return "黄色";
        } else if (color.equals("BLUE")) {
            return "蓝色";
        } else if (color.equals("GREEN")) {
            return "绿色";
        } else if (color.equals("CYAN")) {
            return "青色";
        } else if (color.equals("PURPLE")) {
            return "紫色";
        } else if (color.equals("ORANGE")) {
            return "橘色";
        }
        return "";
    }


    public static int getShapeIntByChinese(String shapeName){
       switch (shapeName){
           case "矩形":
               return getShapeInt(ShapeName.SQU);
           case "圆形":
               return getShapeInt(ShapeName.CIR);
           case "三角形":
               return getShapeInt(ShapeName.TRI);
           case "菱形":
               return getShapeInt(ShapeName.RHOM);
           case "五角星":
               return getShapeInt(ShapeName.STAR);
           default:
               return getShapeInt(ShapeName.SQU);
       }

//       if ( "矩形".equals(shapeName)){
//           return getShapeInt(ShapeName.SQU);
//       }else if ("圆形".equals(shapeName)){
//           return getShapeInt(ShapeName.CIR);
//       }else if ("三角形".equals(shapeName)){
//           return getShapeInt(ShapeName.TRI);
//       }else if ("菱形".equals(shapeName)){
//           return getShapeInt(ShapeName.RHOM);
//       }else if ("五角星".equals(shapeName)){
//           return getShapeInt(ShapeName.STAR);
//       }else {
//           return getShapeInt(ShapeName.SQU);
//       }

    }


    public static int getColorIntByChinese(String colorName){
        switch (colorName){
            case "红色":
                return getColorInt(ColorName.RED);
            case "绿色":
                return getColorInt(ColorName.GREEN);
            case "蓝色":
                return getColorInt(ColorName.BLUE);
            case "黄色":
                return getColorInt(ColorName.YELLOW);
            case "品红色":
                return getColorInt(ColorName.PURPLE);
            case "品色":
                return getColorInt(ColorName.PURPLE);
            case "青色":
                return getColorInt(ColorName.CYAN);
            case "黑色":
                return getColorInt(ColorName.BLACK);
            case "白色":
                return getColorInt(ColorName.WHITE);
            default:
                return getColorInt(ColorName.RED);
        }
    }




    // 传入英文,获得中文形状
    public static String getChineseShapeName(String shape) {
        if (shape.equals("TRI")) {
            return "三角形";
        } else if (shape.equals("RHOM")) {
            return "菱形";
        } else if (shape.equals("STAR")) {
            return "五角星";
        } else if (shape.equals("CIR")) {
            return "圆形";
        } else if (shape.equals("SQU")) {
            return "矩形";
        }
        return "";
    }



    /**
     * 将交通灯的数据结果转为 对应的文字  0 红 1 黄 2 绿
     * @return
     */
    public static String trafficLightToString(int i){
        String result;
        switch (i){
            case TrafficLightName.RED:
                result = "红色";
                break;
            case TrafficLightName.GREEN:
                result = "绿色";
                break;
            case TrafficLightName.YELLOW:
                result = "黄色";
                break;
            default:
                result = "绿色";
                break;
        }
        return result;
    }

}
