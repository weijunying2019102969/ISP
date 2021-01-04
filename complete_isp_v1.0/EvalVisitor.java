import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

public class EvalVisitor extends ConfgBaseVisitor<Double>{
    Map<String, Double> memory = new HashMap<String, Double>();

    public ArrayList<String[]> iproutes = new ArrayList<String[]>();

    @Override
    public Double visitPrint(ConfgParser.PrintContext ctx){
        int size = ctx.getChildCount();
        String[] iproute = new String[4];
        if(size > 1)
        {
            String first_ipaddress = "";
            String netmask = "";
            String next_ipaddress = "";
            String priority = "";
            if(size == 3){
                for(int i = 0; i < size; i = i+1)
                    {
                        if(i == 0)
                        {
                            first_ipaddress = ctx.getChild(i).getText();
                        }
                        else if(i == 1)
                        {
                            netmask = ctx.getChild(i).getText();
                        }
                        else if(i == 2)
                        {
                            next_ipaddress = ctx.getChild(i).getText();
                        }
                    }
                    iproute[0] = first_ipaddress;
                    iproute[1] = netmask;
                    iproute[2] = next_ipaddress;
                    iproute[3] = priority;
            }
            //if expr has two children
            else if(size == 2)
            {
                //get its left child
                for(int j = 0; j < ctx.getChild(0).getChildCount(); j = j+1)
                {
                    if(j == 0)
                    {
                        first_ipaddress = ctx.getChild(0).getChild(j).getText();
                    }
                    else if(j == 1)
                    {
                        //netmask = ctx.getChild(i).getChild(j);
                        //GetLastipaddress last = new GetLastipaddress();
                        netmask = ctx.getChild(0).getChild(j).getText();
                        //netmask = last.getLastipaddress(ctx.getChild(0).getChild(0).getText(),ctx.getChild(0).getChild(1).getText());
                    }
                    else if(j == 2)
                    {
                        next_ipaddress = ctx.getChild(0).getChild(j).getText();
                    }
                }
                priority = ctx.getChild(1).getText();
                iproute[0] = first_ipaddress;
                iproute[1] = netmask;
                iproute[2] = next_ipaddress;
                iproute[3] = priority;
            }
            iproutes.add(iproute);
            //System.out.println(Arrays.toString(iproute));
        }
        return 0.0;
    }

    
}