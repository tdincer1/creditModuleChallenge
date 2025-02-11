package com.talhadincer.creditModule.data.dto.service;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoanDto {

    private Long id;
    private BigDecimal amount;
    private Integer numberOfInstallments;
    private boolean paid;
}
