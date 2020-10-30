package com.clevercattv.student.list.controller;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.dto.StudentResponse;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.service.StudentJpaService;
import com.clevercattv.student.list.service.StudentService;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StudentController {

    private final StudentService service;
    private final StudentJpaService jpaService;

    @GetMapping("/{id}")
    public Mono<StudentResponse> readOne(@PathVariable("id") Long id) {
        log.info("GET [/student/{}] - read Student", id);
        return service.readOne(id);
    }

    @GetMapping
    public Flux<StudentResponse> readAll() {
        log.info("GET [/student] - read all Students");
        return service.readAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest createRequest) {
        log.info("POST [/student] - create {}", createRequest);
        return service.create(createRequest);
    }

    @GetMapping("/jpa")
    public List<StudentResponse> readAllJpa() {
        log.info("GET [/student/jpa] - read all Students");
        return jpaService.readAll();
    }

    @GetMapping("/jpa/lazy")
    public List<Student> readAllJpaLazy() {
        log.info("GET [/student/jpa/lazy] - read all Students");
        return jpaService.readAllLazy();
    }

    @PostMapping(value = "/jpa",consumes = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public StudentResponse createStudentJpa(@Valid @RequestBody CreateStudentRequest createRequest) {
        log.info("POST [/student/jpa] - create {}", createRequest);
        return jpaService.create(createRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<Void> deleteOne(@PathVariable("id") Long id) {
        log.info("DELETE [/student/{}] - delete Student", id);
        return service.deleteOne(id);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDatabaseException(DataIntegrityViolationException exception) {
        log.error("DataIntegrityViolationException", exception);
        if (exception.getCause() instanceof R2dbcDataIntegrityViolationException) {
            R2dbcDataIntegrityViolationException cause =
                    (R2dbcDataIntegrityViolationException) exception.getCause();
            // have no other idea how to get what type of exception it is
            if (cause.getErrorCode() == 23505) {
                return new ResponseEntity<>(
                        Collections.singletonMap("error", "Such a student already exists in the database"),
                        new HttpHeaders(), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
