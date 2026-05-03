package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.model.EmployeeChangeLog;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Controller responsible for displaying the employee change log in a TableView.
 * It populates the table with records retrieved from serialized change log files,
 * showing details such as the employee ID, type of object, action performed,
 * timestamp of the action, and the role of the user who performed the action.
 */
public class EmployeeLogController {

    @FXML
    private TableView<EmployeeChangeLog> logTableView;

    @FXML
    private TableColumn<EmployeeChangeLog, String> objectIdColumn;

    @FXML
    private TableColumn<EmployeeChangeLog, String> objectTypeColumn;

    @FXML
    private TableColumn<EmployeeChangeLog, String> performedActionColumn;

    @FXML
    private TableColumn<EmployeeChangeLog, String> actionTimestampColumn;

    @FXML
    private TableColumn<EmployeeChangeLog, String> roleColumn;

    /**
     * Initializes the TableView columns and loads employee change logs
     * from the serialized data file into the view.
     * Each column is bound to a property of the {@code EmployeeChangeLog} model,
     * formatted as string properties for display.
     */
    public void initialize() {
        try {
            objectIdColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(cellData.getValue().getEmployee().getId())));

            objectTypeColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getEmployee().getClass().getSimpleName()));

            performedActionColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getAction().toString()));

            actionTimestampColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getTimestamp().toString()));

            roleColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getRole().toString()));

            List<EmployeeChangeLog> employeeChangeList = ChangeLogUtils.getAllEmployeeChanges();
            ObservableList<EmployeeChangeLog> employeeChangeLogObservableList =
                    FXCollections.observableArrayList(employeeChangeList);
            logTableView.setItems(employeeChangeLogObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
