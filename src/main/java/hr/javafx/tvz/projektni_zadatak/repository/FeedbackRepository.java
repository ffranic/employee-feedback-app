package hr.javafx.tvz.projektni_zadatak.repository;

import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.exceptions.FeedbackNotFoundException;
import hr.javafx.tvz.projektni_zadatak.exceptions.RepositoryAccessException;
import hr.javafx.tvz.projektni_zadatak.model.Feedback;
import hr.javafx.tvz.projektni_zadatak.utils.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for accessing and managing feedback data from the database.
 */
@Slf4j
public class FeedbackRepository implements AbstractRepository<Feedback> {
    private boolean isConnected = false;

    /**
     * Retrieves all feedback records from the database.
     *
     * @return a list of all feedback entries.
     * @throws RepositoryAccessException if a database access error occurs.
     */
    @Override
    public synchronized List<Feedback> findAll() throws RepositoryAccessException {
        List<Feedback> feedbacks = new ArrayList<>();

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
                        "SELECT id, ID, EMPLOYEE_ID, FEEDBACK_PERIOD_START, " +
                                "FEEDBACK_PERIOD_END, FEEDBACK_SUMMARY, RATING FROM feedback");
                while (resultSet.next()) {
                    Feedback feedback = extractFeedbackFromResultSet(resultSet);
                    feedbacks.add(feedback);
                }
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while retrieving feedbacks from database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
        return feedbacks;
    }

    /**
     * Extracts a {@link Feedback} object from the current row of the given {@link ResultSet}.
     *
     * @param resultSet the result set to extract data from.
     * @return the extracted {@link Feedback} object.
     * @throws SQLException if there is an error reading from the result set.
     */
    private static Feedback extractFeedbackFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int employeeId = resultSet.getInt("employee_ID");
        LocalDate feedbackStartDate = resultSet.getDate("feedback_period_start").toLocalDate();
        LocalDate feedbackEndDate = resultSet.getDate("feedback_period_end").toLocalDate();
        String feedbackSummary = resultSet.getString("feedback_summary");
        int rating = resultSet.getInt("rating");
        return new Feedback(id, employeeId, feedbackStartDate,
                feedbackEndDate, feedbackSummary, rating);
    }

    /**
     * Retrieves a single feedback record by its ID.
     *
     * @param id the ID of the feedback to retrieve.
     * @return the feedback with the given ID.
     * @throws RepositoryAccessException if a database access error occurs.
     * @throws FeedbackNotFoundException if no feedback with the specified ID exists.
     */
    @Override
    public synchronized Feedback findById(int id) throws RepositoryAccessException {
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
                    "SELECT id, employee_id, feedback_period_start, feedback_period_end, " +
                            "feedback_summary, rating FROM feedback WHERE ID = ?")) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return extractFeedbackFromResultSet(resultSet);
                } else {
                    log.error("Feedback with id {} not found in database", id);
                    throw new FeedbackNotFoundException("Feedback with ID " + id + " not found");
                }
            }
        } catch (IOException | SQLException exc) {
            log.error("Error while retrieving feedback from database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    /**
     * Saves a list of feedback entries to the database.
     *
     * @param feedbacks the list of feedback objects to save.
     * @throws RepositoryAccessException if a database access error occurs.
     */
    @Override
    public void save(List<Feedback> feedbacks) throws RepositoryAccessException {
        for (Feedback feedback : feedbacks) {
            save(feedback);
        }
    }

    /**
     * Saves a single feedback entry to the database.
     *
     * @param feedback the feedback object to save.
     * @throws RepositoryAccessException if a database access error occurs.
     */
    @Override
    public synchronized void save(Feedback feedback) throws RepositoryAccessException {
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
                    "INSERT INTO feedback (EMPLOYEE_ID, FEEDBACK_PERIOD_START, FEEDBACK_PERIOD_END, " +
                            "FEEDBACK_SUMMARY, RATING)" +
                            "VALUES(?, ?, ?, ?, ?)"
            )) {
                statement.setInt(1, feedback.getEmployeeId());
                statement.setDate(2, Date.valueOf(feedback.getFeedbackStartDate()));
                statement.setDate(3, Date.valueOf(feedback.getFeedbackEndDate()));
                statement.setString(4, feedback.getSummary());
                statement.setInt(5, feedback.getRating());
                statement.executeUpdate();
            }
        } catch (SQLException | IOException exc) {
            log.error("Error while saving feedback to database: ", exc);
            throw new RepositoryAccessException(exc);
        } finally {
            isConnected = false;
            notifyAll();
        }
    }

    @Override
    public void delete(Feedback feedback) throws RepositoryAccessException {
        // delete not necessary for feedbacks, as feedbacks are not editable after creation
    }

    @Override
    public void update(int id, BigDecimal salary, Role role) throws RepositoryAccessException {
        //update not necessary for feedbacks, as feedbacks are not editable after creation
    }
}
