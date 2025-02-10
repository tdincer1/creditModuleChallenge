package com.talhadincer.creditModule.controller;

import com.google.gson.Gson;
import com.talhadincer.creditModule.core.constant.LoanStatus;
import com.talhadincer.creditModule.data.dto.controller.CreateLoanRequest;
import com.talhadincer.creditModule.data.dto.controller.PayLoanRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@AutoConfigureMockMvc(addFilters = false)
class WebControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void createLoanShouldReturnOk() throws Exception {

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
                .andExpect(jsonPath("$.responseCode").value(0));
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
                .andExpect(status().isBadRequest());
                //.andExpect(jsonPath("$.responseCode").value(0));
    }

    @Test
    public void listLoansShouldReturnOk() throws Exception {
        this.mvc
                .perform(get("/listLoans?customerId=1&loanStatus=" + LoanStatus.ALL.getStatus()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(0));
    }

    @Test
    public void listInstallmentsShouldReturnOk() throws Exception {
        this.mvc
                .perform(get("/listInstallments?customerId=1&loanId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(0));
    }

    @Test
    public void payLoanShouldReturnOk() throws Exception {

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
                .andExpect(jsonPath("$.responseCode").value(0));
    }
}