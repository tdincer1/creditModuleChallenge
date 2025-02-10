package com.talhadincer.creditModule.data.dto.service;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class LoanDto {

    private Long id;
    private BigDecimal amount;
    private Integer numberOfInstallments;
    private boolean isPaid;
}
