package com.github.yasbc.utils.converter;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

public abstract class AbstractEnumConverter implements Converter {

    public abstract Convertable getConvertableEnum();

    @Override
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    public void initialize(final DatabaseMapping mapping, final Session session) {
        // Edit if necessary
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object convertDataValueToObjectValue(final Object data, final Session session) {
        if (data == null) {
            return getConvertableEnum();
        }

        final Object convertableEnum = getConvertableEnum().getFromValue(data);

        if (convertableEnum == null) {
            throw new IllegalArgumentException(
                    "Data not with a value suitable got [" + data.getClass() + " : " + data
                            + "] expected a valid value of ["
                            + getConvertableEnum().getClass() + "]"
            );
        } else {
            return convertableEnum;
        }
    }

    @Override
    public Object convertObjectValueToDataValue(final Object data, final Session session) {
        if (data == null) {
            return getConvertableEnum().convert();
        }

        if (data instanceof Convertable) {
            return ((Convertable) data).convert();
        }

        throw new IllegalArgumentException("Data not of correct type got ["
                + data.getClass() + "] expected [Convertable]");
    }
}
