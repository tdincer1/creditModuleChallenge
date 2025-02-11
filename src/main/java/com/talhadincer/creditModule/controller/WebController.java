package com.talhadincer.creditModule.controller;

import com.talhadincer.creditModule.core.exception.BusinessException;
import com.talhadincer.creditModule.core.exception.DataException;
import com.talhadincer.creditModule.data.dto.controller.*;
import com.talhadincer.creditModule.data.dto.service.CreateLoanOutput;
import com.talhadincer.creditModule.data.dto.service.ListInstallmentsOutput;
import com.talhadincer.creditModule.data.dto.service.ListLoansOutput;
import com.talhadincer.creditModule.data.dto.service.PayLoanOutput;
import com.talhadincer.creditModule.service.CreateLoanService;
import com.talhadincer.creditModule.service.InstallmentInquiryService;
import com.talhadincer.creditModule.service.LoanInquiryService;
import com.talhadincer.creditModule.service.PayLoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    private final WebControllerMapper mapper;
    private final CreateLoanService createLoanService;
    private final LoanInquiryService loanInquiryService;
    private final InstallmentInquiryService installmentInquiryService;
    private final PayLoanService payLoanService;

    public WebController(CreateLoanService createLoanService, LoanInquiryService loanInquiryService, InstallmentInquiryService installmentInquiryService, PayLoanService payLoanService) {
        this.createLoanService = createLoanService;
        this.loanInquiryService = loanInquiryService;
        this.installmentInquiryService = installmentInquiryService;
        this.payLoanService = payLoanService;
        this.mapper = Mappers.getMapper(WebControllerMapper.class);
    }

    @GetMapping("/")
    public String publicPage() {
        return "Hello!";
    }

    @PostMapping("/createLoan")
    public CreateLoanResponse createLoan(@Valid @RequestBody CreateLoanRequest request) throws DataException, BusinessException {

        CreateLoanOutput serviceOutput = createLoanService.createLoan(mapper.mapInput(request));

        return mapper.mapResponse(serviceOutput);
    }

    @GetMapping("/listLoans")
    public ListLoansResponse listLoans(@RequestParam @Positive long customerId,
                                       @RequestParam @Pattern(regexp = "all|paid|unpaid",
                                               flags = Pattern.Flag.CASE_INSENSITIVE) String loanStatus) throws DataException {
        ListLoansOutput serviceOutput = loanInquiryService.listLoans(customerId, loanStatus);

        return mapper.mapResponse(serviceOutput);
    }

    @GetMapping("/listInstallments")
    public ListInstallmentsResponse listInstallments(@RequestParam @Positive long customerId,
                                                     @RequestParam @Positive long loanId) throws DataException {

        ListInstallmentsOutput serviceOutput = installmentInquiryService.listInstallments(customerId, loanId);

        return mapper.mapResponse(serviceOutput);
    }

    @PostMapping("/payLoan")
    public PayLoanResponse payLoan(@Valid @RequestBody PayLoanRequest request) throws DataException, BusinessException {

        PayLoanOutput serviceOutput = payLoanService.payLoan(mapper.mapInput(request));

        return mapper.mapResponse(serviceOutput);
    }
}
