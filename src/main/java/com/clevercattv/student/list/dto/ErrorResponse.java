package com.clevercattv.student.list.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse<T> {

    T error;

}
