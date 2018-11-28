package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.junit.Test;

public class IOReadWriteTest {
    public void source() throws IOException {
	// InputStream initialStream = new FileInputStream(new
	// File("sample.txt"));
	ByteArrayInputStream bais = new ByteArrayInputStream("import java.util.*;".getBytes());
	test(bais);
    }

    public void test(InputStream initialStream) throws IOException {

	byte[] buffer = new byte[initialStream.available()];
	initialStream.read(buffer);

	File targetFile = new File("targetFile.tmp");
	OutputStream outStream = new FileOutputStream(targetFile);
	outStream.write(buffer);
    }

    @Test
    public void testTheTest() {
	try {
	    source();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) throws IOException {
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	File file = new File("source.txt");
	BufferedReader reader = new BufferedReader(new FileReader(file));

	String line = "";

	System.out.print("Find: ");

	String del = input.readLine();

	while ((line = reader.readLine()) != null)
	    if (line.contains(del))
		System.out.print(line);

	System.out.print("\nReplace with:");

	String replacement = input.readLine();

	if (line.contains(del))
	    line = replacement.replace(del, line);

	FileWriter writer = new FileWriter("StudGrades.itp");
	writer.write(line);
	reader.close();
	writer.close();

    }

}
