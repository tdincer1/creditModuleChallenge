package com.talhadincer.creditModule.core.exception;

public class ServiceException extends CreditModuleBaseException {
    public ServiceException(int errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
