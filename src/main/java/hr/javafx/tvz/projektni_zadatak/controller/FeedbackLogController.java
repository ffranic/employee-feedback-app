package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.model.FeedbackChangeLog;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Controller class responsible for displaying a log of feedback changes.
 * The logs include details such as the feedback ID, type, action performed,
 * timestamp of the action, and the role of the user who performed the action.
 */
public class FeedbackLogController {

    @FXML
    private TableView<FeedbackChangeLog> logTableView;

    @FXML
    private TableColumn<FeedbackChangeLog, String> objectIdColumn;

    @FXML
    private TableColumn<FeedbackChangeLog, String> objectTypeColumn;

    @FXML
    private TableColumn<FeedbackChangeLog, String> performedActionColumn;

    @FXML
    private TableColumn<FeedbackChangeLog, String> actionTimestampColumn;

    @FXML
    private TableColumn<FeedbackChangeLog, String> roleColumn;

    /**
     * Initializes the controller by setting up the table columns and loading feedback change logs
     * from persistent storage. Each column is bound to the appropriate property of the FeedbackChangeLog object.
     */
    public void initialize() {
        try {
            objectIdColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(cellData.getValue().getFeedback().getId())));

            objectTypeColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getFeedback().getClass().getSimpleName()));

            performedActionColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getAction().toString()));

            actionTimestampColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getTimestamp().toString()));

            roleColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getRole().toString()));

            List<FeedbackChangeLog> feedbackChangeList = ChangeLogUtils.getAllFeedbackChanges();
            ObservableList<FeedbackChangeLog> feedbackChangeLogObservableList =
                    FXCollections.observableArrayList(feedbackChangeList);
            logTableView.setItems(feedbackChangeLogObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
