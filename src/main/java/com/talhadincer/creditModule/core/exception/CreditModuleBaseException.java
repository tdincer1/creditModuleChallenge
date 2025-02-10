package com.talhadincer.creditModule.core.exception;

import lombok.Getter;

@Getter
public abstract class CreditModuleBaseException extends Exception {

    private final int errorCode;

    public CreditModuleBaseException(int errorCode, String errorDescription) {
        super(errorDescription);
        this.errorCode = errorCode;
    }
}
