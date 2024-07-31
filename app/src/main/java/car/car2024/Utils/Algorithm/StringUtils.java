package car.car2024.Utils.Algorithm;

/**
 * 进制转换的工具类.
 * 用于进制的转换
 * @author hdy
 *
 */
public class StringUtils {
	/*
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src byte[] data
	 * 
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	// 把byte 转化为两位十六进制数
	public static String toHex(byte b) {
		String result = Integer.toHexString(b & 0xFF);
		if (result.length() == 1) {
			result = '0' + result;
		}
		return result;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 二进制转十进制
	 * 
	 * @param binaryNumber
	 * @return
	 */
	public static int BinaryToDecimal(int binaryNumber) {

		int decimal = 0;
		int p = 0;
		while (true) {
			if (binaryNumber == 0) {
				break;
			} else {
				int temp = binaryNumber % 10;
				decimal += temp * Math.pow(2, p);
				binaryNumber = binaryNumber / 10;
				p++;
			}
		}
		return decimal;
	}

	/**
	 * 字符串转换二进制
	 * 
	 * @param str
	 * @return
	 */
	private static String StrToBinstr(String str) {
		char[] strChar = str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			result += Integer.toBinaryString(strChar[i]);
		}
		return result;
	}

	// byte变换为String的十六进制
	private static String toHexString(byte b) {
		String hex = Integer.toHexString(b & 0xff);
		hex = hex.toUpperCase();
		if (hex.length() == 1)
			hex = '0' + hex;
		return "0x" + hex;
	}

	/**
	 * 字符串倒序
	 * 
	 * @return
	 */
	public static String reverseString(String str) {
		StringBuffer buffer = new StringBuffer(str);
		return buffer.reverse().toString();
	}
	/**
	 * 16进制转ASCII码
	 * @param s
	 * @return
	 */
	public static String toStringHex1(String s) {  
		 char[] chars = s.toCharArray();  
		  
	      StringBuffer hex = new StringBuffer();  
	      for(int i = 0; i < chars.length; i++){  
	        hex.append(Integer.toHexString((int)chars[i]));  
	      }  
	  
	      return hex.toString();  
	      }  
	  
	      public String convertHexToString(String hex){  
	  
	      StringBuilder sb = new StringBuilder();  
	      StringBuilder temp = new StringBuilder();  
	  
	      //49204c6f7665204a617661 split into two characters 49, 20, 4c...  
	      for( int i=0; i<hex.length()-1; i+=2 ){  
	  
	          //grab the hex in pairs  
	          String output = hex.substring(i, (i + 2));  
	          //convert hex to decimal  
	          int decimal = Integer.parseInt(output, 16);  
	          //convert the decimal to character  
	          sb.append((char)decimal);  
	  
	          temp.append(decimal);  
	      }  
	  
	      return sb.toString();  
		}
	      /**
	       * 数字字符串转ASCII码字符串
	       * 
	       * @param String
	       *            字符串
	       * @return ASCII字符串
	       */
	      public static String StringToAsciiString(String content) {
	          String result = "";
	          int max = content.length();
	          for (int i = 0; i < max; i++) {
	              char c = content.charAt(i);
	              String b = Integer.toHexString(c);
	              result = result + b;
	          }
	          return result;
	      }
}
