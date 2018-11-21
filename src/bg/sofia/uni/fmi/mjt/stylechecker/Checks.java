package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class Checks {
    
    private InputStream input = null;
    private OutputStream output = null;
    
    public Checks(InputStream source, OutputStream output) throws IOException{
            setInput(source); // source of reading for checks
            setOutput(output); //destination to write
    }
   
    public void wildcardImport(InputStream is, OutputStream os) {
       System.out.println("first check"); //TODO
        //TODO close streams when ready
    }

    public InputStream getInput() {  //TODO
        return input;
    }
    public void setInput(InputStream input) {
        this.input = input;//TODO
    }
    public OutputStream getOutput() {//TODO
        return output;
    }
    public void setOutput(OutputStream output) {//TODO
        this.output = output; 
    }

    public void statementsPerLine() {
        System.out.println("States per line check");//TODO
        
    }

    public void openingBrackets() {
       System.out.println("Opening Brackets");//TODO
        
    }

    public void lenghtOfLine(String length) {
        int lineLength = Integer.parseInt(length);
        System.out.println("Lenght Of Line"); //TODO
        
    }

}
