package com.talhadincer.creditModule.core.exception;

public class DataException extends CreditModuleBaseException {
    public DataException(int errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
