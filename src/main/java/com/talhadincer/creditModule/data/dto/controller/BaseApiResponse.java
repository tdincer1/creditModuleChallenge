package com.talhadincer.creditModule.data.dto.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BaseApiResponse {

    private int responseCode;
    private String responseDescription;
    private Map<String, String> additionalInfo;
}
