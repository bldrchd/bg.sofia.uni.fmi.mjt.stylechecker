package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checks {

    private static final Logger logger = Logger.getLogger(StyleChecker.class.getName());
    private File tempInput = new File("test/bg/sofia/uni/fmi/mjt/stylechecker/tempInput.txt");
    private File resultFromCheck = new File("test/bg/sofia/uni/fmi/mjt/stylechecker/ResultFromCheck.txt");
    private final String commentWildcards = "// FIXME Wildcards are not allowed in import statements\n";
    private final String commentOpeningBrackets = "// FIXME Opening brackets should be placed on the same line as the declaration";
    private final String commentOneLineStatement = "// FIXME Only one statement per line is allowed";
    private final String commentLineLimit = "// FIXME Length of line should not exceed 100 characters";
    private InputStream input = null;
    private OutputStream output = null;

    public Checks(InputStream inputStr, OutputStream outputStr) throws IOException {
	this.input = inputStr;
	this.output = outputStr;
    }

    void wildcardImport() throws IOException {

	storeInputInTempFile(input);

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput));
	        BufferedWriter bw = new BufferedWriter(new FileWriter(resultFromCheck))) {

	    String line = null;
	    while ((line = br.readLine()) != null) {
		if ((line.startsWith("import")) && (line.contains("*;"))) {
		    String appended = commentWildcards.concat(line);
		    bw.write(appended);
		    continue;
		}
		bw.write(line + "\n");
	    }

	    output.write(redirectToOutput().toByteArray());
	} catch (IOException ioe) {
	    logger.log(Level.SEVERE, "Exception in wildcardImport.", ioe);
	}
    }

    void statementsPerLine() throws IOException {

	storeInputInTempFile(input);

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput));
	        BufferedWriter bw = new BufferedWriter(new FileWriter(resultFromCheck))) {

	    String line = null;
	    int counter = 0;
	    while ((line = br.readLine()) != null) {
		for (int i = 0; i < line.length() - 1; i++) {
		    if (line.charAt(i) == ';')
			counter++;

		}
		if (counter > 1) {
		    String appended = commentOneLineStatement.concat(line);
		    bw.write(appended);
		    counter = 0;
		}
		bw.write(line + "\n");
	    }
	    output.write(redirectToOutput().toByteArray());
	} catch (IOException ioe) {
	    logger.log(Level.SEVERE, "Exception in statementsPerLine.", ioe);
	}
    }

    void openingBrackets() throws IOException {
	System.out.println("Opening Brackets");

	storeInputInTempFile(input);

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput));
	        BufferedWriter bw = new BufferedWriter(new FileWriter(resultFromCheck))) {

	    String line = null;
	    while ((line = br.readLine()) != null) {
		String subStr = line.trim();
		if (subStr.startsWith("{")) {
		    String appended = commentOpeningBrackets.concat(line);
		    bw.write(appended);
		}
		bw.write(line + "\n");
	    }
	    output.write(redirectToOutput().toByteArray());
	} catch (IOException ioe) {
	    logger.log(Level.SEVERE, "Exception in openingBrackets.", ioe);
	}
    }

    void lenghtOfLine(String length) throws IOException {
	int lineLength = Integer.parseInt(length);
	System.out.println("Lenght Of Line");
	storeInputInTempFile(input);

	try (BufferedReader br = new BufferedReader(new FileReader(tempInput));
	        BufferedWriter bw = new BufferedWriter(new FileWriter(resultFromCheck))) {

	    String line = null;
	    while ((line = br.readLine()) != null) {
		if (line.length() > lineLength) {
		    String appended = commentLineLimit.concat(line);
		    bw.write(appended);
		    bw.write(line + "\n");
		}
	    }
	    output.write(redirectToOutput().toByteArray());
	} catch (IOException ioe) {
	    logger.log(Level.SEVERE, "Exception in lengthOfLine", ioe);
	}
    }

    private void storeInputInTempFile(InputStream input) throws IOException, FileNotFoundException {
	if (!tempInput.exists())
	    tempInput.createNewFile();
	if (!resultFromCheck.exists())
	    resultFromCheck.createNewFile();

	byte[] buffer = new byte[input.available()];
	input.read(buffer);

	try (OutputStream os = new FileOutputStream(tempInput)) {
	    os.write(buffer);
	} catch (IOException ioe) {
	    logger.log(Level.SEVERE, "Exception in creation of temp file.", ioe);
	}
    }

    public ByteArrayOutputStream redirectToOutput() throws IOException {
	try (InputStream ios = new FileInputStream(resultFromCheck);
	        ByteArrayOutputStream outs = new ByteArrayOutputStream()) {
	    byte[] buffer = new byte[4096];
	    int read = 0;
	    while ((read = ios.read(buffer)) != -1) {
		outs.write(buffer, 0, read);
	    }
	    System.out.println("O: " + outs);

	    return outs;
	} catch (IOException ioe) {
	    logger.log(Level.SEVERE, "Exception in output stream.", ioe);
	}
	return null;
    }
}
