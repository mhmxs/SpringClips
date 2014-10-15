package com.github.springclips.utils.converter;

public interface Convertable<E> {
    Object convert();

    E getFromValue(Object value);
}
