package codeanalizer;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.*;

public class CodeAnalizer {

	public static void main(String args[]) {
		new CodeAnalizer().run("res/");
		//new CodeAnalizer().run("src/codeanalizer/");
	}

	public void run(String pathToPackage) {
		List<String> classList = FileUtil.getSourceCodeList(pathToPackage);
		System.out.println("number of classes in the package: " + classList.size());

		for(String className : classList) {
			System.out.println("\n" + className + "\n");
			run(pathToPackage, className);
		}
	}
	public void run(String pathToPackage, String className) {
			String rawCode = FileUtil.readSourceCode(pathToPackage + className);

			if(rawCode == null) return;

			String formattedCode = MyParser.format(rawCode);

			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setSource(formattedCode.toCharArray());
			CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());

			MyVisitor visitor = new MyVisitor(formattedCode);
			unit.accept(visitor);

			setLineNum(visitor, rawCode);
			/*
			printMethodDetail(unit, formattedCode);
			printVariableDetail(unit, formattedCode);
			 */
			
			showWarning(unit, formattedCode);
			
			//System.out.println("Cyclomatic complexity: " + visitor.totalCyclomaticComplexity());
		}
	

	private static void printMethodDetail(CompilationUnit unit, String code) {
		MyVisitor visitor = new MyVisitor(code);
		unit.accept(visitor);
		for(MethodDeclaration method : visitor.getMethodList()) {
			if(method != null) {
				System.out.printf("可視性等  =%s%n", method.modifiers());
				System.out.printf("戻り型    =%s%n", method.getReturnType2());
				System.out.printf("メソッド名=%s%n", method.getName().getIdentifier());
				System.out.printf("引数      =%s%n", method.parameters());
				// System.out.printf("本体 =%s%n", method.getBody());
				System.out.printf("行数 =%s%n", method.properties().get(MyVisitor.LINE_COUNT));
				System.out.printf("McCabe=%s%n", method.properties().get(MyVisitor.CYCLOMATIC_COMPLEXITY));
				System.out.println();
			}
		}
	}

	private static void printVariableDetail(CompilationUnit unit, String code) {
		MyVisitor visitor = new MyVisitor(code);
		unit.accept(visitor);
		for(VariableDeclarationFragment variable : visitor.getVariableList()) {
			if(variable != null) {
				System.out.printf("変数名   =%s%n", variable.getName().getIdentifier());
				System.out.printf("開始行 =%s%n", variable.getProperty(MyVisitor.DECLARED_LINE));
				System.out.printf("初期化子  =%s%n", variable.getInitializer());
				System.out.printf("寿命=%s%n", variable.getProperty(MyVisitor.LIFE_SPAN));
				System.out.println(variable.getProperty(MyVisitor.DEFINITION_PLACE) instanceof MethodDeclaration
						? "ローカル変数" : "フィールド変数");
				System.out.println();
			}
		}
	}

	private void setLineNum(MyVisitor formattedVisitor, String rawCode) {
		MyVisitor visitor = new MyVisitor(rawCode);
		List<VariableDeclarationFragment> varList = formattedVisitor.getVariableList();

		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(rawCode.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
		unit.accept(visitor);

		for(int i = 0; i < varList.size(); i++) {
			varList.get(i).setProperty(MyVisitor.DECLARED_LINE,
					unit.getLineNumber(visitor.getVariableList().get(i).getStartPosition()));
		}
	}
	
	void showWarning(CompilationUnit unit, String code) {
		MyVisitor visitor = new MyVisitor(code);
		unit.accept(visitor);
		for(VariableDeclarationFragment variable : visitor.getVariableList()) {
			int lifespan = (int)variable.getProperty(MyVisitor.LIFE_SPAN);
			if(lifespan > 15 && variable.getProperty(MyVisitor.DEFINITION_PLACE) instanceof MethodDeclaration) {
				System.out.println(variable.getName() + "の寿命が長い(" + lifespan + "行)");
			}
		}
		System.out.println();
		for(MethodDeclaration method : visitor.getMethodList()) {
			int line = (int)method.getProperty(MyVisitor.LINE_COUNT);
			if(line > 15) {
				System.out.println(method.getName() + "の行数が長い(" + line + "行)");
			}
			int mcCabe = (int)method.getProperty(MyVisitor.CYCLOMATIC_COMPLEXITY);
			if(mcCabe > 10) {
				System.out.println(method.getName() + "のサイクロマチック数が大きい(" + mcCabe + ")");
			}
		}
	}
}
