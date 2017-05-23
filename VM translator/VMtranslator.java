/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vmtranslator;

import java.io.*;
import java.util.*;
import java.lang.*;

/**
 *
 * @author Lokesh
 */
class CodeWriter
{
    static int i = 0;
    static int r = 0;
    static int rd = 0;

    void WriteBoot(String infile,String output)throws IOException
    {
        FileWriter out = new FileWriter(output,true);
        BufferedWriter outF = new BufferedWriter(out);
        
        outF.write("//bootstrap code"); outF.newLine();
        
        outF.write("@256");outF.newLine();
        outF.write("D = A");outF.newLine();
        outF.write("@SP");outF.newLine();
        outF.write("M = D");outF.newLine();
        
        outF.write("@"+infile+"$"+"Sys.init");outF.newLine();
        outF.write("0;JMP");outF.newLine();
        
        outF.flush();
        outF.close();
        
    }
    void WriteArith(String output,String str)throws IOException
    {
        FileWriter out = new FileWriter(output,true);
        BufferedWriter outF = new BufferedWriter(out);
        
        outF.write("//Airthmatic code"); outF.newLine();
        
        if(str.contains("add")||str.contains("and")||str.contains("or"))
        {
            outF.write("@SP");  outF.newLine();
            outF.write("AM = M - 1");  outF.newLine();
            outF.write("D = M");  outF.newLine();
            outF.write("A = A - 1");  outF.newLine();
            if(str.contains("add"))
                outF.write("M = D + M");
            else if(str.contains("and"))
                outF.write("M = D&M");
            else if(str.contains("or"))
                outF.write("M = D|M");
            outF.newLine();
        }
        else if(str.contains("sub"))
        {
            outF.write("@SP");  outF.newLine();
            outF.write("AM = M - 1");  outF.newLine();
            outF.write("D = M");  outF.newLine();
            outF.write("A = A - 1");  outF.newLine();
            outF.write("M = M - D");  outF.newLine();
        }
        else if(str.contains("not")||str.contains("neg"))
        {
            outF.write("@SP");        outF.newLine();
            outF.write("A = M - 1");  outF.newLine();
            if(str.contains("not"))
                outF.write("M = !M");
            else
                outF.write("M = -M");
            
            outF.newLine();
            
        }
        else if(str.contains("eq")||str.contains("gt")||str.contains("lt"))
        {
            outF.write("@SP");outF.newLine();
            outF.write("AM= M - 1");outF.newLine();
            outF.write("D = M");  outF.newLine();
            outF.write("A = A - 1");  outF.newLine();
            outF.write("D = M - D");  outF.newLine();
            outF.write("@CMP"+i);  outF.newLine();
            if(str.contains("eq"))
                 outF.write("D;JEQ");
            else if(str.contains("gt"))
                outF.write("D;JGT");
            else if(str.contains("lt"))
                outF.write("D;JLT");
            
            outF.newLine();
            outF.write("@SP");  outF.newLine();
            outF.write("A = M - 1");  outF.newLine();
            outF.write("M = 0");  outF.newLine();
            outF.write("@E"+i);  outF.newLine();
            outF.write("0;JMP");  outF.newLine();
            outF.write("(CMP"+i+")");  outF.newLine();
            outF.write("@SP");  outF.newLine();
            outF.write("A = M - 1");  outF.newLine();
            outF.write("M = -1");  outF.newLine();
            outF.write("(E"+i+")");  outF.newLine();
            
            i++;
        }
        
        outF.flush();
        outF.close();
    }
    
    void help_push(String infile,String outfile,String index,String segment)throws IOException
    {
        FileWriter out = new FileWriter(outfile,true);
        BufferedWriter outF = new BufferedWriter(out);
        
        outF.write("//push code"); outF.newLine();
        
        if(segment.equals("constant"))
        {
            outF.write("@"+index);outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
        }
        else if(segment.equals("static"))
        {
            String s = infile+"."+index;
            
            outF.write("@"+s);outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
        }
        else if(segment.equals("temp")||segment.equals("R3"))
        {
            int tm = 0;
            
            if(segment.equals("temp"))
                tm = 5 + Integer.parseInt(index);
            else
                tm = 3 + Integer.parseInt(index);
            
            outF.write("@R"+tm);outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
        }
        else
        {
            outF.write("@"+index);outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@"+segment);outF.newLine();
            outF.write("A = M + D");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
            
        }
        
        outF.flush();
        outF.close();
    }
    
