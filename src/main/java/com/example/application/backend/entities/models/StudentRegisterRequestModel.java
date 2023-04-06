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
@Table(name = "create_student_account_requests")
public class StudentRegisterRequestModel {

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(name = "student_first_name")
    private String studentFirstName;

    @Column(name = "student_last_name")
    private String studentLastName;

    @Column(name = "student_course_of_study")
    private Integer studentCourseOfStudy;

    @Column(name = "student_document_number")
    private String studentDocumentNumber;

    @Column(name = "student_phone_number")
    private String studentPhoneNumber;

    @Column(name = "student_email")
    private String studentEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private CreateAccountRequestStatus requestStatus;

    @Column(name = "admin_comments")
    private String adminComments;

    @Column(name = "request_date")
    private LocalDateTime requestDate;
}
