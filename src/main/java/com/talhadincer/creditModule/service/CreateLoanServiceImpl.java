package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.DateUtil;
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
import com.talhadincer.creditModule.data.entity.LoanInstallment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CreateLoanServiceImpl implements CreateLoanService {

    private final CustomerDao customerDao;
    private final LoanDao loanDao;
    private final LoanInstallmentDao loanInstallmentDao;

    @Override
    public CreateLoanOutput createLoan(CreateLoanInput input) throws DataException, BusinessException {

        inputValidation(input);

        Customer customerEntity = getCustomer(input.getCustomerId());

        BigDecimal totalLoanAmount = calculateTotalLoanAmount(input);
        BigDecimal installmentAmount = calculateInstallmentAmount(totalLoanAmount, input.getNumberOfInstallments());

        checkCreditLimit(customerEntity, totalLoanAmount);

        updateCustomerLimit(customerEntity, totalLoanAmount);
        long loanId = insertLoanRecord(input, totalLoanAmount);
        insertInstallmentRecords(input, loanId, installmentAmount);

        CreateLoanOutput output = new CreateLoanOutput();
        output.setSuccess();
        return output;
    }

    private void inputValidation(CreateLoanInput input) throws BusinessException {
        switch (input.getNumberOfInstallments()) {
            case 6, 9, 12, 14:
                break;
            default:
                throw new BusinessException(ResponseCodes.INVALID_NUMBER_OF_INS_COUNT.getCode(),
                        ResponseCodes.INVALID_NUMBER_OF_INS_COUNT.getDescription());
        }
    }

    private Customer getCustomer(long customerId) throws DataException {
        return customerDao.findById(customerId)
                .orElseThrow(() -> new DataException(ResponseCodes.CUSTOMER_NOT_FOUND.getCode(),
                        ResponseCodes.CUSTOMER_NOT_FOUND.getDescription()));
    }

    private void checkCreditLimit(Customer customer, BigDecimal desiredAmount) throws BusinessException {

        BigDecimal availableLimit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());

        if (availableLimit.compareTo(desiredAmount) < 0) {
            throw new BusinessException(ResponseCodes.LIMIT_NOT_SUFFICIENT.getCode(),
                    ResponseCodes.LIMIT_NOT_SUFFICIENT.getDescription());
        }
    }

    private BigDecimal calculateTotalLoanAmount(CreateLoanInput input) {
        BigDecimal rate = input.getInterestRate().add(new BigDecimal("1"));
        return input.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateInstallmentAmount(BigDecimal totalLoanAmount, Integer numberOfInstallments) {
        return totalLoanAmount.divide(new BigDecimal(numberOfInstallments), 2, RoundingMode.HALF_EVEN);
    }

    private void updateCustomerLimit(Customer entity, BigDecimal loanAmount) {
        BigDecimal newUsedLimit = entity.getUsedCreditLimit().add(loanAmount);
        entity.setUsedCreditLimit(newUsedLimit);
        customerDao.save(entity);
    }

    private long insertLoanRecord(CreateLoanInput input, BigDecimal totalLoanAmount) {

        Loan entity = new Loan();
        entity.setCustomerId(input.getCustomerId());
        entity.setNumberOfInstallments(input.getNumberOfInstallments());
        entity.setLoanAmount(totalLoanAmount);
        entity.setIsPaid(false);

        return loanDao.save(entity).getId();
    }

    private void insertInstallmentRecords(CreateLoanInput input, long loanId, BigDecimal installmentAmount) {
        List<LoanInstallment> entityList = new ArrayList<>();

        for (int i = 0; i < input.getNumberOfInstallments(); i++) {
            LoanInstallment entity = new LoanInstallment();
            entity.setLoanId(loanId);
            entity.setAmount(installmentAmount);
            entity.setDueDate(DateUtil.getFirstDayOfTheFutureMonth(i + 1));
            entity.setIsPaid(false);

            entityList.add(entity);
        }

        loanInstallmentDao.saveAll(entityList);
    }
}
