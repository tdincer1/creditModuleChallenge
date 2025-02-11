package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dao.CustomerDao;
import com.talhadincer.creditModule.data.dao.LoanDao;
import com.talhadincer.creditModule.data.dao.LoanInstallmentDao;
import com.talhadincer.creditModule.data.dto.service.PayLoanInput;
import com.talhadincer.creditModule.data.dto.service.PayLoanOutput;
import com.talhadincer.creditModule.data.dto.service.ProcessLoanPaymentInput;
import com.talhadincer.creditModule.data.dto.service.ProcessLoanPaymentOutput;
import com.talhadincer.creditModule.data.entity.Customer;
import com.talhadincer.creditModule.data.entity.Loan;
import com.talhadincer.creditModule.data.entity.LoanInstallment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayLoanServiceImpl implements PayLoanService {

    private final CustomerDao customerDao;
    private final LoanDao loanDao;
    private final LoanInstallmentDao loanInstallmentDao;
    private final ProcessLoanPaymentService paymentService;

    @Override
    public PayLoanOutput payLoan(PayLoanInput input) throws DataException, BusinessException {

        Customer customerEntity = getCustomer(input.getCustomerId());
        Loan loanEntity = findLoan(input.getLoanId());
        List<LoanInstallment> installmentList = findInstallments(input.getLoanId());

        ProcessLoanPaymentOutput processPaymentOutput = processPayment(input, customerEntity, loanEntity, installmentList);

        PayLoanOutput output = new PayLoanOutput();
        output.setSuccess();
        output.setPaidAmount(processPaymentOutput.getPaidAmount());
        output.setNumberOfInstallmentsPaid(processPaymentOutput.getNumberOfInstallmentsPaid());
        output.setLoanClosed(processPaymentOutput.isLoanClosed());
        return output;
    }

    private Customer getCustomer(long customerId) throws DataException {
        return customerDao.findById(customerId)
                .orElseThrow(() -> new DataException(ResponseCodes.CUSTOMER_NOT_FOUND.getCode(),
                        ResponseCodes.CUSTOMER_NOT_FOUND.getDescription()));
    }

    private Loan findLoan(long loanId) throws DataException {
        Optional<Loan> loanOptional = loanDao.findByIdAndIsPaid(loanId, false);

        if (loanOptional.isEmpty()) {
            throw new DataException(ResponseCodes.LOAN_NOT_FOUND.getCode(), ResponseCodes.LOAN_NOT_FOUND.getDescription());
        }

        return loanOptional.get();
    }

    private List<LoanInstallment> findInstallments(long loanId) throws DataException {
        List<LoanInstallment> loanList = loanInstallmentDao.findByLoanIdAndIsPaidOrderByDueDate(loanId, false);

        if (loanList.isEmpty()) {
            throw new DataException(ResponseCodes.INSTALLMENT_NOT_FOUND.getCode(), ResponseCodes.INSTALLMENT_NOT_FOUND.getDescription());
        }

        return loanList;
    }

    private ProcessLoanPaymentOutput processPayment(PayLoanInput payLoanInput, Customer customerEntity, Loan loanEntity, List<LoanInstallment> installmentList) throws BusinessException {
        ProcessLoanPaymentInput input = ProcessLoanPaymentInput.builder()
                .customerId(payLoanInput.getCustomerId())
                .loanId(payLoanInput.getLoanId())
                .paymentAmount(payLoanInput.getAmount())
                .customerEntity(customerEntity)
                .loanEntity(loanEntity)
                .installmentEntityList(installmentList)
                .build();

        return paymentService.process(input);
    }
}
