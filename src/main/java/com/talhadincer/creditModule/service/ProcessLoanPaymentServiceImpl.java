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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ProcessLoanPaymentServiceImpl implements ProcessLoanPaymentService {

    private final CustomerDao customerDao;
    private final LoanDao loanDao;
    private final LoanInstallmentDao loanInstallmentDao;

    @Override
    public ProcessLoanPaymentOutput process(ProcessLoanPaymentInput input) throws BusinessException {

        ProcessLoanPaymentOutput output = processInstallments(input.getInstallmentEntityList(), input.getPaymentAmount());

        if (output.getNumberOfInstallmentsPaid() == 0) {
            throw new BusinessException(ResponseCodes.PAYMENT_NOT_PROCESSED.getCode(),
                    ResponseCodes.PAYMENT_NOT_PROCESSED.getDescription());
        }

        if (output.isLoanClosed()) {
            updateLoanPaidStatus(input.getLoanEntity());
        }

        updateCustomerRecord(input.getCustomerEntity(), output.getPaidAmount());

        output.setSuccess();

        return output;
    }

    private ProcessLoanPaymentOutput processInstallments(List<LoanInstallment> list, BigDecimal totalAmount) {

        BigDecimal remainingAmount = totalAmount;
        int numberOfInstallmentsPaid = 0;
        boolean loanClosed = false;
        LocalDate limitDateForPayment = DateUtil.getFirstDayOfTheFutureMonth(3);

        Iterator<LoanInstallment> iterator = list.iterator();
        while (iterator.hasNext()) {
            LoanInstallment installment = iterator.next();

            //pay up to 3 months
            if (installment.getDueDate().isAfter(limitDateForPayment)) {
                break;
            }

            BigDecimal currentInstallmentAmount = calculateCurrentInstallmentAmount(installment.getAmount(), installment.getDueDate());
            //partial payment not allowed!
            if (remainingAmount.compareTo(currentInstallmentAmount) < 0) {
                break;
            }

            installment.setPaidAmount(currentInstallmentAmount);
            installment.setPaymentDate(LocalDate.now());
            installment.setIsPaid(true);

            remainingAmount = remainingAmount.subtract(currentInstallmentAmount);
            numberOfInstallmentsPaid++;

            //all installment paid?
            if (!iterator.hasNext()) {
                loanClosed = true;
            }

            if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }

        if (numberOfInstallmentsPaid > 0) {
            loanInstallmentDao.saveAll(list);
        }

        return ProcessLoanPaymentOutput.builder()
                .paidAmount(totalAmount.subtract(remainingAmount))
                .numberOfInstallmentsPaid(numberOfInstallmentsPaid)
                .loanClosed(loanClosed)
                .build();
    }

    //apply discount/penalty to payment based on payment and due dates.
    private BigDecimal calculateCurrentInstallmentAmount(BigDecimal installmentAmount, LocalDate dueDate) {

        BigDecimal dayDifference = new BigDecimal(DateUtil.dayDifferenceToCurrentDate(dueDate));

        //is payment on dueDate?
        if (dayDifference.compareTo(BigDecimal.ZERO) == 0) {
            return installmentAmount;
        }

        BigDecimal dailyInterest = new BigDecimal("0.001");
        BigDecimal markUp = dayDifference.multiply(dailyInterest);
        BigDecimal additionalAmount = installmentAmount.multiply(markUp).setScale(2, RoundingMode.HALF_EVEN);

        return installmentAmount.add(additionalAmount);
    }

    private void updateCustomerRecord(Customer entity, BigDecimal paidAmount) {
        BigDecimal formerLimit = entity.getUsedCreditLimit();
        entity.setUsedCreditLimit(formerLimit.subtract(paidAmount));
        customerDao.save(entity);
    }

    private void updateLoanPaidStatus(Loan entity) {
        entity.setIsPaid(true);
        loanDao.save(entity);
    }
}
