package com.clevercattv.student.list.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StudentResponse {

    Long id;
    String firstName;
    String lastName;
    String university;
    String specialty;
    Integer semester;
    Integer age;

}
