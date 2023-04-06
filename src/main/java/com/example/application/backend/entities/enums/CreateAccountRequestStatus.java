package com.example.application.backend.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public enum CreateAccountRequestStatus {

    REQUEST_CREATED,
    REQUEST_CONFIRMED,
    REQUEST_DENIED;

    @Getter
    @Setter
    private String status;
}
