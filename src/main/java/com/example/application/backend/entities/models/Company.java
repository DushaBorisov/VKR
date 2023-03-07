package com.example.application.backend.entities.models;

import com.example.application.backend.entities.security.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
public class Company {


    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "jobId")
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
