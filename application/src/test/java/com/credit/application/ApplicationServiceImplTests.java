package com.credit.application;

import com.credit.application.dto.LoanApplicationRequestDTO;
import com.credit.application.dto.LoanOfferDTO;
import com.credit.application.exception.PreScoringException;
import com.credit.application.feignclient.DealFeignClient;
import com.credit.application.service.impl.ApplicationServiceImpl;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTests {

    @Mock
    private DealFeignClient dealFeignClient;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    void getOffersTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(400000))
                .firstName("Ivan")
                .middleName("Ivanovich")
                .lastName("Ivanov")
                .term(18)
                .email("ivan@gmail.com")
                .birthdate(LocalDate.of(1999, 5, 11))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        LoanOfferDTO loanOfferDTO1 = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(400000))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23656.14))
                .rate(BigDecimal.valueOf(8))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        LoanOfferDTO loanOfferDTO2 = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(412000.00).setScale(2))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23991.46))
                .rate(BigDecimal.valueOf(6.00).setScale(2))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        LoanOfferDTO loanOfferDTO3 = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(400000))
                .totalAmount(BigDecimal.valueOf(400000))
                .term(18)
                .monthlyPayment(BigDecimal.valueOf(23473.96))
                .rate(BigDecimal.valueOf(7.00).setScale(2))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO loanOfferDTO4 = LoanOfferDTO.builder().applicationId(1L)
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

        when(dealFeignClient.getOffers(loanApplicationRequestDTO)).thenReturn(ResponseEntity.ok(expectedLoanOffers));

        List<LoanOfferDTO> actualLoanOffers = applicationService.getOffers(loanApplicationRequestDTO);

        assertEquals(expectedLoanOffers, actualLoanOffers);

        EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        LoanApplicationRequestDTO loanApplicationRequestDTO2 = random.nextObject(LoanApplicationRequestDTO.class);

        assertThrows(PreScoringException.class, () -> applicationService.getOffers(loanApplicationRequestDTO2));
    }

    @Test
    void applyOfferTest() {
        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(100000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(17008.73))
                .rate(BigDecimal.valueOf(7))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        applicationService.applyOffer(loanOfferDTO);

        verify(dealFeignClient, times(1)).applyOffer(loanOfferDTO);
    }
}
