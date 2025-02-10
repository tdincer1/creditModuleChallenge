package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.constant.LoanStatus;
import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dao.CustomerDao;
import com.talhadincer.creditModule.data.dao.LoanDao;
import com.talhadincer.creditModule.data.dto.service.ListLoansOutput;
import com.talhadincer.creditModule.data.dto.service.LoanDto;
import com.talhadincer.creditModule.data.entity.Customer;
import com.talhadincer.creditModule.data.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LoanInquiryServiceImpl implements LoanInquiryService {

    private final CustomerDao customerDao;
    private final LoanDao loanDao;

    @Override
    public ListLoansOutput listLoans(long customerId, String loanStatus) throws DataException {

        checkCustomer(customerId);
        List<Loan> loanEntityList = findLoans(customerId, loanStatus);
        List<LoanDto> loanDtoList = mapLoanList(loanEntityList);

        ListLoansOutput output = new ListLoansOutput();
        output.setLoans(loanDtoList);
        output.setSuccess();

        return output;
    }

    private void checkCustomer(long customerId) throws DataException {
        Optional<Customer> customer = customerDao.findById(customerId);
        if (customer.isEmpty()) {
            throw new DataException(ResponseCodes.CUSTOMER_NOT_FOUND.getCode(), ResponseCodes.CUSTOMER_NOT_FOUND.getDescription());
        }
    }

    private List<Loan> findLoans(long customerId, String loanStatus) throws DataException {
        List<Loan> loanList;
        try {
            if (LoanStatus.ALL.getStatus().equals(loanStatus)) {
                loanList = loanDao.findByCustomerId(customerId);
            } else if (LoanStatus.PAID.getStatus().equals(loanStatus)) {
                loanList = loanDao.findByCustomerIdAndIsPaid(customerId, true);
            } else {
                loanList = loanDao.findByCustomerIdAndIsPaid(customerId, false);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new DataException(ResponseCodes.LOAN_NOT_FOUND.getCode(), ResponseCodes.LOAN_NOT_FOUND.getDescription());
        }
        return loanList;
    }

    private List<LoanDto> mapLoanList(List<Loan> loanEntityList) {
        List<LoanDto> dtoList = new ArrayList<>();
        for (Loan entity : loanEntityList) {
            dtoList.add(LoanDto.builder()
                    .id(entity.getId())
                    .amount(entity.getLoanAmount())
                    .numberOfInstallments(entity.getNumberOfInstallments())
                    .isPaid(entity.getIsPaid())
                    .build());
        }
        return dtoList;
    }
}
