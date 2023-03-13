package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

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

    public Student getStudentById(Long studentId) {
        return entityManager.createQuery("select s from Student s where s.studentId = :id", Student.class)
                .setParameter("id", studentId)
                .getSingleResult();
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
    public void updateStudent(Student student){
        entityManager.merge(student);
    }

}
