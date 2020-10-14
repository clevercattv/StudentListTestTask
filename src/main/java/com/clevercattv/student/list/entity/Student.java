package com.clevercattv.student.list.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"firstName", "lastName"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student implements Serializable {

    private static final long serialVersionUID = 413385207841147485L;
    public static final String NAME_PATTERN = "^[a-zA-Z ,.'-]*$";

    @Id
    Long id;

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
