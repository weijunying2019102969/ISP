import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MatchIPv6 {

  private ArrayList<Long> intlist = new ArrayList<Long>();
  public ArrayList<String[]> ipv6routeschanged = new ArrayList<String[]>();
  public ArrayList<String[]> ipv6routesbeforechanged = new ArrayList<String[]>();
  public ArrayList<String[]> ipv6routesadded = new ArrayList<String[]>();
  private static String[] unicomipv6 = new String[]{"2001:250:7401:250::254"};
  private static String[] cernetipv6 = new String[]{"2001:250:7401:5::1"};


  // ipv6routearraylist 为路由配置文件解析结果
  // ipv6addresswaitforinsert为读取出来的最佳路由
  public void MatchIpv6(ArrayList<String[]> ipv6routearraylist, ArrayList<String[]> ipv6addresswaitforinsert)
  {
    // 遍历最佳路由链表，进行匹配
    for(int i = 0; i < ipv6addresswaitforinsert.size(); i++)
    {
      int target_flag_seq = -1;
      // 二进制最佳路由结果目标ipv6
      String binary_target_ipv6 = ConvertIPv6ToBinary(ipv6addresswaitforinsert.get(i)[0]);
      // 是否匹配成功标识
      //boolean matched_flag = false;
      // 遍历配置结果，查看是否有与ipv6addresswaitforinsert.get(i)[0]匹配的目标网段
      for(int j = 0; j < ipv6routearraylist.size(); j++)
      {
        // 判断匹配标记
        boolean in_flag = true;
        String binary_target_ipv6_networksegment = ConvertIPv6ToBinary(ipv6routearraylist.get(j)[0]);
        int length_of_network_segment = Integer.parseInt(ipv6routearraylist.get(j)[1]);
        
        String sub_binary_target_ipv6 = binary_target_ipv6.substring(0, length_of_network_segment);
        String sub_binary_target_ipv6_networksegment = binary_target_ipv6_networksegment.substring(0, length_of_network_segment);
        if(sub_binary_target_ipv6.equals(sub_binary_target_ipv6_networksegment))
        {
          //System.out.println("j: " + j);
          target_flag_seq = j;
          break;
        }
      }
      if(target_flag_seq == -1)
      {
        // 匹配失败
        String[] route_wait_for_add = TransTwoToOfficial(ipv6addresswaitforinsert.get(i));  //将待插入数据转变为iproutearraylist同样的格式。
        ipv6routesadded.add(route_wait_for_add);//插入一条记录
      }
      else
      {
        // 匹配成功
        if((InUnicomIPv6(ipv6routearraylist.get(target_flag_seq)[2]) && InCernetIPv6(ipv6addresswaitforinsert.get(i)[1])) || (InCernetIPv6(ipv6routearraylist.get(target_flag_seq)[2]) && InUnicomIPv6(ipv6addresswaitforinsert.get(i)[1])))
        {
          String[] route_wait_for_delete = TransThreeToOfficial(ipv6routearraylist.get(target_flag_seq));  //将待插入数据转变为iproutearraylist同样的格式。
          ipv6routesbeforechanged.add(route_wait_for_delete);
          String[] route_wait_for_add = TransTwoToOfficial(ipv6addresswaitforinsert.get(i));  //将待插入数据转变为iproutearraylist同样的格式。
          ipv6routeschanged.add(route_wait_for_add);
        }
      }
    }
    return;
  }


  private static boolean InUnicomIPv6(String ipv6)
  {
    String binaryipv6 = ConvertIPv6ToBinary(ipv6);
    for(int i = 0; i < unicomipv6.length; i++)
    {
      boolean equal = true;
      String binaryunicomipv6 = ConvertIPv6ToBinary(unicomipv6[i]);
      if(binaryipv6.equals(binaryunicomipv6))
      {
        return true;
      }
    }
    return false;
  }

  private static boolean InCernetIPv6(String ipv6)
  {
    String binaryipv6 = ConvertIPv6ToBinary(ipv6);
    for(int i = 0; i < cernetipv6.length; i++)
    {
      String binarycernetipv6 = ConvertIPv6ToBinary(cernetipv6[i]);
      if(binaryipv6.equals(binarycernetipv6))
      {
        return true;
      }
    }
    return false;
  }
  //转化为子网掩码为128的路由记录
  private static String[] TransTwoToOfficial(String[] listroute)
  {
    String[] newListroute = new String[2];
    newListroute[0] = listroute[0] + "/128";
    newListroute[1] = listroute[1];
    return newListroute;
  }

  //转化为子网掩码为128的路由记录
  private static String[] TransThreeToOfficial(String[] listroute)
  {
    String[] newListroute = new String[2];
    newListroute[0] = listroute[0] + "/" + listroute[1];
    newListroute[1] = listroute[2];
    //newListroute[2] = listroute[3];
    return newListroute;
  }

  private static String ConvertIPv6ToBinary(String ipv6)
  {
    String binaryipv6 = "";
    int location = GetDoubleColonLocation(ipv6);
    //System.out.println("location: " + location);
    String[] intarray = ipv6.split("[:]");
    //System.out.println("intarray.length: " + intarray.length);
    if(location == 7)
    {
      for(int i = 0; i < intarray.length; i++)
      {
        binaryipv6 = binaryipv6 + HexstringToBinarystring(intarray[i]);
      }
      return binaryipv6;
    }
    String sixteenzero = "0000000000000000";
    int countsixteenzero = 8 - intarray.length;
    String zerotofill = "";
    for(int i = 0; i < countsixteenzero; i++)
    {
      zerotofill = zerotofill + sixteenzero;
    }
    for(int i = 0; i < intarray.length; i++)
    {
      if(i == location)
      {
        binaryipv6 = binaryipv6 + zerotofill;
      }
      binaryipv6 = binaryipv6 + HexstringToBinarystring(intarray[i]);
      //System.out.println("binaryipv6: " + binaryipv6);
      //System.out.println("binaryipv6.length: " + binaryipv6.length());
    }
    
    for(int i = binaryipv6.length(); i < 128;i++)
    {
      binaryipv6 = binaryipv6 + "0";
    }
    return binaryipv6;
  }

  private static int GetDoubleColonLocation(String ipv6)
  {
    int location = 0;
    for(int i = 0; i < ipv6.length();i++)
    {
      if(ipv6.charAt(i) == ':')
      {
        location++;
        if(ipv6.charAt(i+1) == ':')
          break;
      }
    }
    return location;
  }

  private static String HexstringToBinarystring(String hexstring)
  {
    int lenofhex = hexstring.length();

    String binarystring = "";
    HashMap<String, String> tranlist = new HashMap<String, String>();
    
    tranlist.put("0", "0000");
    tranlist.put("1", "0001");
    tranlist.put("2", "0010");
    tranlist.put("3", "0011");
    tranlist.put("4", "0100");
    tranlist.put("5", "0101");
    tranlist.put("6", "0110");
    tranlist.put("7", "0111");
    tranlist.put("8", "1000");
    tranlist.put("9", "1001");
    tranlist.put("a", "1010");
    tranlist.put("b", "1011");
    tranlist.put("c", "1100");
    tranlist.put("d", "1101");
    tranlist.put("e", "1110");
    tranlist.put("f", "1111");
    for(int i = 0; i < lenofhex; i++) 
    {
      binarystring = binarystring + tranlist.get(String.valueOf(hexstring.charAt(i)));
    }
    String zero = "0";
    String res = "";
    for(int i = 0; i < 16-binarystring.length();i++)
    {
      res = res + zero;
    }
    res = res + binarystring;
    return res;
  }

}
