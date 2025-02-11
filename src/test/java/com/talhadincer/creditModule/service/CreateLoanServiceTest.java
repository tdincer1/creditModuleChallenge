package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dao.CustomerDao;
import com.talhadincer.creditModule.data.dao.LoanDao;
import com.talhadincer.creditModule.data.dao.LoanInstallmentDao;
import com.talhadincer.creditModule.data.dto.service.CreateLoanInput;
import com.talhadincer.creditModule.data.dto.service.CreateLoanOutput;
import com.talhadincer.creditModule.data.entity.Customer;
import com.talhadincer.creditModule.data.entity.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLoanServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private LoanDao loanDao;

    @Mock
    private LoanInstallmentDao installmentDao;

    private CreateLoanService service;

    @BeforeEach
    void setUp() {
        service = new CreateLoanServiceImpl(customerDao, loanDao, installmentDao);
    }

    @Test
    void createLoan() throws DataException, BusinessException {
        Loan loan = new Loan();
        loan.setId(123L);
        loan.setCustomerId(1L);
        loan.setLoanAmount(new BigDecimal("1000"));
        loan.setNumberOfInstallments(12);
        loan.setIsPaid(true);

        when(customerDao.findById(anyLong())).thenReturn(getDummyCustomer("1000", "100"));
        when(loanDao.save(any())).thenReturn(loan);
        CreateLoanOutput output = service.createLoan(getDummyCreateLoanInput());

        assertEquals(ResponseCodes.SUCCESS.getCode(), output.getResponseCode());
        verify(customerDao).save(any(Customer.class));
        verify(loanDao).save(any(Loan.class));
        verify(installmentDao).saveAll(any());
    }

    @Test
    void limitNotSufficient() {

        when(customerDao.findById(anyLong())).thenReturn(getDummyCustomer("1000", "1000"));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.createLoan(getDummyCreateLoanInput()));
        assertEquals(ResponseCodes.LIMIT_NOT_SUFFICIENT.getCode(), exception.getErrorCode());
    }

    private CreateLoanInput getDummyCreateLoanInput() {

        return CreateLoanInput.builder()
                .amount(new BigDecimal("100"))
                .customerId(1L)
                .numberOfInstallments(12)
                .interestRate(new BigDecimal("0.2"))
                .build();
    }

    private Optional<Customer> getDummyCustomer(String creditLimit, String usedCreditLimit) {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Michael");
        customer.setSurname("Jackson");
        customer.setCreditLimit(new BigDecimal(creditLimit));
        customer.setUsedCreditLimit(new BigDecimal(usedCreditLimit));
        return Optional.of(customer);

    }
}