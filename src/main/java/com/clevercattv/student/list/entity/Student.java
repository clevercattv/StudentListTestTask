package com.clevercattv.student.list.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student implements Serializable {

    private static final long serialVersionUID = 413385207841147485L;
    private static final String NAME_PATTERN = "^[a-zA-Z ,.'-]$";

    @Id
    Long id;

    @Size(min = 2, max = 26, message = "Incorrect first name size!")
    @Pattern(regexp = NAME_PATTERN, message = "Incorrect ")
    String firstName;

    @Size(min = 2, max = 26, message = "Incorrect middle name size!")
    @Pattern(regexp = NAME_PATTERN, message = "")
    String middleName;

    @Size(min = 2, max = 26, message = "Incorrect last name size!")
    @Pattern(regexp = NAME_PATTERN, message = "")
    String lastName;

    @Size(min = 2, max = 26, message = "Incorrect last name size!")
    String university;
    String specialty;
    String semester;

    LocalDate entryDate;
    LocalDate birthDate;

}
