package hr.javafx.tvz.projektni_zadatak.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for establishing a database connection using properties defined
 * in the {@code database.properties} configuration file.
 * This class is not meant to be instantiated and provides a static method
 * for creating a database connection.
 */
public class DatabaseUtil {

    private DatabaseUtil() {}

    /**
     * Establishes and returns a new database connection using configuration values
     * from the {@code database.properties} file.
     * This method is synchronized to ensure thread-safe access when used in
     * multi-threaded environments.
     * @return a {@link Connection} object representing the active connection to the database
     * @throws SQLException if an error occurs while connecting to the database
     * @throws IOException if an error occurs while reading the properties file
     */
    public static synchronized Connection connectToDatabase() throws SQLException, IOException {

        Properties props = new Properties();
        try (FileReader fileReader = new FileReader("database.properties")) {
            props.load(fileReader);
            return DriverManager.getConnection(props.getProperty("databaseUrl"),
                    props.getProperty("username"), props.getProperty("password"));
        }
    }
}