    void WritePush(String infile,String outfile,String str)throws IOException
    {
        str = str.replaceAll("\\s","");
        
        if(str.contains("constant"))
        {
            String n = str.substring(12);
            help_push(infile,outfile,n,"constant");
        }
        else if(str.contains("static"))
        {
            String n = str.substring(10);
            help_push(infile,outfile,n,"static");
        }
        else if(str.contains("local"))
        {
            String n = str.substring(9);
            help_push(infile,outfile,n,"LCL");
        }
        else if(str.contains("argument"))
        {
            String n = str.substring(12);
            help_push(infile,outfile,n,"ARG");
        }
        else if(str.contains("temp"))
        {
            String n = str.substring(8);
            help_push(infile,outfile,n,"temp");
        }
        else if(str.contains("this"))
        {
            String n = str.substring(8);
            help_push(infile,outfile,n,"THIS");
        }
        else if(str.contains("that"))
        {
            String n = str.substring(8);
            help_push(infile,outfile,n,"THAT");
        }
        else if(str.contains("pointer"))
        {
            String n = str.substring(11);
            help_push(infile,outfile,n,"R3");
        }
        
    }
    
    void help_pop(String infile,String outfile,String index,String segment)throws IOException
    {
        
        FileWriter out = new FileWriter(outfile,true);
        BufferedWriter outF = new BufferedWriter(out);
        
        outF.write("//pop code"); outF.newLine();
        
        if(segment.equals("static"))
        {
            String s = infile+"."+index;
            
            outF.write("@SP");outF.newLine();
            outF.write("AM = M - 1");outF.newLine();    
            outF.write("D = M");outF.newLine();
            outF.write("@"+s);outF.newLine();
            outF.write("M = D");outF.newLine();
           
        }
        else if(segment.equals("temp")||segment.equals("R3"))
        {
            int tm = 0;
            
            if(segment.equals("temp"))
                tm = 5 + Integer.parseInt(index);
            else
                tm = 3 + Integer.parseInt(index);
            
            outF.write("@SP");outF.newLine();
            outF.write("AM = M - 1");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@R"+tm);outF.newLine();
            outF.write("M = D");outF.newLine();
        }
        else
        {
            outF.write("@"+index);outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@"+segment);outF.newLine();
            outF.write("D = M + D");outF.newLine();
            outF.write("@P");outF.newLine();
            outF.write("M = D");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("AM = M - 1");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@P");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D");outF.newLine();
            
         
        }
        
