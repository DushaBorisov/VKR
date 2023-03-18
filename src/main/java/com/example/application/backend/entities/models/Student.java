package com.example.application.backend.entities.models;

import com.example.application.backend.entities.security.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "student_name")
    private String name;

    @Column(name = "student_surname")
    private String surname;

    @Column(name = "student_email")
    private String email;

    @Column(name = "student_phone_number")
    private String phone;

    @Column(name = "student_resume")
    private String resume;

    @Column(name = "desired_position")
    private String desiredPosition;

    @Column(name = "desired_salary")
    private Integer desiredSalary;

    @Column(name = "desired_employment")
    private String desiredEmployment;

    @Column(name = "search_status")
    private String searchStatus;

    @Column(name = "course_of_study")
    private String courseOfStudy;

    @Column(name = "experience")
    private Integer experience;
}
