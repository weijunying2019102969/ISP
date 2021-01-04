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

public class route
{
      
      public static void main(String[] args) throws Exception {   
      

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

        ArrayList<String[]> iproutelist = new ArrayList<String[]>();
        eval.visit(tree);
        iproutelist = eval.iproutes;
       
        MatchandAdd match = new MatchandAdd();
        ArrayList<String[]> ipaddresswaitforinsert = new ArrayList<String[]>();

        try{
            List<Map<String,String>> list = new ArrayList<Map<String, String>>();
            list = Speedlog.GetList();

            for (Map<String, String> item:list){
                for (Entry<String, String> entry : item.entrySet()){
                    String[] iprouteString = new String[]{entry.getKey(), entry.getValue()};
                    ipaddresswaitforinsert.add(iprouteString);
                }
            }
        }catch(Exception e){  
            e.printStackTrace();
        }
        
        match.matchandadd(iproutelist, ipaddresswaitforinsert);
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd-HHmmss");  
        String datetime = tempDate.format(new java.util.Date());  

        for(int i = 0;i < match.iproutesbeforechanged.size();i++)
        {
          writefile(datetime,"no ip route ");
          for(int j = 0 ; j < match.iproutesbeforechanged.get(i).length;j++)
          {
            writefile(datetime,match.iproutesbeforechanged.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
        }

        for(int i = 0;i < match.iprouteschanged.size();i++)
        {
         writefile(datetime,"ip route ");
          for(int j = 0 ; j < match.iprouteschanged.get(i).length;j++)
          {
            writefile(datetime,match.iprouteschanged.get(i)[j]+" ");
          }
          writefile(datetime,"\r\n");
      	}
        //writefile(datetime,"------\r\n");
        
        for(int i = 0;i < match.iproutesadded.size();i++)
        {
          writefile(datetime,"ip route ");
          for(int j = 0 ; j < match.iproutesadded.get(i).length;j++)
          {
            writefile(datetime,match.iproutesadded.get(i)[j]+" ");
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
