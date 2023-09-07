package com.credit.dossier.service;

import com.credit.dossier.dto.EmailMessage;
import com.credit.dossier.exception.EmailProcessingException;
import com.credit.dossier.model.enums.Theme;
import com.credit.dossier.entity.Application;
import com.credit.dossier.feignclient.DealFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final DocumentService documentService;
    private final EmailService emailService;
    private final DealFeignClient dealFeignClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "finish-registration", groupId = "deal")
    public void finishRegistrationMessage(String data) {
        EmailMessage emailMessage = getEmailMessage(data);
        Theme theme = emailMessage.getTheme();

        log.info("consumer received the message with topic = {}", theme);

        Application application = dealFeignClient.getApplicationById(emailMessage.getApplicationId());
        String text = String.format(
                "Hello %s, please, finish registration your application id-%s.", application.getClient().getFirstName(), application.getId());
        String address = emailMessage.getAddress();
        log.info("Dossier service try a send message for {}", address);

        emailService.sendMessage(address, text, "Finish registration in credit-conveyor");

        log.info("Message for {} successfully delivered", address);

    }

    @KafkaListener(topics = "create-documents", groupId = "deal")
    public void createDocumentMessage(String data) {
        EmailMessage emailMessage = getEmailMessage(data);
        Theme theme = emailMessage.getTheme();

        log.info("consumer received the message with topic = {}", theme);

        Application application = dealFeignClient.getApplicationById(emailMessage.getApplicationId());
        String address = emailMessage.getAddress();

        String messageText = String.format(
                "Hello %s, can prepare and send documents for you?", application.getClient().getFirstName());

        log.info("Dossier service try to send a message to {}", emailMessage.getAddress());
        emailService.sendMessage(emailMessage.getAddress(), messageText, "Create documents for your loan application");
        log.info("Message for {} successfully delivered", address);
    }

    @KafkaListener(topics = "send-documents", groupId = "deal")
    public void sendDocumentMessage(String data) {
        EmailMessage emailMessage = getEmailMessage(data);
        Theme theme = emailMessage.getTheme();
        String address = emailMessage.getAddress();

        log.info("consumer received the message with topic = {}", theme);

        Application application = dealFeignClient.getApplicationById(emailMessage.getApplicationId());
        List<File> files = documentService.createFiles(application);

        log.info("Dossier service try a send message for {}", address);
        files.forEach(file -> emailService.sendDocument(address, file));
        log.info("Message for {} successfully delivered", address);
    }

    @KafkaListener(topics = "send-ses", groupId = "deal")
    public void sesCodeMessage(String data) {
        EmailMessage emailMessage = getEmailMessage(data);
        Application application = dealFeignClient.getApplicationById(emailMessage.getApplicationId());
        Theme theme = emailMessage.getTheme();

        log.info("consumer received the message with topic = {}", theme);

        String sesCode = application.getSesCode().toString();

        String address = emailMessage.getAddress();

        emailService.sendMessage(address, sesCode, "Your sesCode");

        log.info("Message for {} successfully delivered", address);

    }

    @KafkaListener(topics = "credit-issued", groupId = "deal")
    public void creditIssuedMessage(String data) {
        EmailMessage emailMessage = getEmailMessage(data);
        Theme theme = emailMessage.getTheme();
        String address = emailMessage.getAddress();
        log.info("consumer received the message with topic = {}", theme);

        String text = "Loan successfully approved!";

        emailService.sendMessage(address, text, "Credit issued!");
        log.info("Message for {} successfully delivered", address);
    }

    private EmailMessage getEmailMessage(String data) {
        try {
            return objectMapper.readValue(data, EmailMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing email message: {}", e.getMessage());
            throw new EmailProcessingException("Error deserializing email message");
        }
    }

}
