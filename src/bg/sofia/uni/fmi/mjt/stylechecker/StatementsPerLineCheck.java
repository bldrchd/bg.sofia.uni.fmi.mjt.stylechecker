package bg.sofia.uni.fmi.mjt.stylechecker;

public class StatementsPerLineCheck implements Check {

    @Override
    public boolean isValid(String line) {
	line = line.trim();
	int index = line.indexOf(';');
	int index2 = line.indexOf(';', index + 1);
	return (index2 != -1);
    }

    @Override
    public String getErrorComment() {
	return "// FIXME Only one statement per line is allowed\n";
    }

}
