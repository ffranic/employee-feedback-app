package hr.javafx.tvz.projektni_zadatak.enums;

/**
 * Enumeration representing user roles within the system.
 * Roles determine the access level and permissions of a user.
 */
public enum Role {
    ADMIN, MANAGER, EMPLOYEE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
