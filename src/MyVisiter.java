import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MyVisiter extends ASTVisitor {
	@Override
	public boolean visit(MethodDeclaration node) {
		// 処理
		return super.visit(node);
	}
}
