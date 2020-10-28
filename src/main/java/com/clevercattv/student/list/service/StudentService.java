package com.clevercattv.student.list.service;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.dto.StudentResponse;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.exception.NoSuchStudentException;
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

    public Mono<StudentResponse> create(CreateStudentRequest createRequest) {
        Student student = mapper.toEntity(createRequest);
        return repository.save(student).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Mono<StudentResponse> readOne(Long id) {
        return readStudent(id).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Flux<StudentResponse> readAll() {
        return repository.findAll()
                .map(mapper::toResponse);
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
