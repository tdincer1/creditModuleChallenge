package com.talhadincer.creditModule.data.dto.controller;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstallmentDto {

    private Long loanId;
    private BigDecimal amount;
    private String dueDate;
    private String paymentDate;
    private boolean paid;
}
