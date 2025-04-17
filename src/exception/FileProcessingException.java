package exception;

public abstract class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }
}
