package com.talhadincer.creditModule.service;

import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.data.dto.service.ProcessLoanPaymentInput;
import com.talhadincer.creditModule.data.dto.service.ProcessLoanPaymentOutput;

public interface ProcessLoanPaymentService {

    ProcessLoanPaymentOutput process(ProcessLoanPaymentInput input) throws BusinessException;
}
