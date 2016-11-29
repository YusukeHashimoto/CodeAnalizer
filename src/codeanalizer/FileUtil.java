package codeanalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
	static String readSourceCode(String path) {
		try {
			FileReader fr = new FileReader(new File(path));
			BufferedReader br = new BufferedReader(fr);
			StringBuilder sb = new StringBuilder();
			/*
					String buf;
					while((buf = br.readLine()) != null) {
						sb.append(buf);
						sb.append('\n');
					}
			*/
			for(String buf = br.readLine(); buf != null; buf = br.readLine()) {
				sb.append(buf);
				sb.append('\n');
			}

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
