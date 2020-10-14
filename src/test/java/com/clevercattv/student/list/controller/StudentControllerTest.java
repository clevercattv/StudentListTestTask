package com.clevercattv.student.list.controller;

import com.clevercattv.student.list.config.R2dbcConfig;
import com.clevercattv.student.list.config.ValidationConfig;
import com.clevercattv.student.list.config.WebFluxConfig;
import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.exception.GlobalExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.stream.Stream;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {R2dbcConfig.class, WebFluxConfig.class, ValidationConfig.class, GlobalExceptionHandler.class})
class StudentControllerTest {

    private static final CreateStudentRequest.CreateStudentRequestBuilder VALID_REQUEST_BUILDER = CreateStudentRequest.builder()
            .firstName("Anabal")
            .lastName("Gilhoolie")
            .university("University of the Philippines Mindanao")
            .specialty("Cyber security")
            .semester(6)
            .entryDate(LocalDate.now())
            .age(21)
            .creationTime(LocalDateTime.now());

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void createStudent_ValidRequest_ReturnStudent() {
        CreateStudentRequest request = VALID_REQUEST_BUILDER.build();
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
                .returnResult()
                .getResponseBody();
    }

    @ParameterizedTest
    @MethodSource("createStudentRequests")
    void createStudent_invalidRequest_ReturnValidationMessage(
            CreateStudentRequest request, String path, String[] messages) {
        client.post()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateStudentRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath(path).value(Matchers.containsInAnyOrder(messages));
    }

    private static Stream<Arguments> createStudentRequests() {

        return Stream.of(
                Arguments.of(
                        buildRequest(request -> request.setFirstName("")), // change param to invalid
                        "$.firstName[*]",
                        new String[]{"Incorrect symbols, allowed [a-zA-Z ,.'-]", "Incorrect first name size!"}
                ),
                Arguments.of(
                        buildRequest(request -> request.setFirstName("a")), // change param to invalid
                        "$.firstName[*]",
                        new String[]{"Incorrect first name size!"}
                ),
                Arguments.of(
                        buildRequest(request -> request.setLastName("")), // change param to invalid
                        "$.lastName[*]",
                        new String[]{"Incorrect symbols, allowed [a-zA-Z ,.'-]", "Incorrect last name size!"}
                ),
                Arguments.of(
                        buildRequest(request -> request.setLastName("a")), // change param to invalid
                        "$.lastName[*]",
                        new String[]{"Incorrect last name size!"}
                )
        );
    }

    private static CreateStudentRequest buildRequest(@NotNull Consumer<CreateStudentRequest>... consumers) {
        CreateStudentRequest request = VALID_REQUEST_BUILDER.build();
        for (Consumer<CreateStudentRequest> consumer : consumers) {
            consumer.accept(request);
        }
        return request;
    }


}
