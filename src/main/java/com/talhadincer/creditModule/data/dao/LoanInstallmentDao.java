package com.talhadincer.creditModule.data.dao;

import com.talhadincer.creditModule.data.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanInstallmentDao extends JpaRepository<LoanInstallment, Long> {
}
