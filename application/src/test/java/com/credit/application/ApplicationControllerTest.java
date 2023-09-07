package com.credit.application;

import com.credit.application.controller.ApplicationController;
import com.credit.application.dto.LoanApplicationRequestDTO;
import com.credit.application.dto.LoanOfferDTO;
import com.credit.application.service.impl.ApplicationServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {

    @MockBean
    private ApplicationServiceImpl applicationService;

    @Autowired
    private MockMvc mockMvc;
    @Test
    public void getOffersTest() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(400000))
                .term(18).birthdate(LocalDate.parse("1994-11-12"))
                .firstName("Ivan").lastName("Ivanov")
                .email("ivan@gmail.com").passportNumber("123456")
                .passportSeries("1234").build();

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(400000))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23656.14))
                .rate(BigDecimal.valueOf(8))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        List<LoanOfferDTO> expectedLoanOffers = Collections.singletonList(loanOfferDTO);

        when(applicationService.getOffers(loanApplicationRequestDTO)).thenReturn(expectedLoanOffers);

        mockMvc.perform(MockMvcRequestBuilders.post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loanApplicationRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(expectedLoanOffers)));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
