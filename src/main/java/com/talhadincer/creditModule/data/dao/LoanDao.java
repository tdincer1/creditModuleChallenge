package com.talhadincer.creditModule.data.dao;

import com.talhadincer.creditModule.data.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanDao extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(long customerId);
    List<Loan> findByCustomerIdAndIsPaid(long customerId, boolean isPaid);
}
