package hr.javafx.tvz.projektni_zadatak.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a feedback entry associated with an employee.
 * Contains information about the time period of the feedback,
 * a textual summary, and a rating score.
 * Extends {@link Entity}, inheriting the unique identifier field.
 */
@Data
public class Feedback extends Entity implements Serializable {

    private final int employeeId;
    private final LocalDate feedbackStartDate;
    private final LocalDate feedbackEndDate;
    private final String summary;
    private final int rating;

    public Feedback(int id, int employeeId, LocalDate feedbackStartDate, LocalDate feedbackEndDate,
                    String feedback, int rating) {
        super(id);
        this.employeeId = employeeId;
        this.feedbackStartDate = feedbackStartDate;
        this.feedbackEndDate = feedbackEndDate;
        this.summary = feedback;
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeId, summary, rating);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Feedback other = (Feedback) obj;
        return Objects.equals(this.employeeId, other.employeeId) &&
                Objects.equals(this.feedbackStartDate, other.feedbackStartDate) &&
                Objects.equals(this.summary, other.summary) &&
                Objects.equals(this.rating, other.rating);
    }
}
