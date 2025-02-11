package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dto.service.CreateLoanInput;
import com.talhadincer.creditModule.data.dto.service.CreateLoanOutput;

public interface CreateLoanService {

    CreateLoanOutput createLoan(CreateLoanInput input) throws DataException, BusinessException;
}
