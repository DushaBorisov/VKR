package com.example.application.backend.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public enum EmploymentEnum {

    FULL("Полная"),
    PART("Частичная"),
    INTERNSHIP("Стажировка");

    private static final Map<String, EmploymentEnum> lookup = new HashMap<>();

    static {
        for (EmploymentEnum en : EmploymentEnum.values()) {
            lookup.put(en.getEmploymentType(), en);
        }
    }

    public static EmploymentEnum getEnumByStringValue(String stringValue) {
        return lookup.get(stringValue);
    }

    @Getter
    @Setter
    private String employmentType;
}
