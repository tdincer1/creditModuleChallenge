package com.talhadincer.creditModule.data.dto.service;

import com.talhadincer.creditModule.core.constant.ResponseCodes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseServiceResponse {

    private int responseCode;
    private String responseDescription;

    public void setSuccess() {
        this.setResponseCode(ResponseCodes.SUCCESS.getCode());
        this.setResponseDescription(ResponseCodes.SUCCESS.getDescription());
    }
}
