package com.clevercattv.student.list.repository;

import com.clevercattv.student.list.entity.Book;
import com.clevercattv.student.list.entity.Student;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HibernateStudentDao {

    private final SessionFactory sessionFactory;

    public Student save(Student student) {
        Session session = sessionFactory.getCurrentSession();
        Long savedId = Long.valueOf(String.valueOf(session.save(student)));
        session.save(Book.builder() // add test book for fast testing
                .name("test")
                .student(session.get(Student.class, savedId))
                .build()
        );
        return student;
    }

    public List<Student> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from STUDENT", Student.class)
                .getResultList();
    }

}
