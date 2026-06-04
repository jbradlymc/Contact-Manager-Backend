package com.example.contactmanager.exception;

import java.util.Map;

public class ApiErrorResponse {

    private int errorCode;

    private String errorMessage;

    private Map<String, Object> errorDetails;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(
            int errorCode,
            String errorMessage,
            Map<String, Object> errorDetails
    ) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetails = errorDetails;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, Object> getErrorDetails() {
        return errorDetails;
    }

}
