package com.talhadincer.creditModule.controller;

import com.talhadincer.creditModule.core.exception.CreditModuleBaseException;
import com.talhadincer.creditModule.data.dto.controller.BaseApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //HandlerMethodValidationException da eklenebilir. Swt qrdan bak gerekirse.

    @ExceptionHandler(CreditModuleBaseException.class)
    protected ResponseEntity<Object> handleInternalExceptions(CreditModuleBaseException ex) {
        BaseApiResponse response = new BaseApiResponse();
        response.setResponseCode(ex.getErrorCode());
        response.setResponseDescription(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
