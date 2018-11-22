package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.*;

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
    private static final Logger logger = Logger.getLogger(StyleChecker.class.getName());
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
        try {
            properties = new Properties();
            FileInputStream input = new FileInputStream("properties.cfg");
            properties.load(input);
            System.out.println(properties);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
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
            System.out.println(properties); 
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
           inputStream.close();
        }
    }

    /**
     * For each line from the given {@code source} InputStream writes 
     * comment for the violated rules (if any) with an explanation of the style
     * error followed by the line itself in the {@code output}.
     * 
     * @param sourceToRead
     * @param output
     * @throws IOException
     */
    public OutputStream checkStyle(InputStream sourceToRead, OutputStream output) throws IOException {
        try (InputStream inputSource = sourceToRead; OutputStream outputDestination = output) {
            Checks checks = new Checks(sourceToRead, output);
            
            if (Boolean.parseBoolean(properties.getProperty("wildcard.import.check.active"))) 
                checks.wildcardImport(sourceToRead, output); 
            if (Boolean.parseBoolean(properties.getProperty("statements.per.line.check.active")))
               checks.statementsPerLine(sourceToRead, output);
            if (Boolean.parseBoolean(properties.getProperty("opening.bracket.check.active")))
                checks.openingBrackets(sourceToRead, output);
            if (Boolean.parseBoolean(properties.getProperty("length.of.line.check.active")))
                checks.lenghtOfLine(sourceToRead, output, properties.getProperty("line.length.limit"));
 
            return output;
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Exception in checkStyle.", ioe);
        }
        return output;
    }
}
