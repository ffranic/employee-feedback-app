package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.application.TeamPulseApplication;
import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.authentification.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller class for the manager's menu screen.
 * Displays user-related information such as login time, name, ID, and role.
 * Also provides logout functionality.
 */
public class ManagerMenuController {

    @FXML
    private Label loginDateLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Label userRoleLabel;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss");

    /**
     * Initializes the manager menu screen.
     * Displays the current login timestamp and details of the currently logged-in user.
     */
    public void initialize() {
        loginDateLabel.setText(LocalDateTime.now().format(formatter));

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
     * Splits a concatenated full name string into first and last name using camel case detection.
     * @param fullName the full name in camel case
     * @return a string array with [firstName, lastName] or empty if split not possible
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
     * Capitalizes the first letter of a given name.
     *
     * @param name the name to capitalize
     * @return the capitalized name (e.g., "john" → "John")
     */
    public static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Logs the user out by clearing the session and redirecting to the login screen.
     */
    public void logOut() {
        Session.clearSession();
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/loginView.fxml");
    }
}
