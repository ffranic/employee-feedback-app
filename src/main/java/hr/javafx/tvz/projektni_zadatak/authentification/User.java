package hr.javafx.tvz.projektni_zadatak.authentification;

import hr.javafx.tvz.projektni_zadatak.enums.Role;

/**
 * Represents an authenticated user of the application.
 * Contains user identification, credentials (hashed), and role-based access information.
 *
 * @param id           the unique identifier of the user
 * @param username     the user's username
 * @param passwordHash the user's hashed password
 * @param role         the user's role in the system
 */
public record User(int id, String username, String passwordHash, Role role) {

}
