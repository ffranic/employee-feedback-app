package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.messages.Message;
import hr.javafx.tvz.projektni_zadatak.model.Employee;
import hr.javafx.tvz.projektni_zadatak.model.EmployeeChangeLog;
import hr.javafx.tvz.projektni_zadatak.repository.EmployeeDatabaseRepository;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for updating existing employee records.
 * Allows selection of an employee from a list, modification of their salary and role,
 * and saving the changes to the database. Also logs the changes with user role information.
 */
public class EmployeeUpdateController {

    private final EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();

    @FXML
    private ListView<Employee> employeeListView;

    @FXML
    private TextField salaryTextField;

    @FXML
    private ComboBox<Role> employeeRoleComboBox;

    /**
     * Initializes the controller by populating the employee list view and role combo box
     * with data from the database and enum values respectively.
     */
    public void initialize() {
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(
                employeeDatabaseRepository.findAll());
        employeeListView.setItems(employeeObservableList);

        ObservableList<Role> roleObservableList = FXCollections.observableArrayList(Role.values());
        employeeRoleComboBox.setItems(roleObservableList);
    }

    /**
     * Updates the selected employee's salary and role with the new values entered by the user.
     * Validates inputs, confirms the action with the user, saves changes to the repository,
     * logs the update action, and shows appropriate alerts.
     */
    public void updateEmployee() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        String salary = salaryTextField.getText();
        Role newRole = employeeRoleComboBox.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null || salary.isEmpty() || newRole == null) {
            showAlert(Alert.AlertType.ERROR, Message.UPDATE_ERROR, "Empty fields",
                    "Please fill all the fields");
            return;
        }

        BigDecimal newSalary;
        try {
            newSalary = new BigDecimal(salary);
            if (newSalary.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert(Alert.AlertType.ERROR, Message.UPDATE_ERROR, "Invalid salary",
                        "Salary cannot be negative or zero");
                return;
            }
        } catch (NumberFormatException _) {
            showAlert(Alert.AlertType.ERROR, Message.UPDATE_ERROR, "Invalid salary",
                    "Salary must be a number");
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert(
                "Confirmation",
                "Are you sure you want to make these updates?",
                "Old salary: " + selectedEmployee.getSalary() + "\n New salary: " + newSalary +
                        "\nOld role: " + selectedEmployee.getRole() + "\n New role: " + newRole
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            employeeDatabaseRepository.update(selectedEmployee.getId(), newSalary, newRole);
            List<EmployeeChangeLog> changes = ChangeLogUtils.getAllEmployeeChanges();
            EmployeeChangeLog change = new EmployeeChangeLog(Action.UPDATE, selectedEmployee,
                    Session.getCurrentUser().role());
            changes.add(change);
            ChangeLogUtils.saveAllEmployeeChanges(changes);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Update status",
                    "Employee updated");
        }
    }

    /**
     * Shows an alert dialog with the specified alert type, title, header, and message.
     * @param alertType the type of the alert (e.g., ERROR, INFORMATION)
     * @param title     the title of the alert window
     * @param header    the header text of the alert
     * @param message   the detailed message to display inside the alert
     */
    public void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation alert dialog with the specified title, header, and message,
     * and waits for the user's response.
     * @param title   the title of the confirmation dialog
     * @param header  the header text of the confirmation dialog
     * @param message the message content of the confirmation dialog
     * @return an Optional containing the ButtonType of the user's choice
     */
    public Optional<ButtonType> showConfirmationAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
