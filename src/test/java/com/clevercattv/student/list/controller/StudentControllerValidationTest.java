package com.clevercattv.student.list.controller;

import com.clevercattv.student.list.config.R2dbcConfig;
import com.clevercattv.student.list.config.WebFluxConfig;
import com.clevercattv.student.list.dto.CreateStudentRequest;
import org.hamcrest.Matcher;
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

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.clevercattv.student.list.util.StudentValidationConstant.MAX_AGE;
import static com.clevercattv.student.list.util.StudentValidationConstant.MAX_FIRST_NAME_LENGTH;
import static com.clevercattv.student.list.util.StudentValidationConstant.MAX_SEMESTER;
import static com.clevercattv.student.list.util.StudentValidationConstant.MAX_UNIVERSITY_LENGTH;
import static com.clevercattv.student.list.util.StudentValidationConstant.MIN_AGE;
import static com.clevercattv.student.list.util.StudentValidationConstant.MIN_FIRST_NAME_LENGTH;
import static com.clevercattv.student.list.util.StudentValidationConstant.MIN_SEMESTER;
import static com.clevercattv.student.list.util.StudentValidationConstant.MIN_UNIVERSITY_LENGTH;
import static com.clevercattv.student.list.util.StudentValidationConstant.TEXT_PATTERN;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {R2dbcConfig.class, WebFluxConfig.class})
class StudentControllerValidationTest {

    private static final String NOT_NULL_MESSAGE = "Incorrect value! A null is forbidden";
    private static final String MAX_SEMESTER_MESSAGE = "Incorrect value! Maximum allowed value " + MAX_SEMESTER;
    private static final String MIN_SEMESTER_MESSAGE = "Incorrect value! Minimum allowed value " + MIN_SEMESTER;
    private static final String MAX_AGE_MESSAGE = "Incorrect value! Maximum allowed value " + MAX_AGE;
    private static final String MIN_AGE_MESSAGE = "Incorrect value! Minimum allowed value " + MIN_AGE;
    private static final String SIZE_NAME_MESSAGE = String.format("Incorrect length! Allowed length from %d to %d",
            MIN_FIRST_NAME_LENGTH, MAX_FIRST_NAME_LENGTH);
    private static final String SIZE_UNIVERSITY_MESSAGE = String.format("Incorrect length! Allowed length from %d to %d",
            MIN_UNIVERSITY_LENGTH, MAX_UNIVERSITY_LENGTH);
    private static final String PATTERN_MESSAGE = String.format("Incorrect symbols! Allowed symbols %s",
            TEXT_PATTERN.substring(TEXT_PATTERN.indexOf('[') + 1, TEXT_PATTERN.indexOf(']')));

    private static final String JSON_PATH_FIRST_NAME = "$.errors.firstName";
    private static final String JSON_PATH_LAST_NAME = "$.errors.lastName";
    private static final String JSON_PATH_UNIVERSITY = "$.errors.university";
    private static final String JSON_PATH_SPECIALTY = "$.errors.specialty";
    private static final String JSON_PATH_SEMESTER = "$.errors.semester";
    private static final String JSON_PATH_AGE = "$.errors.age";

    private static final CreateStudentRequest.CreateStudentRequestBuilder VALID_REQUEST_BUILDER = CreateStudentRequest.builder()
            .firstName("Anabal")
            .lastName("Gilhoolie")
            .university("University of the Philippines Mindanao")
            .specialty("Cyber security")
            .semester(6)
            .age(21);

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void createStudent_emptyRequest_ReturnValidationMessage() {
        Matcher<List<String>> notNullMatcher = Matchers.is(Collections.singletonList(NOT_NULL_MESSAGE));

        client.post()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(CreateStudentRequest.builder().build()), CreateStudentRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath(JSON_PATH_FIRST_NAME).value(notNullMatcher)
                .jsonPath(JSON_PATH_LAST_NAME).value(notNullMatcher)
                .jsonPath(JSON_PATH_UNIVERSITY).value(notNullMatcher)
                .jsonPath(JSON_PATH_SPECIALTY).value(notNullMatcher)
                .jsonPath(JSON_PATH_SEMESTER).value(notNullMatcher)
                .jsonPath(JSON_PATH_AGE).value(notNullMatcher);
    }

