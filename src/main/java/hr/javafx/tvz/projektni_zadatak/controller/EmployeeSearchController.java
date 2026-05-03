package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.model.Employee;
import hr.javafx.tvz.projektni_zadatak.repository.EmployeeDatabaseRepository;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for searching and filtering employees based on various criteria such as
 * name, salary range, and role. Displays filtered employees in a table view and continuously
 * updates the label showing the best paid employee.
 */
public class EmployeeSearchController {

    private final EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();

    @FXML
    private TextField employeeNameSurnameTextField;

    @FXML
    private TextField employeeSalaryMinTextField;

    @FXML
    private TextField employeeSalaryMaxTextField;

    @FXML
    private ComboBox<Role> employeeRoleComboBox;

    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Employee, String> employeeIdTableColumn;

    @FXML
    private TableColumn<Employee, String> employeeNameTableColumn;

    @FXML
    private TableColumn<Employee, String> employeeSurnameTableColumn;

    @FXML
    private TableColumn<Employee, String> employeeEmailTableColumn;

    @FXML
    private TableColumn<Employee, String> employeeSalaryTableColumn;

    @FXML
    private TableColumn<Employee, String> employeeRoleTableColumn;

    @FXML
    private Label bestPaidEmployeeLabel;

    /**
     * Initializes the controller by setting up table columns, populating role combo box,
     * and starting a timeline that updates the best paid employee label every second.
     */
    public void initialize() {
        employeeIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

        employeeNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));

        employeeSurnameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));

        employeeEmailTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));

        employeeSalaryTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSalary())));

        employeeRoleTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole().name()));

        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        employeeRoleComboBox.setItems(roles);

        Timeline bestPaidEmployeeTimeline = getBestPaidEmployeeTimeline();
        bestPaidEmployeeTimeline.play();
    }

    /**
     * Filters employees based on user input for name, minimum salary, maximum salary, and role.
     * Updates the employee table with the filtered results.
     */
    public void filterEmployees() {

        List<Employee> employees = employeeDatabaseRepository.findAll();

        String nameSurname = employeeNameSurnameTextField.getText();
        String salaryMin = employeeSalaryMinTextField.getText();
        String salaryMax = employeeSalaryMaxTextField.getText();

        if (!nameSurname.isEmpty()) {
            employees = employees.stream()
                    .filter(employee ->
                            (employee.getFirstName() + " " + employee.getLastName()).equalsIgnoreCase(nameSurname))
                    .toList();
        }

        if (!salaryMin.isEmpty()) {
            BigDecimal minSalary;
            try {
                minSalary = new BigDecimal(salaryMin);
            } catch (NumberFormatException _) {
                showAlert(Alert.AlertType.ERROR, "Invalid salary", "Please enter a valid salary");
                return;
            }
            employees = employees.stream()
                    .filter(employee -> employee.getSalary().compareTo(minSalary) >= 0)
                    .toList();
        }

        if (!salaryMax.isEmpty()) {
            BigDecimal maxSalary;
            try {
                maxSalary = new BigDecimal(salaryMax);
            } catch (NumberFormatException _) {
                showAlert(Alert.AlertType.ERROR, "Invalid salary", "Please enter a valid salary");
                return;
            }
            employees = employees.stream()
                    .filter(employee -> employee.getSalary().compareTo(maxSalary) <= 0)
                    .toList();
        }

        Role selectedRole = employeeRoleComboBox.getSelectionModel().getSelectedItem();
        if (selectedRole != null) {
            employees = employees.stream()
                    .filter(employee ->
                            employee.getRole() == selectedRole)
                    .toList();
        }

        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(employees);
        employeeTableView.setItems(employeeObservableList);
    }

    /**
     * Shows an alert dialog with specified type, title, and message.
     * @param alertType Type of alert (e.g., ERROR, INFORMATION)
     * @param title     Title of the alert window
     * @param message   Content message of the alert
     */
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates and returns a Timeline that updates the label showing the best paid employee every second.
     *
     * @return Timeline object that runs indefinitely
     */
    private Timeline getBestPaidEmployeeTimeline() {
        Timeline bestPaidEmployeeTimeline = new Timeline(new KeyFrame(Duration.ZERO, _ -> {
            Optional<Employee> bestPaidEmployee = employeeDatabaseRepository.findBestPaidEmployee();

            bestPaidEmployee.ifPresent(employee -> bestPaidEmployeeLabel.setText("Best paid employee is "
                    + employee.getFirstName() + " " +
                    employee.getLastName() + " with salary: " + employee.getSalary()));
        }
        ),
                new KeyFrame(Duration.seconds(1))
        );

        bestPaidEmployeeTimeline.setCycleCount(Animation.INDEFINITE);
        return bestPaidEmployeeTimeline;
    }
}