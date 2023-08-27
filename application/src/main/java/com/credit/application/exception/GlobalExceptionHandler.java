package com.credit.application.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = PreScoringException.class)
    public ResponseEntity<IncorrectData> handleException(
            PreScoringException exception)
    {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());
        log.info("exceptionInfo = {}", data);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

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
