package com.clevercattv.student.list.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.clevercattv.student.list.entity.Student.NAME_PATTERN;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateStudentRequest {

    @Size(min = 2, max = 26, message = "Incorrect first name size!")
    @Pattern(regexp = NAME_PATTERN, message = "Incorrect symbols, allowed [a-zA-Z ,.'-]")
    String firstName;

    @Size(min = 2, max = 26, message = "Incorrect last name size!")
    @Pattern(regexp = NAME_PATTERN, message = "Incorrect symbols, allowed [a-zA-Z ,.'-]")
    String lastName;

    @Size(min = 2, max = 127, message = "Incorrect last name size!")
    String university;
    String specialty;
    Integer semester;

    Integer age;

    LocalDate entryDate;
    LocalDateTime creationTime;

}
