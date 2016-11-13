import org.eclipse.jdt.core.dom.*;

public class VariableFindVisitor extends ASTVisitor {
	private String variableName;

	private VariableDeclaration variable;
	public VariableFindVisitor(String variableName) {
		this.variableName = variableName;
	}
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		String name = node.getName().getIdentifier();
		if (name.equals(this.variableName)) {
			this.variable = node;
		}
		return false;
	}
	public VariableDeclaration getFoundVariable() {
		return this.variable;
	}
}
