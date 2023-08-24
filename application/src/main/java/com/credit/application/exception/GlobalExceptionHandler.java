package com.credit.application.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = PreScoringException.class)
    public ResponseEntity<String> handleException(
            PreScoringException exception)
    {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());
        log.info("data = {}", data.getInfo());
        return new ResponseEntity<>(data.getInfo(), HttpStatus.BAD_REQUEST);
    }
}
