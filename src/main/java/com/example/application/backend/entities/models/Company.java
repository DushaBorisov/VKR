package com.example.application.backend.entities.models;

import com.example.application.backend.entities.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
public class Company {


    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "jobId", cascade =CascadeType.REMOVE)
    private Set<Job> jobs;

    @Column(name = "company_name")
    private String name;

    @Column(name = "company_description")
    private String description;

    @Column(name = "company_email")
    private String email;

    @Column(name = "company_phone_number")
    private String phoneNumber;

}
