package com.credit.dossier.service;

import com.credit.dossier.dto.LoanOfferDTO;
import com.credit.dossier.entity.Application;
import com.credit.dossier.entity.Client;
import com.credit.dossier.entity.Credit;
import com.credit.dossier.model.Employment;
import com.credit.dossier.model.Passport;
import com.credit.dossier.model.enums.ApplicationStatus;
import com.credit.dossier.model.enums.Gender;
import com.credit.dossier.model.enums.MaritalStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Spy
    private DocumentService documentService;

    @Test
    void testCreateFiles() {
        Client client = Client.builder()
                .firstName("ivan")
                .middleName("ivanovich")
                .lastName("ivanov")
                .email("test@test.ru")
                .passport(new Passport())
                .birthDate(LocalDate.of(1997, 6, 16))
                .account("account")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(0)
                .employment(new Employment())
                .build();

        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(250000))
                .rate(BigDecimal.valueOf(8))
                .monthlyPayment(BigDecimal.valueOf(250000))
                .amount(BigDecimal.valueOf(250000))
                .paymentSchedule(new ArrayList<>())
                .term(12)
                .build();

        Application application = Application.builder()
                .credit(credit)
                .client(client)
                .creationDate(LocalDateTime.now())
                .appliedOffer(new LoanOfferDTO())
                .status(ApplicationStatus.CREDIT_ISSUED)
                .build();

        List<File> files = documentService.createFiles(application);

        assertFalse(files.isEmpty());

        assertEquals(3, files.size());
    }
}
