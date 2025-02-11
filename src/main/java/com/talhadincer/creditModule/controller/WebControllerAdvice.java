package com.talhadincer.creditModule.controller;

import com.talhadincer.creditModule.core.constant.ResponseCodes;
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
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        BaseApiResponse response = new BaseApiResponse();
        response.setAdditionalInfo(errors);
        response.setResponseCode(ResponseCodes.VALIDATION_ERROR.getCode());
        response.setResponseDescription(ResponseCodes.VALIDATION_ERROR.getDescription());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreditModuleBaseException.class)
    protected ResponseEntity<Object> handleInternalExceptions(CreditModuleBaseException ex) {
        BaseApiResponse response = new BaseApiResponse();
        response.setResponseCode(ex.getErrorCode());
        response.setResponseDescription(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
