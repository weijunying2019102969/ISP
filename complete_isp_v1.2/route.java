import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import java.io.InputStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class route
{
    public static void main(String[] args) throws Exception {   
      
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd-HHmmss");  
        String datetime = tempDate.format(new java.util.Date());  
        CharStream input;
        if(args.length == 1) {
            String fileName = String.valueOf(args[0]);
            input = CharStreams.fromFileName(fileName);
        }else if(args.length > 1 || args.length < 0){
            throw  new Exception("the number of arguments is false, Please only give the source file or nothing and then you input your text");
        }else {
            InputStream is = System.in;
            input = CharStreams.fromStream(is);
        }
        ConfgLexer lexer=new ConfgLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ConfgParser parser = new ConfgParser(tokens);
        ParseTree tree = parser.prog(); 
        EvalVisitor eval = new EvalVisitor();

        ArrayList<String[]> ipv4routelist = new ArrayList<String[]>();
        ArrayList<String[]> ipv6routelist = new ArrayList<String[]>();
        eval.visit(tree);
        ipv4routelist = eval.ipv4routes;
        // for(int i = 0; i < ipv4routelist.size(); i++)
        // {
        //   for(int j = 0; j < ipv4routelist.get(i).length; j++)
        //   {
        //     writefile(datetime,ipv4routelist.get(i)[j] + "\r\n");
        //   }
        // }
        
        // for(int i = 0; i < ipv6routelist.size(); i++)
        // {
        //   for(int j = 0; j < ipv6routelist.get(i).length; j++)
        //   {
        //     System.out.println(ipv6routelist.get(i)[j]);
        //   }
        // }
        ipv6routelist = eval.ipv6routes;
        MatchIPv4 match_v4 = new MatchIPv4();
        MatchIPv6 match_v6 = new MatchIPv6();
        ArrayList<String[]> ipv4addresswaitforinsert = new ArrayList<String[]>();
        ArrayList<String[]> ipv6addresswaitforinsert = new ArrayList<String[]>();

        try{
            List<Map<String,String>> list = new ArrayList<Map<String, String>>();
            list = Speedlog.GetList();

            for (Map<String, String> item:list){
                for (Entry<String, String> entry : item.entrySet()){
                    String[] iprouteString = new String[]{entry.getKey(), entry.getValue()};
                    
                    int dotlocation0 = iprouteString[0].indexOf('.');
                    int colonlocation0 = iprouteString[0].indexOf(':');
                    int dotlocation1 = iprouteString[1].indexOf('.');
                    int colonlocation1 = iprouteString[1].indexOf(':');

                    if(dotlocation0 > 0 && dotlocation1 > 0)
                    {
                      ipv4addresswaitforinsert.add(iprouteString);
                    }
                    if(colonlocation0 > 0 && colonlocation1 > 0)
                    {
                      ipv6addresswaitforinsert.add(iprouteString);
                    }
                    
                }
            }
        }catch(Exception e){  
            e.printStackTrace();
        }
        
        match_v4.MatchIpv4(ipv4routelist, ipv4addresswaitforinsert);
        match_v6.MatchIpv6(ipv6routelist, ipv6addresswaitforinsert);
        

        for(int i = 0;i < match_v4.ipv4routesbeforechanged.size();i++)
        {
          writefile(datetime,"no ip route ");
          for(int j = 0 ; j < match_v4.ipv4routesbeforechanged.get(i).length;j++)
          {
            writefile(datetime,match_v4.ipv4routesbeforechanged.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
        }

        for(int i = 0;i < match_v4.ipv4routeschanged.size();i++)
        {
          writefile(datetime,"ip route ");
          for(int j = 0 ; j < match_v4.ipv4routeschanged.get(i).length;j++)
          {
            writefile(datetime,match_v4.ipv4routeschanged.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
      	}
        //writefile(datetime,"------\r\n");
        
        for(int i = 0;i < match_v4.ipv4routesadded.size();i++)
        {
          writefile(datetime,"ip route ");
          for(int j = 0 ; j < match_v4.ipv4routesadded.get(i).length;j++)
          {
            writefile(datetime,match_v4.ipv4routesadded.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
        }

        for(int i = 0;i < match_v6.ipv6routesbeforechanged.size();i++)
        {
          writefile(datetime,"no ip route ");
          for(int j = 0 ; j < match_v6.ipv6routesbeforechanged.get(i).length;j++)
          {
            writefile(datetime,match_v6.ipv6routesbeforechanged.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
        }

        for(int i = 0;i < match_v6.ipv6routeschanged.size();i++)
        {
          writefile(datetime,"ip route ");
          for(int j = 0 ; j < match_v6.ipv6routeschanged.get(i).length;j++)
          {
            writefile(datetime,match_v6.ipv6routeschanged.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
        }
        //writefile(datetime,"------\r\n");
        
        for(int i = 0;i < match_v6.ipv6routesadded.size();i++)
        {
          writefile(datetime,"ip route ");
          for(int j = 0 ; j < match_v6.ipv6routesadded.get(i).length;j++)
          {
            writefile(datetime,match_v6.ipv6routesadded.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
        }

    }

    public static void writefile(String filename, String content) {   
        FileWriter writer = null;  
        try {
            String filePath = "./output"+filename+".txt";
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(filePath, true);     
            writer.write(content);       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    }     

}
