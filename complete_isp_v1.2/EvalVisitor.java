import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

public class EvalVisitor extends ConfgBaseVisitor<Double>{
    Map<String, Double> memory = new HashMap<String, Double>();

    public ArrayList<String[]> ipv4routes = new ArrayList<String[]>();
    public ArrayList<String[]> ipv6routes = new ArrayList<String[]>();

    @Override
    public Double visitPrint(ConfgParser.PrintContext ctx){
        String[] iproute = new String[4];
        String[] ip_from_antlr4 = ctx.getChild(0).getText().split("[ \r\\/]");
        //for(int i = 0; i < ip_from_antlr4.length;i++)
            //System.out.println("ip_from_antlr4[" + i +"]: " + ip_from_antlr4[i]);
        if(ip_from_antlr4[0].equals("ip"))
        {

            iproute[0] = ip_from_antlr4[2];
            iproute[1] = ip_from_antlr4[3];
            iproute[2] = ip_from_antlr4[4];
            if(ip_from_antlr4.length > 5)
                iproute[3] = ip_from_antlr4[5];
            else
                iproute[3] = "";
            if(ip_from_antlr4[3].equals("0.0.0.0"))
            { 
            }
            else
                ipv4routes.add(iproute);
        }
        if(ip_from_antlr4[0].equals("ipv6"))
        {
            iproute[0] = ip_from_antlr4[2];
            iproute[1] = ip_from_antlr4[3];
            iproute[2] = ip_from_antlr4[4];
            iproute[3] = "";
            if(ip_from_antlr4[3].equals("0"))
            { 
            }
            else
                ipv6routes.add(iproute);
        }
        return 0.0;
    }

    
}