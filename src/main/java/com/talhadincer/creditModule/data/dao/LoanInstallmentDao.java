package com.talhadincer.creditModule.data.dao;

import com.talhadincer.creditModule.data.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstallmentDao extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findByLoanId(long loanId);

    List<LoanInstallment> findByLoanIdAndIsPaidOrderByDueDate(long loanId, boolean isPaid);
}
