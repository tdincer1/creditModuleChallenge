package com.talhadincer.creditModule.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long customerId;
    private BigDecimal loanAmount;
    private Integer numberOfInstallments;
    private Boolean isPaid;
}
