package com.credit.deal.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = DealException.class)
    public ResponseEntity<IncorrectData> handleException(
            DealException exception)
    {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());
        log.error("Throw DealException: {}", data);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception)
    {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);
        log.error("Throw ValidationException: {}", body);
        return new ResponseEntity<>(body , HttpStatus.BAD_REQUEST);
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
