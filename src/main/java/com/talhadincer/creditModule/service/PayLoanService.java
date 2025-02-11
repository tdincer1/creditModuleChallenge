package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dto.service.PayLoanInput;
import com.talhadincer.creditModule.data.dto.service.PayLoanOutput;

public interface PayLoanService {

    PayLoanOutput payLoan(PayLoanInput input) throws DataException, BusinessException;
}
