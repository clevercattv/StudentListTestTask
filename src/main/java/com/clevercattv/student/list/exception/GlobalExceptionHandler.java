package com.clevercattv.student.list.exception;

import com.clevercattv.student.list.dto.ErrorResponse;
import com.clevercattv.student.list.dto.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        Map<String, List<String>> errors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));

        return ResponseEntity.badRequest()
                .body(ValidationErrorResponse.builder().errors(errors).build());
    }

    @ExceptionHandler({ServerWebInputException.class})
    public ResponseEntity<Object> handleValidationException(ServerWebInputException exception) {
        log.error("ServerWebInputException", exception);

        if (Objects.nonNull(exception.getCause()) &&
                exception.getCause() instanceof TypeMismatchException) {
            TypeMismatchException mismatchException = (TypeMismatchException) exception.getCause();
            String message = String.format("Failed convert '%s' into %s !",
                    mismatchException.getValue(), mismatchException.getRequiredType().getSimpleName());
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.builder().error(message).build());
        }

        String reason = exception.getReason();
        return ResponseEntity.status(exception.getStatus())
                .body(ErrorResponse.builder().error(reason.substring(0, reason.indexOf(':'))).build());
    }

    @ExceptionHandler({NoSuchStudentException.class})
    public ResponseEntity<Object> handleValidationException(NoSuchStudentException exception) {
        log.error("ServerWebInputException", exception);

        return ResponseEntity.noContent().build(); // Body removes if HttpStatus.NO_CONTENT
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleUnexpectedException(Exception exception) {
        log.error("UnexpectedException", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().error(exception.getMessage()).build());
    }

}
