package hr.javafx.tvz.projektni_zadatak.model;

import hr.javafx.tvz.projektni_zadatak.enums.Role;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an employee in the system. Inherits basic personal information from {@link Person}
 * and adds email, salary and role information.
 */
@Data
public class Employee extends Person implements Serializable {

    private String email;
    private BigDecimal salary;
    private Role role;

    public Employee() {}

    public Employee(int id, String firstName, String lastName, String email, BigDecimal salary, Role role) {
        super(id, firstName, lastName);
        this.email = email;
        this.salary = salary;
        this.role = role;
    }

    public Employee(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.email = builder.email;
        this.salary = builder.salary;
        this.role = builder.role;
    }

    public static class Builder {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private BigDecimal salary;
        private Role role;

        public Builder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder salary(BigDecimal salary) {
            this.salary = salary;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    public Employee(int id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (this.getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Employee other = (Employee) obj;
        return Objects.equals(email, other.email) &&
                Objects.equals(role, other.role) &&
                Objects.equals(salary, other.salary);
    }

    @Override
    public String toString() {
        return super.toString() + ", email: " + email + ", salary: " + salary +  ", role: " + role;
    }
}
