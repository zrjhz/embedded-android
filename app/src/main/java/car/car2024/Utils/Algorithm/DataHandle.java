package car.car2024.Utils.Algorithm;

public class DataHandle {
    public static byte[] getResult(String code) {
        int[][] ints = new int[4][4];
        int length = code.length();
        int[] comp = null;
        if (length < 16) {
            comp = new int[16 - length];
        }
        if (comp != null) {
            for (int i = 0; i < comp.length; i++) {
                comp[i] = i + 1;
            }
        }
        char[] charArray = code.toCharArray();
        for (int i = 0, k = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[i].length; j++) {
                if (4 * i + j < charArray.length)
                    ints[j][i] = charArray[4 * i + j];
                else
                    ints[j][i] = comp[k++];
            }
        }
        byte[] firstAndTwo = getIndex(ints);
        byte[][] bytes = new byte[4][4];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = left4bitAllRow(ints[i]);
        }
        byte[] tData = new byte[8];
        int count = 0;
        for (int i = 0; i < 4; i += 2) {
            for (int j = 0; j < bytes[0].length; j++) {
                tData[count++] = (byte) (bytes[i][j] | bytes[i + 1][j]);
            }
        }
        byte[] sData = new byte[4];
        sData[0] = (byte) ((tData[0] + tData[1]) & 0x0FF);
        sData[1] = (byte) ((tData[2] - tData[3]) & 0x0FF);
        sData[2] = (byte) ((tData[4] & tData[5]) & 0x0FF);
        sData[3] = (byte) ((tData[6] ^ tData[7]) & 0x0FF);
        byte[] data = new byte[6];
        for (int i = 0, j = 0, k = 0; i < data.length; i++) {
            if (j < 2)
                data[j] = firstAndTwo[j++];
            if (k < 4) {
                data[k + 2] = sData[k++];
            }
        }
        return data;
    }

    private static byte[] left4bitAllRow(int[] ints) {
        StringBuilder s = new StringBuilder();
        for (int anInt : ints) {
            String string = Integer.toString(anInt, 16);
            if (string.length() < 2) {
                s.append("0").append(string);
            } else s.append(string);
        }
        int in = Integer.parseInt(s.toString(), 16);
        int left = in << 4;
        int i = left | (in >> 28);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i >> 24);
        bytes[1] = (byte) ((i >> 16) & 0xFF);
        bytes[2] = (byte) ((i >> 8) & 0xFF);
        bytes[3] = (byte) (i & 0xFF);
        return bytes;
    }

    private static byte[] getIndex(int[][] ints) {
        int maximum = 0;
        int min = 0;
        int index = 0;
        int indexI = 0;
        for (int i = 0; i < ints.length; i++) {
            maximum = ints[i][0];
            boolean flag = false;
            for (int j = 0; j < ints[i].length - 1; j++) {
                if (maximum < ints[i][j + 1]) {
                    maximum = ints[i][j];
                    index = j;
                    indexI = i;
                    min = maximum;
                }
            }
            for (int j = 0; j < ints.length - 1; j++) {
                if (min < ints[j + 1][index]) {
                    flag = true;
                } else {
                    min = ints[j + 1][index];
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
        byte[] bytes = new byte[2];
        bytes[0] = (byte) maximum;
        String s = indexI + String.valueOf(index);
        int i = Integer.parseInt(s, 16);
        bytes[1] = (byte) i;
        return bytes;
    }
}
