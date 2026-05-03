package hr.javafx.tvz.projektni_zadatak.authentification;

import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.exceptions.FileAccessException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Service class responsible for loading user data and performing authentication.
 * Users are loaded from a local text file and stored in memory.
 */
@Slf4j
public class UserService {

    private final Map<String, User> users = new HashMap<>();

    /**
     * Loads users from a predefined file and stores them in memory.
     * Each line in the file should be formatted as:
     * {@code id,username,passwordHash,role}
     *
     * @throws FileAccessException if the file cannot be read
     */
    public void loadUsers() throws FileAccessException {
        Path file = Path.of("userData/employees.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int userId = Integer.parseInt(data[0]);
                String username = data[1];
                String passwordHash = data[2];
                Role role = Role.valueOf(data[3]);
                users.put(username, new User(userId, username, passwordHash, role));
            }
        } catch (IOException exc) {
            log.error("Unable to read file: {}", exc.getMessage());
            throw new FileAccessException("Unable to read file", exc);
        }
    }

    /**
     * Authenticates a user by verifying the provided credentials against stored data.
     * @param username the username of the user attempting to log in
     * @param password the plain-text password to verify
     * @return an {@link Optional} containing the authenticated {@link User} if credentials are valid,
     *         or an empty {@code Optional} if authentication fails
     * @throws FileAccessException if user data cannot be loaded
     */
    public Optional<User> authenticate(String username, String password) throws FileAccessException {
        loadUsers();
        User user = users.get(username);
        if (user != null && BCrypt.checkpw(password, user.passwordHash())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
