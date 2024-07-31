package car.car2024.Utils.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhy
 * @create_date 2019-04-24 20:40
 */

// 希尔密码

@SuppressWarnings("all")
public class HillCipher {

    public static void main(String[] args) {
        String info = "{\"Data\":\"paymoremoney\",\"Key\":\"rrfvsvcct\"}";

        String[] infos = info.split(",");

        String mw = infos[0].split(":")[1];
        String my = infos[1].split(":")[1];

        System.out.println("mw = " + mw);
        System.out.println("my = " + my);

        List<Integer> mwList = new ArrayList<>();
        for (int i = 1; i < mw.length() - 1; i++) {
            int c = (int) mw.charAt(i);
            mwList.add(c - 97);

        }

        mwList.forEach(System.out::println);

        System.out.println("---------------------");

        Pattern pattern = Pattern.compile("[A-Za-z]{1}");
        Matcher matcher = pattern.matcher(my);

        List<Integer> myList = new ArrayList<>();
        while (matcher.find()){
            String group = matcher.group();
            int c = (int) group.charAt(0);
            myList.add(c - 97);
        }

        myList.forEach(System.out::println);

        int[][] myGroup = new int[3][3];

        int index = 0;
        for (int i = 0; i < myGroup.length; i++) {
            for (int j = 0; j < myGroup[i].length; j++) {
                myGroup[i][j] = myList.get(index);
                ++index;
            }
        }

        index = 0;

        int[][] mwGroup = new int[4][3];
        for (int i = 0; i < mwGroup.length; i++) {
            for (int j = 0; j < mwGroup[i].length; j++) {
                mwGroup[i][j] = mwList.get(index);
                index++;
            }
        }

        System.out.println("mwGroup-------");
        for (int i = 0; i < mwGroup.length; i++) {
            for (int j = 0; j < mwGroup[i].length; j++) {
                System.out.print(mwGroup[i][j] + ",");
            }
            System.out.println();
        }

        System.out.println("myGroup--------");
        for (int i = 0; i < myGroup.length; i++) {
            for (int j = 0; j < myGroup[i].length; j++) {
                System.out.print( myGroup[i][j] + ",");
            }
            System.out.println();
        }

        index = 0;

        int[][] result = new int[4][3];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                int s = 0;
                for (int z = 0; z < 3; z++) {
                    s += mwGroup[i][z] * myGroup[z][j];
                }
                result[i][j] = s % 26;
            }
        }

        System.out.println("result--------");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print( result[i][j] + ",");
            }
            System.out.println();
        }

        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < 4; i += 3) {
            for (int j = 0; j < 3; j++) {
                resultList.add(result[i][j] + 65);
            }
        }

        System.out.println("---------------------------");
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(Integer.toHexString(resultList.get(i)));
        }


    }
}
