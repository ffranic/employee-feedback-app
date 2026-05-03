package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.model.Feedback;
import hr.javafx.tvz.projektni_zadatak.repository.FeedbackRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Controller class responsible for searching and displaying feedback entries.
 * Allows filtering by start and end dates, and sorting by date or rating.
 */
public class FeedbackSearchController {

    private final FeedbackRepository feedbackRepository = new FeedbackRepository();

    @FXML
    private DatePicker feedbackFromDatePicker;

    @FXML
    private DatePicker feedbackToDatePicker;

    @FXML
    private ComboBox<String> feedbackSortComboBox;

    @FXML
    private TableView<Feedback> feedbackTableView;

    @FXML
    private TableColumn<Feedback, String> feedbackIDTableColumn;

    @FXML
    private TableColumn<Feedback, String> employeeIDTableColumn;

    @FXML
    private TableColumn<Feedback, String> feedbackStartDateTableColumn;

    @FXML
    private TableColumn<Feedback, String> feedbackEndDateTableColumn;

    @FXML
    private TableColumn<Feedback, String> feedbackSummaryTableColumn;

    @FXML
    private TableColumn<Feedback, String> feedbackRatingTableColumn;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    /**
     * Initializes the controller by setting up the sort ComboBox and TableView columns.
     * Configures column value factories for displaying Feedback data in the TableView.
     */
    public void initialize() {
        ObservableList<String> stringObservableList =
                FXCollections.observableArrayList("New first", "Old first", "Rating", "Default");
        feedbackSortComboBox.setItems(stringObservableList);
        feedbackSortComboBox.setValue("Default");

        feedbackIDTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

        employeeIDTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEmployeeId())));

        feedbackStartDateTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFeedbackStartDate().format(formatter)));

        feedbackEndDateTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFeedbackEndDate().format(formatter)));

        feedbackSummaryTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSummary()));

        feedbackRatingTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRating())));
    }

    /**
     * Filters and sorts feedback entries based on selected dates and sorting preference.
     */
    public void search() {
        List<Feedback> feedbacks = feedbackRepository.findAll();

        LocalDate dateFrom = feedbackFromDatePicker.getValue();
        LocalDate dateTo = feedbackToDatePicker.getValue();
        if (dateFrom != null) {
            feedbacks = feedbacks.stream()
                    .filter(feedback -> feedback.getFeedbackStartDate().isAfter(dateFrom) ||
                            feedback.getFeedbackStartDate().isEqual(dateFrom))
                    .toList();
        }

        if (dateTo != null) {
            feedbacks = feedbacks.stream()
                    .filter(feedback -> feedback.getFeedbackEndDate().isBefore(dateTo) ||
                            feedback.getFeedbackEndDate().isEqual(dateTo))
                    .toList();
        }

        String sortingCondition = feedbackSortComboBox.getSelectionModel().getSelectedItem();
        if (sortingCondition != null) {
            if (sortingCondition.equalsIgnoreCase("New First")) {
                feedbacks = feedbacks.stream()
                        .sorted(Comparator.comparing(Feedback::getFeedbackStartDate).reversed())
                        .toList();
            }
            if (sortingCondition.equalsIgnoreCase("Old first")) {
                feedbacks = feedbacks.stream()
                        .sorted(Comparator.comparing(Feedback::getFeedbackStartDate))
                        .toList();
            }
            if (sortingCondition.equalsIgnoreCase("Rating")) {
                feedbacks = feedbacks.stream()
                        .sorted((f1, f2) -> Integer.compare(f2.getRating(), f1.getRating()))
                        .toList();
            }
        }

        ObservableList<Feedback> feedbackObservableList = FXCollections.observableArrayList(feedbacks);
        feedbackTableView.setItems(feedbackObservableList);
    }
}
