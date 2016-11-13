import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodFindVisitor extends ASTVisitor {
	private String methodName;

	private MethodDeclaration method;
	public MethodFindVisitor(String methodName) {
		this.methodName = methodName;
	}
	@Override
	public boolean visit(MethodDeclaration node) {
		String name = node.getName().getIdentifier();
		if (name.equals(this.methodName)) {
			this.method = node;
		}
		return false;
	}
	public MethodDeclaration getFoundMethod() {
		return this.method;
	}
}
