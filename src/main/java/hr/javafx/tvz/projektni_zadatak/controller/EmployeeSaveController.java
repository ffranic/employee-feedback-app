package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.authentification.Session;
import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.messages.Message;
import hr.javafx.tvz.projektni_zadatak.model.EmployeeChangeLog;
import hr.javafx.tvz.projektni_zadatak.model.Employee;
import hr.javafx.tvz.projektni_zadatak.repository.EmployeeDatabaseRepository;
import hr.javafx.tvz.projektni_zadatak.utils.ChangeLogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller responsible for adding a new employee.
 * Handles input validation, confirmation, saving the employee to the database,
 * and logging the creation action.
 */
public class EmployeeSaveController {

    private final EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();

    @FXML
    private TextField nameSurnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField salaryTextField;

    @FXML
    private ComboBox<Role> employeeRoleComboBox;

    /**
     * Initializes the controller by populating the role combo box with all possible roles.
     */
    public void initialize() {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        employeeRoleComboBox.setItems(roles);
    }

    /**
     * Saves a new employee after validating input fields.
     * Shows confirmation alert before saving.
     * On success, adds a change log entry and displays success alert.
     */
    public void saveEmployee() {
        String[] nameSurname = nameSurnameTextField.getText().split(" ");
        String email = emailTextField.getText();
        String salary = salaryTextField.getText();
        Role role = employeeRoleComboBox.getSelectionModel().getSelectedItem();

        if (nameSurname.length == 0 || email.isEmpty() || salary.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Empty fields", Message.SAVING_ERROR,
                    "Please fill all the fields");
            return;
        }

        String firstName = nameSurname[0];
        String lastName = nameSurname[1];

        if (!validate(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid entry", "Invalid email",
                    "Please enter a valid email");
            return;
        }

        BigDecimal employeeSalary;
        try {
            employeeSalary = new BigDecimal(salary);
            if (employeeSalary.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert(Alert.AlertType.ERROR, Message.SAVING_ERROR, "Invalid salary",
                        "Salary cannot be zero or negative");
                return;
            }
        } catch (NumberFormatException _) {
            showAlert(Alert.AlertType.ERROR, Message.SAVING_ERROR, "Invalid salary",
                    "Salary must be a number");
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert(
                "Confirmation",
                "Are you sure you want to add this employee?",
                firstName + " "  + lastName + "\n" + email + "\n" + salary + "\n" + role
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int employeeId = employeeDatabaseRepository.findAll().getLast().getId() + 1;
            Employee employee = new Employee(employeeId, firstName, lastName, email, employeeSalary, role);
            employeeDatabaseRepository.save(employee);
            List<EmployeeChangeLog> changes = ChangeLogUtils.getAllEmployeeChanges();
            EmployeeChangeLog change = new EmployeeChangeLog(Action.CREATE, employee, Session.getCurrentUser().role());
            changes.add(change);
            ChangeLogUtils.saveAllEmployeeChanges(changes);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Saving info",
                    "Employee added successfully");
        }
    }

    /**
     * Shows an alert dialog with specified parameters.
     * @param alertType the type of the alert
     * @param title     the title of the alert dialog
     * @param header    the header text
     * @param content   the content/message of the alert
     */
    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation alert dialog.
     * @param title   the title of the alert dialog
     * @param header  the header text
     * @param message the confirmation message
     * @return an Optional containing the ButtonType selected by the user
     */
    public Optional<ButtonType> showConfirmationAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validates the given email string against a standard email pattern.
     * @param emailStr the email string to validate
     * @return true if the email matches the pattern, false otherwise
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
}
