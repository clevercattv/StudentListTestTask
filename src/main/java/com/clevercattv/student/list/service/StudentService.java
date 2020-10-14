package com.clevercattv.student.list.service;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.repository.StudentRepository;
import com.clevercattv.student.list.service.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;

    public Mono<Student> create(CreateStudentRequest createRequest) {
        Student student = mapper.toEntity(createRequest);
        return repository.save(student);
    }

    @Transactional(readOnly = true)
    public Flux<Student> readAll() {
        return repository.findAll();
    }

}
