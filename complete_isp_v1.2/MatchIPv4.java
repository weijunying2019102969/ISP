import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MatchIPv4 {

	public ArrayList<String[]> ipv4routeschanged = new ArrayList<String[]>();
	public ArrayList<String[]> ipv4routesbeforechanged = new ArrayList<String[]>();
	public ArrayList<String[]> ipv4routesadded = new ArrayList<String[]>();
	private static String[] unicomipv4 = new String[]{"10.22.22.1","10.66.0.254","10.68.240.2"};
	private static String[] cernetipv4 = new String[]{"10.0.4.254","10.68.240.3"};

  // ipv4routearraylist 为路由配置文件解析结果
  // ipv4addresswaitforinsert为读取出来的最佳路由
	public void MatchIpv4(ArrayList<String[]> ipv4routearraylist, ArrayList<String[]> ipv4addresswaitforinsert)
	{
    // 遍历最佳路由链表，进行匹配
    // for(int i = 0; i < ipv4routearraylist.size();i++)
    // {
    //   for(int j = 0; j < ipv4routearraylist.get(i).length;j++)
    //   {
    //     System.out.println("ipv4routearraylist.get("+ i + ")[" + j + "]: " + ipv4routearraylist.get(i)[j]);
    //   }
    // }
    // for(int i = 0; i < ipv4addresswaitforinsert.size();i++)
    // {
    //   for(int j = 0; j < ipv4addresswaitforinsert.get(i).length;j++)
    //   {
    //     System.out.println("ipv4addresswaitforinsert.get("+ i + ")[" + j + "]: " + ipv4addresswaitforinsert.get(i)[j]);
    //   }
    // }
    for(int i = 0; i < ipv4addresswaitforinsert.size(); i++)
    {
      int target_flag_seq = -1;
      // 二进制最佳路由结果目标ipv4
      String binary_target_ipv4 = ConvertIPv4ToBinary(ipv4addresswaitforinsert.get(i)[0]);
      // 是否匹配成功标识
      for(int j = 0; j < ipv4routearraylist.size(); j++)
      {
        // 判断匹配标记
        String binary_target_ipv4_networksegment = ConvertIPv4ToBinary(ipv4routearraylist.get(j)[0]);
        int length_of_network_segment = GetMaskLength(ipv4routearraylist.get(j)[1]);
        
        String sub_binary_target_ipv4 = binary_target_ipv4.substring(0, length_of_network_segment);
        String sub_binary_target_ipv4_networksegment = binary_target_ipv4_networksegment.substring(0, length_of_network_segment);
        if(sub_binary_target_ipv4.equals(sub_binary_target_ipv4_networksegment))
        {
          target_flag_seq = j;
          break;
        }
      }
      if(target_flag_seq == -1)
      {
        // 匹配失败
        String[] route_wait_for_add = trans(ipv4addresswaitforinsert.get(i));  //将待插入数据转变为iproutearraylist同样的格式。
        ipv4routesadded.add(route_wait_for_add);//插入一条记录
      }
      else
      {
        // 匹配成功
        if((InUnicomIPv4(ipv4routearraylist.get(target_flag_seq)[2]) && InCernetIPv4(ipv4addresswaitforinsert.get(i)[1])) || (InCernetIPv4(ipv4routearraylist.get(target_flag_seq)[2]) && InUnicomIPv4(ipv4addresswaitforinsert.get(i)[1])))
        {
          ipv4routesbeforechanged.add(ipv4routearraylist.get(target_flag_seq));
          String[] route_wait_for_add = trans(ipv4addresswaitforinsert.get(i));  //将待插入数据转变为iproutearraylist同样的格式。
          ipv4routeschanged.add(route_wait_for_add);
        }
      }
    }
    return;
	}

	private static String ConvertIPv4ToBinary(String ipv4)
  {
    String binaryipv4 = "";
    String[] intarray = ipv4.split("[.]");
    for(int i = 0; i < intarray.length; i++)
    {
      binaryipv4 = binaryipv4 + DecimalstringToBinarystring(intarray[i]);
    }
    return binaryipv4;
  }

  private static int GetMaskLength(String maskip)
  {
    int masklength = 0;
    String binarymaskip = ConvertIPv4ToBinary(maskip);
    for(int i = 0; i < binarymaskip.length();i++)
    {
      if(binarymaskip.charAt(i) == '1')
      {
        masklength++;
      }
      else
      {
        break;
      }
    }
    return masklength; 
  }

  private static String DecimalstringToBinarystring(String decimalstring)
  {
    int decimalint = Integer.parseInt(decimalstring);
    String binarystring = Integer.toBinaryString(decimalint);
    String zero = "0";
    String res = "";
    for(int i = 0; i < 8-binarystring.length();i++)
    {
      res = res + zero;
    }

    res = res + binarystring;
    return res;
  }

  private static boolean InUnicomIPv4(String ipv4)
  {
    String binaryipv4 = ConvertIPv4ToBinary(ipv4);
    for(int i = 0; i < unicomipv4.length; i++)
    {
      String binaryunicomipv4 = ConvertIPv4ToBinary(unicomipv4[i]);
      if(binaryipv4.equals(binaryunicomipv4))
      {
        return true;
      }
    }
    return false;
  }

  private static boolean InCernetIPv4(String ipv4)
  {
    String binaryipv4 = ConvertIPv4ToBinary(ipv4);
    for(int i = 0; i < cernetipv4.length; i++)
    {
      String binarycernetipv4 = ConvertIPv4ToBinary(cernetipv4[i]);
      if(binaryipv4.equals(binarycernetipv4))
      {
        return true;
      }
    }
    return false;
  }

  //转化为子网掩码为255.255.255.255的路由记录
  public static String[] trans(String[] listroute)
  {
    String[] newListroute = new String[3];
    newListroute[0] = listroute[0];
    newListroute[1] = "255.255.255.255";
    newListroute[2] = listroute[1];
    return newListroute;
  }
}