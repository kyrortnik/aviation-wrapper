package com.mpiatrenka.aviationwrapper.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AirportDetailsExternalServiceException.class)
    public ResponseEntity<String> handleRuntimeException(AirportDetailsExternalServiceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
