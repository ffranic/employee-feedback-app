package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.application.TeamPulseApplication;
import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.authentification.User;
import hr.javafx.tvz.projektni_zadatak.authentification.UserService;
import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.exceptions.FailedAuthentificationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Controller responsible for handling the login process of users.
 * Supports showing/hiding the password, validating credentials, and redirecting
 * users to their respective views based on their role.
 */
@Slf4j
public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordTextField;

    @FXML
    private CheckBox showPasswordCheckBox;

    private final UserService userService = new UserService();

    /**
     * Initializes the login form.
     * Binds visible and hidden password fields, and sets up the show/hide password toggle.
     */
    public void initialize() {
        visiblePasswordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        showPasswordCheckBox.selectedProperty().addListener((
                _, _, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                visiblePasswordTextField.setVisible(true);
                passwordField.setVisible(false);
            } else {
                visiblePasswordTextField.setVisible(false);
                passwordField.setVisible(true);
            }
        });
    }

    /**
     * Handles the login process.
     */
    public void signIn() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty fields", "Please fill all the fields");
            return;
        }

        try {
            User user = userService.authenticate(username, password).orElseThrow(() ->
                    new FailedAuthentificationException("Invalid username or password"));

            Session.setCurrentUser(user);
            ZonedDateTime loginTime = ZonedDateTime.now(ZoneId.of("Europe/Zagreb"));
            Session.setLoginTime(loginTime);

            if (user.role() == Role.ADMIN) {
                TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/adminView.fxml");
            } else if (user.role() == Role.MANAGER) {
                TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/managerView.fxml");
            } else {
                TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeView.fxml");
            }
        } catch (FailedAuthentificationException e) {
            log.error("Failed to authenticate user: {}", e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Login error", e.getMessage());
        } catch (Exception e) {
            log.error("Fatal error occurred, try inspecting the stack trace for more information: ", e);
            showAlert(Alert.AlertType.ERROR, "System error",
                    "An unexpected error occurred. Please try again later.");
        }
    }

    /**
     * Displays an alert with the specified type, title, and message.
     * @param alertType the type of alert to show
     * @param title     the title of the alert dialog
     * @param message   the content message of the alert
     */
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}