    @ParameterizedTest(name = "{index} [{1}] messages {2} request {0}")
    @MethodSource({
            "invalidFirstNameRequestArguments", "invalidLastNameRequestArguments",
            "invalidUniversityRequestArguments", "invalidSpecialtyRequestArguments",
            "invalidSemesterRequestArguments", "invalidAgeRequestArguments"})
    void createStudent_invalidRequest_ReturnValidationMessage(CreateStudentRequest request,
                                                              String path,
                                                              String[] messages) {
        client.post()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateStudentRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.errors.*").value(Matchers.hasSize(1))
                .jsonPath(path).value(Matchers.containsInAnyOrder(messages));
    }

    private static Stream<Arguments> invalidFirstNameRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setFirstName(null)),
                        JSON_PATH_FIRST_NAME,
                        new String[]{NOT_NULL_MESSAGE}
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setFirstName("")),
                        JSON_PATH_FIRST_NAME,
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setFirstName("a")),
                        JSON_PATH_FIRST_NAME,
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setFirstName("a$")),
                        JSON_PATH_FIRST_NAME,
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setFirstName("$")),
                        JSON_PATH_FIRST_NAME,
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setFirstName("Hector Sausage-Hausen King.")),
                        JSON_PATH_FIRST_NAME,
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setFirstName("Hector# Sausage-Hausen King.")),
                        JSON_PATH_FIRST_NAME,
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidLastNameRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setLastName(null)),
                        JSON_PATH_LAST_NAME,
                        new String[]{NOT_NULL_MESSAGE}
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setLastName("")),
                        JSON_PATH_LAST_NAME,
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setLastName("a")),
                        JSON_PATH_LAST_NAME,
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setLastName("a$")),
                        JSON_PATH_LAST_NAME,
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setLastName("$")),
                        JSON_PATH_LAST_NAME,
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setLastName("Hector Sausage-Hausen King.")),
                        JSON_PATH_LAST_NAME,
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setLastName("Hector# Sausage-Hausen King.")),
                        JSON_PATH_LAST_NAME,
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidUniversityRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setUniversity(null)),
                        JSON_PATH_UNIVERSITY,
                        new String[]{NOT_NULL_MESSAGE}
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setUniversity("")),
                        JSON_PATH_UNIVERSITY,
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setUniversity("a")),
                        JSON_PATH_UNIVERSITY,
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setUniversity("a$")),
                        JSON_PATH_UNIVERSITY,
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setUniversity("$")),
                        JSON_PATH_UNIVERSITY,
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setUniversity("The University of Manchester The University of Manchester Manchester")),
                        JSON_PATH_UNIVERSITY,
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setUniversity("The University# of Manchester The University of Manchester Manchester")),
                        JSON_PATH_UNIVERSITY,
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidSpecialtyRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setSpecialty(null)),
                        JSON_PATH_SPECIALTY,
                        new String[]{NOT_NULL_MESSAGE}
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setSpecialty("")),
                        JSON_PATH_SPECIALTY,
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setSpecialty("a")),
                        JSON_PATH_SPECIALTY,
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setSpecialty("a$")),
                        JSON_PATH_SPECIALTY,
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setSpecialty("$")),
                        JSON_PATH_SPECIALTY,
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setSpecialty("The University of Manchester The University of Manchester Manchester")),
                        JSON_PATH_SPECIALTY,
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setSpecialty("The University# of Manchester The University of Manchester Manchester")),
                        JSON_PATH_SPECIALTY,
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidSemesterRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setSemester(null)),
                        JSON_PATH_SEMESTER,
                        new String[]{NOT_NULL_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setSemester(0)),
                        JSON_PATH_SEMESTER,
                        new String[]{MIN_SEMESTER_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setSemester(21)),
                        JSON_PATH_SEMESTER,
                        new String[]{MAX_SEMESTER_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidAgeRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setAge(null)),
                        JSON_PATH_AGE,
                        new String[]{NOT_NULL_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setAge(0)),
                        JSON_PATH_AGE,
                        new String[]{MIN_AGE_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setAge(201)),
                        JSON_PATH_AGE,
                        new String[]{MAX_AGE_MESSAGE}
                )
        );
    }

    private static CreateStudentRequest buildRequest(Consumer<CreateStudentRequest> consumer) {
        CreateStudentRequest request = VALID_REQUEST_BUILDER.build();
        consumer.accept(request);
        return request;
    }


}
