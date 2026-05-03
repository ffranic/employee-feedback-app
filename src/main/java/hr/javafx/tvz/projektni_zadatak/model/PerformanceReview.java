package hr.javafx.tvz.projektni_zadatak.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a performance review for an employee over a specific period.
 * Contains details about the employee, review dates, summary, and rating.
 */
@Data
public class PerformanceReview extends Entity implements Serializable {

    private final int employeeId;
    private final LocalDate reviewPeriodStartDate;
    private final LocalDate reviewPeriodEndDate;
    private final String summary;
    private final int rating;

    public PerformanceReview(int id, int employeeId, LocalDate reviewPeriodStartDate,
                             LocalDate reviewPeriodEndDate, String summary, int rating) {
        super(id);
        this.employeeId = employeeId;
        this.reviewPeriodStartDate = reviewPeriodStartDate;
        this.reviewPeriodEndDate = reviewPeriodEndDate;
        this.summary = summary;
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeId, reviewPeriodStartDate,
                reviewPeriodEndDate, summary, rating);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        PerformanceReview other = (PerformanceReview) obj;
        return Objects.equals(this.employeeId, other.employeeId) &&
                Objects.equals(this.reviewPeriodStartDate, other.reviewPeriodStartDate) &&
                Objects.equals(this.reviewPeriodEndDate, other.reviewPeriodEndDate) &&
                Objects.equals(this.summary, other.summary) &&
                Objects.equals(this.rating, other.rating);
    }
}
