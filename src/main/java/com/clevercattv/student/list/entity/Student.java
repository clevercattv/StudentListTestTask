package com.clevercattv.student.list.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.clevercattv.student.list.util.StudentValidationConstant.*;

@Data
@EqualsAndHashCode(of = {"firstName", "lastName", "university", "specialty", "semester", "age"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student implements Serializable {

    private static final long serialVersionUID = 413385207841147485L;

    @Id
    Long id;

    @NotNull
    @Size(min = MIN_FIRST_NAME_LENGTH, max = MAX_FIRST_NAME_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String firstName;

    @NotNull
    @Size(min = MIN_LAST_NAME_LENGTH, max = MAX_LAST_NAME_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String lastName;

    @NotNull
    @Size(min = MIN_UNIVERSITY_LENGTH, max = MAX_UNIVERSITY_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String university;

    @NotNull
    @Size(min = MIN_SPECIALTY_LENGTH, max = MAX_SPECIALTY_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String specialty;

    @NotNull
    @Min(MIN_SEMESTER)
    @Max(MAX_SEMESTER)
    Integer semester;

    @NotNull
    @Min(MIN_AGE)
    @Max(MAX_AGE)
    Integer age;

    @Builder.Default
    LocalDateTime creationTime = LocalDateTime.now();

}
