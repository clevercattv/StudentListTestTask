package com.clevercattv.student.list.service;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.dto.StudentResponse;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.repository.HibernateStudentDao;
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

    private final HibernateStudentDao dao;
    private final StudentMapper mapper;

    public StudentResponse create(CreateStudentRequest createRequest) {
        Student student = mapper.toEntity(createRequest);
        return mapper.toResponse(dao.save(student));
    }

    @Transactional(transactionManager = "transactionManager")
    public List<StudentResponse> readAll() {
        return dao.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // Lazy loading available inside transaction, outside we get exception
    public List<Student> readAllStudent() {
        return dao.findAll()
                .stream()
                .map(mapper::toEntity) // map Hibernate Proxy to Student and load lazy collections books (call get methods)
                .collect(Collectors.toList());
    }


}
