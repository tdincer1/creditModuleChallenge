package com.talhadincer.creditModule.core.constant;

import lombok.Getter;

@Getter
public enum LoanStatus {

    ALL("ALL"),
    PAID("PAID"),
    UNPAID("UNPAID");

    private final String status;

    LoanStatus(String status) {
        this.status = status;
    }
}
