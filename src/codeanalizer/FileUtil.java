package codeanalizer;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
	static String readSourceCode(String path) {
		try {
			FileReader fr = new FileReader(new File(path));
			BufferedReader br = new BufferedReader(fr);
			StringBuilder sb = new StringBuilder();

			br.lines().forEach(s -> sb.append(s).append('\n'));

			br.close();
			return sb.toString();
		} catch(IOException e) {
			System.err.println(e);
			return null;
		}
	}

	static List<String> getSourceCodeList(String pathToPackage) {
		return Arrays.asList((new File(pathToPackage)).list());
	}
}
