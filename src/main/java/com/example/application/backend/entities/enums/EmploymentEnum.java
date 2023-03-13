package com.example.application.backend.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public enum EmploymentEnum {

    FULL("Полная"),
    PART("Частичная"),
    INTERNSHIP("Стажировка");

    @Getter
    @Setter
    private String employmentType;
}
