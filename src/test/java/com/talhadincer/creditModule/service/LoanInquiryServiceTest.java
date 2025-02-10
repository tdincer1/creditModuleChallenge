package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.constant.LoanStatus;
import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dao.CustomerDao;
import com.talhadincer.creditModule.data.dao.LoanDao;
import com.talhadincer.creditModule.data.dto.service.ListLoansOutput;
import com.talhadincer.creditModule.data.entity.Customer;
import com.talhadincer.creditModule.data.entity.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanInquiryServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private LoanDao loanDao;

    private LoanInquiryService service;

    @BeforeEach
    void setUp() {
        service = new LoanInquiryServiceImpl(customerDao, loanDao);
    }

    @Test
    void success() throws DataException {
        when(customerDao.findById(anyLong())).thenReturn(getDummyCustomer());
        when(loanDao.findByCustomerId(anyLong())).thenReturn(getDummyLoanList());

        ListLoansOutput output = service.listLoans(1, LoanStatus.ALL.getStatus());
        assertEquals(ResponseCodes.SUCCESS.getCode(), output.getResponseCode());
        assertEquals(2, output.getLoans().size());
    }

    @Test
    void customerNotFoundError() {
        DataException exception = assertThrows(DataException.class, () -> service.listLoans(99, LoanStatus.PAID.getStatus()));
        assertEquals(ResponseCodes.CUSTOMER_NOT_FOUND.getCode(), exception.getErrorCode());
    }

    @Test
    void loanNotFoundError() {

        when(customerDao.findById(anyLong())).thenReturn(getDummyCustomer());
        when(loanDao.findByCustomerId(anyLong())).thenThrow(new EmptyResultDataAccessException(1));

        DataException exception = assertThrows(DataException.class, () -> service.listLoans(123, LoanStatus.ALL.getStatus()));
        assertEquals(ResponseCodes.LOAN_NOT_FOUND.getCode(), exception.getErrorCode());
    }

    @Test
    void matchingLoanNotFoundError() {

        when(customerDao.findById(anyLong())).thenReturn(getDummyCustomer());
        when(loanDao.findByCustomerIdAndIsPaid(anyLong(), anyBoolean())).thenThrow(new EmptyResultDataAccessException(1));

        DataException exception = assertThrows(DataException.class, () -> service.listLoans(123, LoanStatus.UNPAID.getStatus()));
        assertEquals(ResponseCodes.LOAN_NOT_FOUND.getCode(), exception.getErrorCode());
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

    private List<Loan> getDummyLoanList() {
        Loan loan = new Loan();
        loan.setId(123L);
        loan.setCustomerId(1L);
        loan.setLoanAmount(new BigDecimal("1000"));
        loan.setNumberOfInstallments(6);
        loan.setIsPaid(true);

        Loan loan2 = new Loan();
        loan2.setId(124L);
        loan2.setCustomerId(1L);
        loan2.setLoanAmount(new BigDecimal("300"));
        loan2.setNumberOfInstallments(9);
        loan2.setIsPaid(false);
        return List.of(loan, loan2);
    }
}