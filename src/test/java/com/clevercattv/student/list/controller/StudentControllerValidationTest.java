package com.clevercattv.student.list.controller;

import com.clevercattv.student.list.config.R2dbcConfig;
import com.clevercattv.student.list.config.WebFluxConfig;
import com.clevercattv.student.list.dto.CreateStudentRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.function.Consumer;
import java.util.stream.Stream;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {R2dbcConfig.class, WebFluxConfig.class})
class StudentControllerValidationTest {

    private static final String NOT_NULL_MESSAGE = "Incorrect value! A null is forbidden";
    private static final String MIN_SEMESTER_MESSAGE = "Incorrect value! Minimum allowed value 1";
    private static final String MAX_SEMESTER_MESSAGE = "Incorrect value! Maximum allowed value 20";
    private static final String MIN_AGE_MESSAGE = "Incorrect value! Minimum allowed value 1";
    private static final String MAX_AGE_MESSAGE = "Incorrect value! Maximum allowed value 200";
    private static final String SIZE_NAME_MESSAGE = "Incorrect length! Allowed length from 2 to 26";
    private static final String SIZE_UNIVERSITY_MESSAGE = "Incorrect length! Allowed length from 2 to 63";
    private static final String PATTERN_MESSAGE = "Incorrect symbols! Allowed symbols a-zA-Z ,.'-";

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

    @ParameterizedTest(name = "[{index}] [{1}] messages {2} request {0}")
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
                .jsonPath(path).value(Matchers.containsInAnyOrder(messages));
    }

    private static Stream<Arguments> invalidFirstNameRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setFirstName(null)), // change valid param to invalid
                        "$.firstName[*]", // jsonPath
                        new String[]{NOT_NULL_MESSAGE} // validation messages
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setFirstName("")),
                        "$.firstName[*]",
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setFirstName("a")),
                        "$.firstName[*]",
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setFirstName("a$")),
                        "$.firstName[*]",
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setFirstName("$")),
                        "$.firstName[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setFirstName("Hector Sausage-Hausen King.")),
                        "$.firstName[*]",
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setFirstName("Hector# Sausage-Hausen King.")),
                        "$.firstName[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidLastNameRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setLastName(null)), // change valid param to invalid
                        "$.lastName[*]", // jsonPath
                        new String[]{NOT_NULL_MESSAGE} // validation messages
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setLastName("")),
                        "$.lastName[*]",
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setLastName("a")),
                        "$.lastName[*]",
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setLastName("a$")),
                        "$.lastName[*]",
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setLastName("$")),
                        "$.lastName[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setLastName("Hector Sausage-Hausen King.")),
                        "$.lastName[*]",
                        new String[]{SIZE_NAME_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setLastName("Hector# Sausage-Hausen King.")),
                        "$.lastName[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_NAME_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidUniversityRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setUniversity(null)), // change valid param to invalid
                        "$.university[*]", // jsonPath
                        new String[]{NOT_NULL_MESSAGE} // validation messages
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setUniversity("")),
                        "$.university[*]",
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setUniversity("a")),
                        "$.university[*]",
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setUniversity("a$")),
                        "$.university[*]",
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setUniversity("$")),
                        "$.university[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setUniversity("The University of Manchester The University of Manchester Manchester")),
                        "$.university[*]",
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setUniversity("The University# of Manchester The University of Manchester Manchester")),
                        "$.university[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidSpecialtyRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setSpecialty(null)), // change valid param to invalid
                        "$.specialty[*]", // jsonPath
                        new String[]{NOT_NULL_MESSAGE} // validation messages
                ),
                Arguments.of( // Empty value
                        buildRequest(request -> request.setSpecialty("")),
                        "$.specialty[*]",
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setSpecialty("a")),
                        "$.specialty[*]",
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern
                        buildRequest(request -> request.setSpecialty("a$")),
                        "$.specialty[*]",
                        new String[]{PATTERN_MESSAGE}
                ),
                Arguments.of( // Pattern + Min size
                        buildRequest(request -> request.setSpecialty("$")),
                        "$.specialty[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setSpecialty("The University of Manchester The University of Manchester Manchester")),
                        "$.specialty[*]",
                        new String[]{SIZE_UNIVERSITY_MESSAGE}
                ),
                Arguments.of( // Pattern + Max size
                        buildRequest(request -> request.setSpecialty("The University# of Manchester The University of Manchester Manchester")),
                        "$.specialty[*]",
                        new String[]{PATTERN_MESSAGE, SIZE_UNIVERSITY_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidSemesterRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setSemester(null)), // change valid param to invalid
                        "$.semester[*]", // jsonPath
                        new String[]{NOT_NULL_MESSAGE} // validation messages
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setSemester(0)),
                        "$.semester[*]",
                        new String[]{MIN_SEMESTER_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setSemester(21)),
                        "$.semester[*]",
                        new String[]{MAX_SEMESTER_MESSAGE}
                )
        );
    }

    private static Stream<Arguments> invalidAgeRequestArguments() {
        return Stream.of(
                Arguments.of( // null
                        buildRequest(request -> request.setAge(null)), // change valid param to invalid
                        "$.age[*]", // jsonPath
                        new String[]{NOT_NULL_MESSAGE} // validation messages
                ),
                Arguments.of( // Min size
                        buildRequest(request -> request.setAge(0)),
                        "$.age[*]",
                        new String[]{MIN_AGE_MESSAGE}
                ),
                Arguments.of( // Max size
                        buildRequest(request -> request.setAge(201)),
                        "$.age[*]",
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
