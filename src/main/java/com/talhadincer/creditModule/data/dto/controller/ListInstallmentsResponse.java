package com.talhadincer.creditModule.data.dto.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListInstallmentsResponse extends BaseApiResponse {

    private List<InstallmentDto> installments;
}
