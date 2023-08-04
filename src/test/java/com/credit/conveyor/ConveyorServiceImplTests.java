package com.credit.conveyor;

import com.credit.conveyor.dto.*;
import com.credit.conveyor.exception.ScoringException;
import com.credit.conveyor.service.imp.ConveyorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class ConveyorServiceImplTests {

    @Mock
    ConveyorServiceImpl conveyorService = new ConveyorServiceImpl();

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(conveyorService, "baseRate", "8");
    }

    @Test
    void calculateCreditTest() {
        int expectedRate = 5;
        EmploymentDTO employmentDTOTest = EmploymentDTO.builder().employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .employerINN("inn")
                .salary(BigDecimal.valueOf(85000))
                .position(Position.WORKER)
                .workExperienceTotal(13)
                .workExperienceCurrent(4)
                .build();

        ScoringDataDTO scoringDataMock = mock(ScoringDataDTO.class);
        when(scoringDataMock.getAmount()).thenReturn(BigDecimal.valueOf(400000));
        when(scoringDataMock.getTerm()).thenReturn(18);
        when(scoringDataMock.getGender()).thenReturn(Gender.MALE);
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1995-11-12));
        when(scoringDataMock.getEmployment()).thenReturn(employmentDTOTest);
        when(scoringDataMock.getIsInsuranceEnabled()).thenReturn(true);
        when(scoringDataMock.getIsSalaryClient()).thenReturn(true);
        when(scoringDataMock.getDependentAmount()).thenReturn(1);
        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);

        CreditDTO CreditDTO = conveyorService.calculateCredit(scoringDataMock);

        assertEquals(expectedRate, CreditDTO.getRate().intValue());

        EmploymentDTO employmentDTOTest2 = EmploymentDTO.builder().employmentStatus(EmploymentStatus.UNEMPLOYED)
                .employerINN("inn")
                .salary(BigDecimal.valueOf(85000))
                .position(Position.WORKER)
                .workExperienceTotal(5)
                .workExperienceCurrent(1)
                .build();

        ScoringDataDTO scoringDataMock2 = mock(ScoringDataDTO.class);
        when(scoringDataMock2.getAmount()).thenReturn(BigDecimal.valueOf(400000));
        when(scoringDataMock2.getTerm()).thenReturn(18);
        when(scoringDataMock2.getGender()).thenReturn(Gender.MALE);
        when(scoringDataMock2.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1995-11-12));
        when(scoringDataMock2.getEmployment()).thenReturn(employmentDTOTest2);
        when(scoringDataMock2.getIsInsuranceEnabled()).thenReturn(true);
        when(scoringDataMock2.getIsSalaryClient()).thenReturn(true);
        when(scoringDataMock2.getDependentAmount()).thenReturn(1);
        when(scoringDataMock2.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);

        assertThrows(ScoringException.class, () -> conveyorService.calculateCredit(scoringDataMock2));

    }

    @Test
    void getOffersTest() {
        LoanApplicationRequestDTO loanAppMock = mock(LoanApplicationRequestDTO.class);
        when(loanAppMock.getAmount()).thenReturn(BigDecimal.valueOf(400000));
        when(loanAppMock.getTerm()).thenReturn(18);
        when(loanAppMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1994-1-7));

        LoanOfferDTO loanOfferDTO1 = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(400000))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23656.14))
                .rate(BigDecimal.valueOf(8))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        LoanOfferDTO loanOfferDTO2 = LoanOfferDTO.builder().applicationId(2L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(412000.00).setScale(2))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23991.46))
                .rate(BigDecimal.valueOf(6.00).setScale(2))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        LoanOfferDTO loanOfferDTO3 = LoanOfferDTO.builder().applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(400000))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23473.96))
                .rate(BigDecimal.valueOf(7.00).setScale(2))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO loanOfferDTO4 = LoanOfferDTO.builder().applicationId(4L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(412000.00).setScale(2))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23805.56))
                .rate(BigDecimal.valueOf(5.00).setScale(2))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        List<LoanOfferDTO> expectedLoanOffers = Stream.of(
                        loanOfferDTO1, loanOfferDTO2, loanOfferDTO3, loanOfferDTO4
                )
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());

        List<LoanOfferDTO> actualLoanOffers = conveyorService.getOffers(loanAppMock);

        assertEquals(expectedLoanOffers, actualLoanOffers);
    }

}
