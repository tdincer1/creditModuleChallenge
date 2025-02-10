package com.talhadincer.creditModule.data.dto.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListLoansOutput extends BaseServiceResponse {

    private List<LoanDto> loans;
}
