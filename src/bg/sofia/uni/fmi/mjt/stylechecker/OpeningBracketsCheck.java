package bg.sofia.uni.fmi.mjt.stylechecker;

public class OpeningBracketsCheck implements Check {

    @Override
    public boolean isValid(String line) {
	line.trim();
	return (line.startsWith("{"));
    }

    @Override
    public String getErrorComment() {
	return "// FIXME Opening brackets should be placed on the same line as the declaration\n";
    }

}
