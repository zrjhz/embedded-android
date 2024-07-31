package im.fyhz.utils;

import java.util.regex.Pattern;

public class algorithm {

    /**
     * 波雷费密码算法
     * @param raw
     * @return result of the playfairCipher
     */
    public static byte[] playfairCipher(String raw) {
        if (raw == null || raw.length() == 0) {
            return null;
        }
        // step 1
        raw = raw.replaceAll("[<>0-9]", "");
        String []disposing = raw.split("\\|");
        String plaintext = disposing[0].toUpperCase();
        String ciphertext = disposing[1].toUpperCase();

        // step 2
        ciphertext = removeRepeatLetter(ciphertext);

        String [][]matrix = addRestWords(ciphertext);

        // step 3
        plaintext = text3(plaintext);

        String result = solve3(plaintext, matrix);

        // step 4
        result = getThree(result);
        System.out.println("  playfairCipher:     " + result);

        // step 5
        return backByte(result);
    }

    private static byte[] backByte(String result) {
        byte[] sb = result.getBytes();
        for (byte x : sb) {
            System.out.println(x);
        }
        return sb;
    }

    /**
     * 转16进制字符串
     * @param byteArray
     * @return
     */
    public static String toHexString(byte[] byteArray) {
        String str = null;
        if (byteArray != null && byteArray.length > 0) {
            StringBuffer stringBuffer = new StringBuffer(byteArray.length);
            for (byte byteChar : byteArray) {
                stringBuffer.append(String.format("%02X", byteChar));
            }
            str = stringBuffer.toString();
        }
        return str;
    }
    /**
     * 取密文前３个字符与后3个字符（大写字母）
     * @param result
     * @return
     */
    private static String getThree(String result) {
        return result.substring(0,3) + result.substring(result.length()-3);
    }

    /**
     * 在每组中，找出两个字母在矩阵中的地方。
     * 若两个字母不同行也不同列，在矩阵中找出另外两个字母，使这四个字母成为一个长方形的四个角。
     * 若两个字母同行，取这两个字母右方的字母（若字母在最右方则取最左方的字母）。
     * 若两个字母同列，取这两个字母下方的字母（若字母在最下方则取最上方的字母）。
     * @param plaintext
     * @param matrix
     * @return
     */
    private static String solve3(String plaintext, String[][] matrix) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < plaintext.length(); i+=2) {
            int x1 = 0;
            int y1 = 0;
            int x2 = 0;
            int y2 = 0;
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (matrix[j][k].charAt(0) == plaintext.charAt(i)) {
                        x1 = j;
                        y1 = k;
                    }
                    if (matrix[j][k].charAt(0) == plaintext.charAt(i + 1)) {
                        x2 = j;
                        y2 = k;
                    }
                }
            }

            if (x1 != x2 && y1 != y2) {
                sb.append(matrix[x1][y2]);
                sb.append(matrix[x2][y1]);
            } else if (y1 == y2) {
                if (x1 != 4) {
                    sb.append(matrix[x1 + 1][y1]);
                } else {
                    sb.append(matrix[0][y1]);
                }
                if (x2 != 4) {
                    sb.append(matrix[x2 + 1][y2]);
                } else {
                    sb.append(matrix[0][y2]);
                }

            } else {
                if (y1 != 4) {
                    sb.append(matrix[x1][y1+1]);
                } else {
                    sb.append(matrix[x1][0]);
                }
                if (y2 != 4) {
                    sb.append(matrix[x2][y2+1]);
                } else {
                    sb.append(matrix[x2][0]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将要加密的讯息分成两个一组。若组内的字母相同，将X加到该组的第一个字母后，重新分组。若剩下一个字，也加入X字。
     * @param plaintext
     * @return
     */
    private static String text3(String plaintext) {
        for (int i = 0; i < plaintext.length()/2*2; i+=2) {
            if (plaintext.charAt(i) == plaintext.charAt(i+1)) {
                StringBuffer sb = new StringBuffer(plaintext);
                sb.insert(i + 1,"X");
                plaintext = sb.toString();
                text3(plaintext);
            }
        }
        if (plaintext.length() % 2 != 0) {
            StringBuffer sb = new StringBuffer(plaintext);
            sb.insert(sb.length(),"X");
            plaintext = sb.toString();
        }
        return plaintext;
    }

    /**
     * dispose the ciphertext to a String[][] followed by the unrepeat words in the 25 words
     * @param ciphertext
     * @return the disposed matrix
     */
    private static String[][] addRestWords(String ciphertext) {
        String [][]matrix = new String[5][5];
        String words = ciphertext + "ABCDEFGHIJKLMNOPRSTUVWXYZ";
        String result = removeRepeatLetter(words);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = String.valueOf(result.charAt(i*5 + j));
            }
        }
        return matrix;
    }

    /**
     * remove the repeat letters in the String
     * @param raw
     * @return disposed result
     */
    public static String removeRepeatLetter(String raw) {
        String result = "";
        for (int i = 0; i < raw.length(); i++) {
            String word = String.valueOf(raw.charAt(i));
            String pattern = ".*" + word + ".*";
            boolean isSame = Pattern.matches(pattern, result);
            if (!isSame) {
                result += word;
            }
        }
        return result;
    }
}
