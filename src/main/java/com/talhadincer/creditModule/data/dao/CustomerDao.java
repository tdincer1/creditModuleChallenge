package com.talhadincer.creditModule.data.dao;

import com.talhadincer.creditModule.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer, Long> {
}
