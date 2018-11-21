package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class DefaultProperties {
    private Properties properties;
    
    public DefaultProperties() throws IOException{
        setProperties();
        getProperties();
    }
    private void setProperties() throws IOException{
        try {
        properties = new Properties();
        FileOutputStream output = new FileOutputStream("properties.cfg");
        properties.setProperty("wildcard.import.check.active", "true");
        properties.setProperty("statements.per.line.check.active", "true");
        properties.setProperty("opening.bracket.check.active", "true");
        properties.setProperty("length.of.line.check.active", "true"); 
        properties.setProperty("line.length.limit", "100");
        properties.store(output, "Initial");
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    public void getProperties() throws IOException {
        try {
            properties.load(new FileInputStream("properties.cfg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
