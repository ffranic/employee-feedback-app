package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.application.TeamPulseApplication;
import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.authentification.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the admin menu view.
 * Displays current user information, login time, and provides a logout option.
 */
public class AdminMenuController {

    @FXML
    private Label loginDateLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Label userRoleLabel;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /**
     * Initializes the admin menu view by displaying user-related session data.
     * Extracts the user's name, ID, role, and login time from the session.
     */
    public void initialize() {
        ZonedDateTime loginTime = Session.getLoginTime();
        if (loginTime != null) {
            loginDateLabel.setText(loginTime.format(formatter));
        }

        User user = Session.getCurrentUser();

        if (user != null) {
            String usernameFull = user.username();

            String[] usernameArray = usernameFull.split("@");
            String fullName = usernameArray[0];

            String[] userFullName = splitFullName(fullName);
            welcomeLabel.setText("Welcome " + userFullName[0] + "\uD83D\uDE0A");

            userIdLabel.setText(String.valueOf(user.id()));
            userRoleLabel.setText(String.valueOf(user.role()));
        }
    }

    /**
     * Splits a camel case full name string into first and last name.
     * @param fullName a concatenated full name, e.g. "JohnDoe"
     * @return an array where index 0 is the capitalized first name and index 1 is the last name,
     *         or an empty array if splitting fails
     */
    public static String[] splitFullName(String fullName) {
        for (int i = 1; i < fullName.length(); i++) {
            if (Character.isUpperCase(fullName.charAt(i))) {
                String firstName = fullName.substring(0, i);
                String lastName = fullName.substring(i);
                return new String[]{capitalize(firstName), lastName};
            }
        }
        return new String[0];
    }

    /**
     * Capitalizes the first character of a string.
     * @param name the string to capitalize
     * @return a string with the first letter in uppercase
     */
    public static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Logs the user out by clearing the session and navigating back to the login view.
     */
    public void logOut() {
        Session.clearSession();
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/loginView.fxml");
    }
}
