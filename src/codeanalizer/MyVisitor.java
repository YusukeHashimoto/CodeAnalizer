package codeanalizer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MyVisitor extends ASTVisitor {
	private List<MethodDeclaration> methodList;
	private List<VariableDeclarationFragment> variableList;
	private List<Block> blockList;
	static final String LINE_COUNT = "line";
	static final String LIFE_SPAN = "life";
	static final String DECLARED_LINE = "declared_line";
	private MyParser parser;
	private int cyclomaticComplexity = 1;
	static String code;

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
		// if(parser == null) parser = new MyParser(node.getRoot().toString());
		if(parser == null) parser = new MyParser(code);
		// System.err.println(node.getRoot().equals(code));
		node.setProperty(LIFE_SPAN, parser.lifeSpanOf(node));
		variableList.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Block node) {
		blockList.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(IfStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(SwitchCase node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(WhileStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(ForStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);

	}

	@Override
	public boolean visit(CatchClause node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(DoStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	int totalCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	private static int countLines(String code) {
		int n = 0;
		for(char c : code.toCharArray()) {
			if(c == '\n') n++;
		}
		return n;
	}
}
