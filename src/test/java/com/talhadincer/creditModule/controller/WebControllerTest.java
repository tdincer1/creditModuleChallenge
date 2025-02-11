package com.talhadincer.creditModule.controller;

import com.google.gson.Gson;
import com.talhadincer.creditModule.core.DateUtil;
import com.talhadincer.creditModule.core.constant.LoanStatus;
import com.talhadincer.creditModule.core.constant.ResponseCodes;
import com.talhadincer.creditModule.data.dto.controller.CreateLoanRequest;
import com.talhadincer.creditModule.data.dto.controller.PayLoanRequest;
import com.talhadincer.creditModule.data.dto.service.*;
import com.talhadincer.creditModule.service.CreateLoanService;
import com.talhadincer.creditModule.service.InstallmentInquiryService;
import com.talhadincer.creditModule.service.LoanInquiryService;
import com.talhadincer.creditModule.service.PayLoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebController.class)
@AutoConfigureMockMvc(addFilters = false)
class WebControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreateLoanService createLoanService;

    @MockitoBean
    private LoanInquiryService loanInquiryService;

    @MockitoBean
    private InstallmentInquiryService installmentInquiryService;

    @MockitoBean
    private PayLoanService payLoanService;

    @Test
    public void createLoanShouldReturnOk() throws Exception {

        CreateLoanOutput output = new CreateLoanOutput();
        output.setSuccess();
        when(createLoanService.createLoan(any(CreateLoanInput.class))).thenReturn(output);

        CreateLoanRequest request = CreateLoanRequest.builder()
                .customerId(1L)
                .amount(new BigDecimal("1000"))
                .interestRate(new BigDecimal("0.2"))
                .numberOfInstallments(1)
                .build();

        this.mvc
                .perform(post("/createLoan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(ResponseCodes.SUCCESS.getCode()));
    }

    @Test
    public void createLoanShouldReturnError() throws Exception {

        CreateLoanRequest request = CreateLoanRequest.builder()
                .customerId(1L)
                .amount(new BigDecimal("1000"))
                .interestRate(new BigDecimal("0.0"))
                .numberOfInstallments(1)
                .build();

        this.mvc
                .perform(post("/createLoan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseCode").value(ResponseCodes.VALIDATION_ERROR.getCode()));
    }

    @Test
    public void listLoansShouldReturnOk() throws Exception {
        LoanDto loanDto = LoanDto.builder()
                .id(123L)
                .amount(new BigDecimal("1000"))
                .numberOfInstallments(6)
                .paid(true)
                .build();

        ListLoansOutput output = new ListLoansOutput();
        output.setLoans(Collections.singletonList(loanDto));
        output.setSuccess();

        when(loanInquiryService.listLoans(anyLong(), anyString())).thenReturn(output);

        this.mvc
                .perform(get("/listLoans?customerId=1&loanStatus=" + LoanStatus.ALL.getStatus()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(ResponseCodes.SUCCESS.getCode()));
    }

    @Test
    public void listInstallmentsShouldReturnOk() throws Exception {
        InstallmentDto installmentDto = InstallmentDto.builder()
                .loanId(123L)
                .amount(new BigDecimal("100"))
                .dueDate(DateUtil.toStringNow())
                .paid(false).build();

        ListInstallmentsOutput output = new ListInstallmentsOutput();
        output.setInstallments(Collections.singletonList(installmentDto));
        output.setSuccess();
        when(installmentInquiryService.listInstallments(anyLong(), anyLong())).thenReturn(output);

        this.mvc
                .perform(get("/listInstallments?customerId=1&loanId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(ResponseCodes.SUCCESS.getCode()));
    }

    @Test
    public void payLoanShouldReturnOk() throws Exception {

        PayLoanOutput output = new PayLoanOutput();
        output.setPaidAmount(new BigDecimal("300"));
        output.setLoanClosed(true);
        output.setNumberOfInstallmentsPaid(2);
        output.setSuccess();
        when(payLoanService.payLoan(any(PayLoanInput.class))).thenReturn(output);

        PayLoanRequest request = PayLoanRequest.builder()
                .customerId(1)
                .loanId(123)
                .amount(new BigDecimal("1000"))
                .build();

        this.mvc
                .perform(post("/payLoan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(ResponseCodes.SUCCESS.getCode()));
    }
}