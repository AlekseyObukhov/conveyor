package com.credit.deal;

import com.credit.deal.dto.LoanOfferDTO;
import com.credit.deal.dto.LoanApplicationRequestDTO;
import com.credit.deal.entity.Application;
import com.credit.deal.entity.Client;
import com.credit.deal.feignclient.DealFeignClient;
import com.credit.deal.model.enums.ApplicationStatus;
import com.credit.deal.repository.ApplicationRepository;
import com.credit.deal.repository.ClientRepository;
import com.credit.deal.repository.CreditRepository;
import com.credit.deal.service.imp.DealServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DealServiceImplTest {

    @Mock
    private DealFeignClient dealFeignClient;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CreditRepository creditRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private DealServiceImpl dealService;

    @Test
    public void getOffersTest() {
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

        when(dealFeignClient.generateOffers(loanApplicationRequestDTO)).thenReturn(expectedLoanOffers);

        List<LoanOfferDTO> actualLoanOffers = dealService.getOffers(loanApplicationRequestDTO);

        assertEquals(expectedLoanOffers, actualLoanOffers);
    }

    @Test
    public void applyOfferTest() {
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

        Application application = Application.builder()
                .id(1L)
                .client(mock(Client.class))
                .creationDate(LocalDate.now())
                .status(ApplicationStatus.APPROVED)
                .statusHistory(new ArrayList<>())
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));

        dealService.applyOffer(loanOfferDTO);
        Optional<Application> optionalApplication = applicationRepository.findById(1L);

        Application application1 = null;
        if (optionalApplication.isPresent()) {
            application1 = optionalApplication.get();
        }
        assert application1 != null;
        assertNotNull(application1.getAppliedOffer());
    }

    @Test
    public void calculateCreditTest() {
        //TODO
    }
}
