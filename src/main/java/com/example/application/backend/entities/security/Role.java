package com.example.application.backend.entities.security;

import lombok.*;

import javax.persistence.*;


@Entity
@Data
@Table(name = "role")
@AllArgsConstructor
public class Role  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}
