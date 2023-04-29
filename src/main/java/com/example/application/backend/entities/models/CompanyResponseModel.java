package com.example.application.backend.entities.models;

import com.example.application.backend.entities.security.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company_responses")
public class CompanyResponseModel {

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

    @Column(name = "student_answer")
    private Boolean studentAnswer;
}
