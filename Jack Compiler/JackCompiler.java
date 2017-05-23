import java.io.File;



public class JackCompiler {

	public static void main(String[] args) {
		
		
		String name = "Main";
		File in = new File(name+".jack");
		File out = new File(name+".vm");

		 CompilationEngine compilationEngine = new CompilationEngine(in,out);
		 compilationEngine.compileClass();
	}

}
