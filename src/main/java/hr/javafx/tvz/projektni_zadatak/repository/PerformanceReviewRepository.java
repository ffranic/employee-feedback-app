package hr.javafx.tvz.projektni_zadatak.repository;

import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.exceptions.RepositoryAccessException;
import hr.javafx.tvz.projektni_zadatak.exceptions.ReviewNotFoundException;
import hr.javafx.tvz.projektni_zadatak.model.PerformanceReview;
import hr.javafx.tvz.projektni_zadatak.utils.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for accessing and managing performance review data from the database.
 */
@Slf4j
public class PerformanceReviewRepository implements AbstractRepository<PerformanceReview> {
    private boolean isConnected = false;

    /**
     * Retrieves all performance review records from the database.
     *
     * @return a list of all performance reviews.
     * @throws RepositoryAccessException if a database access error occurs.
     */
    @Override
    public synchronized List<PerformanceReview> findAll() throws RepositoryAccessException {
        List<PerformanceReview> reviews = new ArrayList<>();
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
                        "SELECT id, ID, EMPLOYEE_ID, REVIEW_PERIOD_START, REVIEW_PERIOD_END, " +
                                "SUMMARY, RATING FROM performance_review");
                while (resultSet.next()) {
                    PerformanceReview review = extractReviewFromResultSet(resultSet);
                    reviews.add(review);
                }
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while retrieving performance reviews from database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
        return reviews;
    }

    @Override
    public PerformanceReview findById(int id) throws RepositoryAccessException {
        return null;
    }

    /**
     * Saves a list of performance reviews to the database.
     *
     * @param reviews the list of performance reviews to be saved.
     * @throws RepositoryAccessException if a database access error occurs.
     */
    @Override
    public void save(List<PerformanceReview> reviews) throws RepositoryAccessException {
        for (PerformanceReview review : reviews) {
            save(review);
        }
    }

    /**
     * Saves a single performance review to the database.
     *
     * @param review the performance review to be saved.
     * @throws RepositoryAccessException if a database access error occurs.
     */
    @Override
    public synchronized void save(PerformanceReview review) throws RepositoryAccessException {
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
                    "INSERT INTO PERFORMANCE_REVIEW(employee_id, review_period_start," +
                            " review_period_end, summary, rating) " +
                            "VALUES(?, ?, ?, ?, ?)"
            )) {
                statement.setInt(1, review.getEmployeeId());
                statement.setDate(2, Date.valueOf(review.getReviewPeriodStartDate()));
                statement.setDate(3, Date.valueOf(review.getReviewPeriodEndDate()));
                statement.setString(4, review.getSummary());
                statement.setInt(5, review.getRating());
                statement.executeUpdate();
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while saving performance review to database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    /**
     * Deletes a performance review from the database.
     *
     * @param review the performance review to be deleted.
     * @throws RepositoryAccessException if a database access error occurs or review is null.
     */
    @Override
    public synchronized void delete(PerformanceReview review) throws RepositoryAccessException {
        if (review == null) {
            log.error("Review cannot be null");
            throw new ReviewNotFoundException("Review cannot be null");
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
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM PERFORMANCE_REVIEW WHERE ID = ?"
            )) {
                statement.setInt(1, review.getId());
                statement.executeUpdate();
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while deleting performance review from database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    @Override
    public void update(int id, BigDecimal salary, Role role) throws RepositoryAccessException {
        // update not necessary for performance reviews, as performance reviews are not editable after creation
    }

    /**
     * Extracts a {@link PerformanceReview} object from the current row of the given {@link ResultSet}.
     *
     * @param resultSet the result set to extract data from.
     * @return the extracted {@link PerformanceReview} object.
     * @throws SQLException if there is an error reading from the result set.
     */
    private static PerformanceReview extractReviewFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int employeeId = resultSet.getInt("employee_id");
        LocalDate reviewFromDate = resultSet.getDate("review_period_start").toLocalDate();
        LocalDate reviewToDate = resultSet.getDate("review_period_end").toLocalDate();
        String summary = resultSet.getString("summary");
        int rating = resultSet.getInt("rating");
        return new PerformanceReview(id, employeeId, reviewFromDate, reviewToDate, summary, rating);
    }
}
