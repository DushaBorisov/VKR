package com.example.application.backend.entities.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_responses")
public class StudentResponseModel {

    @Id
    @Column(name = "response_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "company_answer")
    private Boolean companyAnswer;
}
