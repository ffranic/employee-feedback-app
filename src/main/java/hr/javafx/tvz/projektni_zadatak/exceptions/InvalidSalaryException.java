package hr.javafx.tvz.projektni_zadatak.exceptions;

public class InvalidSalaryException extends RuntimeException {
    public InvalidSalaryException() {
    }

    public InvalidSalaryException(Throwable cause) {
        super(cause);
    }

    public InvalidSalaryException(String message) {
        super(message);
    }

    public InvalidSalaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSalaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
