package hr.javafx.tvz.projektni_zadatak.controller;

import hr.javafx.tvz.projektni_zadatak.model.Employee;
import hr.javafx.tvz.projektni_zadatak.model.Pair;
import hr.javafx.tvz.projektni_zadatak.model.PerformanceReview;
import hr.javafx.tvz.projektni_zadatak.repository.EmployeeDatabaseRepository;
import hr.javafx.tvz.projektni_zadatak.repository.PerformanceReviewRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for displaying employees and their average performance review scores.
 * Fetches employees and their associated performance reviews from the repositories,
 * calculates the average rating for each employee,
 * displays the results in a table view,
 * and highlights the best employee based on the highest average rating.
 */
public class EmployeeScoreController {

    private final EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();
    private final PerformanceReviewRepository performanceReviewRepository = new PerformanceReviewRepository();

    @FXML
    private Label bestEmployeeLabel;

    @FXML
    private TableView<Pair<Employee, Double>> employeeTableView;

    @FXML
    private TableColumn<Pair<Employee, Double>, String> employeeIdTableColumn;

    @FXML
    private TableColumn<Pair<Employee, Double>, String> employeeNameTableColumn;

    @FXML
    private TableColumn<Pair<Employee, Double>, String> employeeSurnameTableColumn;

    @FXML
    private TableColumn<Pair<Employee, Double>, String> employeeEmailTableColumn;

    @FXML
    private TableColumn<Pair<Employee, Double>, String> employeeSalaryTableColumn;

    @FXML
    private TableColumn<Pair<Employee, Double>, String> employeeRoleTableColumn;

    @FXML
    private TableColumn<Pair<Employee, Double>, String> employeeAverageGradeTableColumn;

    /**
     * Initializes the controller by setting up table columns, calculating average grades for each employee,
     * determining the best employee, and populating the table and label accordingly.
     */
    public void initialize() {
        employeeIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFirst().getId())));

        employeeNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirst().getFirstName()));

        employeeSurnameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirst().getLastName()));

        employeeEmailTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirst().getEmail()));

        employeeSalaryTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFirst().getSalary())));

        employeeRoleTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirst().getRole().name()));

        employeeAverageGradeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSecond())));

        List<Employee> employees = employeeDatabaseRepository.findAll();
        List<PerformanceReview> reviews = performanceReviewRepository.findAll();
        List<Pair<Employee, Double>> employeeAverageGrades = new ArrayList<>();

        for (Employee employee : employees) {
            double averageGrade = 0;
            int numberOfReviews = 0;
            for (PerformanceReview review : reviews) {
                if (review.getEmployeeId() == employee.getId()) {
                    averageGrade += review.getRating();
                    numberOfReviews++;
                }
            }
            if (numberOfReviews != 0) {
                averageGrade /= numberOfReviews;
            }
            employeeAverageGrades.add(new Pair<>(employee, averageGrade));
        }

        Optional<Employee> bestEmployee = employeeAverageGrades.stream()
                .max(Comparator.comparing(Pair::getSecond))
                .map(Pair::getFirst);

        bestEmployee.ifPresent(employee -> bestEmployeeLabel.setText(employee.getFirstName()
                + " " + employee.getLastName()));

        ObservableList<Pair<Employee, Double>> employeeAverageGradesObservableList =
                FXCollections.observableArrayList(employeeAverageGrades);
        employeeTableView.setItems(employeeAverageGradesObservableList);
    }
}
