//package im.zhy.data.dispose;
//
///**
// * @author zhy
// * @create_date 2019-04-27 11:13
// */
//
//import car.car2024.ActivityView.FirstActivity;
//import im.hdy.CarData;
//import im.zhy.data.arithmetic.FormulaCalculator;
//import im.zhy.data.arithmetic.MathUtils;
//import im.zhy.data.arithmetic.StrExtractUtils;
//
//import java.util.List;
//
///**
// * 从车二维码处理
// */
//public class QRCodeDeputy {
//
//    public static byte[] dispose(){
//        String s = CarData.deputy_QRCode;
//        s = StrExtractUtils.trim(s);
//
//        System.out.println(s.split("=")[1]);
//        int b = StrExtractUtils.getNumber(s.split("=")[1]).get(0);
//
//        return new byte[]{(byte) b};
//    }
//
//
//    public static void disposeDataToAGV(){
//        String s = CarData.deputy_QRCode;
//
//        List<Integer> integerList = StrExtractUtils.getNumber(s);
//
//        int even = MathUtils.getEvenOrOddNumberCount(integerList, false);
//
//        List<Character> characterList = StrExtractUtils.getCharNoRepeatChar(s, "[\\+\\-\\*]{1}");
//
//        for (Character character : characterList) {
//            System.out.println(character);
//        }
//
//        byte[] bytes = CarData.prepareDataMap.get(1);
//
//        String ss = "";
//
//        for (int i = 0; i < bytes.length; i++) {
//            ss += characterList.get(i) + "" + bytes[i];
//        }
//
//        ss = even + ss;
//
//        System.out.println("ss = " + ss);
//
//        byte result = (byte) (FormulaCalculator.getResult(ss) % 13);
//
//        System.out.println(result);
//
////        packageOneByteToAGV((byte) 0x02);
//    }
//
//    /**
//     * 封装一个字节的数据给从车
//     * @param by
//     */
//    private static void packageOneByteToAGV(byte by){
//        byte[] bytes = new byte[]{0x55, 0x02, (byte) 0xEF, by, 0x00, 0x00, 0x00, (byte) 0xBB};
//        bytes[6] = (byte) ((bytes[2] + bytes[3] + bytes[4] + bytes[5]) % 256);
//        FirstActivity.Connect_Transport.sendData(bytes);
//    }
//
//    public static void main(String[] args) {
//        CarData.deputy_QRCode = "fds324+&&*^^%*4545j-";
//        CarData.prepareDataMap.put(1, new byte[]{2, 2, 2});
//        disposeDataToAGV();
//
//    }
//}
