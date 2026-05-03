package hr.javafx.tvz.projektni_zadatak.exceptions;

public class RepositoryAccessException extends RuntimeException {
    public RepositoryAccessException() {
    }

    public RepositoryAccessException(Throwable cause) {
        super(cause);
    }

    public RepositoryAccessException(String message) {
        super(message);
    }

    public RepositoryAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
