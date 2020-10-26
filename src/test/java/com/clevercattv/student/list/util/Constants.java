package com.clevercattv.student.list.util;

import com.clevercattv.student.list.entity.Student;

public interface Constants {

     Student EXPECTED_STUDENT_1 = Student.builder()
            .id(1L)
            .firstName("Anny")
            .lastName("Hallbord")
            .university("Francis Marion University")
            .specialty("Indian Statistical Institute")
            .semester(11)
            .age(38)
            .build();

     Student EXPECTED_STUDENT_2 = Student.builder()
             .id(2L)
            .firstName("Jackie")
            .lastName("Hallbord")
            .university("Francis Marion University")
            .specialty("Indian Statistical Institute")
            .semester(7)
            .age(33)
            .build();

     Student EXPECTED_STUDENT_3 = Student.builder()
             .id(3L)
            .firstName("Ogden")
            .lastName("Abbati")
            .university("Francis Marion University")
            .specialty("Indian Statistical Institute")
            .semester(3)
            .age(22)
            .build();

     Student DELETEABLE_STUDENT = Student.builder()
             .id(4L)
            .firstName("Abbati")
            .lastName("Ogden")
            .university("Francis Marion University")
            .specialty("Indian Statistical Institute")
            .semester(2)
            .age(21)
            .build();

}
