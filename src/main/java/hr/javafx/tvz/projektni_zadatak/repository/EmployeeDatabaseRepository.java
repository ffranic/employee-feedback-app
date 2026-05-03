package hr.javafx.tvz.projektni_zadatak.repository;

import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.exceptions.EmployeeNotFoundException;
import hr.javafx.tvz.projektni_zadatak.exceptions.RepositoryAccessException;
import hr.javafx.tvz.projektni_zadatak.messages.Message;
import hr.javafx.tvz.projektni_zadatak.model.Employee;
import hr.javafx.tvz.projektni_zadatak.utils.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository implementation for managing {@link Employee} entities using a database as the data source.
 * Provides methods for CRUD operations including finding, saving, updating, and deleting employees.
 * Also supports retrieving the best-paid employee.
 * Password hashing and saving employee data to a text file are done during the save operation.
 */
@Slf4j
public class EmployeeDatabaseRepository implements AbstractRepository<Employee> {
    private boolean isConnected = false;

    /**
     * Retrieves all employees from the database.
     * @return a list of all employees
     * @throws RepositoryAccessException if a database access error occurs
     */
    @Override
    public synchronized List<Employee> findAll() throws RepositoryAccessException {

        List<Employee> employees = new ArrayList<>();
        while (isConnected) {
            try {
                wait();
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
                throw new RepositoryAccessException("Repository is interrupted");
            }
        }
        isConnected = true;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(
                        "SELECT id, first_name, last_name, email, salary, role FROM EMPLOYEE");

                while (resultSet.next()) {
                    Employee employee = extractEmployeeFromResultSet(resultSet);
                    employees.add(employee);
                }
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while retrieving employees from database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }

