package codeanalizer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MyVisitor extends ASTVisitor {
	private List<MethodDeclaration> methodList;
	private List<VariableDeclarationFragment> variableList;
	private List<Block> blockList;
	static final String LINE_COUNT = "line";
	static final String LIFE_SPAN = "life";
	static final String DECLARED_LINE = "declared_line";
	private MyParser parser;

	public List<MethodDeclaration> getMethodList() {
		return methodList;
	}

	public List<VariableDeclarationFragment> getVariableList() {
		return variableList;
	}

	public List<Block> getBlockList() {
		return blockList;
	}

	MyVisitor() {
		methodList = new ArrayList<>();
		variableList = new ArrayList<>();
		blockList = new ArrayList<>();
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		node.setProperty(LINE_COUNT, Integer.valueOf(countLines(node.getBody().toString())));
		methodList.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		if(parser == null) parser = new MyParser(node.getRoot().toString());
		node.setProperty(LIFE_SPAN, parser.lifeSpanOf(node));
		variableList.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Block node) {
		blockList.add(node);
		return super.visit(node);
	}

	private static int countLines(String code) {
		int n = 0;
		for(char c : code.toCharArray()) {
			if(c == '\n') n++;
		}
		return n;
	}
}
