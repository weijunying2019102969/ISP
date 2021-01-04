import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MatchandAdd {

	private ArrayList<Long> intlist = new ArrayList<Long>();
	public ArrayList<String[]> iprouteschanged = new ArrayList<String[]>();
	public ArrayList<String[]> iproutesbeforechanged = new ArrayList<String[]>();
	public ArrayList<String[]> iproutesadded = new ArrayList<String[]>();
	private String[] unicomip = new String[]{"10.22.22.1","10.66.0.254","10.68.240.2"};
	private String[] cernetip = new String[]{"10.0.4.254","10.68.240.3"};
	//private String[] unicomip = new String[]{"127.0.0.1","192.168.1.201"};
	//private String[] cernetip = new String[]{"192.168.1.216"};

	public void matchandadd(ArrayList<String[]> iproutearraylist, ArrayList<String[]> ipaddresswaitforinsert)
	{
		GetIpaddressRange range = new GetIpaddressRange();
		for(int i = 0; i < iproutearraylist.size(); i++)
		{
			iproutearraylist.get(i)[0] = range.getFirstipaddress(iproutearraylist.get(i)[0],iproutearraylist.get(i)[1]);
		}
		this.getintarray(iproutearraylist);
		Collections.sort(intlist);
		for(int j = 0; j < ipaddresswaitforinsert.size(); j = j +1)
		{
			int numoflocation = bisearch(intlist , getint(ipaddresswaitforinsert.get(j)[0]));//二分查找
			
			boolean isexist = inornot(iproutearraylist.get(numoflocation),ipaddresswaitforinsert.get(j)[0]);	//比对待插入数据是否在对应记录起始ip地址之间
			
			if(isexist)
			{
				int search_wait_in_unicom = Arrays.binarySearch(unicomip,ipaddresswaitforinsert.get(j)[1]);
				int search_iproute_in_unicom = Arrays.binarySearch(unicomip,iproutearraylist.get(numoflocation)[2]);
				int search_wait_in_cernet = Arrays.binarySearch(cernetip,ipaddresswaitforinsert.get(j)[1]);
				int search_iproute_in_cernet = Arrays.binarySearch(cernetip,iproutearraylist.get(numoflocation)[2]);

				if(search_wait_in_unicom >= 0)
				{
					if(search_iproute_in_unicom >= 0)
					{
						continue;
					}
					else if(search_iproute_in_cernet >= 0)
					{
						this.change(iproutearraylist,ipaddresswaitforinsert,j,numoflocation);
					}
					else
					{
						System.out.println("The ip of iproutelist is unknown!");
					}
				}
				else if(search_wait_in_cernet >= 0)
				{
					if(search_iproute_in_unicom >= 0)
					{
						this.change(iproutearraylist,ipaddresswaitforinsert,j,numoflocation);
					}
					else if(search_iproute_in_cernet >= 0)
					{
						continue;
					}
					else
					{
						System.out.println("The ip of iproutelist is unknown!");
					}
				}
				else
				{
					System.out.println("The ip wait for insert is unknown!");
				}
			}
			else
			{
				String[] sameroute = trans(ipaddresswaitforinsert.get(j));  //将待插入数据转变为iproutearraylist同样的格式。
				int insertlocation3 = insertbyorder(iproutesadded,sameroute);
				if(insertlocation3 >= iproutesadded.size())
				{
					iproutesadded.add(sameroute);
				}
				else{
					iproutesadded.add(insertlocation3,sameroute);
				}
				iproutearraylist.add(numoflocation+1, sameroute);//插入一条记录
				this.getintarray(iproutearraylist);
			}
			
		}
		return;
	}

	//修改一条记录以及之前可能重复的记录
	public void change(ArrayList<String[]> iproutearraylist, ArrayList<String[]> ipaddresswaitforinsert,int j,int num)
	{
		String[] beforechange1 = new String[]{iproutearraylist.get(num)[0],iproutearraylist.get(num)[1],iproutearraylist.get(num)[2],iproutearraylist.get(num)[3]};
		iproutearraylist.get(num)[2] = ipaddresswaitforinsert.get(j)[1];
		int insertlocation1 = insertbyorder(iproutesbeforechanged,iproutearraylist.get(num-1));
		int insertlocation2 = insertbyorder(iprouteschanged,iproutearraylist.get(num));
		if(insertlocation1 >= iproutesbeforechanged.size())
		{
			iproutesbeforechanged.add(beforechange1);
		}
		else{
			iproutesbeforechanged.add(insertlocation1,beforechange1);
		}
		if(insertlocation2 >= iprouteschanged.size())
		{
			iprouteschanged.add(iproutearraylist.get(num));
		}
		else{
			iprouteschanged.add(insertlocation2,iproutearraylist.get(num));
		}
		long pre = getint(iproutearraylist.get(num-1)[0]);
		long itself = getint(iproutearraylist.get(num)[0]);
		if(pre == itself)
		{
			String[] beforechange2 = new String[]{iproutearraylist.get(num-1)[0],iproutearraylist.get(num-1)[1],iproutearraylist.get(num-1)[2],iproutearraylist.get(num-1)[3]};
			iproutearraylist.get(num-1)[2] = ipaddresswaitforinsert.get(j)[1];
			//int insertlocation1 = insertbyorder(iprouteschanged,iproutearraylist.get(num-1));
			if(insertlocation1 >= iproutesbeforechanged.size())
			{
				iproutesbeforechanged.add(beforechange2);
			}
			else{
				iproutesbeforechanged.add(insertlocation1,beforechange2);
			}
			// if(insertlocation1 >= iprouteschanged.size())
			// {
			// 	iprouteschanged.add(iproutearraylist.get(num-1));
			// }
			// else{
			// 	iprouteschanged.add(insertlocation2,iproutearraylist.get(num-1));
			// }
		}
	}
	//二分查找
	public static int bisearch(ArrayList<Long> intlist, long introute)
	{
		Long temp;
		int low = 0;
		int mid = 0;
		int high = intlist.size() - 1;
		boolean flag = true;

		while(low<=high)
		{
			mid = (low+high)/2;
			if(intlist.get(mid) == introute)
			{
				return mid;
			}
			else if(intlist.get(mid) > introute)
			{
				high = mid - 1;
				flag = false;
			}
			else
			{
				low = mid + 1;
				flag = true;
			}
		}
		if(flag)
		{
			return mid;
		}
		else{
			return mid-1;
		}
		
	}
	//看是否新的路由是否在目的网络的范围内。
	public static boolean inornot(String[] iproute, String newroute)
	{

		long intiproute1 = getint(iproute[0]);
		GetIpaddressRange rangeininornot = new GetIpaddressRange();
		String lastipaddress = rangeininornot.getLastipaddress(iproute[0],iproute[1]);
		long intiproute2 = getint(lastipaddress);
		long intnewroute = getint(newroute);
		if((intiproute1<=intnewroute)&&(intnewroute<=intiproute2))
		{
			return true;
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
	//把字符串转化为long数据并存储至intlist
	public void getintarray(ArrayList<String[]> iproutearraylist)
	{
		int size = iproutearraylist.size();
		this.intlist.clear();
		for(int i = 0; i < size;i = i + 1)
		{
			this.intlist.add(getint(iproutearraylist.get(i)[0]));
		}

		return;
	}
	//输入ip字符串，输出interger大小。
	public static Long getint(String ipaddress)
	{
		long intnumber = 0;
		int temp = 1000000000;
		String[] ipaddresses=ipaddress.split("[.]");
		for(String str:ipaddresses)
		{
			long i = Integer.valueOf(str).intValue();
			i = i*temp;
			temp = temp / 1000;
			intnumber = intnumber + i;
		}
		//System.out.println(intnumber);
		return intnumber;
	}

	//打印输出intlist
	public void printintlist()
	{
		for(int i = 0; i < this.intlist.size(); i++)
		{
			System.out.println(this.intlist.get(i));
		}
		return;
	}

	//按顺序插入
	public static int insertbyorder(ArrayList<String[]> iprouteslist, String[] iproute)
	{
		if(iprouteslist.size() < 1)
		{
			return 0;
		}
		int i = 0;
		for(i = 0;i < iprouteslist.size();i++)
		{
			if(compare(iprouteslist.get(i)[0],iproute[0]))
			{
				break;
			}
		}
		return i;
	}
	//比较两个ip地址的大小
	public static boolean compare(String ipaddress1, String ipaddress2)
	{
		long intipaddress1 = getint(ipaddress1);
		long intipaddress2 = getint(ipaddress2);
		if(intipaddress1 > intipaddress2)
		{
			return false;
		}
		return true;
	}

}