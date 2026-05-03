package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.model.Feedback;
import hr.javafx.tvz.projektni_zadatak.model.FeedbackChangeLog;
import hr.javafx.tvz.projektni_zadatak.repository.FeedbackRepository;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for handling the creation and saving of feedback entries.
 * Provides a form with date pickers, summary input, and a rating selector.
 * Saves feedback to the repository and logs the action.
 */
@Slf4j
public class FeedbackSaveController {

    private final FeedbackRepository feedbackRepository = new FeedbackRepository();

    @FXML
    private DatePicker feedbackStartDatePicker;

    @FXML
    private DatePicker feedbackEndDatePicker;

    @FXML
    private TextArea summaryTextArea;

    @FXML
    private ComboBox<Integer> ratingComboBox;

    /**
     * Initializes the feedback form with available rating values.
     */
    public void initialize() {
        ObservableList<Integer> ratings = FXCollections.observableArrayList(5, 4, 3, 2, 1);
        ratingComboBox.setItems(ratings);
    }

    /**
     * Adds a new feedback entry based on user input.
     * Validates input fields, confirms the action with the user,
     * saves the feedback to the repository, and logs the change.
     */
    public void addFeedback() {
        LocalDate feedbackStartDate = feedbackStartDatePicker.getValue();
        LocalDate feedbackEndDate = feedbackEndDatePicker.getValue();
        String summary = summaryTextArea.getText();
        Integer rating = ratingComboBox.getValue();

        if (feedbackStartDate == null || feedbackEndDate == null || summary == null || rating == null) {
            showAlert(Alert.AlertType.ERROR, "Saving error", "Empty fields",
                    "Please fill all fields");
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert("Confirmation alert", "Saving check",
                "Are you sure you want to save this feedback?");

        if (result.isPresent() &&result.get() == ButtonType.OK) {
            int feedbackId = feedbackRepository.findAll().getLast().getId() + 1;
            int employeeId = Session.getCurrentUser().id();
            Feedback feedback = new Feedback(feedbackId, employeeId, feedbackStartDate,
                    feedbackEndDate, summary, rating);
            feedbackRepository.save(feedback);
            List<FeedbackChangeLog> changes = ChangeLogUtils.getAllFeedbackChanges();
            FeedbackChangeLog change = new FeedbackChangeLog(Action.CREATE, feedback, Session.getCurrentUser().role());
            changes.add(change);
            ChangeLogUtils.saveAllFeedbackChanges(changes);
            log.info("Feedback saved successfully");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Saving successful",
                    "Feedback added");
        }
    }

    /**
     * Shows a basic alert dialog with provided details.
     * @param alertType the type of alert to display
     * @param title     the title of the alert window
     * @param header    the header text of the alert
     * @param content   the message body of the alert
     */
    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation alert dialog and returns the user's response.
     * @param title   the title of the confirmation dialog
     * @param header  the header text
     * @param content the message body
     * @return Optional containing the selected ButtonType
     */
    public Optional<ButtonType> showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
