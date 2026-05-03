package hr.javafx.tvz.projektni_zadatak.authentification;

import java.time.ZonedDateTime;

/**
 * Utility class representing a simple user session.
 * Stores information about the currently logged-in user and the time of login.
 */
public class Session {
    private static User currentUser;
    private static ZonedDateTime loginTime;

    private Session() {}

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void setLoginTime(ZonedDateTime time) {
        loginTime = time;
    }

    public static ZonedDateTime getLoginTime() {
        return loginTime;
    }

    /**
     * Clears the current session data.
     * Sets both the current user and login time to {@code null}.
     */
    public static void clearSession() {
        currentUser = null;
        loginTime = null;
    }
}
