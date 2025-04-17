package exception;

public class WrongFileFormat extends FileProcessingException {
    public WrongFileFormat(String expectedFormat, String actualFormat) {
        super("Wrong file format: " + expectedFormat + "\nexpected: " + actualFormat);
    }
}
