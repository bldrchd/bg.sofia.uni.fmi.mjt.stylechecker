package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Checks {

    File tempInput = new File("source.txt");
    private final String commentWildcards = "// FIXME Wildcards are not allowed in import statements\n";
    private final String commentOpeningBrackets = "// FIXME Opening brackets should be placed on the same line as the declaration\n";
    private final String commentOneLineStatement = "// FIXME Only one statement per line is allowed\n";
    private final String commentLineLimit = "// FIXME Length of line should not exceed 100 characters\n";

    public Checks() {
    }

    void wildcardImport() throws IOException {

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput))) {

	    String line;
	    List<String> fileContent = new ArrayList<>();

	    while ((line = br.readLine()) != null) {
		if ((line.trim().startsWith("import")) && (line.contains("*;"))) {
		    fileContent.add(commentWildcards.concat(line));
		    continue;
		}
		fileContent.add(line);
	    }
	    Files.write(tempInput.toPath(), fileContent);
	}
    }

    void statementsPerLine() throws IOException {

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput))) {

	    String line;
	    List<String> fileContent = new ArrayList<>();

	    while ((line = br.readLine()) != null) {
		line = line.trim();
		int index = line.indexOf(';');
		int index2 = line.indexOf(';', index + 1);
		if (index2 != -1) {
		    String appended = commentOneLineStatement.concat(line);
		    fileContent.add((appended));
		    continue;
		}
		fileContent.add((line));
	    }
	    Files.write(tempInput.toPath(), fileContent);
	}
    }

    void openingBrackets() throws IOException {

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput))) {

	    String line;
	    List<String> fileContent = new ArrayList<>();

	    while ((line = br.readLine()) != null) {
		String subStr = line.trim();
		if (subStr.startsWith("{")) {
		    String appended = commentOpeningBrackets.concat(line);
		    fileContent.add(appended);
		    continue;
		}
		fileContent.add(line);
	    }
	    Files.write(tempInput.toPath(), fileContent, StandardCharsets.UTF_8);
	}
    }

    void lenghtOfLine(int length) throws IOException {

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput))) {

	    String line;
	    List<String> fileContent = new ArrayList<>();

	    while ((line = br.readLine()) != null) {
		if (line.length() > length) {
		    String appended = commentLineLimit.concat(line);
		    fileContent.add(appended);
		    continue;
		}
		fileContent.add(line);
	    }
	    Files.write(tempInput.toPath(), fileContent, StandardCharsets.UTF_8);
	}
    }
}
