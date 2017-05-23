/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.*;
import java.util.*;


/**
 * @author Lokesh Koshale
 * CS15B049
 */

class Symbol
{
    String sym;
    String val;
}


class SymbolTable
{
    LinkedList ll = new LinkedList();
    
    String intTobin(String s)
    {
        String out;
        
        int dec = Integer.parseInt(s);
        out = Integer.toBinaryString(dec);
        int len = out.length();
            
        String bits = "0000000000000000";
            
        if( len < 16)
            out = bits.substring(0,16-len).concat(out);
        else 
            out = out.substring(len-16);
            
        return out;
    }
    
    void Add(String sym,String val)
    {
        Symbol a = new Symbol();
        a.sym = sym;
        a.val = intTobin(val);
        ll.add(a);   
    }
    
    void AddDefault()
    {
        Add("SP","0");
        Add("LCL","1");
        Add("ARG","2");
        Add("THIS","3");
        Add("THAT","4");
        Add("R0","0");
        Add("R1","1");
        Add("R2","2");
        Add("R3","3");
        Add("R4","4");
        Add("R5","5");
        Add("R6","6");
        Add("R7","7");
        Add("R8","8");
        Add("R9","9");
        Add("R10","10");
        Add("R11","11");
        Add("R12","12");
        Add("R13","13");
        Add("R14","14");
        Add("R15","15");
        Add("SCREEN","16384");
        Add("KBD","24576");
    }
    
    String Search(String s)
    {
        String out = null;
        
        for(int i=0;i<ll.size();i++)
        {
            Symbol a;
            a = (Symbol) ll.get(i);
            
            if(a.sym.equals(s))
            {
                out = a.val;
                break;
            }
        }
        
        return out;
    }
    
}


class Code
{
    String Dest(String str)
    {
        String out = "000";
        
        if(str.equals("M"))
            out = "001";
        else if(str.equals("D"))
            out = "010";
        else if(str.equals("MD"))
            out = "011";
        else if(str.equals("A"))
            out = "100";
        else if(str.equals("AM"))
            out = "101";
        else if(str.equals("AD"))
            out = "110";
        else if(str.equals("AMD"))
            out = "111";
        
        return out;
                        
    }
    
    String Comp(String str)
    {
        String out = null;
        
        if(str.equals("0"))
            out = "0101010";
        else if(str.equals("1"))
            out = "0111111";
        else if(str.equals("-1"))
            out = "0111010";
        else if(str.equals("D"))
            out = "0001100";
        else if(str.equals("A"))
            out = "0110000";
        else if(str.equals("!D"))
            out = "0001101";
        else if(str.equals("!A"))
            out = "0110001";
        else if(str.equals("-D"))
            out = "0001111";
        else if(str.equals("-A"))
            out = "0110011";
        else if(str.endsWith("D+1"))
            out = "0011111";
        else if(str.equals("A+1"))
            out = "0110111";
        else if(str.equals("D-1"))
            out = "0001110";
        else if(str.equals("A-1"))
            out = "0110010";
        else if(str.equals("D+A"))
            out = "0000010";
        else if(str.equals("D-A"))
            out = "0010011";
        else if(str.equals("A-D"))
            out = "0000111";
        else if(str.equals("D&A"))
            out = "0000000";
        else if(str.equals("D|A"))
            out = "0010101";                    //end of a=0;
        else if(str.equals("M"))
            out = "1110000";
        else if(str.equals("!M"))
            out = "1110001";
        else if(str.equals("-M"))
            out = "1110011";
        else if(str.equals("M+1"))
            out = "1110111";
        else if(str.equals("M-1"))
            out = "1110010";
        else if(str.equals("D+M"))
            out = "1000010";
        else if(str.equals("D-M"))
            out = "1010011";
        else if(str.equals("M-D"))
            out = "1000111";
        else if(str.equals("D&M"))
            out = "1000000";
        else if(str.equals("D|M"))
            out = "1010101";
        
        return out;
                    
    }
    
    String Jump(String str)
    {
         String out = "000";
        
        if(str.equals("JGT"))
            out = "001";
        else if(str.equals("JEQ"))
            out = "010";
        else if(str.equals("JGE"))
            out = "011";
        else if(str.equals("JLT"))
            out = "100";
        else if(str.equals("JNE"))
            out = "101";
        else if(str.equals("JLE"))
            out = "110";
        else if(str.equals("JMP"))
            out = "111";
        
        return out;
    }
    
}


class Parser extends SymbolTable
{
    public static final int A_COMMAND = 0;
    public static final int C_COMMAND = 1;
    public static final int L_COMMAND = 2;
    public static final int COMMENT = 3;
    
    int commandType(String str)
    {
        int x;
        
        if('@'==str.charAt(0))
        {
            x = A_COMMAND;
        }
        else if(str.charAt(0)=='(' && str.charAt(str.length()-1)==')' )
        {
            x = L_COMMAND;
        }
        else if(str.charAt(0)=='A'||str.charAt(0)=='D'||str.charAt(0)=='M'||str.charAt(0)=='0')
        {
            x = C_COMMAND;
        }
        else if(str.charAt(0)=='/' && str.charAt(1)=='/' )
        { 
            x = COMMENT;
        }
        else
        {
            x = -1;
            System.out.println("Invalid Command");
            System.exit(0);
        }
        
        return x;
    }
    
