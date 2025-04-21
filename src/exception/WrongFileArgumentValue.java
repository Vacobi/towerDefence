package exception;

public class WrongFileArgumentValue extends FileProcessingException {
    public WrongFileArgumentValue(String expected, String actual) {
        super("Wrong argument in road line. Expected: " + expected + ", Actual: " + actual);
    }
}
