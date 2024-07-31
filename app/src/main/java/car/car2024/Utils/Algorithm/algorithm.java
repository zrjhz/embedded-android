package car.car2024.Utils.Algorithm;


import android.util.Log;


@SuppressWarnings("all")
public class algorithm {

//	public static void main(String[] args)
//	{
//		CRC_Code("(<AaBbCcDd>/<g(x)=x16+x15+x2+1>)",CRC_order_data);
//
//		Affine("A/5,2;B\\C[D.y(EF]Gf,3,7",Affine_order_data);
//	}



	/****************************************************************************************************
	 CRC
	 ****************************************************************************************************/

	//CRC     (<AaBbCcDd>/<g(x)=x16+x15+x2+1>)


	public static void main(String[] args) {
		CRC_Code("<Aa12x16,Fg.5tx15/x2+\\1/hgBb>", new byte[6]);
	}

	private static byte[] data = {0x41,0x61,0x42,0x62};

	public static void CRC_Code(String SrcString,byte[] order_buffer)
	{
		char s = 0;
		char s1=0,s2=0,s3=0;
		char temp = 0;
		char[] buf = new char[4];
		char[] Num = new char[3];
		int i=0;
		int j=0;
		char PolyCode = 0;

		char  CRC = 0xFFFF;   //CRC寄存器
		char  CRC2 = 0;

		//SrcString为二维码里获取的字符串
		//获取前两个明文字符
		for (i = 0; i < SrcString.length(); i++)
		{
			s = SrcString.charAt(i);
			if((s>= 'a' && s<= 'z'&&s!='x') || (s>='A' && s<= 'Z'&&s!='X'))
			{
				buf[temp] = s;
				temp++;
				if(temp>=2) break;
			}
		}

		//获取后两个明文字符
		temp = 0;
		for (i = (SrcString.length()-1); i>=0; i--)
		{
			s = SrcString.charAt(i);
			if((s>= 'a' && s<= 'z'&&s!='x') || (s>='A' && s<= 'Z'&&s!='X'))
			{
				buf[3-temp] = s;
				temp++;
				if(temp>=2) break;
			}
		}

		//提取多项式码
		for (i = 0; i < SrcString.length(); i++)
		{
			s1 = SrcString.charAt(i);
			if(s1=='x')
			{
				//s1 = SrcString.GetAt(i);
				s2 = SrcString.charAt(i+1);
				s3 = SrcString.charAt(i+2);
				if(j<3)
				{
					if((s1=='x')&&(s2>= '0' && s2<= '9')&&(s3<'0'||s3>'9'))
					{
						Num[j] = (char)(s2-'0');
						j++;
					}
					else if((s1=='x')&&(s2>= '0' && s2<= '9')&&(s3>= '0' && s3<= '9'))
					{
						Num[j] = (char)((s2-'0')*10+s3-'0');
						if(Num[j]>9&&Num[j]<16)
						{
							j++;
						}
					}
				}
			}
		}
		PolyCode = (char)( 0x0001+(0x0001<<(Num[0]))+(0x0001<<(Num[1]))+(0x0001<<(Num[2])));
		System.out.println(BToH(PolyCode));

		System.out.println(BToH(WORD_WordInvert(PolyCode)));

		for(j=0;j<4;j++)
		{
			CRC =  (char) (CRC ^ buf[j]);
			for(i= 0;i<8;i++)
			{
				CRC2 = (char) (CRC & 0x0001);
				if(CRC2 == 0x0001)
				{
					CRC = (char)((CRC>>1)^ WORD_WordInvert(PolyCode));
				}
				else
				{
					CRC = (char)(CRC>>1);
				}
			}
		}

		System.out.println(Integer.toHexString(CRC));

		order_buffer[0] = (byte)( (CRC>>8)&0xFF);  	//得到高位

		order_buffer[5] = (byte) (CRC&0xFF);	//得到低位

		System.out.println(BToH(CRC));

		for (i = 0; i < 4; i++)
		{
			order_buffer[i+1] = (byte) buf[i];
		}

		for(i = 0;i < 6; i++)
		{
			Log.e("data",""+BToH((char)((order_buffer[i])&0xFF)));
		}
	}


