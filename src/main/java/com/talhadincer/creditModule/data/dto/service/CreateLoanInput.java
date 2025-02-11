package com.talhadincer.creditModule.data.dto.service;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanInput {

    private long customerId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private int numberOfInstallments;
}
