package hr.javafx.tvz.projektni_zadatak.model;

import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.enums.Role;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a log entry for a change made to an {@link Employee} object.
 * It records the action performed, the employee involved, the role of the user who performed the action,
 * and the timestamp of the operation.
 */
@Data
public class EmployeeChangeLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private Action action;
    private Employee employee;
    private final Role role;

    public EmployeeChangeLog(Action action, Employee employee, Role role) {
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.employee = employee;
        this.role = role;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + action + " - " + employee + ", made by: " + this.role;
    }
}
