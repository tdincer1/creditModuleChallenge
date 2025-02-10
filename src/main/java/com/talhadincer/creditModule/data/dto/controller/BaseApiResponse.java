package com.talhadincer.creditModule.data.dto.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseApiResponse {

    private int responseCode;
    private String responseDescription;
}
