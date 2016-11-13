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
				System.out.printf("������  =%s%n", method.modifiers());
				System.out.printf("�߂�^    =%s%n", method.getReturnType2());
				System.out.printf("���\�b�h��=%s%n", method.getName().getIdentifier());
				System.out.printf("����      =%s%n", method.parameters());
				System.out.printf("�{��      =%s%n", method.getBody());
			}
			
			VariableFindVisitor vfv = new VariableFindVisitor("hello");
			unit.accept(vfv);
			VariableDeclaration variable = vfv.getFoundVariable();
			if(variable != null) {
				System.out.printf("�ϐ���   =%s%n", variable.getName().getIdentifier());
				System.out.printf("�J�n�s   =%s%n", unit.getLineNumber(variable.getStartPosition()));
				System.out.printf("�������q  =%s%n", variable.getInitializer());
			}

		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}
}
