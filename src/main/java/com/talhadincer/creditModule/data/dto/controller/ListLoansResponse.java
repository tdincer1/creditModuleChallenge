package com.talhadincer.creditModule.data.dto.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListLoansResponse extends BaseApiResponse {

    private List<LoanDto> loans;
}
