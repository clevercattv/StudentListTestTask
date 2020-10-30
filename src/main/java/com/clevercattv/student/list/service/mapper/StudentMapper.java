package com.clevercattv.student.list.service.mapper;

import com.clevercattv.student.list.dto.CreateStudentRequest;
import com.clevercattv.student.list.dto.StudentResponse;
import com.clevercattv.student.list.entity.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student toEntity(Student student);
    Student toEntity(CreateStudentRequest request);
    StudentResponse toResponse(Student student);

}
