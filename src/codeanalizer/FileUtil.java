package codeanalizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {

	static String readSourceCode(String path) {
		try(Stream<String> lines = Files.lines(Paths.get(path))) {
			StringBuilder sb = new StringBuilder();
			lines.forEach(s -> sb.append(s).append('\n'));
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
