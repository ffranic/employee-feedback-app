package hr.javafx.tvz.projektni_zadatak.model;

import lombok.Data;

/**
 * A generic class that represents a pair of two values.
 * Useful for grouping two related objects together.
 * @param <T> the type of the first value
 * @param <U> the type of the second value
 */
@Data
public class Pair<T, U> {

    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

}
