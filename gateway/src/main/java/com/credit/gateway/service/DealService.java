package com.credit.gateway.service;

import com.credit.gateway.dto.FinishRegistrationRequestDTO;
import com.credit.gateway.entity.Application;
import com.credit.gateway.feignclient.DealFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final DealFeignClient dealFeignClient;

    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        dealFeignClient.calculateCredit(finishRegistrationRequestDTO, applicationId);
        log.info("credit calculate successfully");
    }

    public void sendDocumentsRequest(Long applicationId) {
        dealFeignClient.sendDocumentRequest(applicationId);
        log.info("request send document");
    }

    public void signDocumentRequest(Long applicationId) {
        dealFeignClient.signDocumentRequest(applicationId);
        log.info("request sign document");
    }

    public void signDocument(Long applicationId, Integer sesCode) {
        dealFeignClient.signDocuments(applicationId, sesCode);
        log.info("sign documents");
    }
    public Application getApplicationById(Long id) {
        Application application = dealFeignClient.getApplicationById(id);
        log.info("get application {}", application);
        return application;
    }

    public List<Application> getApplications() {
        List<Application> applications = dealFeignClient.getApplications();
        log.info("get application list {}", applications);
        return applications;
    }
}