    String acom(String str)
    {
        String out = null;
        if(str.charAt(0) == '@')
        {
            out = intTobin(str.substring(1));
        }
        return out;
    }
    
    String comp(String str)
    {
        String out = null;
        int i = str.indexOf('=');
        int j = str.indexOf(';');
        int k = str.indexOf("//");
        
        if(i != -1 && k == -1)
            out = str.substring(i+1);
        else if(i !=-1 && k != -1)
            out = str.substring(i+1, k);
        else if(j != -1)
            out = str.substring(0, j);
        
        return out;
    }
 
    int DestOrJump(String str)
    {
        int x = -1;
        int i = str.indexOf('=');
        int j = str.indexOf(';');
        
        if(i != -1 )
        {
            x = 0 ;
        }
        else if(j != -1)
        {
            x = 1;
        }
        
        return x;
    }
    
    String dest(String str)
    {
        String out = null;
        int i = str.indexOf('=');
        if(i != -1)
        {
            out = str.substring(0, i);
        }
        
        return out;
    }
    
    String jump(String str)
    {
        String out = null;
        int i = str.indexOf(';');
        int j = str.indexOf("//");
        if(i != -1 && j!= -1)
        {
            out = str.substring(i+1,j);
        }
        else if( i!= -1 && j == -1)
            out = str.substring(i+1);
 
        return out;
    }
    
    
    boolean checkN(char ch)
    {
        return ch=='0'||ch=='1'||ch=='2'||ch=='3'||ch=='4'||ch=='5'||ch=='6'||ch=='7'||ch=='8'||ch=='9';
    }
    
    void pass1(String infile)throws IOException
    {
        AddDefault();
        
        FileReader in = new FileReader(infile);
        BufferedReader inF  = new BufferedReader(in);
       
        String line; 
        
        int no = 0;
        
        while((line = inF.readLine())!= null)
        {
            line = line.trim();
            line = line.replaceAll("\\s", "");
       
            
            if(!line.isEmpty())
            {
                int c = commandType(line);
                 
                if(c == A_COMMAND)
                {
                    no++;
                }
                else if( c == L_COMMAND)
                {
                    int len = line.length();
                    
                    String sno = Integer.toString(no);
                    
                    Add(line.substring(1,len-1),sno);
                
                }
                else if(c == C_COMMAND)
                    no++;
                
            }
            
        }
      
        inF.close();
        
    }
    
    
    void pass2(String infile) throws IOException
    {
        pass1(infile);
        
        FileReader in = new FileReader(infile);
        BufferedReader inF  = new BufferedReader(in);
       
        String line; 
        
        int var = 16;
        
        while((line = inF.readLine())!= null)
        {
            line = line.trim();
            line = line.replaceAll("\\s", "");
       
            if(!line.isEmpty())
            {
                int c = commandType(line);
                 
                if(c == A_COMMAND)
                {
                    if(!checkN(line.charAt(1)))
                    {
                        String ch = Search(line.substring(1));
                        if(ch == null)
                        {
                            String forVar = Integer.toString(var);
                            Add(line.substring(1),forVar);
                            var++;
                        }
                    }
                    
                }
            }
            
        }
      
        inF.close();
        
        
    }
    
    
   
    
    void input(String infile)throws IOException
    {
        pass2(infile);
        
        FileReader in = new FileReader(infile);
        BufferedReader inF  = new BufferedReader(in); 
        FileWriter out = new FileWriter("C:/Users/Lokesh/Desktop/nand2tetris/projects/06/Assembler/Prog.hack");
        BufferedWriter outF = new BufferedWriter(out);
       
        String line; 
        
        Code a = new Code();
       
        while((line = inF.readLine())!= null)
        {
            line = line.trim();
            line = line.replaceAll("\\s", "");
       
            if(!line.isEmpty())
            {   
                int c = commandType(line);
            
                
                if(c == A_COMMAND)
                {   
                    if(!checkN(line.charAt(1)))
                    {
                        String sym = Search(line.substring(1));
                        outF.write(sym);
                        outF.newLine();
                      
                    }
                    else
                    {
                        String Acom = acom(line);
                        outF.write(Acom);
                        outF.newLine();
                    }
                  
                }
                else if(c == C_COMMAND)
                {
                    String Comp = comp(line);
                    String Dest;
                    String Jump; 
                    String AComp = a.Comp(Comp);
                    String ADest = "000";
                    String AJump = "000";
                        
                   int DorJ = DestOrJump(line);
                    
                   if(DorJ == 0)
                    {
                        Dest = dest(line);
                        ADest = a.Dest(Dest);
                    }
                    else if(DorJ == 1)
                    {
                        Jump = jump(line);
                        AJump = a.Jump(Jump);
                    }
                
                    outF.write("111"+AComp+ADest+AJump);
                    outF.newLine();
                }
                else if(c == L_COMMAND)
                {
                    
                    
                }
                else if(c == COMMENT)
                {
                    
                }
            }
          
        }
       
        outF.flush();
        outF.close();
        inF.close();
    }
    
}


public class Assembler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Parser obj = new Parser();
        
        String path = "C:/Users/Lokesh/Desktop/nand2tetris/projects/06/Assembler/";
        try
        {
            obj.input(path+"Prog.asm");
        }
        catch(IOException e)
        {
            System.out.println("file error");
        }
        
    }
    
}
