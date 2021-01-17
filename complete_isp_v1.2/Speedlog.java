import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Speedlog {
	/**
	* 调用该函数即可得到List<Map<String, String>>结果
	* @return List<Map<String, String>>
	 * @throws Exception 
	*/
	public static List<Map<String, String>> GetList() throws Exception{
			// 执行查询
	        String sql = null;
	        sql = "select t1.student_ip, t1.student_url from (select t.student_url, t.avg_speed, t.student_ip from (select student_url, avg(student_speed) as avg_speed, student_ip from student_speed group by student_url, student_ip) as t order by t.student_url, t.avg_speed) as t1 where t1.avg_speed in (select dt.min_speed from (select min(t2.avg_speed) as min_speed from(select t.student_url, t.avg_speed, t.student_ip from (select student_url, avg(student_speed) as avg_speed, student_ip from student_speed group by student_url, student_ip) as t order by t.student_url, t.avg_speed) as t2 group by t2.student_url) as dt);";
	        
	        ResultSet rs = ExecuteQuery(sql);//调用函数ExecuteQuery(sql)得到ResultSet
	             
	        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
	        list = ToList(rs);//调用函数ToList(rs)得到List
	        rs.close();// 关闭ResultSet
	        return list;	
	}
	/**
	* ResultSet转到List<Map<String, String>>存储，过程中把URL转换成IP，把URL与IP以键值对的形式存到List里的Map中
	* @param ResultSet
	* @return List<Map<String, String>>
	*/
	public static List<Map<String, String>> ToList(ResultSet rs) throws Exception{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		while (rs.next()) {//数据集不为空
			String domain = rs.getObject("t1.student_url").toString();//把URL对应的域名存到domain中
			//System.out.println(domain);
			List<String> listIP = new ArrayList<String>();
			listIP = GetMapIP(domain);//调用GetMapIP(domain)把URL转换成IP的List
			//循环listIP把每个IP与IPS存到Map中
			//System.out.println(listIP);
			for (String item:listIP)
			{
				//System.out.println("item: " + item);
				String temp = "";
				if(rs.getObject("t1.student_ip").toString().equals("10.253.1.4") || rs.getObject("t1.student_ip").toString().equals("127.0.0.1"))
        		{
            		temp += "10.68.240.2";
        		}
        		if(rs.getObject("t1.student_ip").toString().equals("10.253.1.7"))
        		{
        			temp += "10.68.240.3";
        		}
				if(rs.getObject("t1.student_ip").toString().equals("2001:da8:270:2021::107"))
        		{
            		temp += "2001:250:7401:250::254";
        		}
        		if(rs.getObject("t1.student_ip").toString().equals("2001:da8:270:2021::104"))
        		{
        			temp += "2001:250:7401:5::1";
        		}
        		
				map.put(item,temp);//获取IP键名和IPS键值				
			}
		}
		list.add(map);	
		return list;
	}
	/**
	* 定义Map字典存储IP的List,传入域名，返回域名IP的List
	* @param domain 
	* @return List<String>
	* @throws Exception 
	*/
	public static List<String> GetMapIP(String domain) throws Exception{
		//分别把各域名所有IP存入List中
		final List<String> list1 = new ArrayList<String>() 
		{{ add("222.22.29.103");add("47.246.4.233");}};
		final List<String> list2 = new ArrayList<String>() 
		{{ add("203.81.18.57");}};
		final List<String> list3 = new ArrayList<String>() 
		{{ add("54.201.108.98"); add("35.155.145.37"); }};
		final List<String> list4 = new ArrayList<String>() 
		{{ add("14.0.41.202"); add("14.128.1.161");}};
		final List<String> list5 = new ArrayList<String>() 
		{{ add("2402:f000:1:404:166:111:4:100");}};
		final List<String> list6 = new ArrayList<String>() 
		{{ add("2001:da8:207:e214::3"); }};
		final List<String> list7 = new ArrayList<String>() 
		{{ add("2600:140b:10:898::255e"); }};
		final List<String> list8 = new ArrayList<String>() 
		{{ add("2620:12a:8000::1"); }};
		final List<String> list9 = new ArrayList<String>() 
		{{ add("61.135.169.125"); add("61.135.169.121"); }};
		final List<String> list10 = new ArrayList<String>() 
		{{ add("218.60.6.130"); }};
		final List<String> list11 = new ArrayList<String>() 
		{{ add("2401:b180:2000:10::2c"); }};
		final List<String> list12 = new ArrayList<String>() 
		{{ add("2408:4001:f10::fd");}};
		final List<String> list13 = new ArrayList<String>() 
		{{ add("162.159.130.81"); add("162.159.129.81");}};
		final List<String> list14 = new ArrayList<String>() 
		{{ add("104.16.103.29"); add("104.16.104.29");}};
		final List<String> list15 = new ArrayList<String>() 
		{{ add("140.234.254.11"); }};
		final List<String> list16 = new ArrayList<String>() 
		{{ add("123.58.178.129"); add("123.58.178.130");}};

		//把各域名与其对应IP的List以键值形式存入Map中
		Map<String, List<String>> map = new HashMap<String, List<String>>() 
		{{ put("http://www.taobao.com/", list1); 
		put("http://www.cnki.net", list2);
		put("http://isiknowledge.com", list3); 
		put("http://www.engineeringvillage.com", list4);
		put("http://www.tsinghua.edu.cn", list5); 
		put("http://www.bnu.edu.cn", list6);
		put("http://www.mit.edu", list7); 
		put("http://www.harvard.edu", list8);
		put("http://www.baidu.com", list9); 
		put("http://www.sohu.com", list10);
		put("http://www.xiami.com", list11); 
		put("http://www.youku.com", list12);
		put("http://www.sciencedirect.com", list13); 
		put("http://www.scopus.com", list14);
		put("http://search.ebscohost.com", list15); 
		put("http://mail.nenu.edu.cn/", list16);}};

		//取返回某域名IP的List
	    return map.get(domain);	
	}
	
	/**
	* 连接MySQL数据库
	* @return Connection
	*/
	public static Connection GetConnectMySQL(){
		Connection conn = null; 
		//MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		//如果不指定字符集则在封装的关键字里查询不到结果  
	    final String DB_URL = "jdbc:mysql://localhost:3306/speed201503?useSSL=false"; 
	    //数据库的用户名与密码，需要根据自己的设置
	    final String USER = "root";
	    final String PASS = "59568352";
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        } 
        return conn;
	}
	/**
	* 查询结果【Query】
    * @param sql 
    * @return ResultSet 
    */
	public static ResultSet ExecuteQuery(String sql) {
       Connection conn = null; 
       Statement stmt = null; 
       ResultSet rs = null;

       try {
           conn = GetConnectMySQL();//连接数据库
           stmt = conn.createStatement(); //创建连接语句
           rs = stmt.executeQuery(sql); //执行查询
       } catch (SQLException e) { 
           e.printStackTrace(); 
       } 
       return rs;
   } 
}