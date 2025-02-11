package com.talhadincer.creditModule.controller;

import com.talhadincer.creditModule.data.dto.controller.*;
import com.talhadincer.creditModule.data.dto.service.*;
import org.mapstruct.Mapper;

@Mapper
public interface WebControllerMapper {

    CreateLoanInput mapInput(CreateLoanRequest request);

    CreateLoanResponse mapResponse(CreateLoanOutput output);

    ListLoansResponse mapResponse(ListLoansOutput output);

    ListInstallmentsResponse mapResponse(ListInstallmentsOutput output);

    PayLoanInput mapInput(PayLoanRequest request);

    PayLoanResponse mapResponse(PayLoanOutput output);
}
