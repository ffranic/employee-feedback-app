package hr.javafx.tvz.projektni_zadatak.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a base abstract class for all entity objects that are identifiable by a unique ID.
 * Provides standard implementations for {@code equals}, {@code hashCode}, and {@code toString} methods
 * based on the entity's {@code id}.
 */
@Data
public abstract class Entity implements Serializable {

    private int id;

    protected Entity() {}

    protected Entity(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (getClass() != obj.getClass()) return false;

        Entity other = (Entity) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "ID: " + id;
    }
}
