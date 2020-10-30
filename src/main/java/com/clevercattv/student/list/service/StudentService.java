package com.clevercattv.student.list.service;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.dto.StudentResponse;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.exception.NoSuchStudentException;
import com.clevercattv.student.list.repository.HibernateStudentDao;
import com.clevercattv.student.list.repository.StudentRepository;
import com.clevercattv.student.list.service.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "connectionFactoryTransactionManager")
public class StudentService {

    private final HibernateStudentDao dao;
    private final StudentRepository repository;
    private final StudentMapper mapper;

    // now return Unsupported array type: com...Book (need find how ignore field on save)
    public Mono<StudentResponse> create(CreateStudentRequest createRequest) {
        Student student = mapper.toEntity(createRequest);
        return repository.save(student).map(mapper::toResponse);
    }

    @Transactional(transactionManager = "transactionManager")
    public StudentResponse createStudentHibernate(CreateStudentRequest createRequest) {
        Student student = mapper.toEntity(createRequest);
        return mapper.toResponse(dao.save(student));
    }

    @Transactional(readOnly = true, transactionManager = "connectionFactoryTransactionManager")
    public Mono<StudentResponse> readOne(Long id) {
        return readStudent(id).map(mapper::toResponse);
    }

    @Transactional(readOnly = true, transactionManager = "connectionFactoryTransactionManager")
    public Flux<StudentResponse> readAll() {
        return repository.findAll()
                .map(mapper::toResponse);
    }

    @Transactional(transactionManager = "transactionManager")
    public List<StudentResponse> readAllHibernate() {
        return dao.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(transactionManager = "transactionManager") // Lazy loading available inside transaction, outside we get exception
    public List<Student> readAllStudentHibernate() {
        return dao.findAll()
                .stream()
                .map(mapper::toEntity) // map Hibernate Proxy to Student and load lazy collections books (call get methods)
                .collect(Collectors.toList());
    }

    public Mono<Void> deleteOne(Long id) {
        return readStudent(id)
                .flatMap(repository::delete);
    }

    private Mono<Student> readStudent(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        () -> new NoSuchStudentException(String.format("Can't find user with id:%s", id))));
    }

}
