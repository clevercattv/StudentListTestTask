package com.clevercattv.student.list.util;

public interface StudentValidationConstant {

    String TEXT_PATTERN = "^[a-zA-Z ,.'-]*$";

    int MAX_FIRST_NAME_LENGTH = 26;
    int MIN_FIRST_NAME_LENGTH = 2;

    int MAX_LAST_NAME_LENGTH = 26;
    int MIN_LAST_NAME_LENGTH = 2;

    int MAX_UNIVERSITY_LENGTH = 63;
    int MIN_UNIVERSITY_LENGTH = 2;

    int MAX_SPECIALTY_LENGTH = 63;
    int MIN_SPECIALTY_LENGTH = 2;

    int MAX_SEMESTER = 20;
    int MIN_SEMESTER = 1;

    int MAX_AGE = 200;
    int MIN_AGE = 16;
}
