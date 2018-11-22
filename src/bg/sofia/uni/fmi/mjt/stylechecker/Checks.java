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
import java.nio.file.Files;

public class Checks {

    private File tempFromInput = new File("test/bg/sofia/uni/fmi/mjt/stylechecker/temp.txt");
    private File outputFromCheck = new File("test/bg/sofia/uni/fmi/mjt/stylechecker/OutputFromCheck");
    private final String commentWildcards = "// FIXME Wildcards are not allowed in import statements\n";
    private final String commentOpeningBrackets = "// FIXME Opening brackets should be placed on the same line as the declaration";
    private final String commentOneLineStatement = "// FIXME Only one statement per line is allowed";
    private final String commentLineLimit = "// FIXME Length of line should not exceed 100 characters";

    public Checks(InputStream source, OutputStream output) throws IOException {
    }

    public void wildcardImport(InputStream input, OutputStream output) throws IOException {

        storeInputInTempFile(input);

        try (BufferedReader br = new BufferedReader(new FileReader(tempFromInput));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFromCheck))) {

            String line = null;
            while ((line = br.readLine()) != null) {
                if ((line.startsWith("import")) && (line.contains("*;"))) {
                    String appended = commentWildcards.concat(line);
                    bw.write(appended);
                    continue;
                }
                bw.write(line + "\n");
            }
            redirectToOutput();
            // TODO - get this stream as OUTPUT STREAM
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            input.close();
            output.close();
        }
    }

    /*
     * public InputStream getInput() { return input; } public void
     * setInput(InputStream input) { this.input = input; } public OutputStream
     * getOutput() { return output; } public void setOutput(OutputStream output)
     * { this.output = output; }
     */
    public void statementsPerLine(InputStream input, OutputStream output) throws IOException {
        System.out.println("States per line check");

        storeInputInTempFile(input);

        try (BufferedReader br = new BufferedReader(new FileReader(tempFromInput));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFromCheck))) {

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
            redirectToOutput();
        }
    }

    public void openingBrackets(InputStream input, OutputStream output) throws IOException {
        System.out.println("Opening Brackets");
        
        storeInputInTempFile(input);
       
        try (BufferedReader br = new BufferedReader(new FileReader(tempFromInput));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFromCheck))) {
            
        String line = null;
        while ((line = br.readLine()) != null) {
            String subStr = line.trim();
            if (subStr.startsWith("{")) {
                String appended = commentOpeningBrackets.concat(line);
                bw.write(appended);
            }
            bw.write(line + "\n");
        }
        redirectToOutput();
        }
    }

    public void lenghtOfLine(InputStream input, OutputStream output, String length) throws IOException {
        int lineLength = Integer.parseInt(length);
        System.out.println("Lenght Of Line");
        storeInputInTempFile(input);
        
        try (BufferedReader br = new BufferedReader(new FileReader(tempFromInput));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFromCheck))) {
            
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.length()>lineLength){
                String appended = commentLineLimit.concat(line);
                bw.write(appended);
                bw.write(line + "\n");
            }
        }
        redirectToOutput();
        }
    }
    
    private void storeInputInTempFile(InputStream input) throws IOException, FileNotFoundException {
        if (!tempFromInput.exists())
            tempFromInput.createNewFile();
        if (!outputFromCheck.exists())
            outputFromCheck.createNewFile();

        byte[] buffer = new byte[input.available()];
        input.read(buffer);
        OutputStream os = new FileOutputStream(tempFromInput);
        os.write(buffer); // TODO try-wirh-resc
    }
    
    public ByteArrayOutputStream redirectToOutput() throws IOException {
        try (InputStream ios = new FileInputStream(outputFromCheck)) {
            ByteArrayOutputStream outs = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                outs.write(buffer, 0, read);
            }
            System.out.println("O: " + outs);
            return outs;
        }
    }


}
