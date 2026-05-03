package hr.javafx.tvz.projektni_zadatak.exceptions;

public class FailedAuthentificationException extends Exception {
    public FailedAuthentificationException() {
    }

    public FailedAuthentificationException(Throwable cause) {
        super(cause);
    }

    public FailedAuthentificationException(String message) {
        super(message);
    }

    public FailedAuthentificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FailedAuthentificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
