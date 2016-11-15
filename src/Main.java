import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Main {
	public static void main(String args[]) {
		String code;
		try {
			code = readSourceCode("src/Main.java");
		} catch(IOException e) {
			System.out.println(e.toString());
			return;
		}

		// Create AST Parser
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(code.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());

		MyVisitor visitor = new MyVisitor();
		unit.accept(visitor);

		for(MethodDeclaration method : visitor.getMethodList()) {
			if(method != null) {
				System.out.printf("可視性等  =%s%n", method.modifiers());
				System.out.printf("戻り型    =%s%n", method.getReturnType2());
				System.out.printf("メソッド名=%s%n", method.getName().getIdentifier());
				System.out.printf("引数      =%s%n", method.parameters());
				System.out.printf("本体      =%s%n", method.getBody());
			}
		}

		for(VariableDeclarationFragment variable : visitor.getVariableList()) {
			if(variable != null) {
				System.out.printf("変数名   =%s%n", variable.getName().getIdentifier());
				System.out.printf("開始行   =%s%n", unit.getLineNumber(variable.getStartPosition()));
				System.out.printf("初期化子  =%s%n", variable.getInitializer());
				System.out.println();
			}
		}

		System.out.println("Number of block: " + visitor.getBlockList().size());
	}

	private static String readSourceCode(String path) throws IOException {
		FileReader fr = new FileReader(new File(path));
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		String buf;
		while((buf = br.readLine()) != null) {
			sb.append(buf);
			sb.append('\n');
		}

		br.close();
		return sb.toString();
	}
}
