package bg.sofia.uni.fmi.mjt.stylechecker;

public interface Check {

    boolean isValid(String line);

    String getErrorComment();
}
