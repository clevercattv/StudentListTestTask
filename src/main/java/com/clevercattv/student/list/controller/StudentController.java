package com.clevercattv.student.list.controller;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.entity.Student;
import com.clevercattv.student.list.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService service;

    @GetMapping
    public Flux<Student> readAll() {
        return service.readAll();
    }

    @PostMapping(consumes = "application/json")
    public Mono<Student> createStudent(@Valid @RequestBody CreateStudentRequest createRequest) {
        return service.create(createRequest);
    }

}
