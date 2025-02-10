package com.talhadincer.creditModule.data.dto.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayLoanRequest {

    @Positive
    private long customerId;

    @Positive
    private long loanId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
