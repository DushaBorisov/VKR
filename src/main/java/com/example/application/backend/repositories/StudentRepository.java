package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.models.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public Student getStudentByUserName(String userMane) {
        return entityManager.createQuery("select s from Student s where s.user.username = :name", Student.class)
                .setParameter("name", userMane)
                .getSingleResult();
    }

    public Optional<Student> getStudentById(Long studentId) {
        Student student;
        try {
            student = entityManager.createQuery("select s from Student s where s.studentId = :id", Student.class)
                    .setParameter("id", studentId)
                    .getSingleResult();
        }catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(student);
    }

    public List<Student> getAllStudents() {
        return entityManager.createQuery("select s from Student s ", Student.class).getResultList();
    }

    @Transactional
    public void addNewStudent(Student student) {
        entityManager.persist(student);
    }

    @Transactional
    public void removeAllStudents() {
        entityManager.createQuery("delete from Student").executeUpdate();
    }

    @Transactional
    public void removeStudent(Student student) {
        entityManager.remove(student);
    }

    @Transactional
    public void updateStudent(Student student){
        entityManager.merge(student);
    }

}
