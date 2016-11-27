package codeanalizer;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
	static String readSourceCode(String path) throws IOException {
		FileReader fr = new FileReader(new File(path));
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		String buf;
		while((buf = br.readLine()) != null) {
			sb.append(buf);
			sb.append('\n');
		}

		br.close();
		return sb.toString();
	}

	static List<String> getSourceCodeList(String pathToPackage) {
		return Arrays.asList((new File(pathToPackage)).list());
	}
}
