import java.io.*;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.*;

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

			MethodFindVisitor mfv = new MethodFindVisitor("main");
			unit.accept(mfv);
			MethodDeclaration method = mfv.getFoundMethod();
			if(method != null) {
				System.out.printf("可視性等  =%s%n", method.modifiers());
				System.out.printf("戻り型    =%s%n", method.getReturnType2());
				System.out.printf("メソッド名=%s%n", method.getName().getIdentifier());
				System.out.printf("引数      =%s%n", method.parameters());
				System.out.printf("本体      =%s%n", method.getBody());
			}
			
			VariableFindVisitor vfv = new VariableFindVisitor("hello");
			unit.accept(vfv);
			VariableDeclaration variable = vfv.getFoundVariable();
			if(variable != null) {
				System.out.printf("変数名   =%s%n", variable.getName().getIdentifier());
				System.out.printf("開始行   =%s%n", unit.getLineNumber(variable.getStartPosition()));
				System.out.printf("初期化子  =%s%n", variable.getInitializer());
			}

		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}
}
