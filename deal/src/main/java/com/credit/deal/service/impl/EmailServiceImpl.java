package com.credit.deal.service.impl;

import com.credit.deal.dto.EmailMessage;
import com.credit.deal.entity.Application;
import com.credit.deal.entity.Credit;
import com.credit.deal.exception.DealException;
import com.credit.deal.model.StatusHistory;
import com.credit.deal.model.enums.ApplicationStatus;
import com.credit.deal.model.enums.CreditStatus;
import com.credit.deal.model.enums.Theme;
import com.credit.deal.repository.ApplicationRepository;
import com.credit.deal.repository.CreditRepository;
import com.credit.deal.sender.SenderMessage;
import com.credit.deal.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final SenderMessage senderMessage;

    public void createDocumentsRequest(Long applicationId) {

        Application application = getApplicationById(applicationId);

        if (application.getStatus() != ApplicationStatus.CC_APPROVED) {
            throw new DealException("Application with id = " + applicationId + " has status " + application.getStatus()
                    + " but status must be " + ApplicationStatus.CC_APPROVED);
        }

        log.info("Sending create document request for application {}, to email {}",
                application, application.getClient().getEmail());

        senderMessage.sendMessage(EmailMessage.builder()
                .address(application.getClient().getEmail())
                .theme(Theme.CREATE_DOCUMENTS)
                .applicationId(applicationId)
                .build());
    }
    @Override
    public void sendDocumentRequest(Long applicationId) {
        log.info("Method sendDocumentRequest starts for application with id = {}", applicationId);

        Application application = getApplicationById(applicationId);

        if (application.getStatus() != ApplicationStatus.CC_APPROVED) {
            throw new DealException("Application with id = " + applicationId + " has status " + application.getStatus()
                    + " but status must be " + ApplicationStatus.CC_APPROVED);
        }

        updateApplicationStatus(application, ApplicationStatus.PREPARE_DOCUMENTS);

        applicationRepository.save(application);
        log.info("save application in database {}", application);


        senderMessage.sendMessage(EmailMessage.builder()
                .address(application.getClient().getEmail())
                .theme(Theme.SEND_DOCUMENTS)
                .applicationId(applicationId)
                .build());
        log.info("send message to kafka with topic = {}", Theme.SEND_DOCUMENTS);
    }

    @Override
    public void signDocumentRequest(Long applicationId) {
        log.info("Method signDocumentRequest starts for application with id = {}", applicationId);

        Application application = getApplicationById(applicationId);

        Integer sesCode = ThreadLocalRandom.current().nextInt(1000, 9999);

        log.debug("set sesCode = {} for application with id = {}", sesCode, application);
        application.setSesCode(sesCode);

        applicationRepository.save(application);
        log.info("save application in database {}", application);

        log.info("send message to kafka with topic = {}", Theme.SEND_SES);
        senderMessage.sendMessage(EmailMessage.builder()
                .address(application.getClient().getEmail())
                .applicationId(applicationId)
                .theme(Theme.SEND_SES)
                .build());
    }

    @Override
    public void signDocument(Long applicationId, Integer sesCode) {
        log.info("Method signDocument starts for application with id = {}", applicationId);

        Application application = getApplicationById(applicationId);

        if (!sesCode.equals(application.getSesCode())) {
            throw new DealException("Wrong sesCode" + sesCode + "for application with id = " + applicationId
                    + ". It must be equals " + application.getSesCode());
        }

        application.setSignDate(LocalDateTime.now());
        updateApplicationStatus(application, ApplicationStatus.DOCUMENT_SIGNED);

        applicationRepository.save(application);
        log.info("save application in database {}", application);

        issueCredit(application);

        log.info("send message to kafka with topic = {}", Theme.CREDIT_ISSUED);
        senderMessage.sendMessage(EmailMessage.builder()
                .address(application.getClient().getEmail())
                .applicationId(applicationId)
                .theme(Theme.CREDIT_ISSUED)
                .build()
        );
    }

    private Application getApplicationById(Long applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);

        Application application;
        if (applicationOptional.isPresent()) {
            application = applicationOptional.get();
        } else {
            throw new DealException("Application id not found");
        }
        return application;
    }



    private void issueCredit(Application application) {
        if (application.getCredit() == null) {
            throw new DealException("credit  is not exists");
        }

        Long creditId = application.getCredit().getId();
        Optional<Credit> creditOptional = creditRepository.findById(creditId);

        Credit credit;
        if (creditOptional.isPresent()) {
            credit = creditOptional.get();
        } else {
            throw new DealException("Application id not found");
        }

        application.setStatus(ApplicationStatus.CREDIT_ISSUED);
        updateApplicationStatus(application, ApplicationStatus.CREDIT_ISSUED);
        credit.setCreditStatus(CreditStatus.ISSUED);

        creditRepository.save(credit);
        log.info("update credit in database {}", credit);
    }

    private void updateApplicationStatus(Application application, ApplicationStatus applicationStatus) {
        application.setStatus(applicationStatus);
        log.info("application status change on {}", application);
        StatusHistory statusHistory = StatusHistory.builder()
                .date(LocalDateTime.now())
                .status(applicationStatus)
                .build();
        if (application.getStatusHistory() == null) {
            application.setStatusHistory(new ArrayList<>());
        }
        application.getStatusHistory().add(statusHistory);
    }

}
