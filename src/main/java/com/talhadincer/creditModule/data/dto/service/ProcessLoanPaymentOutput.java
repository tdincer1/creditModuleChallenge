package com.talhadincer.creditModule.data.dto.service;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessLoanPaymentOutput extends BaseServiceResponse {

    BigDecimal paidAmount;
    int numberOfInstallmentsPaid;
    boolean loanClosed;
}
