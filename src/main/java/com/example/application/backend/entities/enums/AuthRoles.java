package com.example.application.backend.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public enum AuthRoles {

    ROLE_USER("ROLE_USER"),
    ROLE_COMPANY("ROLE_COMPANY");

    @Getter
    @Setter
    private String roleName;
}