        outF.flush();
        outF.close();
    }
    
    void WritePop(String infile,String outfile,String str)throws IOException
    {
        str = str.replaceAll("\\s","");
        
        if(str.contains("static"))
        {
            String n = str.substring(9);
            help_pop(infile,outfile,n,"static");
        }
        else if(str.contains("local"))
        {
            String n = str.substring(8);
            help_pop(infile,outfile,n,"LCL");
        }
        else if(str.contains("argument"))
        {
            String n = str.substring(11);
            help_pop(infile,outfile,n,"ARG");
        }
        else if(str.contains("temp"))
        {
            String n = str.substring(7);
            help_pop(infile,outfile,n,"temp");
        }
        else if(str.contains("this"))
        {
            String n = str.substring(7);
            help_pop(infile,outfile,n,"THIS");
        }
        else if(str.contains("that"))
        {
            String n = str.substring(7);
            help_pop(infile,outfile,n,"THAT");
        }
        else if(str.contains("pointer"))
        {
            String n = str.substring(10);
            help_pop(infile,outfile,n,"R3");
        }
        
    }
    
    void WriteGoto(String infile,String outfile,String str)throws IOException
    {
        str = str.replaceAll("\\s","");
        
        FileWriter out = new FileWriter(outfile,true);
        BufferedWriter outF = new BufferedWriter(out);
        
        String n = str.substring(4);
        outF.write("//goto code"); outF.newLine();
        outF.write("@"+infile+"$"+n);outF.newLine();
        outF.write("0;JMP");outF.newLine();
        
        outF.flush();
        outF.close();
    }
    
    void WriteIF(String infile,String outfile,String str)throws IOException
    {
        str = str.replaceAll("\\s","");
        
        FileWriter out = new FileWriter(outfile,true);
        BufferedWriter outF = new BufferedWriter(out);
        
        String n = str.substring(7);
        outF.write("//if goto code"); outF.newLine();
        outF.write("@SP");outF.newLine();
        outF.write("AM = M - 1");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@"+infile+"$"+n);outF.newLine();
        outF.write("D;JLT");outF.newLine();
        
        outF.flush();
        outF.close();
        
    }
    
    void WriteLabel(String infile,String outfile,String str)throws IOException
    {
        str = str.replaceAll("\\s","");
        
        FileWriter out = new FileWriter(outfile,true);
        BufferedWriter outF = new BufferedWriter(out);
         
        String n = str.substring(5);
        
        outF.write("("+infile+"$"+n+")");outF.newLine();
        
        outF.flush();
        outF.close();
    }
    
    void WriteFun(String infile,String outfile,String str)throws IOException
    {
        int j = str.indexOf(" ");
        
        int k = str.lastIndexOf(" ");
        
        if(k!= -1 && j!= -1)
        {
            String Fname = str.substring(j+1, k);
            int lcl = Integer.parseInt(str.substring(k+1));
            WriteLabel(infile,outfile,"label "+Fname);
            
            while(lcl>0)
            {
                WritePush(infile,outfile,"push constant 0");
                lcl--;
            }
            
        }
       
    }
    
    void WriteCall(String infile,String outfile,String str)throws IOException
    {
        FileWriter out = new FileWriter(outfile,true);
        BufferedWriter outF = new BufferedWriter(out);
      
        int j = str.indexOf(" ");
        
        int k = str.lastIndexOf(" ");
        
        if(k!= -1 && j!= -1)
        {
            String Fname = str.substring(j+1, k);
            int arg = Integer.parseInt(str.substring(k+1));
            
            outF.write("//call fn"); outF.newLine();
            
            outF.write("@"+infile+"$"+Fname+".ReturnA"+rd);outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
            
            outF.write("//fn call push local 0");outF.newLine();
            
            outF.write("@0");outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@LCL");outF.newLine();
            outF.write("A = M + D");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
            
            
            outF.write("//fn call push arg 0");outF.newLine();
            
            outF.write("@0");outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@ARG");outF.newLine();
            outF.write("A = M + D");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
            
            
            outF.write("//fn call push this 0");outF.newLine();
            
            outF.write("@0");outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@THIS");outF.newLine();
            outF.write("A = M + D");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
            
            
            outF.write("//fn call push that 0");outF.newLine();
            
            outF.write("@0");outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@THAT");outF.newLine();
            outF.write("A = M + D");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D ");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("M = M + 1");outF.newLine();
            
            
            //arg = sp-n-5
            outF.write("@SP");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@5");outF.newLine();
            outF.write("D = D - A");outF.newLine();
            outF.write("@"+arg);outF.newLine();
            outF.write("D = D - A");outF.newLine();
            outF.write("@ARG");outF.newLine();
            outF.write("M = D");outF.newLine();
            
            //lcl = sp
            outF.write("@SP");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@LCL");outF.newLine();
            outF.write("M = D");outF.newLine();
            
            
            outF.write("@"+infile+"$"+Fname);outF.newLine();
            outF.write("0;JMP");outF.newLine();
       
            outF.write("("+infile+"$"+Fname+".ReturnA"+rd+")");outF.newLine();
            
            rd++;
        }
        
        outF.flush();
        outF.close();
    }
    
    void WriteReturn(String infile,String outfile,String str)throws IOException
    {
        
        FileWriter out = new FileWriter(outfile,true);
        BufferedWriter outF = new BufferedWriter(out);
      
        outF.write("//return fn code"); outF.newLine();
        
        outF.write("@LCL");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@frame"+r);outF.newLine();
        outF.write("M = D");outF.newLine();
        
        outF.write("@frame"+r);outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@5");outF.newLine();
        outF.write("D = D - A");outF.newLine();
        outF.write("@RET"+r);outF.newLine();
        outF.write("A = D");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@RET"+r);outF.newLine();
        outF.write("M = D");outF.newLine();
        
        
        
            outF.write("@0");outF.newLine();
            outF.write("D = A");outF.newLine();
            outF.write("@ARG");outF.newLine();
            outF.write("D = M + D");outF.newLine();
            outF.write("@P");outF.newLine();
            outF.write("M = D");outF.newLine();
            outF.write("@SP");outF.newLine();
            outF.write("AM = M - 1");outF.newLine();
            outF.write("D = M");outF.newLine();
            outF.write("@P");outF.newLine();
            outF.write("A = M");outF.newLine();
            outF.write("M = D");outF.newLine();
           
         
        outF.write("@ARG");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@SP");outF.newLine();
        outF.write("M = D + 1");outF.newLine();
        
        outF.write("@frame"+r);outF.newLine();
        outF.write("D = M - 1");outF.newLine();
        outF.write("@THAT");outF.newLine();
        outF.write("A = D");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@THAT");outF.newLine();
        outF.write("M = D");outF.newLine();
        
        outF.write("@frame"+r);outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@2");outF.newLine();
        outF.write("D = D - A");outF.newLine();
        outF.write("@THIS");outF.newLine();
        outF.write("A = D");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@THIS");outF.newLine();
        outF.write("M = D");outF.newLine();
        
        
        outF.write("@frame"+r);outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@3");outF.newLine();
        outF.write("D = D - A");outF.newLine();
        outF.write("@ARG");outF.newLine();
        outF.write("A = D");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@ARG");outF.newLine();
        outF.write("M = D");outF.newLine();
        
        
        outF.write("@frame"+r);outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@4");outF.newLine();
        outF.write("D = D - A");outF.newLine();
        outF.write("@LCL");outF.newLine();
        outF.write("A = D");outF.newLine();
        outF.write("D = M");outF.newLine();
        outF.write("@LCL");outF.newLine();
        outF.write("M = D");outF.newLine();
        outF.write("@RET"+r);outF.newLine();
        outF.write("A = M");outF.newLine();
        outF.write("0;JMP");outF.newLine();
        
        r++;
        outF.flush();
        outF.close();
    }
}



