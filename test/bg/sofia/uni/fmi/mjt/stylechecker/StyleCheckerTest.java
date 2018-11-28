package bg.sofia.uni.fmi.mjt.stylechecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class StyleCheckerTest {
    private static File expectedOutput = new File("ExpectedOutput.txt");

    @Test
    public void testImport() {
	try (ByteArrayInputStream input = new ByteArrayInputStream("import java.util.*;".getBytes()); // source
	        ByteArrayOutputStream output = new ByteArrayOutputStream()) {

	    OutputStream os = new ByteArrayOutputStream();
	    StyleChecker checker = new StyleChecker();
	    os = checker.checkStyle(input, output);
	    String actual = new String(os.toString());
	    System.out.println("Actual: [" + actual + "]");

	    assertEquals("// FIXME Wildcards are not allowed in import statements\nimport java.util.*;".trim(),
	            actual.trim());
	    os.close();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

    @Test
    public void testLoadedProperties() throws IOException {
	try (ByteArrayInputStream input = new ByteArrayInputStream("statements.per.line.check.active=false".getBytes());
	        ByteArrayInputStream source = new ByteArrayInputStream("echo(1);echo(2);echo(3);".getBytes());
	        ByteArrayOutputStream output = new ByteArrayOutputStream()) {

	    OutputStream os = new ByteArrayOutputStream();
	    StyleChecker checker = new StyleChecker(input);

	    os = checker.checkStyle(source, output);
	    String actual = new String(os.toString());
	    System.out.println("Actual: " + actual);
	    assertEquals("echo(1);echo(2);echo(3);".trim(), actual.trim());

	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

    @Test
    public void testPropertiesMulti() throws IOException {
	try (ByteArrayInputStream input = new ByteArrayInputStream("statements.per.line.check.active=true".getBytes());
	        ByteArrayInputStream source = new ByteArrayInputStream(
	                "import java.util.*;\necho(1);echo(2);echo(3);".getBytes());
	        ByteArrayOutputStream output = new ByteArrayOutputStream()) {

	    OutputStream os = new ByteArrayOutputStream();
	    StyleChecker checker = new StyleChecker(input);

	    os = checker.checkStyle(source, output);
	    String actual = new String(os.toString());
	    System.out.println("Actual: [" + actual + "]");
	    assertEquals(
	            "// FIXME Wildcards are not allowed in import statements\n import java.util.*;\n// FIXME Only one statement per line is allowed\necho(1);echo(2);echo(3);\n"
	                    .trim(),
	            actual.trim());

	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

    @Test
    public void testWithFile() throws IOException {
	try (InputStream is = new FileInputStream("demoInput.txt"); OutputStream os = new ByteArrayOutputStream()) {

	    StyleChecker checkerDefault = new StyleChecker();
	    checkerDefault.checkStyle(is, os);
	    File result = new File("source.txt");
	    Path resultPath = Paths.get(result.getAbsolutePath());
	    assertTrue(checkIfFilesAreEquals(expectedOutput, resultPath));
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

    private boolean checkIfFilesAreEquals(File testFileForCompare, Path result) throws IOException {

	Path pathToExpectedFile = Paths.get(testFileForCompare.getAbsolutePath());

	System.out.println("p1: " + result + " p2: " + pathToExpectedFile);

	try (Stream<String> linesFromOutput = Files.lines(result);
	        Stream<String> linesFromExpected = Files.lines(pathToExpectedFile)) {
	    // Maybe there is a better way?
	    String dataActual = linesFromOutput.collect(Collectors.joining("\n"));
	    String dataExpected = linesFromExpected.collect(Collectors.joining("\n"));
	    // System.out.println("E : " + dataExpected);
	    System.out.println("A : " + dataActual);
	    return dataExpected.equals(dataActual);

	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
	return false;
    }
}
