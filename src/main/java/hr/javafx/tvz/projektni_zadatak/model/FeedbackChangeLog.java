package hr.javafx.tvz.projektni_zadatak.model;

import hr.javafx.tvz.projektni_zadatak.enums.Action;
import hr.javafx.tvz.projektni_zadatak.enums.Role;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a log entry tracking a change made to a {@link Feedback} object.
 * Each log includes the timestamp of the change, the type of action performed,
 * the role of the user who made the change, and the specific feedback instance affected.
 */
@Data
public class FeedbackChangeLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private Action action;
    private Feedback feedback;
    private final Role role;

    public FeedbackChangeLog(Action action, Feedback feedback, Role role) {
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.feedback = feedback;
        this.role = role;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + action + " - " + feedback + ", made by: + " + this.role;
    }
}
