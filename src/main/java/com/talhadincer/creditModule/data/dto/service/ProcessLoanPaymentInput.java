package com.talhadincer.creditModule.data.dto.service;

import com.talhadincer.creditModule.data.entity.Customer;
import com.talhadincer.creditModule.data.entity.Loan;
import com.talhadincer.creditModule.data.entity.LoanInstallment;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class ProcessLoanPaymentInput {

    private final long customerId;
    private final long loanId;
    private final BigDecimal paymentAmount;
    private final Customer customerEntity;
    private final Loan loanEntity;
    private final List<LoanInstallment> installmentEntityList;

}
