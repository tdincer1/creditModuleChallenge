package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dto.service.ListLoansOutput;

public interface LoanInquiryService {

    ListLoansOutput listLoans(long customerId, String loanStatus) throws DataException;
}
