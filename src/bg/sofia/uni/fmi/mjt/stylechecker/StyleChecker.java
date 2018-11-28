package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * Used for static code checks of Java files.
 *
 * Depending on a stream from user-defined configuration or default values, it
 * checks if the following rules are applied:
 * <ul>
 * <li>there is only one statement per line;</li>
 * <li>the line lengths do not exceed 100 (or user-defined number of)
 * characters;</li>
 * <li>the import statements do not use wildcards;</li>
 * <li>each opening block bracket is on the same line as the declaration.</li>
 * </ul>
 */
public class StyleChecker {
    private Properties properties = null;

    /**
     * Creates a StyleChecker with properties having the following default
     * values:
     * <ul>
     * <li>{@code wildcard.import.check.active=true}</li>
     * <li>{@code statements.per.line.check.active=true}</li>
     * <li>{@code opening.bracket.check.active=true }</li>
     * <li>{@code length.of.line.check.active=true}</li>
     * <li>{@code line.length.limit=100}</li>
     * </ul>
     **/

    public StyleChecker() throws IOException {

	properties = new Properties();
	FileInputStream input = new FileInputStream("properties.cfg");
	properties.load(input);
	System.out.println(properties);

    }

    /**
     * Creates a StyleChecker with custom configuration, based on the content
     * from the given {@ inputStream}. If the stream does not contain any of the
     * properties, the missing ones get their default values.
     * 
     * @param inputStream
     * @throws IOException
     */
    public StyleChecker(InputStream inputStream) throws IOException {
	try (Reader defaultprops = new FileReader("properties.cfg")) {
	    properties = new Properties();
	    properties.load(defaultprops);
	    properties.load(inputStream);
	    if (properties.getProperty("length.of.line.check.active").equals("false"))
		properties.setProperty("line.length.limit", null);
	} finally {
	    inputStream.close();
	}
    }

    /**
     * For each line from the given {@code source} InputStream writes comment
     * for the violated rules (if any) with an explanation of the style error
     * followed by the line itself in the {@code output}.
     * 
     * @param sourceToRead
     * @param output
     * @throws IOException
     */
    public OutputStream checkStyle(InputStream sourceToRead, OutputStream output) throws IOException {
	int length = 0;

	inputToFile(sourceToRead);

	try {
	    Checks checks = new Checks();
	    if (Boolean.parseBoolean(properties.getProperty("wildcard.import.check.active"))) {
		checks.wildcardImport();
	    }
	    if (Boolean.parseBoolean(properties.getProperty("statements.per.line.check.active"))) {
		checks.statementsPerLine();
	    }
	    if (Boolean.parseBoolean(properties.getProperty("opening.bracket.check.active"))) {
		checks.openingBrackets();
	    }
	    if (Boolean.parseBoolean(properties.getProperty("length.of.line.check.active"))) {
		length = Integer.parseInt(properties.getProperty("line.length.limit"));
		checks.lenghtOfLine(length);
	    }
	    return output = readResult();

	} finally {

	}

	/*
	 * try (InputStream ins = new FileInputStream(resultFromCheck)) { //
	 * OutputStream output = new ByteArrayOutputStream()) { byte[] buffer =
	 * new byte[4096]; int read = 0; while ((read = ins.read(buffer)) != -1)
	 * { output.write(buffer, 0, read); } System.out.println("O: [" + output
	 * + "]"); }
	 */

    }

    private OutputStream readResult() throws FileNotFoundException, IOException {
	try (InputStream ins = new FileInputStream("source.txt"); OutputStream output = new ByteArrayOutputStream()) {
	    byte[] buffer = new byte[4096];
	    int read = 0;
	    while ((read = ins.read(buffer)) != -1) {
		output.write(buffer, 0, read);
	    }
	    System.out.println("O: [" + output + "]");
	    return output;
	}
    }

    private void inputToFile(InputStream sourceToRead) throws FileNotFoundException, IOException {
	try (OutputStream os = new FileOutputStream("source.txt")) {

	    byte[] buffer = new byte[4096];
	    int count = 0;
	    while ((count = sourceToRead.read(buffer)) > 0) {
		os.write(buffer, 0, count);
	    }
	}
    }
}
