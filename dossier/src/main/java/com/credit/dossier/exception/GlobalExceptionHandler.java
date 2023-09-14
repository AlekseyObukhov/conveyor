package com.credit.dossier.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignClientException(FeignException exception)
    {
        int statusCode = exception.status();
        String errorMessage = exception.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(statusCode, errorMessage);

        if (statusCode == -1) {
            errorMessage = "FeignClient request failed";
            log.error("FeignException: {}", errorResponse);
            return new ResponseEntity<>(new ErrorResponse(statusCode, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.error("FeignException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }
}
