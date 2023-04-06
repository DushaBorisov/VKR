package com.example.application.backend.entities.models;

import com.example.application.backend.entities.enums.CreateAccountRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "create_company_account_requests")
public class CompanyRegisterRequestModel {

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_phone_number")
    private String companyPhoneNumber;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_description")
    private String companyDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private CreateAccountRequestStatus requestStatus;

    @Column(name = "admin_comments")
    private String adminComments;

    @Column(name = "request_date")
    private LocalDateTime requestDate;
}
