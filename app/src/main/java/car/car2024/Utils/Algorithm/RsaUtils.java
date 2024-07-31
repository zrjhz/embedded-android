package car.car2024.Utils.Algorithm;

import java.util.ArrayList;

/**
 * 比赛出的RSA的算法.
 * 最后出现的问题在于极大数取余的问题
 * @author hdy
 *
 */
public class RsaUtils {

    /**
     * Rsa 解密
     */
	public static void decode(int[] code, int[] data) {
        // 选出两个不相等且最大的质数
		// int[] code = { 3, 4, 5, 6, 7, 8, 9, 11 };
		ArrayList<Integer> lists = new ArrayList<Integer>();
		for (int j = 0; j < code.length; j++) {
			boolean flag = false;
			for (int i = 1; i <= 1000; i++) {
				if (i == 1 || i == code[j]) {
					continue;
				}
				int temp = code[j] % i;
				if (temp == 0) {
					flag = true;
					break;
				} else {
					flag = false;
				}
			}
			if (!flag) {
				lists.add(code[j]);
			}
		}
        // 获取到所有的质数,获取其中两个不相等而且最大的质数p\q
		int p = 0, q = 0;
		for (int i = 0; i < lists.size(); i++) {
			Integer integer = lists.get(i);
			if (p <= integer) {
				p = integer;
			}
		}
		for (int i = 0; i < lists.size(); i++) {
			Integer integer = lists.get(i);
			if (q <= integer && p > integer) {
				q = integer;
			}
		}
        // 获取到相应的p和q之后,计算p q的乘积n
        // 乘积
		int n = p * q;
		// $n,
		int n2 = (p - 1) * (q - 1);
		// System.out.println(n2);

		int e = n2;
		for (int i = 2; i < n2; i++) {
			if (n2 % i != 0) {
				if (e > i) {
					e = i;
				}
			}
		}

        // 获取到符合条件最小的正整数
        // 模反元素
		int d = 65536;
		for (int i = 0; i <= 65536; i++) {
			int temp = (e * i) % n2;
			if (temp == 1) {
				// ˵����ģ��Ԫ��
				if (d >= i) {
					d = i;
				}
			}
		}
		int[] public_key = { n, e };
		int[] private_key = { n, d };
		int[] temp_data2 = new int[6];
		for (int i = 0; i < data.length; i++) {
			int m = (int) (Math.pow(data[i], d) % n);
			temp_data2[i] = m;
		}

		for (int i = 0; i < temp_data2.length; i++) {
			System.out.println(temp_data2[i]);
		}

		byte[] temp_byte = new byte[6];
		String result = "";
		for (int i = 0; i < temp_data2.length; i++) {
			String string = StringUtils.toHex((byte) temp_data2[i]);
			result += string;
		}
	}
}
