package bg.sofia.uni.fmi.mjt.stylechecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private static File testFileForCompare = new File ("test/bg/sofia/uni/fmi/mjt/stylechecker/ExpectedOutputFromTest.java");
    
    @Test
    public void test() throws IOException {
        // todo try-with-resources
        ByteArrayInputStream input = new ByteArrayInputStream("import java.util.*;".getBytes()); // source
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        StyleChecker checker = new StyleChecker();

        checker.checkStyle(input, output);
        String actual = new String(output.toByteArray()); 

        assertEquals("// FIXME Wildcards are not allowed in import statements\nimport java.util.*;", actual.trim());

    } 

    @Test
    public void testLoadedProperties() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("statements.per.line.check.active=false".getBytes());
        // input properties

        StyleChecker checker = new StyleChecker(input);

        ByteArrayInputStream source = new ByteArrayInputStream("echo(1);echo(2);echo(3);".getBytes()); // source
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        checker.checkStyle(source, output);
        String actual = new String(output.toByteArray());

        assertEquals("echo(1);echo(2);echo(3);", actual.trim()); 

    }
    
    @Test
    public void testWithFile() throws IOException{
        try (InputStream is = new FileInputStream("test/bg/sofia/uni/fmi/mjt/stylechecker/InputSourceFileForTest.java"); 
             OutputStream os = new FileOutputStream("test/bg/sofia/uni/fmi/mjt/stylechecker/OutputFromTest.java")) {
                
            StyleChecker checkerDefault = new StyleChecker(); 
                checkerDefault.checkStyle(is, os); //gets the IS and the result is written to the OS. 
                assertTrue(checkIfFilesAreEquals(testFileForCompare, os)); 
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    private boolean checkIfFilesAreEquals(File testFileForCompare, OutputStream os) throws IOException{
        File file = new File("test/bg/sofia/uni/fmi/mjt/stylechecker/OutputFromTest.java");
        Path pathToOutput = Paths.get(file.getAbsolutePath()); 
        Path pathToExpectedFile = Paths.get(testFileForCompare.getAbsolutePath()); 
        
        System.out.println("p1: " + pathToOutput+ " p2: "+pathToExpectedFile);
        try ( Stream<String> linesFromOutput = Files.lines(pathToOutput);
              Stream<String> linesFromExpected = Files.lines(pathToExpectedFile) ){        
            
            String dataActual = linesFromOutput.collect(Collectors.joining("\n"));
            String dataExpected = linesFromExpected.collect(Collectors.joining("/n"));
        
            return dataExpected.equals(dataActual); 
        }
    }
}
