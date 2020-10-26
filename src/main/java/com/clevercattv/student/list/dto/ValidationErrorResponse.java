package com.clevercattv.student.list.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ValidationErrorResponse {

    // Map where the key is field and value are errors.
    Map<String, List<String>> errors;

}
