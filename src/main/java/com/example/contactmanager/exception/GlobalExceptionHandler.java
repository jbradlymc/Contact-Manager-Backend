package com.example.contactmanager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(Exception exception) {

        NotFoundException raiseException = (NotFoundException) exception;

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                raiseException.getErrorCode(),
                raiseException.getErrorMessage(),
                raiseException.getErrorDetails()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);

    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictException (Exception exception) {

        ConflictException raiseException = (ConflictException) exception;

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                raiseException.getErrorCode(),
                raiseException.getErrorMessage(),
                raiseException.getErrorDetails()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(Exception exception) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for request body.",
                getErrorDetails(exception)
        );

        return status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private static Map<String, Object> getErrorDetails(Exception exception) {

        Map<String, Object> errorDetails = new HashMap<>();

        List<FieldError> fieldErrors =
                ((MethodArgumentNotValidException) exception)
                        .getBindingResult()
                        .getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            errorDetails.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage());
        }

        return errorDetails;

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            Exception exception) {

        logger.error("Unexpected error occurred", exception);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                Collections.emptyMap()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}
