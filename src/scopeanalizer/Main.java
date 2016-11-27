package scopeanalizer;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.*;

public class Main {
	public static void main(String args[]) {
		String code;
		List<String> classList = getSourceCodeList("src/");
		System.out.println("number of classes in the package: " + classList.size());
		try {
			code = readSourceCode("src/Main.java");
		} catch(IOException e) {
			System.out.println(e.toString());
			return;
		}

		code = MyParser.format(code);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(code.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());

		MyVisitor visitor = new MyVisitor();
		unit.accept(visitor);

		MyParser myParser = new MyParser(code);

		for(MethodDeclaration method : visitor.getMethodList()) {
			if(method != null) {
				System.out.printf("可視性等  =%s%n", method.modifiers());
				System.out.printf("戻り型    =%s%n", method.getReturnType2());
				System.out.printf("メソッド名=%s%n", method.getName().getIdentifier());
				System.out.printf("引数      =%s%n", method.parameters());
				//System.out.printf("本体      =%s%n", method.getBody());
			}
		}

		for(VariableDeclarationFragment variable : visitor.getVariableList()) {
			if(variable != null) {
				System.out.printf("変数名   =%s%n", variable.getName().getIdentifier());
				System.out.printf("開始行   =%s%n", unit.getLineNumber(variable.getStartPosition()));
				System.out.printf("初期化子  =%s%n", variable.getInitializer());
				System.out.printf("寿命 =%s%n", myParser.lifeSpanOf(variable));
				System.out.println();
			}
		}

		System.out.println("Number of block: " + visitor.getBlockList().size());

		//System.out.println(code);
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

	private static List<String> getSourceCodeList(String pathToPackage) {
		return Arrays.asList((new File(pathToPackage)).list());
	}
}