class Parser
{
    public static final int C_ARITH = 0;
    public static final int C_PUSH = 1;
    public static final int C_POP = 2;
    public static final int C_LABEL = 3;
    public static final int C_GOTO = 4;
    public static final int C_IF = 5;
    public static final int C_FUN = 6;
    public static final int C_RETURN = 7;
    public static final int C_CALL = 8;
  
    
    int commandType(String str)
    {   
        if(str.contains("add")||str.contains("sub")||str.contains("eq")||str.contains("gt")||str.contains("lt")||str.contains("and")||str.contains("or")||str.contains("not")||str.contains("neg"))
            return C_ARITH;
        else if(str.contains("push"))
            return C_PUSH;
        else if(str.contains("pop"))
            return C_POP;
        else if(str.contains("if-goto"))
            return C_IF;
        else if(str.contains("goto"))
            return C_GOTO;
        else if(str.contains("function"))
            return C_FUN;
        else if(str.contains("label"))
            return C_LABEL;
        else if(str.contains("return"))
            return C_RETURN;
        else if(str.contains("call"))
            return C_CALL;
        else
            return -1;
       
    }
    
    
    void input(String path,String File,String Folder)throws IOException
    {
        
        String infile = Folder ;
         
        System.out.println(File);
        
        FileReader in = new FileReader(path+"/"+File+".vm");
        BufferedReader inF  = new BufferedReader(in);
        
        
        String line ; 
        String output;
        CodeWriter W = new CodeWriter();
        
        if(File.equals("Sys")||File.equals("Main")||File.equals("Class1")||File.equals("Class2"))
                output = path+"/"+"output.asm";
        else
                output = path+"/"+File+".asm";
        
        FileWriter out = new FileWriter(output,true);
        BufferedWriter outF = new BufferedWriter(out);
        
        
        
        if( File.equals("Sys") ){
        
            W.WriteBoot(infile, output);
            //W.WriteFun(infile,output,"function Sys.init 0");
        } 
 
        outF.close();
        
        
        while((line = inF.readLine())!= null)
        {
            line = line.trim();
            
            int idx = line.indexOf("//");
           
            if(idx == 0)
                line = "";
            else if(idx != -1)
                line = line.substring(0, idx);
            
            line = line.trim();
            
            
            
            
            if(!line.isEmpty())
            {
                
                int com = commandType(line);
                
                if(com == C_ARITH)
                {
                     W.WriteArith(output, line);
                }
                else if( com == C_PUSH)
                {
                    W.WritePush(infile,output, line);
                }
                else if( com == C_POP)
                {
                    W.WritePop(infile,output, line);
                }
                else if(com == C_GOTO)
                {
                    W.WriteGoto(infile,output,line);
                }
                else if(com == C_IF)
                {
                    W.WriteIF(infile,output, line);
                }
                else if(com == C_LABEL)
                {
                    W.WriteLabel(infile,output, line);
                }
                else if(com == C_FUN)
                {
                    W.WriteFun(infile,output, line);
                }
                else if(com == C_CALL)
                {
                    W.WriteCall(infile,output,line);
                }
                else if(com == C_RETURN)
                {
                    W.WriteReturn(infile,output,line);
                }
            }
            
        }
    }
}


public class VMtranslator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Parser obj = new Parser();
        
        String File = "FibonacciElement";
            
        String path = "C:/Users/Lokesh/Desktop/nand2tetris/projects/VMtranslator/"+File;
        
        int flag =0;
        
    try
    {
        if(File.equals("FibonacciElement"))
        {
            obj.input(path,"Sys",File);
            System.out.println("next");
            obj.input(path,"Main",File);
        }
        else if(File.equals("NestedCall"))
        {
            obj.input(path,"Sys",File);
        }
        else if(File.equals("StaticsTest"))
        {
            obj.input(path,"Sys",File);
            obj.input(path,"Class1",File);
            obj.input(path,"Class2",File);
        }
        else
        {
            obj.input(path, File,File);
        }
   
    }
    catch(IOException e)
        {
            System.out.println("file error");
        }
        
        
    }
    
}
