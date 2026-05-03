package hr.javafx.tvz.projektni_zadatak.exceptions;

public class FileAccessException extends Exception {
    public FileAccessException() {
    }

    public FileAccessException(Throwable cause) {
        super(cause);
    }

    public FileAccessException(String message) {
        super(message);
    }

    public FileAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
