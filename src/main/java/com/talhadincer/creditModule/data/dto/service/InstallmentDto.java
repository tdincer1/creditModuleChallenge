package com.talhadincer.creditModule.data.dto.service;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class InstallmentDto {

    private Long loanId;
    private BigDecimal amount;
    private String dueDate;
    private String paymentDate;
    private boolean isPaid;
}
