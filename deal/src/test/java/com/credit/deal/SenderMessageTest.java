package com.credit.deal;

import com.credit.deal.dto.EmailMessage;
import com.credit.deal.model.enums.Theme;
import com.credit.deal.sender.SenderMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SenderMessageTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SenderMessage senderMessage;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @Test
    public void testSendMessage() throws JsonProcessingException {
        EmailMessage emailMessage = EmailMessage.builder()
                .address("test@example.com")
                .theme(Theme.SEND_DOCUMENTS)
                .applicationId(1L)
                .build();

        when(objectMapper.writeValueAsString(any())).thenReturn("{\"address\":\"test@example.com\",\"theme\":\"SEND_DOCUMENTS\",\"applicationId\":1}");

        senderMessage.sendMessage(emailMessage);

        verify(kafkaTemplate).send(topicCaptor.capture(), messageCaptor.capture());

        log.info("topic = {}, message = {}", topicCaptor.getValue(), messageCaptor.getValue());

        assertEquals("send-documents", topicCaptor.getValue());
        assertEquals("{\"address\":\"test@example.com\",\"theme\":\"SEND_DOCUMENTS\",\"applicationId\":1}", messageCaptor.getValue());
    }
}
