import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MyParser {
	private String code;

	public MyParser(String code) {
		this.code = code;
	}

	/**
	 * 変数の寿命を行数で返す <br>
	 * コメント行や空行も1行として数える<br>
	 * エラーがあれば-1を返す
	 * 
	 * @param variable
	 * @return
	 */
	int lifeSpanOf(VariableDeclarationFragment variable) {
		int start = variable.getStartPosition();
		for(int i = 0, left = 0, right = 0, lines = 0; i < code.length(); i++) {
			switch(code.charAt(start + i)) {
			case '{':
				left++;
				break;
			case '}':
				right++;
				break;
			case '\n':
				lines++;
			}
			if(right > left)
				return lines;
		}
		return -1;
	}
}
