package hr.javafx.tvz.projektni_zadatak.utils;

import hr.javafx.tvz.projektni_zadatak.exceptions.RepositoryAccessException;
import hr.javafx.tvz.projektni_zadatak.model.EmployeeChangeLog;
import hr.javafx.tvz.projektni_zadatak.model.FeedbackChangeLog;
import hr.javafx.tvz.projektni_zadatak.model.PerformanceChangeLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for serializing and deserializing change log lists to and from files.
 * This class provides static methods for saving and loading change logs related to employees,
 * feedback, and performance reviews. Each type of change log is stored in its own file.
 * All methods throw {@link RepositoryAccessException} if any IO or class loading errors occur.
 */
public class ChangeLogUtils {

    private ChangeLogUtils() {}

    private static final String EMPLOYEE_FILE_NAME = "employeeChanges.dat";
    private static final String FEEDBACK_FILE_NAME = "feedbackChanges.dat";
    private static final String REVIEW_FILE_NAME = "reviewChanges.dat";

    /**
     * Serializes and saves the list of employee change logs to the file {@value #EMPLOYEE_FILE_NAME}.
     * @param changes the list of {@link EmployeeChangeLog} to save
     * @throws RepositoryAccessException if an I/O error occurs during saving
     */
    public static void saveAllEmployeeChanges(List<EmployeeChangeLog> changes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMPLOYEE_FILE_NAME))) {
            oos.writeObject(changes);
        } catch (IOException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Reads and deserializes the list of employee change logs from the file {@value #EMPLOYEE_FILE_NAME}.
     * @return a list of {@link EmployeeChangeLog}, or an empty list if the file does not exist or is empty
     * @throws RepositoryAccessException if an I/O or class loading error occurs during reading
     */
    @SuppressWarnings("unchecked")
    public static List<EmployeeChangeLog> getAllEmployeeChanges() {
        File file = new File(EMPLOYEE_FILE_NAME);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<EmployeeChangeLog>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Serializes and saves the list of feedback change logs to the file {@value #FEEDBACK_FILE_NAME}.
     * @param changes the list of {@link FeedbackChangeLog} to save
     * @throws RepositoryAccessException if an I/O error occurs during saving
     */
    public static void saveAllFeedbackChanges(List<FeedbackChangeLog> changes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FEEDBACK_FILE_NAME))) {
            oos.writeObject(changes);
        } catch (IOException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Reads and deserializes the list of feedback change logs from the file {@value #FEEDBACK_FILE_NAME}.
     * @return a list of {@link FeedbackChangeLog}, or an empty list if the file does not exist or is empty
     * @throws RepositoryAccessException if an I/O or class loading error occurs during reading
     */
    @SuppressWarnings("unchecked")
    public static List<FeedbackChangeLog> getAllFeedbackChanges() {
        File file = new File(FEEDBACK_FILE_NAME);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<FeedbackChangeLog>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Serializes and saves the list of performance review change logs to the file {@value #REVIEW_FILE_NAME}.
     * @param changes the list of {@link PerformanceChangeLog} to save
     * @throws RepositoryAccessException if an I/O error occurs during saving
     */
    public static void saveAllReviewChanges(List<PerformanceChangeLog> changes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REVIEW_FILE_NAME))) {
            oos.writeObject(changes);
        } catch (IOException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Reads and deserializes the list of performance review change logs from the file {@value #REVIEW_FILE_NAME}.
     * @return a list of {@link PerformanceChangeLog}, or an empty list if the file does not exist or is empty
     * @throws RepositoryAccessException if an I/O or class loading error occurs during reading
     */
    @SuppressWarnings("unchecked")
    public static List<PerformanceChangeLog> getAllReviewChanges() {
        File file = new File(REVIEW_FILE_NAME);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<PerformanceChangeLog>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
    }
}
