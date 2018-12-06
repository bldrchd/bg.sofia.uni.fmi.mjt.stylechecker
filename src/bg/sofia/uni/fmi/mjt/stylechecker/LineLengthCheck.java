package bg.sofia.uni.fmi.mjt.stylechecker;

public class LineLengthCheck implements Check {
    int length = 0;

    public LineLengthCheck(int length) {
	this.length = length;
    }

    @Override
    public boolean isValid(String line) {
	return (line.length() > length);
    }

    @Override
    public String getErrorComment() {
	return "// FIXME Length of line should not exceed " + length + " characters\n";
    }

}
