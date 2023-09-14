package com.credit.deal.sender;

import com.credit.deal.dto.EmailMessage;
import com.credit.deal.model.enums.Theme;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenderMessage {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(EmailMessage message) {
        String topic = getTopic(message.getTheme());
        try {
            String jsonEmail = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, jsonEmail);
            log.info("KAFKA SEND MESSAGE FOR CONSUMER WITH TOPIC {}", topic);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private String getTopic(Theme theme) {
        String topic = switch (theme) {
            case FINISH_REGISTRATION -> "finish-registration";
            case CREATE_DOCUMENTS -> "create-documents";
            case SEND_DOCUMENTS -> "send-documents";
            case SEND_SES -> "send-ses";
            case CREDIT_ISSUED -> "credit-issued";
            case APPLICATION_DENIED -> "application-denied";
        };
        return topic;
    }
}


