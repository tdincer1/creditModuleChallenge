package com.talhadincer.creditModule.data.dto.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayLoanOutput extends BaseServiceResponse {

    private BigDecimal paidAmount;
    private Integer numberOfInstallmentsPaid;
    private boolean loanClosed;
}
