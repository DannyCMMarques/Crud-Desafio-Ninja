package com.crud.demo.utils.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> lista) {
        if (lista == null || lista.isEmpty()) return "{}";
        return "{" + String.join(",", lista) + "}";
    }

    @Override
    public List<String> convertToEntityAttribute(String banco) {
        if (banco == null || banco.equals("{}")) return new ArrayList<>();
        return Arrays.asList(banco.replaceAll("[{}]", "").split(","));
    }
}