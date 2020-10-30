package com.clevercattv.student.list.service;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.dto.StudentResponse;
import com.clevercattv.student.list.entity.Book;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.repository.jpa.StudentJpaRepository;
import com.clevercattv.student.list.service.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
public class StudentJpaService {

    private final StudentJpaRepository jpaRepository;
    private final StudentMapper mapper;

    public StudentResponse create(CreateStudentRequest createRequest) {
        Student student = mapper.toEntity(createRequest);
        student.getBooks().add(Book.builder().name("test").student(student).build());
        return mapper.toResponse(jpaRepository.save(student));
    }

    @Transactional(readOnly = true, transactionManager = "transactionManager")
    public List<StudentResponse> readAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true, transactionManager = "transactionManager")
    public List<Student> readAllLazy() {
        return jpaRepository.findAll();
    }

}
