package com.clevercattv.student.list.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.clevercattv.student.list.util.StudentValidationConstant.*;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateStudentRequest {

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

}
