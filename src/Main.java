import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Main {
	public static void main(String args[]) {
		try {
			FileReader fr = new FileReader(new File("src/HelloWorld.java"));
			BufferedReader br = new BufferedReader(fr);
			StringBuilder sb = new StringBuilder();
			String buf;
			while((buf = br.readLine()) != null) {
				sb.append(buf);
				sb.append('\n');
			}
			// System.out.println(sb.toString());
			br.close();
			// Create AST Parser
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(sb.toString().toCharArray());
			CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());

			MyVisiter visitor = new MyVisiter();
			unit.accept(visitor);
			System.out.println(unit);

		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}
}
