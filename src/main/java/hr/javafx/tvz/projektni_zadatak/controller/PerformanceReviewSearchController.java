package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.model.PerformanceReview;
import hr.javafx.tvz.projektni_zadatak.repository.PerformanceReviewRepository;
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
 * Controller class for the performance review search view.
 * Provides functionality to filter performance reviews by date range
 * and sort them based on selected criteria (e.g., rating, date).
 * The results are displayed in a table.
 */
public class PerformanceReviewSearchController {

    private final PerformanceReviewRepository performanceReviewRepository = new PerformanceReviewRepository();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    @FXML
    private DatePicker reviewPeriodFromDatePicker;

    @FXML
    private DatePicker reviewPeriodToDatePicker;

    @FXML
    private ComboBox<String> reviewSortingComboBox;

    @FXML
    private TableView<PerformanceReview> performanceReviewTableView;

    @FXML
    private TableColumn<PerformanceReview, String> reviewIdColumn;

    @FXML
    private TableColumn<PerformanceReview, String> reviewEmployeeIdColumn;

    @FXML
    private TableColumn<PerformanceReview, String> reviewPeriodFromColumn;

    @FXML
    private TableColumn<PerformanceReview, String> reviewPeriodToColumn;

    @FXML
    private TableColumn<PerformanceReview, String> reviewSummaryColumn;

    @FXML
    private TableColumn<PerformanceReview, String> reviewRatingColumn;

    /**
     * Initializes the table and dropdown with default values and cell factories.
     * Binds data columns to appropriate properties from the {@link PerformanceReview} model.
     */
    public void initialize() {
        ObservableList<String> stringObservableList = FXCollections.observableArrayList("New first",
                "Old first", "Rating", "Default");
        reviewSortingComboBox.setItems(stringObservableList);
        reviewSortingComboBox.setValue("Default");

        reviewIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

        reviewEmployeeIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEmployeeId())));

        reviewPeriodFromColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReviewPeriodStartDate().format(formatter)));

        reviewPeriodToColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReviewPeriodEndDate().format(formatter)));

        reviewSummaryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSummary()));

        reviewRatingColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRating())));
    }

    /**
     * Fetches all performance reviews and filters them based on selected date range.
     * Allows sorting by rating, newest first, or oldest first.
     * Populates the table with the resulting list.
     */
    public void search() {
        List<PerformanceReview> reviews = performanceReviewRepository.findAll();

        LocalDate reviewPeriodFrom = reviewPeriodFromDatePicker.getValue();
        LocalDate reviewPeriodTo = reviewPeriodToDatePicker.getValue();
        if (reviewPeriodFrom != null) {
            reviews = reviews.stream()
                    .filter(review ->
                            review.getReviewPeriodStartDate().isAfter(reviewPeriodFrom) ||
                            review.getReviewPeriodStartDate().isEqual(reviewPeriodFrom))
                    .toList();
        }

        if (reviewPeriodTo != null) {
            reviews = reviews.stream()
                    .filter(review ->
                            review.getReviewPeriodEndDate().isBefore(reviewPeriodTo) ||
                            review.getReviewPeriodEndDate().isEqual(reviewPeriodTo))
                    .toList();
        }

        String sortCondition = reviewSortingComboBox.getValue();
        if (sortCondition != null) {
            if (sortCondition.equalsIgnoreCase("Rating")) {
                reviews = reviews.stream()
                        .sorted((r1, r2) ->
                                Integer.compare(r2.getRating(), r1.getRating()))
                        .toList();
            }
            if (sortCondition.equalsIgnoreCase("New first")) {
                reviews = reviews.stream()
                        .sorted(Comparator.comparing(PerformanceReview::getReviewPeriodStartDate))
                        .toList();
            }
            if (sortCondition.equalsIgnoreCase("Old first")) {
                reviews = reviews.stream()
                        .sorted((r1, r2) ->
                                r2.getReviewPeriodStartDate().compareTo(r1.getReviewPeriodStartDate()))
                        .toList();
            }
        }

        ObservableList<PerformanceReview> performanceReviewObservableList = FXCollections.observableArrayList(reviews);
        performanceReviewTableView.setItems(performanceReviewObservableList);
    }
}