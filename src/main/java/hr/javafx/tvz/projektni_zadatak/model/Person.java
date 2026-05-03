package hr.javafx.tvz.projektni_zadatak.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract class representing a person entity with common personal information.
 * Inherits the unique identifier from {@link Entity}.
 * Used as a base class for more specific types of people, such as {@code Employee}.
 */
@Data
public abstract class Person extends Entity implements Serializable {

    private String firstName;
    private String lastName;

    protected Person() {
        super();
    }

    protected Person(int id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Person other = (Person) obj;
        return Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return super.toString() + ", name: " + firstName + ", surname: " + lastName;
    }
}
