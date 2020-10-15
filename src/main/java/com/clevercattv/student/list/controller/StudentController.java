package com.clevercattv.student.list.controller;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.service.StudentService;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StudentController {

    private final StudentService service;

    @GetMapping
    public Flux<Student> readAll() {
        log.info("call GET [/student]");
        return service.readAll();
    }

    @PostMapping(consumes = "application/json")
    public Mono<Student> createStudent(@Valid @RequestBody CreateStudentRequest createRequest) {
        log.info("call POST [/student]");
        return service.create(createRequest);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDatabaseException(DataIntegrityViolationException exception) {
        log.error("Database exception", exception);
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
