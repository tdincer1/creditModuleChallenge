package com.talhadincer.creditModule.core.exception;

public class BusinessException extends CreditModuleBaseException {

    public BusinessException(int errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
