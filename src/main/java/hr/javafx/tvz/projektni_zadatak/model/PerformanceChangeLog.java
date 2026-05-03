package hr.javafx.tvz.projektni_zadatak.model;

import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.enums.Role;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a log entry for changes made to a {@link PerformanceReview}.
 * Captures the action performed, the review affected, the time of the change,
 * and the role of the user who made the change.
 */
@Data
public class PerformanceChangeLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private Action action;
    private PerformanceReview review;
    private final Role role;

    public PerformanceChangeLog(Action action, PerformanceReview review, Role role) {
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.review = review;
        this.role = role;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + action + " - " + review + ", made by: + " + this.role;
    }
}
