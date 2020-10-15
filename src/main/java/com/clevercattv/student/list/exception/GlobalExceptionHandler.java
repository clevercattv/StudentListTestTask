package com.clevercattv.student.list.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({WebExchangeBindException.class})
    public ResponseEntity<Object> handleValidationException(WebExchangeBindException exception) {
        log.error("WebExchangeBindException", exception);
        if (!exception.getGlobalErrors().isEmpty()) {
            return handleUnexpectedException(exception);
        }
        if (exception.getFieldErrors().isEmpty()) {
            return handleUnexpectedException(exception);
        }
        Map<String, List<String>> response = exception.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));

        return new ResponseEntity<>(response, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleUnexpectedException(Exception exception) {

        log.error("UnexpectedException", exception);
        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
