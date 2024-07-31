package im.fyhz.utils.May8;

public class May8Base64 {
    public static String Base64(String raw) {
        char[] BASE64CODE = "Qj6yqBHlbi7zrVGUdI019XDTckst8CFY2mueZ+oRfnv3/LAEgSwM4KPWhpx5NJOa".toCharArray();

        // to binary
        StringBuffer toBinary = new StringBuffer();
        for (int i = 0; i < raw.length(); i++) {
            String temp = Integer.toBinaryString(raw.charAt(i)) + "";
            // to 8 bit
            if (temp.length() < 8) {
                int length = temp.length();
                for (int j = 0; j < 8 - length; j++) {
                    temp = "0" + temp;
                }
                System.out.println(temp.length());
            }
            toBinary.append(temp);
        }
        System.out.println(toBinary.toString());
        String step1str1 = toBinary.toString();

        // 6bit group
        String group[];
        // set the group length
        if (step1str1.length() % 6 != 0) {
            // to 6's times
            int length = step1str1.length()%6;
            for (int i = 0; i < 6-length; i++) {
                step1str1 += "0";
            }
            group = new String[step1str1.length()/6];
            group = to6bit(step1str1, group);
        } else {
            group = new String[step1str1.length() / 6];
            group = to6bit(step1str1, group);
        }

        // add 0 to group
        for (int i = 0; i < group.length; i++) {
            group[i] = "00" + group[i];
        }
        System.out.println("add00-----");
        for (String i :
                group) {
            System.out.println(i);
        }

        // toBASE64
        System.out.println("toBASE64-----");
        int[] base64 = new int[group.length];
        for (int i = 0; i < base64.length; i++) {
            base64[i] = binaryTo10(group[i]);
            System.out.println(base64[i]);
        }

        // to 4's times
        System.out.println("to4'stimes-----");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < base64.length; i++) {
            stringBuffer.append(BASE64CODE[base64[i]]);
        }


        if (stringBuffer.length() % 4 != 0) {
            int n = stringBuffer.length()%4;
            for (int i = 0; i < 4 - n; i++) {
                stringBuffer.append("=");
            }
        }
        String str4times = stringBuffer.toString();
        String result = "";
        int[] number = new int[str4times.length()];
        // string to ascii
        for (int i = 0; i < str4times.length(); i++) {
            number[i] = Integer.valueOf(str4times.charAt(i));
            System.out.println(Integer.valueOf(str4times.charAt(i)));
        }
        // sort
        for (int i = 0; i < str4times.length(); i++) {
            for (int j = 0; j <str4times.length() - 1; j++) {
                if (number[j] < number[j+1]) {
                    int temp = number[j];
                    number[j] = number[j+1];
                    number[j+1] = temp;
                }
            }
        }
        // ascii toString
        for (int i = 0; i < number.length; i++) {
            result += (char) number[i];
        }
        System.out.println(result);
        return result.substring(0,6);
    }

    public static int binaryTo10(String binary) {
        int num = 0;
        for (int i = 0; i < binary.length(); i++) {
//            System.out.println(Integer.parseInt(String.valueOf(binary.charAt(7-i)))*(Math.pow(2,i)));
            num += Integer.parseInt(String.valueOf(binary.charAt(7-i))) * (Math.pow(2,i));
        }
        return num;
    }


    /**
     * to 6 bit group
     * @param step1str1
     * @param group
     */
    private static String[] to6bit(String step1str1, String[] group) {
        // save the 6 bit String
        for (int i = 0; i < group.length; i++) {
            group[i] = step1str1.substring(i*6,(i+1)*6);
        }
        System.out.println("6bit------");
        for (String t : group) {
            System.out.println(t);
        }
        return group;
    }
}
