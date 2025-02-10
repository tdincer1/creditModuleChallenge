package com.talhadincer.creditModule.controller;

import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.data.dto.controller.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @GetMapping("/")
    public String publicPage() {return "Hello!";}

    @PostMapping("/createLoan")
    public CreateLoanResponse createLoan(@Valid @RequestBody CreateLoanRequest request) {
        CreateLoanResponse response = new CreateLoanResponse();
        response.setResponseCode(ResponseCodes.SUCCESS.getCode());
        response.setResponseDescription(ResponseCodes.SUCCESS.getDescription());
        return response;
    }

    @GetMapping("/listLoans")
    public ListLoansResponse listLoans(@RequestParam @Positive long customerId,
                                       @RequestParam @Pattern(regexp = "all|paid|unpaid",
                                               flags = Pattern.Flag.CASE_INSENSITIVE) String loanStatus) {
        ListLoansResponse response = new ListLoansResponse();
        response.setResponseCode(ResponseCodes.SUCCESS.getCode());
        response.setResponseDescription(ResponseCodes.SUCCESS.getDescription());
        return response;
    }

    @GetMapping("/listInstallments")
    public ListInstallmentsResponse listInstallments(@RequestParam @Positive long customerId,
                                                     @RequestParam @Positive long loanId) {
        ListInstallmentsResponse response = new ListInstallmentsResponse();
        response.setResponseCode(ResponseCodes.SUCCESS.getCode());
        response.setResponseDescription(ResponseCodes.SUCCESS.getDescription());
        return response;
    }

    @PostMapping("/payLoan")
    public PayLoanResponse payLoan(@Valid @RequestBody PayLoanRequest request) {
        PayLoanResponse response = new PayLoanResponse();
        response.setResponseCode(ResponseCodes.SUCCESS.getCode());
        response.setResponseDescription(ResponseCodes.SUCCESS.getDescription());
        return response;
    }
}
