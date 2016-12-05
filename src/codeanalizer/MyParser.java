package codeanalizer;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MyParser {
	private String code;

	public MyParser(String code) {
		this.code = code;
	}

	/**
	 * 変数の寿命を行数で返す <br>
	 * 空行は読み飛ばすが， コメント行は1行として数える<br>
	 * エラーがあれば-1を返す
	 * 
	 * @param variable
	 * @return
	 */
	int lifeSpanOf(VariableDeclarationFragment variable) {
		int start = variable.getStartPosition();
		boolean b = false;
		if(variable.getName().getIdentifier().equals("n")) {
			// System.err.println(code);
			// System.err.println(variable);
			b = true;
		}
		for(int i = 0, open = 0, close = 0, lines = 0; i < code.length(); i++) {
			// if(b) System.err.println(code.charAt(start + i));
			switch(code.charAt(start + i)) {
			case '{':
				open++;
				break;
			case '}':
				close++;
				break;
			case '\n':
				lines++;
				break;
			}
			if(close > open) return lines;
		}
		return -1;
	}

	/**
	 * 文字列で与えられたコードから空行を削除して返す
	 * 
	 * @param code
	 * @return
	 */
	static String removeBlankLines(String code) {
		return removeMatchedLine(code, "^\\s*$");
	}

	/**
	 * 与えられたコードから正規表現regexに一致する行を削除して返す
	 * 
	 * @param code
	 * @param regex
	 * @return
	 */
	static String removeMatchedLine(String code, String regex) {
		String lines[] = code.split("\n");
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < lines.length; i++) {
			if(!lines[i].matches(regex)) {
				sb.append(lines[i] + '\n');
			}
		}
		return sb.toString();
	}

	/**
	 * 与えられたコードからコメント行を削除
	 * 
	 * @param code
	 * @return
	 */
	static String removeCommentLines(String code) {
		return removeMatchedLine(code, "^\\s*//.*");
	}

	static String format(String code) {
		return removeBlankLines(removeCommentLines(code));
	}
}
