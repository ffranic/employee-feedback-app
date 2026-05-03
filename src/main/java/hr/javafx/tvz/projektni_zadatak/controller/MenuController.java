package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.application.TeamPulseApplication;
import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.authentification.User;
import hr.javafx.tvz.projektni_zadatak.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Controller class for the main application menu.
 * It configures menu visibility based on the logged-in user's role and
 * handles navigation to different views within the application.
 */
public class MenuController {

    @FXML
    private Menu employeesMenu;

    @FXML
    private Menu feedbacksMenu;

    @FXML
    private Menu performanceMenu;

    @FXML
    private Menu logMenu;

    @FXML
    private MenuItem searchEmployees;

    @FXML
    private MenuItem saveEmployees;

    @FXML
    private MenuItem updateEmployees;

    @FXML
    private MenuItem deleteEmployees;

    @FXML
    private MenuItem searchFeedbacks;

    @FXML
    private MenuItem saveFeedbacks;

    @FXML
    private MenuItem searchReviews;

    @FXML
    private MenuItem saveReviews;

    /**
     * Initializes the menu based on the current user's role.
     * Controls visibility and access to certain menu items.
     */
    public void initialize() {
        User currentUser = Session.getCurrentUser();

        if (currentUser.role() == Role.EMPLOYEE) {
            employeesMenu.setVisible(false);
            performanceMenu.getItems().remove(saveReviews);
            feedbacksMenu.getItems().remove(searchFeedbacks);
            logMenu.setVisible(false);
        }

        if (currentUser.role() == Role.MANAGER) {
            employeesMenu.getItems().remove(saveEmployees);
            employeesMenu.getItems().remove(updateEmployees);
            employeesMenu.getItems().remove(deleteEmployees);
            logMenu.setVisible(false);
        }
    }

    /**
     * Navigates to the home view depending on the user's role.
     */
    public void goToHomeView() {
        if (Session.getCurrentUser().role() == Role.ADMIN) {
            TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/adminView.fxml");
        } else if (Session.getCurrentUser().role() == Role.MANAGER) {
            TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/managerView.fxml");
        } else {
            TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeView.fxml");
        }
    }

    /** Opens the employee search view. */
    public void showEmployeeSearchView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeSearch.fxml");
    }

    /** Opens the employee save view. */
    public void showEmployeeSaveView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeSave.fxml");
    }

    /** Opens the employee update view. */
    public void showEmployeeUpdateView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeUpdate.fxml");
    }

    /** Opens the employee deletion view. */
    public void showEmployeeDeleteView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeDelete.fxml");
    }

    /** Opens the performance review search view. */
    public void showPerformanceReviewSearchView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/performanceSearch.fxml");
    }

    /** Opens the performance review save view. */
    public void showPerformanceReviewSaveView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/performanceSave.fxml");
    }

    /** Opens the feedback search view. */
    public void showFeedbackSearchView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/feedbackSearch.fxml");
    }

    /** Opens the feedback save view. */
    public void showFeedbackSaveView() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/feedbackSave.fxml");
    }

    /** Opens the employee score view. */
    public void showEmployeeScore() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeScore.fxml");
    }

    /** Opens the employee log view. */
    public void showEmployeeLog() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/employeeLog.fxml");
    }

    /** Opens the feedback log view. */
    public void showFeedbackLog() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/feedbackLog.fxml");
    }

    /** Opens the review log view. */
    public void showReviewLog() {
        TeamPulseApplication.changeScene("/hr/javafx/tvz/projektni_zadatak/reviewLog.fxml");
    }

}
