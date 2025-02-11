package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.DateUtil;
import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.data.dao.CustomerDao;
import com.talhadincer.creditModule.data.dao.LoanDao;
import com.talhadincer.creditModule.data.dao.LoanInstallmentDao;
import com.talhadincer.creditModule.data.dto.service.ProcessLoanPaymentInput;
import com.talhadincer.creditModule.data.dto.service.ProcessLoanPaymentOutput;
import com.talhadincer.creditModule.data.entity.Customer;
import com.talhadincer.creditModule.data.entity.Loan;
import com.talhadincer.creditModule.data.entity.LoanInstallment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProcessLoanPaymentServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private LoanDao loanDao;

    @Mock
    private LoanInstallmentDao installmentDao;

    private ProcessLoanPaymentService service;

    @BeforeEach
    void setUp() {
        service = new ProcessLoanPaymentServiceImpl(customerDao, loanDao, installmentDao);
    }

    @Test
    void payAll() throws BusinessException {
        ProcessLoanPaymentOutput output = service.process(getDummyInput("300", "100", 3));
        assertEquals(ResponseCodes.SUCCESS.getCode(), output.getResponseCode());
    }


    private Loan getDummyLoanOptional() {
        Loan loan = new Loan();
        loan.setId(123L);
        loan.setCustomerId(1L);
        loan.setLoanAmount(new BigDecimal("600"));
        loan.setNumberOfInstallments(6);
        loan.setIsPaid(false);
        return loan;
    }

    private ProcessLoanPaymentInput getDummyInput(String amount, String installmentAmount, int numberOfInstallments) {
        return ProcessLoanPaymentInput.builder()
                .customerId(1L)
                .loanId(123L)
                .paymentAmount(new BigDecimal(amount))
                .customerEntity(getDummyCustomer())
                .loanEntity(getDummyLoanOptional())
                .installmentEntityList(getDummyInstallmentList(installmentAmount, numberOfInstallments))
                .build();
    }

    private List<LoanInstallment> getDummyInstallmentList(String amount, int size) {

        List<LoanInstallment> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LoanInstallment loanInstallment = new LoanInstallment();
            loanInstallment.setId(6L);
            loanInstallment.setLoanId(123L);
            loanInstallment.setAmount(new BigDecimal(amount));
            loanInstallment.setIsPaid(false);
            loanInstallment.setId(1L + i);
            loanInstallment.setDueDate(DateUtil.getFirstDayOfTheFutureMonth(i - 1));

            list.add(loanInstallment);
        }

        return list;
    }

    private Customer getDummyCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Michael");
        customer.setSurname("Jackson");
        customer.setCreditLimit(new BigDecimal("10000"));
        customer.setUsedCreditLimit(new BigDecimal("2000"));
        return customer;
    }
}