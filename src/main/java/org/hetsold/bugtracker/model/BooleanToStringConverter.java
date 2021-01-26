package org.hetsold.bugtracker.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean aBoolean) {
        return (aBoolean != null && aBoolean) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String s) {
        if ("Y".equals(s)) {
            return true;
        } else if ("N".equals(s)) {
            return false;
        }
        return null;
    }
}
