package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dao.CustomerDao;
import com.talhadincer.creditModule.data.dao.LoanDao;
import com.talhadincer.creditModule.data.dao.LoanInstallmentDao;
import com.talhadincer.creditModule.data.dto.service.PayLoanInput;
import com.talhadincer.creditModule.data.dto.service.ProcessLoanPaymentInput;
import com.talhadincer.creditModule.data.entity.Customer;
import com.talhadincer.creditModule.data.entity.Loan;
import com.talhadincer.creditModule.data.entity.LoanInstallment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayLoanServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private LoanDao loanDao;

    @Mock
    private LoanInstallmentDao installmentDao;

    @Mock
    private ProcessLoanPaymentService paymentService;

    private PayLoanService service;

    @BeforeEach
    void setUp() {
        service = new PayLoanServiceImpl(customerDao, loanDao, installmentDao, paymentService);
    }

    @Test
    void customerNotFoundError() {
        DataException exception = assertThrows(DataException.class, () -> service.payLoan(getDummyInput("100")));
        assertEquals(ResponseCodes.CUSTOMER_NOT_FOUND.getCode(), exception.getErrorCode());
    }

    @Test
    void loanNotFoundError() {

        when(customerDao.findById(anyLong())).thenReturn(getDummyCustomer());
        when(loanDao.findByIdAndIsPaid(anyLong(), anyBoolean())).thenReturn(Optional.empty());

        DataException exception = assertThrows(DataException.class, () -> service.payLoan(getDummyInput("100")));
        assertEquals(ResponseCodes.LOAN_NOT_FOUND.getCode(), exception.getErrorCode());
    }

    @Test
    void insufficientAmount() throws BusinessException {

        LoanInstallment loanInstallment = new LoanInstallment();
        loanInstallment.setId(6L);
        loanInstallment.setLoanId(123L);
        loanInstallment.setAmount(new BigDecimal("100"));
        loanInstallment.setDueDate(LocalDate.now());
        loanInstallment.setIsPaid(false);

        when(customerDao.findById(anyLong())).thenReturn(getDummyCustomer());
        when(loanDao.findByIdAndIsPaid(anyLong(), anyBoolean())).thenReturn(getDummyLoanOptional());
        when(installmentDao.findByLoanIdAndIsPaidOrderByDueDate(anyLong(), anyBoolean())).thenReturn(Collections.singletonList(loanInstallment));
        when(paymentService.process(any(ProcessLoanPaymentInput.class)))
                .thenThrow(new BusinessException(ResponseCodes.PAYMENT_NOT_PROCESSED.getCode(),
                        ResponseCodes.PAYMENT_NOT_PROCESSED.getDescription()));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.payLoan(getDummyInput("90")));
        assertEquals(ResponseCodes.PAYMENT_NOT_PROCESSED.getCode(), exception.getErrorCode());
    }

    private Optional<Loan> getDummyLoanOptional() {
        Loan loan = new Loan();
        loan.setId(123L);
        loan.setCustomerId(1L);
        loan.setLoanAmount(new BigDecimal("600"));
        loan.setNumberOfInstallments(6);
        loan.setIsPaid(false);
        return Optional.of(loan);
    }

    private PayLoanInput getDummyInput(String amount) {
        PayLoanInput input = new PayLoanInput();
        input.setLoanId(123L);
        input.setAmount(new BigDecimal(amount));
        input.setCustomerId(1L);
        return input;
    }

    private Optional<Customer> getDummyCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Michael");
        customer.setSurname("Jackson");
        customer.setCreditLimit(new BigDecimal("10000"));
        customer.setUsedCreditLimit(new BigDecimal("2000"));
        return Optional.of(customer);
    }
}