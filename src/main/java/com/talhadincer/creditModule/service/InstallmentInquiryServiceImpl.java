package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.DateUtil;
import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dao.CustomerDao;
import com.talhadincer.creditModule.data.dao.LoanDao;
import com.talhadincer.creditModule.data.dao.LoanInstallmentDao;
import com.talhadincer.creditModule.data.dto.service.InstallmentDto;
import com.talhadincer.creditModule.data.dto.service.ListInstallmentsOutput;
import com.talhadincer.creditModule.data.entity.LoanInstallment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InstallmentInquiryServiceImpl implements InstallmentInquiryService {

    private final CustomerDao customerDao;
    private final LoanDao loanDao;
    private final LoanInstallmentDao loanInstallmentDao;

    @Override
    public ListInstallmentsOutput listInstallments(long customerId, long loanId) throws DataException {

        checkCustomer(customerId);
        checkLoan(customerId, loanId);

        List<LoanInstallment> entityList = findInstallments(loanId);
        List<InstallmentDto> installmentDtoList = mapInstallmentList(entityList);

        ListInstallmentsOutput output = new ListInstallmentsOutput();
        output.setInstallments(installmentDtoList);
        output.setSuccess();

        return output;
    }

    private void checkCustomer(long customerId) throws DataException {
        customerDao.findById(customerId)
                .orElseThrow(() -> new DataException(ResponseCodes.CUSTOMER_NOT_FOUND.getCode(),
                        ResponseCodes.CUSTOMER_NOT_FOUND.getDescription()));
    }

    private void checkLoan(long customerId, long loanId) throws DataException {
        loanDao.findByIdAndCustomerId(loanId, customerId)
                .orElseThrow(() -> new DataException(ResponseCodes.LOAN_NOT_FOUND.getCode(),
                        ResponseCodes.LOAN_NOT_FOUND.getDescription()));
    }

    private List<LoanInstallment> findInstallments(long loanId) throws DataException {
        List<LoanInstallment> loanList;

        loanList = loanInstallmentDao.findByLoanId(loanId);

        if (loanList.isEmpty()) {
            throw new DataException(ResponseCodes.INSTALLMENT_NOT_FOUND.getCode(), ResponseCodes.INSTALLMENT_NOT_FOUND.getDescription());
        }

        return loanList;
    }

    private List<InstallmentDto> mapInstallmentList(List<LoanInstallment> entityList) {
        List<InstallmentDto> dtoList = new ArrayList<>();
        for (LoanInstallment entity : entityList) {
            dtoList.add(InstallmentDto.builder()
                    .loanId(entity.getLoanId())
                    .amount(entity.getAmount())
                    .dueDate(DateUtil.toString(entity.getDueDate()))
                    .paymentDate(DateUtil.toString(entity.getPaymentDate()))
                    .paid(entity.getIsPaid())
                    .build());
        }
        return dtoList;
    }
}
