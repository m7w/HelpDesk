package com.training.helpdesk.exception.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private static final String PATH = "path";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(PATH, request.getDescription(false));
        body.put(STATUS, status.getReasonPhrase());

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            body.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(PATH, request.getDescription(false));
        body.put(STATUS, HttpStatus.BAD_REQUEST);

        e.getConstraintViolations().forEach(violation -> {
            String valueName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            body.put(valueName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(PATH, request.getDescription(false));
        body.put(STATUS, HttpStatus.NOT_FOUND);
        body.put(MESSAGE, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}

