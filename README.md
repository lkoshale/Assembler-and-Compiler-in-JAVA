# Assembler-and-Compiler-in-JAVA #

## What is JACK ##
 -  Jack - a simple, Java-like, object-based programming language. for more info ( http://www.nand2tetris.org/09.php )
### Hello world in jack: ###
<pre>/** Hello World program. */
  class Main {
   function void main () {
   // Prints some text using the standard library
     do Output.printString("Hello World");
      do Output.println(); // New line
      return;
     }
 }</pre>

## JackCompiler - ##
1. Compilation :- This takes a *.jack file/files (Written in high level programmimg language JACK) as input and gives the VM-code for the following file as *.vm multiple files for each classes in the *.jack file.
2. Error-Handling :- If there are some synatctical errors in the input file it gives you the line number and wnad what error ocuured in the console output.

## VM-translator ##
 - the VM-translator takes a *.vm file/files and outut a single *.hack file(Assembly language)
 - The Virtual machine is similar like JVM but a simpler one it is Stack based Virtual Machine
 
## What is HACK ##
- Hack is the Assembly level programming language for Hack hardware platform 

## Assembler ##
 - It converts the the Assembly language into the binary-code (0's and 1's) 
