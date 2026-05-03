package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.model.PerformanceChangeLog;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Controller class responsible for displaying a log of performance review changes.
 * This controller initializes a TableView to show details about each change made to performance reviews,
 * including review ID, object type, action performed, timestamp, and role of the user who performed the action.
 */
public class ReviewLogController {

    @FXML
    private TableView<PerformanceChangeLog> logTableView;

    @FXML
    private TableColumn<PerformanceChangeLog, String> objectIdColumn;

    @FXML
    private TableColumn<PerformanceChangeLog, String> objectTypeColumn;

    @FXML
    private TableColumn<PerformanceChangeLog, String> performedActionColumn;

    @FXML
    private TableColumn<PerformanceChangeLog, String> actionTimestampColumn;

    @FXML
    private TableColumn<PerformanceChangeLog, String> roleColumn;

    /**
     * Initializes the table columns with data from PerformanceChangeLog instances.
     * Fetches all review changes from ChangeLogUtils and populates the TableView.
     * Uses lambda expressions to map each column to the appropriate property from PerformanceChangeLog.
     * Exception handling is present to catch any runtime errors during initialization.
     */
    public void initialize() {
        try {
            objectIdColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(cellData.getValue().getReview().getId())));

            objectTypeColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getReview().getClass().getSimpleName()));

            performedActionColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getAction().toString()));

            actionTimestampColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getTimestamp().toString()));

            roleColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getRole().toString()));

            List<PerformanceChangeLog> reviewChangeList = ChangeLogUtils.getAllReviewChanges();
            ObservableList<PerformanceChangeLog> reviewChangeLogObservableList =
                    FXCollections.observableArrayList(reviewChangeList);
            logTableView.setItems(reviewChangeLogObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
