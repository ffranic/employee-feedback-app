package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.application.TeamPulseApplication;
import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.authentification.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the employee main menu screen.
 * It displays the login time, user welcome message, user ID, and role.
 * Also handles navigation to other views such as feedback addition, performance review search, and help.
 */
public class EmployeeMenuController {

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
     * Initializes the view by setting login time and user details.
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
     * Splits a concatenated full name into first and last name
     * based on uppercase letter boundary.
     * @param fullName concatenated full name string
     * @return array with first name at index 0 and last name at index 1,
     * or empty array if split is not possible
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
     * Capitalizes the first letter of the given string.
     * @param name input string
     * @return string with first character capitalized
     */
    public static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Clears the current user session and navigates back to the login view.
     */
    public void logOut() {
        Session.clearSession();
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/loginView.fxml");
    }

}
