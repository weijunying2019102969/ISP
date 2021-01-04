
import java.io.FileInputStream;
import java.io.InputStream;

public class GetIpaddressRange {

	// 输入目标网络第一个ip地址和子网掩码，返回该网络的第一个ip地址
	public String getFirstipaddress(String first_ipaddress, String subnet_mask)
	{
		String binaryfirstipaddress = getbinaryipaddress(first_ipaddress);
        String binarymaskipaddress  = getbinaryipaddress(subnet_mask);
        String unbinarymaskipaddress = getunbinaryipaddressall0(binarymaskipaddress); 
        int lengthofunbinarymaskipaddress = unbinarymaskipaddress.length();
        int lengthofbinaryfirstipaddress = binaryfirstipaddress.length();
        binaryfirstipaddress  = binaryfirstipaddress.substring(0,lengthofbinaryfirstipaddress-lengthofunbinarymaskipaddress) + unbinarymaskipaddress;
        String firstipaddress = getipaddress(binaryfirstipaddress);
        return firstipaddress;
	} 

	// 输入目标网络第一个ip地址和子网掩码，返回该网络的最后一个ip地址
	public String getLastipaddress(String first_ipaddress, String subnet_mask)
	{
		String binaryfirstipaddress = getbinaryipaddress(first_ipaddress);
        String binarymaskipaddress  = getbinaryipaddress(subnet_mask);
        String unbinarymaskipaddress = getunbinaryipaddress(binarymaskipaddress); 
        int lengthofunbinarymaskipaddress = unbinarymaskipaddress.length();
        int lengthofbinaryfirstipaddress = binaryfirstipaddress.length();
        String binarylastipaddress  = binaryfirstipaddress.substring(0,lengthofbinaryfirstipaddress-lengthofunbinarymaskipaddress) + unbinarymaskipaddress;
        String lastipaddress = getipaddress(binarylastipaddress);
        return lastipaddress;
	} 

	// 输入一个ip地址，返回二进制去“.”的ip地址
	public static String getbinaryipaddress(String ipaddress)
	{
		String[] ipaddresses=ipaddress.split("[.]");
		String binaryipaddress = "";
		for(String str:ipaddresses)
		{
			int i = Integer.valueOf(str).intValue();
			String binStr = Integer.toBinaryString(i);
			String allzero = "00000000";
			if(binStr.length() < 8)
			{
				binStr = allzero.substring(0,8-binStr.length()) + binStr;
			}
			binaryipaddress = binaryipaddress + binStr;
		}
		return binaryipaddress;
	}

	// 输入二进制去“.”的ip地址，返回它的非的“全0”的字串
	public static String getunbinaryipaddressall0(String binaryipaddress)
	{
		String unbinaryipaddress = "";
		for (int i = 0;i < binaryipaddress.length() ;i++)
		 {
			if(binaryipaddress.charAt(i) == '1')
			{

			}
			else if(binaryipaddress.charAt(i) == '0')
			{
				unbinaryipaddress = unbinaryipaddress + "0";
			}
			else
			{
				System.out.println("WrongString: expect an binary string!");
			}
		}
		return unbinaryipaddress;
	}

	// 输入二进制去“.”的ip地址，返回它的非的“全1”的字串
	public static String getunbinaryipaddress(String binaryipaddress)
	{
		String unbinaryipaddress = "";
		for (int i = 0;i < binaryipaddress.length() ;i++)
		 {
			if(binaryipaddress.charAt(i) == '1')
			{

			}
			else if(binaryipaddress.charAt(i) == '0')
			{
				unbinaryipaddress = unbinaryipaddress + "1";
			}
			else
			{
				System.out.println("WrongString: expect an binary string!");
			}
		}
		return unbinaryipaddress;
	}

	// 输入二进制去“.”的ip地址，返回ip地址
	public static String getipaddress(String binaryipaddress)
	{
		String firststring = binaryipaddress.substring(0,8);
		String socondstring = binaryipaddress.substring(8,16);
		String thirdstring = binaryipaddress.substring(16,24);
		String fourthstring = binaryipaddress.substring(24,binaryipaddress.length());
		int firstinteger = Integer.parseInt(firststring);
		int secondinteger = Integer.parseInt(socondstring);
		int thirdinteger = Integer.parseInt(thirdstring);
		int fourthinteger = Integer.parseInt(fourthstring);

		String ipaddress = getsubip(firstinteger)+"."+getsubip(secondinteger)+"."+getsubip(thirdinteger)+"."+getsubip(fourthinteger);
		return ipaddress;
	}

	// 输入一个二进制数，转换为对应的十进制数，并转换为String后返回
	public static String getsubip(int subintegerip)
	{
		int decimal = 0;
		int i = 1;
		int j = 1;
		int temp = 0;
		while(subintegerip > 0){
			temp = subintegerip % 10;
			subintegerip = subintegerip / 10;
			temp = temp * i;
			i = i * 2;
			decimal = decimal + temp;
		}
		String stringdecimal = String.valueOf(decimal);
		return stringdecimal;
	}

}