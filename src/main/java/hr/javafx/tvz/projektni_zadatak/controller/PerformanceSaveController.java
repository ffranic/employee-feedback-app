package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.model.Employee;
import hr.javafx.tvz.projektni_zadatak.model.PerformanceChangeLog;
import hr.javafx.tvz.projektni_zadatak.model.PerformanceReview;
import hr.javafx.tvz.projektni_zadatak.repository.EmployeeDatabaseRepository;
import hr.javafx.tvz.projektni_zadatak.repository.PerformanceReviewRepository;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for handling the logic of saving a new performance review.
 * Allows selection of an employee, review period, rating, and summary.
 * Validates input fields before saving.
 * Saves the review and logs the change.
 */
public class PerformanceSaveController {

    private final EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();
    private final PerformanceReviewRepository performanceReviewRepository = new PerformanceReviewRepository();

    @FXML
    private ListView<Employee> employeeListView;

    @FXML
    private DatePicker reviewStartDatePicker;

    @FXML
    private DatePicker reviewEndDatePicker;

    @FXML
    private TextArea reviewSummaryTextArea;

    @FXML
    private ComboBox<Integer> reviewRatingComboBox;

    /**
     * Initializes the UI components by populating the employee list with all employees.
     * Sets rating choices from 5 to 1 in the ComboBox.
     */
    public void initialize() {
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(
                employeeDatabaseRepository.findAll());
        employeeListView.setItems(employeeObservableList);

        ObservableList<Integer> integerObservableList = FXCollections.observableArrayList(5, 4, 3, 2, 1);
        reviewRatingComboBox.setItems(integerObservableList);
    }

    /**
     * Saves a new performance review after validating input fields.
     * Shows a confirmation dialog before saving.
     * Upon confirmation, generates a new review ID, creates the review object,
     * saves it to the repository, logs the creation, and notifies the user.
     */
    public void save() {
        Employee reviewedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        LocalDate reviewStart = reviewStartDatePicker.getValue();
        LocalDate reviewEnd = reviewEndDatePicker.getValue();
        String summary = reviewSummaryTextArea.getText();
        Integer rating = reviewRatingComboBox.getValue();

        if (reviewedEmployee == null || reviewStart == null || reviewEnd == null || summary == null || rating == null) {
            showAlert(Alert.AlertType.ERROR, "Saving error", "Empty fields",
                    "Please fill in all the fields");
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert("Saving confirmation",
                "Are you sure you want to add this review",
                "Review will be visible to the employee immediately");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int reviewId = performanceReviewRepository.findAll().getLast().getId() + 1;
            PerformanceReview review = new PerformanceReview(reviewId, reviewedEmployee.getId(),
                    reviewStart, reviewEnd, summary, rating);
            performanceReviewRepository.save(review);
            List<PerformanceChangeLog> changes = ChangeLogUtils.getAllReviewChanges();
            PerformanceChangeLog change = new PerformanceChangeLog(Action.CREATE, review, reviewedEmployee.getRole());
            changes.add(change);
            ChangeLogUtils.saveAllReviewChanges(changes);
            showAlert(Alert.AlertType.INFORMATION, "Saving info", "Success",
                    "Review added successfully");
        }
    }

    /**
     * Utility method to show alerts with custom title, header, and content.
     * @param alertType The type of the alert
     * @param title     The title of the alert window.
     * @param header    The header text.
     * @param content   The content message shown in the alert.
     */
    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Utility method to show a confirmation dialog and return the user's response.
     * @param title   The title of the confirmation window.
     * @param header  The header text.
     * @param content The content message asking for confirmation.
     * @return Optional<ButtonType> containing the user's choice.
     */
    public Optional<ButtonType> showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
