package com.talhadincer.creditModule.data.dto.controller;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayLoanResponse extends BaseApiResponse {

    private BigDecimal paidAmount;
    private Integer numberOfInstallmentsPaid;
    private boolean loanClosed;
}