	private static byte ByteInvert(byte temp)
	{
		char[] sta ={0x00,0x08,0x04,0x0C,0x02,0x0A,0x06,0x0E,0x01,0x09,0x05,0x0D,0x03,0x0B,0x07,0x0F};
		byte d = 0;
		d |= (sta[temp&0xF]) << 4;
		d |= sta[(temp>>4)&0xF];
		return d;
	}

	private static char WORD_WordInvert(char w)
	{
		byte temp = 0;
		char d = 0;

		temp=(byte)(w&0xFF);
		temp=ByteInvert(temp);
		d=(char) ((temp<<8)&0xFF00);
		temp=(byte)((w>>8)&0xFF);
		temp=ByteInvert(temp);
		d|=temp;
		return d;
	}


	/****************************************************************************************************
	 仿射密码解密
	 ****************************************************************************************************/


	public  void Affine(String SrcString,byte[] order_buffer)
	{
		char s1=0,s2=0;
		char Num1[]= new char[7];//待解密密文
		char Num2[]= new char[7];//解密后明文
		char[] Qr_SrcString = new char[SrcString.length()];
		int i=0;
		int j=0;
		int K1=0,K2=0,K3=0,b=0;;

		char t1[] = { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
		char t2[] = { 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };


		for(i = 0; i < SrcString.length(); i++ )				 //将字符串存放在数组中
		{
			Qr_SrcString[i] = SrcString.charAt(i);
		}



		for (i = 0; i < Qr_SrcString.length; i++)				//提取出字符串中前7个大写字母
		{
			s1 = Qr_SrcString[i];
			if((s1>= 'A') && (s1<= 'Z'))
			{
				if(j<7)
				{
					Num1[j] = s1;
					j++;
				}
			}
		}



		for (i = 0; i < Qr_SrcString.length; i++)
		{
			s1 = Qr_SrcString[i];
			if((s1== '0') | (s1== '3')| (s1== '5')| (s1== '7')| (s1== '9'))
			{
				K1 = s1-'0';
				break;
			}
		}

		for (i = Qr_SrcString.length; i > 0; i--)
		{
			s2 = Qr_SrcString[i-1];
			if((s2>= '0') && (s2<= '9'))
			{
				K2 = s2-'0';
				break;
			}
		}

		while(((K1*K3)%26)!=1)
		{
			if(K3>65535) {return;}
			K3++;
		}


		for (int n = 0; n < 7; n++)
		{
			for (int l = 0; l < 26; l++)
			{
				if (Num1[n] == t2[l])
				{
					if(l<K2)
						b = 26-((K3 *(K2 - l)) % 26);
					else
						b = (K3 *(l - K2)) % 26;

					Num2[n] = t1[b];
				}
			}
		}



		for(i = 0;i<6;i++)
		{
			if((i%2)==0)
			{
				order_buffer[i] = (byte) ((Math.abs(Num2[i+1]-Num2[i]))%255);

			}
			else
			{
				order_buffer[i] = (byte) ((Math.abs(Num2[i+1]+Num2[i]))%255);
			}
		}

		for(i =0; i<6; i++)
		{
			System.out.println(BToH((char)(order_buffer[i]&0xFF)));

		}


	}

	/**********************************************************************************
	 * 							数据处理
	 * *******************************************************************************/
	// 二进制转十六进制
	public static String BToH(char a)
	{
		String b = Integer.toHexString(a);
		return b;
	}



	//将字符串转十进制
	public static int SToB(String a)
	{
		int b = Integer.parseInt(a);

		System.out.println("STOB"+b);
		return b;
	}

	//字符串截取
	public String cut_out(String data,int start,int end)
	{
		String Temp_data = null;
		Temp_data =data.substring(start, end);
		return Temp_data;
	}



}
