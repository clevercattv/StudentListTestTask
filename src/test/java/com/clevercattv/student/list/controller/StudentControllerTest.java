package com.clevercattv.student.list.controller;

import com.clevercattv.student.list.config.R2dbcConfig;
import com.clevercattv.student.list.config.WebFluxConfig;
import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.entity.Student;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {R2dbcConfig.class, WebFluxConfig.class})
class StudentControllerTest {

    private static final Student EXPECTED_STUDENT_1 = Student.builder()
            .firstName("Anny")
            .lastName("Hallbord")
            .university("Francis Marion University")
            .specialty("Indian Statistical Institute")
            .semester(11)
            .age(38)
            .build();

    private static final Student EXPECTED_STUDENT_2 = Student.builder()
            .firstName("Jackie")
            .lastName("Hallbord")
            .university("Francis Marion University")
            .specialty("Indian Statistical Institute")
            .semester(7)
            .age(33)
            .build();

    private static final Student EXPECTED_STUDENT_3 = Student.builder()
            .firstName("Ogden")
            .lastName("Abbati")
            .university("Francis Marion University")
            .specialty("Indian Statistical Institute")
            .semester(3)
            .age(22)
            .build();

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test // schema.sql creates these students
    void getStudent_ValidCall_ReturnStudents() {
        client.get()
                .uri("/student")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Student.class)
                .contains(EXPECTED_STUDENT_1, EXPECTED_STUDENT_2, EXPECTED_STUDENT_3);
    }

    @Test
    void createStudent_EmptyBody_ReturnErrorMessage() {
        client.post()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.error").isNotEmpty();
    }

    @Test
    void createStudent_DuplicateStudent_ReturnErrorMessage() {
        client.post()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(EXPECTED_STUDENT_1), CreateStudentRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.error").value(Matchers.is("Such a student already exists in the database"));
    }

    @ParameterizedTest
    @MethodSource("validRequestsMethodSource")
    void createStudent_ValidRequest_ReturnStudent(CreateStudentRequest request) {
        client.post()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateStudentRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").value(Matchers.is(request.getFirstName()))
                .jsonPath("$.lastName").value(Matchers.is(request.getLastName()))
                .jsonPath("$.university").value(Matchers.is(request.getUniversity()))
                .jsonPath("$.specialty").value(Matchers.is(request.getSpecialty()))
                .jsonPath("$.semester").value(Matchers.is(request.getSemester()))
                .jsonPath("$.age").value(Matchers.is(request.getAge()))
                .jsonPath("$.creationTime").isNotEmpty();
    }

    private static Stream<CreateStudentRequest> validRequestsMethodSource() {
        return Stream.of(
                CreateStudentRequest.builder()
                        .firstName("Ogden")
                        .lastName("Hallbord")
                        .university("Francis Marion University")
                        .specialty("Indian Statistical Institute")
                        .semester(11)
                        .age(46)
                        .build(),
                CreateStudentRequest.builder()
                        .firstName("Anny")
                        .lastName("Abbati")
                        .university("Fukuoka Dental College")
                        .specialty("Koyasan University")
                        .semester(11)
                        .age(38)
                        .build(),
                CreateStudentRequest.builder()
                        .firstName("Jackie")
                        .lastName("Goff")
                        .university("Universidad San Juan de la Cruz")
                        .specialty("Instituto Nacional de Educacion")
                        .semester(12)
                        .age(134)
                        .build()
        );
    }

}
