package car.car2024.Utils.Algorithm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecuritySHA1Utils {
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
//            System.out.println(e);
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args) throws Exception {
        String str = "amigoxiexiexingxing";
        System.out.println("原始：" + str);
        System.out.println("SHA后：" + shaEncode(str));
    }

}