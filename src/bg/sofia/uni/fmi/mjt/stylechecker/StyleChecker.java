package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
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
    Properties defaultProperties = new Properties();
    FileInputStream fis = new FileInputStream("properties.cfg");
    Properties properties = new Properties();

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
	loadDefProperties();
	properties = defaultProperties;
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
	loadDefProperties();
	properties = new Properties(defaultProperties);
	properties.load(inputStream);
	inputStream.close();
    }

    private void loadDefProperties() throws IOException {
	defaultProperties.load(fis);
	fis.close();
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

	ArrayList<Check> checksToDo = new ArrayList<>();
	ArrayList<String> comments = new ArrayList<>();
	LinkedList<String> inputData = new LinkedList<>();

	try {
	    accomulateChecksFromProperties(checksToDo);

	    try (BufferedReader br = new BufferedReader(new InputStreamReader(sourceToRead));
	            ObjectOutputStream oos = new ObjectOutputStream(output)) {
		String line;
		while ((line = br.readLine()) != null) {
		    inputData.add(line);
		}
		runChecks(checksToDo, comments, inputData);

		oos.writeObject(inputData);
		return output;
	    }
	} finally {
	    sourceToRead.close();
	    output.close();
	}

    }

    private void runChecks(ArrayList<Check> checksToDo, ArrayList<String> comments, LinkedList<String> inputData) {
	for (int i = 0; i <= inputData.size() - 1; i++) {
	    String inputDataLine = inputData.get(i);
	    for (Check check : checksToDo) {
		if (!check.isValid(inputDataLine))
		    comments.add(check.getErrorComment());
	    }
	    if (i == 0)
		inputData.offerFirst(comments.iterator().next()); // TODO check
	    inputData.add(0, comments.iterator().next());
	}
    }

    private void accomulateChecksFromProperties(ArrayList<Check> checksToDo) {
	int length;
	if (Boolean.parseBoolean(properties.getProperty("wildcard.import.check.active"))) {
	    checksToDo.add(new WildcardImportCheck());
	}
	if (Boolean.parseBoolean(properties.getProperty("statements.per.line.check.active"))) {
	    checksToDo.add(new StatementsPerLineCheck());
	}
	if (Boolean.parseBoolean(properties.getProperty("opening.bracket.check.active"))) {
	    checksToDo.add(new OpeningBracketsCheck());
	}
	if (Boolean.parseBoolean(properties.getProperty("length.of.line.check.active"))) {
	    length = Integer.parseInt(properties.getProperty("line.length.limit"));
	    checksToDo.add(new LineLengthCheck(length));
	}
    }

    @Deprecated
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

    @Deprecated
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
