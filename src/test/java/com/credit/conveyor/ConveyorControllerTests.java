package com.credit.conveyor;

import com.credit.conveyor.controller.ConveyorController;
import com.credit.conveyor.dto.LoanApplicationRequestDTO;
import com.credit.conveyor.dto.LoanOfferDTO;
import com.credit.conveyor.dto.EmploymentDTO;
import com.credit.conveyor.dto.ScoringDataDTO;
import com.credit.conveyor.dto.CreditDTO;
import com.credit.conveyor.dto.Position;
import com.credit.conveyor.dto.MaritalStatus;
import com.credit.conveyor.dto.Gender;
import com.credit.conveyor.dto.EmploymentStatus;
import com.credit.conveyor.service.imp.ConveyorServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(ConveyorController.class)
public class ConveyorControllerTests {

    @MockBean
    private ConveyorServiceImpl conveyorService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(conveyorService, "baseRate", "8");
    }

    @Test
    public void getOffersTest() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
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

        when(conveyorService.getOffers(requestDTO)).thenReturn(expectedLoanOffers);

        mockMvc.perform(MockMvcRequestBuilders.post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(expectedLoanOffers)));
    }

    @Test
    public void calculateCreditTest() throws Exception {
        EmploymentDTO employmentDTO = EmploymentDTO.builder().employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .employerINN("inn")
                .salary(BigDecimal.valueOf(85000))
                .position(Position.WORKER)
                .workExperienceTotal(13)
                .workExperienceCurrent(4)
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(400000))
                .term(18).gender(Gender.MALE).firstName("Ivan")
                .lastName("Ivanov").birthdate(LocalDate.parse("1994-11-12"))
                .passportSeries("1234").passportNumber("123456")
                .passportIssueDate(LocalDate.parse("2016-11-12"))
                .passportIssueBranch("issuebranch")
                .employment(employmentDTO).isInsuranceEnabled(true)
                .isSalaryClient(true).dependentAmount(1).maritalStatus(MaritalStatus.SINGLE)
                .account("account").build();

        CreditDTO creditDTO = CreditDTO.builder().build();

        when(conveyorService.calculateCredit(scoringDataDTO)).thenReturn(creditDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scoringDataDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }

}
