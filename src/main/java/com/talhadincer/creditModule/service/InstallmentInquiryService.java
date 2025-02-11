package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dto.service.ListInstallmentsOutput;

public interface InstallmentInquiryService {

    ListInstallmentsOutput listInstallments(long customerId, long loanId) throws DataException;
}
