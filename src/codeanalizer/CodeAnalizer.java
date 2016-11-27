package codeanalizer;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.*;

public class CodeAnalizer {
	
	public static void main(String args[]) {
		new CodeAnalizer().run("src/scopeanalizer/");
	}

	public void run(String pathToPackage) {
		List<String> classList = FileUtil.getSourceCodeList(pathToPackage);
		System.out.println("number of classes in the package: " + classList.size());

		for(String className : classList) {
			System.out.println("\n" + className + "\n");

			String code;
			try {
				code = FileUtil.readSourceCode(pathToPackage + className);
			} catch(IOException e) {
				System.out.println(e.toString());
				continue;
			}

			code = MyParser.format(code);
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setSource(code.toCharArray());
			CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());

			unit.accept(new MyVisitor());

			printMethodDetail(unit);
			printVariableDetail(unit, new MyParser(code));
		}
	}
	
	private static void printMethodDetail(CompilationUnit unit) {
		MyVisitor visitor = new MyVisitor();
		unit.accept(visitor);
		for(MethodDeclaration method : visitor.getMethodList()) {
			if(method != null) {
				System.out.printf("可視性等  =%s%n", method.modifiers());
				System.out.printf("戻り型    =%s%n", method.getReturnType2());
				System.out.printf("メソッド名=%s%n", method.getName().getIdentifier());
				System.out.printf("引数      =%s%n", method.parameters());
				//System.out.printf("本体      =%s%n", method.getBody());
				System.out.printf("行数 =%s%n", method.properties().get(MyVisitor.LINE_COUNT));
				System.out.println();
			}
		}
	}
	private static void printVariableDetail(CompilationUnit unit, MyParser parser) {
		MyVisitor visitor = new MyVisitor();
		unit.accept(visitor);
		for(VariableDeclarationFragment variable : visitor.getVariableList()) {
			if(variable != null) {
				System.out.printf("変数名   =%s%n", variable.getName().getIdentifier());
				System.out.printf("開始行   =%s%n", unit.getLineNumber(variable.getStartPosition()));
				System.out.printf("初期化子  =%s%n", variable.getInitializer());
				System.out.printf("寿命 =%s%n", parser.lifeSpanOf(variable));
				System.out.println();
			}
		}
	}

}
