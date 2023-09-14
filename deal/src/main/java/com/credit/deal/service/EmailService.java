package com.credit.deal.service;

public interface EmailService {

    void createDocumentsRequest(Long applicationId);

    void sendDocumentRequest(Long applicationId);

    void signDocumentRequest(Long applicationId);

    void signDocument(Long applicationId, Integer sesCode);

}
