package com.github.yasbc.utils.converter;

public interface Convertable<E> {
    Object convert();

    E getFromValue(Object value);
}
