package com.talhadincer.creditModule.core.constant;


import lombok.Getter;

@Getter
public enum ResponseCodes {

    SUCCESS(0, "success"),
    CUSTOMER_NOT_FOUND(1, "customer not found!"),
    LIMIT_NOT_SUFFICIENT(2, "limit not sufficient!"),
    INVALID_NUMBER_OF_INS_COUNT(2, "invalid number of installment count!"),
    LOAN_NOT_FOUND(3, "loan not found!"),
    PAYMENT_NOT_PROCESSED(4, "payment not processed!")
    ;

    private final int code;
    private final String description;

    ResponseCodes(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
