package com.example.contactmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

}
