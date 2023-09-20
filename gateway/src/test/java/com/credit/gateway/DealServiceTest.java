package com.credit.gateway;

import com.credit.gateway.dto.EmploymentDTO;
import com.credit.gateway.dto.FinishRegistrationRequestDTO;
import com.credit.gateway.dto.LoanOfferDTO;
import com.credit.gateway.entity.Application;
import com.credit.gateway.entity.Client;
import com.credit.gateway.entity.Credit;
import com.credit.gateway.feignclient.DealFeignClient;
import com.credit.gateway.model.enums.ApplicationStatus;
import com.credit.gateway.model.enums.Gender;
import com.credit.gateway.model.enums.MaritalStatus;
import com.credit.gateway.service.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {

    @Mock
    private DealFeignClient dealFeignClient;

    @InjectMocks
    private DealService dealService;

    @Captor
    private ArgumentCaptor<FinishRegistrationRequestDTO> finishRegistrationRequestDTOCaptor;

    @Captor
    private ArgumentCaptor<Long> applicationIdCaptor;

    @Test
    void calculateCreditTest() {
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = FinishRegistrationRequestDTO.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .passportIssueDate(LocalDate.of(2021, 1, 1))
                .passportIssueBranch("Branch")
                .employment(new EmploymentDTO())
                .account("Account")
                .build();
        Long applicationId = 1L;

        dealService.calculateCredit(finishRegistrationRequestDTO, applicationId);

        verify(dealFeignClient, times(1)).calculateCredit(finishRegistrationRequestDTOCaptor.capture(), applicationIdCaptor.capture());
        assertEquals(finishRegistrationRequestDTO, finishRegistrationRequestDTOCaptor.getValue());
        assertEquals(applicationId, applicationIdCaptor.getValue());
    }

    @Test
    void sendDocumentsRequestTest() {
        Long applicationId = 1L;

        dealService.sendDocumentsRequest(applicationId);

        verify(dealFeignClient, times(1)).sendDocumentRequest(applicationIdCaptor.capture());
        assertEquals(applicationId, applicationIdCaptor.getValue());
    }

    @Test
    void signDocumentRequestTest() {
        Long applicationId = 1L;

        dealService.signDocumentRequest(applicationId);

        verify(dealFeignClient, times(1)).signDocumentRequest(applicationIdCaptor.capture());
        assertEquals(applicationId, applicationIdCaptor.getValue());
    }

    @Test
    void signDocumentTest() {
        Long applicationId = 1L;
        Integer sesCode = 1234;

        dealService.signDocument(applicationId, sesCode);

        verify(dealFeignClient, times(1)).signDocuments(applicationIdCaptor.capture(), eq(sesCode));
        assertEquals(applicationId, applicationIdCaptor.getValue());
    }

    @Test
    public void getApplicationById() {
        Long actualId = 1L;
        Application expectedApp = Application.builder()
                .id(1L)
                .sesCode(1234)
                .creationDate(LocalDateTime.now())
                .client(new Client())
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .credit(new Credit())
                .signDate(LocalDateTime.now())
                .status(ApplicationStatus.DOCUMENT_SIGNED)
                .build();

        when(dealFeignClient.getApplicationById(actualId)).thenReturn(expectedApp);
        Application actualApp = dealService.getApplicationById(actualId);

        assertEquals(expectedApp, actualApp);
    }

    @Test
    public void getAllApplication() {
        Application expectedApp1 = Application.builder()
                .id(1L)
                .sesCode(1234)
                .creationDate(LocalDateTime.now())
                .client(new Client())
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .credit(new Credit())
                .signDate(LocalDateTime.now())
                .status(ApplicationStatus.APPROVED)
                .build();
        Application expectedApp2 = Application.builder()
                .id(1L)
                .sesCode(1234)
                .creationDate(LocalDateTime.now())
                .client(new Client())
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .credit(new Credit())
                .signDate(LocalDateTime.now())
                .status(ApplicationStatus.APPROVED)
                .build();

        List<Application> listAppExpected = List.of(expectedApp1, expectedApp2);

        when(dealFeignClient.getApplications()).thenReturn(listAppExpected);
        List<Application> listApplicationActual = dealService.getApplications();

        assertEquals(listAppExpected, listApplicationActual);
    }
}
