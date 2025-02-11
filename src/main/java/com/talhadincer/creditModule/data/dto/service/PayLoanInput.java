package com.talhadincer.creditModule.data.dto.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayLoanInput {

    private long customerId;
    private long loanId;
    private BigDecimal amount;
}
