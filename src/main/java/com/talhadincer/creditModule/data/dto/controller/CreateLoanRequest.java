package com.talhadincer.creditModule.data.dto.controller;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanRequest {

    @Positive
    private long customerId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("0.5")
    private BigDecimal interestRate; //(between 0.1 – 0.5)

    private int numberOfInstallments; // (6, 9, 12, 24)
}
