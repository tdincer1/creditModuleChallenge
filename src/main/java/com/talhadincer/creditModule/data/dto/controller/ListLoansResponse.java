package com.talhadincer.creditModule.data.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListLoansResponse extends BaseApiResponse {

    private List<LoanDto> loans;
}
