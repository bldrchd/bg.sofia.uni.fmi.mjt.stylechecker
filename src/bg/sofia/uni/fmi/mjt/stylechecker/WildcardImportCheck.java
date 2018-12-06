package bg.sofia.uni.fmi.mjt.stylechecker;

public class WildcardImportCheck implements Check {

    @Override
    public boolean isValid(String line) {
	return ((line.trim().startsWith("import")) && (line.contains("*;")));
    }

    @Override
    public String getErrorComment() {
	return "// FIXME Wildcards are not allowed in import statements\n";
    }

}
