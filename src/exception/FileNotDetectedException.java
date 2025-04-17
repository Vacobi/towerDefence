package exception;

public class FileNotDetectedException extends FileProcessingException {
    public FileNotDetectedException(String path) {
        super("File with path: " + path + " wasn't found");
    }
}
