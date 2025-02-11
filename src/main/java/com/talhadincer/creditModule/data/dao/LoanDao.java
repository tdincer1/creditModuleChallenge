package com.talhadincer.creditModule.data.dao;

import com.talhadincer.creditModule.data.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanDao extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(long customerId);

    List<Loan> findByCustomerIdAndIsPaid(long customerId, boolean isPaid);

    Optional<Loan> findByIdAndCustomerId(long id, long customerId);

    Optional<Loan> findByIdAndIsPaid(long id, boolean isPaid);
}
