package hr.javafx.tvz.projektni_zadatak.exceptions;

public class SceneChangeException extends RuntimeException {
    public SceneChangeException() {
    }

    public SceneChangeException(Throwable cause) {
        super(cause);
    }

    public SceneChangeException(String message) {
        super(message);
    }

    public SceneChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SceneChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
