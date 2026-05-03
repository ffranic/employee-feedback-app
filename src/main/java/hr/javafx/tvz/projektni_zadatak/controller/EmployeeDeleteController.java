package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.model.Employee;
import hr.javafx.tvz.projektni_zadatak.model.EmployeeChangeLog;
import hr.javafx.tvz.projektni_zadatak.repository.EmployeeDatabaseRepository;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling the deletion of employees within the application.
 * Allows administrators to select and remove employees from the database,
 * with confirmation and logging of the action to a changelog file.
 */
public class EmployeeDeleteController {

    private final EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();

    @FXML
    private ListView<Employee> employeeListView;

    /**
     * Initializes the view by loading all employees from the database
     * and displaying them in the {@code ListView}.
     */
    public void initialize() {
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(
                employeeDatabaseRepository.findAll()
        );
        employeeListView.setItems(employeeObservableList);
    }

    /**
     * Removes the selected employee from the system.
     * Prompts the user for confirmation before deleting the employee and logs the action.
     * If no employee is selected, an error alert is shown.
     */
    public void removeEmployee() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.ERROR, "Remove error", "Selection not made",
                    "Please select an employee first");
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert(
                "Confirmation",
                "Are you sure you want to remove this employee?",
                selectedEmployee.toString()
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            employeeDatabaseRepository.delete(selectedEmployee);
            List<EmployeeChangeLog> changes = ChangeLogUtils.getAllEmployeeChanges();
            EmployeeChangeLog change = new EmployeeChangeLog(Action.DELETE, selectedEmployee,
                    Session.getCurrentUser().role());
            changes.add(change);
            ChangeLogUtils.saveAllEmployeeChanges(changes);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Remove status",
                    "Employee removed successfully");
        }
    }

    /**
     * Displays an alert dialog with the specified parameters.
     * @param alertType the type of the alert (e.g., ERROR, INFORMATION)
     * @param title     the title of the alert dialog
     * @param header    the header text of the alert
     * @param message   the content message of the alert
     */
    public void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation alert and returns the user's response.
     * @param title   the title of the confirmation dialog
     * @param header  the header text of the dialog
     * @param message the content message of the dialog
     * @return an {@code Optional<ButtonType>} indicating the user's choice
     */
    public Optional<ButtonType> showConfirmationAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