        return employees;
    }

    /**
     * Finds the employee with the highest salary.
     * @return an {@link Optional} containing the best-paid employee, or empty if no employees exist
     * @throws RepositoryAccessException if a database access error occurs
     */
    public synchronized Optional<Employee> findBestPaidEmployee() {

        while (isConnected) {
            try {
                wait();
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
                throw new RepositoryAccessException("Repository is interrupted");
            }
        }
        isConnected = true;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT employee.* FROM EMPLOYEE WHERE SALARY = " +
                        "(SELECT MAX(SALARY) FROM EMPLOYEE)");

                if (resultSet.next()) {
                    return Optional.of(extractEmployeeFromResultSet(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (IOException | SQLException exc) {
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    /**
     * Finds an employee by their unique ID.
     * @param id the ID of the employee to find
     * @return the employee with the specified ID
     * @throws EmployeeNotFoundException  if the employee does not exist
     * @throws RepositoryAccessException  if a database access error occurs
     */
    @Override
    public synchronized Employee findById(int id) throws RepositoryAccessException {
        while (isConnected) {
            try {
                wait();
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
                throw new RepositoryAccessException("Repository is interrupted");
            }
        }
        isConnected = true;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, first_name, last_name, email, salary, role FROM employee WHERE id = ?")) {
                statement.setInt(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return extractEmployeeFromResultSet(resultSet);
                } else {
                    log.error("Employee with id {} not found in database", id);
                    throw new EmployeeNotFoundException("Employee with id " + id + " not found");
                }
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while retrieving employee from database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    /**
     * Saves a list of employees to the database.
     * @param employees the list of employees to save
     * @throws RepositoryAccessException if a database access error occurs
     */
    @Override
    public void save(List<Employee> employees) throws RepositoryAccessException {
        for (Employee employee : employees) {
            save(employee);
        }
    }

    /**
     * Saves a single employee to the database, hashes a generated password, and appends employee info to a text file.
     * @param employee the employee to save
     * @throws RepositoryAccessException if a database or file access error occurs
     */
    @Override
    public synchronized void save(Employee employee) throws RepositoryAccessException {
        while (isConnected) {
            try {
                wait();
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
                throw new RepositoryAccessException("Repository is interrupted");
            }
        }
        isConnected = true;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO EMPLOYEE(first_name, last_name, email, salary, role)" +
                            " VALUES(?, ?, ?, ?, ?)")) {
                statement.setString(1, employee.getFirstName());
                statement.setString(2, employee.getLastName());
                statement.setString(3, employee.getEmail());
                statement.setBigDecimal(4, employee.getSalary());
                statement.setString(5, employee.getRole().toString().toUpperCase());
                statement.executeUpdate();
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while saving employee to database: ", exc);
            throw new RepositoryAccessException(Message.DATABASE_CONNECTION_ERROR, exc);
        } finally {
            isConnected = false;
            notifyAll();
        }

        Path destination = Path.of("userData/employees.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination.toFile(), true))) {
            writer.write(employee.getId() + ",");
            writer.write(employee.getEmail() + ",");
            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            String formattedFirstName = Character.toUpperCase(firstName.charAt(0)) +
                    firstName.substring(1, firstName.length() - 1) +
                    Character.toUpperCase(firstName.charAt(firstName.length() - 1));
            String formattedLastName = Character.toUpperCase(lastName.charAt(0)) +
                    lastName.substring(1, lastName.length() - 1) +
                    Character.toUpperCase(lastName.charAt(lastName.length() - 1));
            String password = formattedFirstName + formattedLastName;
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            writer.write(hashedPassword + ",");
            writer.write(employee.getRole().toString().toUpperCase() + "\n");
        } catch (IOException exc) {
            log.error("Error while saving employee to file: ", exc);
            throw new RepositoryAccessException(exc);
        }
    }

    /**
     * Deletes an employee from the database.
     * @param employee the employee to delete
     * @throws EmployeeNotFoundException  if the employee is null or does not exist
     * @throws RepositoryAccessException  if a database access error occurs
     */
    @Override
    public synchronized void delete(Employee employee) throws RepositoryAccessException {
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee cannot be null");
        }

        if (!employeeExists(employee.getId())) {
            throw new EmployeeNotFoundException("Employee with id: " + employee.getId() + " not found");
        }

        while (isConnected) {
            try {
                wait();
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
                throw new RepositoryAccessException("Repository is interrupted");
            }
        }
        isConnected = true;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM employee WHERE id = ?")) {
                preparedStatement.setInt(1, employee.getId());
                preparedStatement.executeUpdate();
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while deleting employee from database: ", exc);
            throw new RepositoryAccessException(Message.DATABASE_CONNECTION_ERROR, exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    /**
     * Updates the salary and role of an employee identified by their ID.
     * @param id     the ID of the employee to update
     * @param salary the new salary to set
     * @param role   the new role to set
     * @throws RepositoryAccessException if a database access error occurs
     */
    @Override
    public synchronized void update(int id, BigDecimal salary, Role role) throws RepositoryAccessException {
        while (isConnected) {
            try {
                wait();
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
                throw new RepositoryAccessException("Repository is interrupted");
            }
        }
        isConnected = true;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE EMPLOYEE SET SALARY = ?, ROLE = ? WHERE id = ?"
            )) {
                preparedStatement.setBigDecimal(1, salary);
                preparedStatement.setString(2, role.toString().toUpperCase());
                preparedStatement.setInt(3, id);
                preparedStatement.executeUpdate();
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while updating employee in database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    /**
     * Checks if an employee exists in the database by their ID.
     * @param employeeId the ID of the employee to check
     * @return true if the employee exists, false otherwise
     * @throws RepositoryAccessException if a database access error occurs
     */
    private boolean employeeExists(int employeeId) throws RepositoryAccessException {
        String query = "SELECT COUNT(*) FROM employee WHERE id = ?";
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, employeeId);
                ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next();
            }
        } catch (IOException | SQLException exc) {
            log.error("Error occurred while checking if employee exists in database: ", exc);
            throw new RepositoryAccessException(Message.DATABASE_CONNECTION_ERROR, exc);
        }
    }

    /**
     * Helper method to extract an {@link Employee} object from a {@link ResultSet}.
     * @param resultSet the result set containing employee data
     * @return the extracted {@link Employee}
     * @throws SQLException if an SQL error occurs while extracting data
     */
    private static Employee extractEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        BigDecimal salary = resultSet.getBigDecimal("salary");
        Role role = Role.valueOf(resultSet.getString("role"));

        return new Employee(id, firstName, lastName, email, salary, role);
    }

}