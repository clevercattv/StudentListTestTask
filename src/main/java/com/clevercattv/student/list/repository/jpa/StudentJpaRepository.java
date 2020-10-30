package com.clevercattv.student.list.repository.jpa;

import com.clevercattv.student.list.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, Long> {



}
